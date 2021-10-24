/**
 * This file is part of PowerRanks, licensed under the MIT License.
 *
 * Copyright (c) svenar (Sven) <powerranks@svenar.nl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

package nl.svenar.powerranks.common.storage.provided;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;

import org.bson.Document;

import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

/**
 * MongoDB storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class MongoDBStorageManager extends PowerStorageManager {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Document> ranksCollection, playersCollection;

    /**
     * Initialize this storage method by connecting to the external database
     * 
     * @param sqlConfig
     * @param hideErrors
     */
    public MongoDBStorageManager(PowerSQLConfiguration sqlConfig, boolean hideErrors) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        try {
            this.mongoClient = new MongoClient(sqlConfig.getHost(), sqlConfig.getPort());
        } catch (Exception e) {
            if (!hideErrors)
                e.printStackTrace();
        }

        if (!isConnected())
            return;

        // MongoClientOptions options = this.mongoClient.getMongoClientOptions();
        // options.setSocketKeepAlive(true);

        this.db = mongoClient.getDatabase(sqlConfig.getDatabase());

        this.ranksCollection = db.getCollection("ranks");
        this.playersCollection = db.getCollection("players");

        // boolean auth = db.authenticate(sqlConfig.getUsername(),
        // sqlConfig.getPassword());

    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public String getType() {
        return "MONGODB";
    }

    /**
     * Check if the storage method is connected. Returns true if a file base storage
     * is used.
     * 
     * Only used by external storage implementations like databases.
     * 
     * @return true if connected, false otherwise
     */
    @Override
    public boolean isConnected() {
        return !Objects.isNull(this.mongoClient);
    }

    /**
     * Load all ranks from the storage method, can be retrieved by calling
     * getRanks()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadRanks() {
        Gson gson = new Gson();

        boolean success = false;

        this.setRanks(new ArrayList<PRRank>());

        Map<String, Object> rawDBData = new HashMap<String, Object>();

        Type mapType = new TypeToken<Object>() {
        }.getType();

        FindIterable<Document> iterDoc = this.ranksCollection.find();
        Iterator<Document> it = iterDoc.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            String key = doc.get("keyname").toString();
            for (String keyPart : doc.keySet()) {
                if (keyPart.startsWith("_") || keyPart.equals("keyname"))
                    continue;

                rawDBData.put(key + "." + keyPart.replaceAll("_", "\\."),
                        gson.fromJson(doc.get(keyPart).toString(), mapType));
            }
        }

        Map<String, Map<String, Object>> rawRanksData = new HashMap<String, Map<String, Object>>();

        for (Entry<String, Object> entry : rawDBData.entrySet()) {
            String rankName = entry.getKey().split("\\.")[0];
            if (!rawRanksData.containsKey(rankName)) {
                Map<String, Object> updatedData = new HashMap<String, Object>();
                updatedData.put("name", rankName);
                rawRanksData.put(rankName, updatedData);
            }

            Map<String, Object> updatedData = rawRanksData.get(rankName);
            if (entry.getKey().split("\\.").length > 2) {
                String key = String.join(".",
                        Arrays.copyOfRange(entry.getKey().split("\\."), 2, entry.getKey().split("\\.").length));
                String propertyKey = entry.getKey().split("\\.")[1];

                if (!rawRanksData.get(rankName).containsKey(propertyKey)) {
                    Map<String, Object> newMap = new HashMap<String, Object>();
                    rawRanksData.get(rankName).put(propertyKey, newMap);
                }

                Map<String, Object> dataMap = (Map<String, Object>) rawRanksData.get(rankName).get(propertyKey);
                dataMap.put(key, entry.getValue());
                rawRanksData.get(rankName).put(propertyKey, dataMap);
            } else {
                updatedData.put(entry.getKey().split("\\.")[1], entry.getValue());
                rawRanksData.put(rankName, updatedData);
            }

        }

        for (Entry<String, Map<String, Object>> entry : rawRanksData.entrySet()) {
            PRRank rank = this.getSerializer().deserialize(entry.getValue(), PRRank.class);

            if (Objects.nonNull(rank)) {
                this.addRank(rank);
                success = true;
            }
        }

        if (!success) {
            this.setRanks(new ArrayList<PRRank>());
        }
    }

    /**
     * Load all players from the storage method, can be retrieved by calling
     * getPlayers()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadPlayers() {

        Gson gson = new Gson();

        boolean success = false;

        this.setPlayers(new ArrayList<PRPlayer>());

        Map<String, Object> rawDBData = new HashMap<String, Object>();

        Type mapType = new TypeToken<Object>() {
        }.getType();

        FindIterable<Document> iterDoc = this.playersCollection.find();
        Iterator<Document> it = iterDoc.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            String key = doc.get("keyname").toString();
            for (String keyPart : doc.keySet()) {
                if (keyPart.startsWith("_") || keyPart.equals("keyname"))
                    continue;

                rawDBData.put(key + "." + keyPart.replaceAll("_", "\\."),
                        gson.fromJson(doc.get(keyPart).toString(), mapType));
            }
        }
        // Iterator<Document> it = iterDoc.iterator();
        // while (it.hasNext()) {
        // Document doc = it.next();
        // rawDBData.put(doc.get("keyname").toString(),
        // gson.fromJson(doc.get("val").toString(), mapType));
        // }

        Map<String, Map<String, Object>> rawPlayersData = new HashMap<String, Map<String, Object>>();

        for (Entry<String, Object> entry : rawDBData.entrySet()) {
            String playerUUID = entry.getKey().split("\\.")[0];
            if (!rawPlayersData.containsKey(playerUUID)) {
                Map<String, Object> updatedData = new HashMap<String, Object>();
                updatedData.put("uuid", playerUUID);
                rawPlayersData.put(playerUUID, updatedData);
            }

            Map<String, Object> updatedData = rawPlayersData.get(playerUUID);
            if (entry.getKey().split("\\.").length > 2) {
                String key = String.join(".",
                        Arrays.copyOfRange(entry.getKey().split("\\."), 2, entry.getKey().split("\\.").length));
                String propertyKey = entry.getKey().split("\\.")[1];

                if (!rawPlayersData.get(playerUUID).containsKey(propertyKey)) {
                    Map<String, Object> newMap = new HashMap<String, Object>();
                    rawPlayersData.get(playerUUID).put(propertyKey, newMap);
                }

                Map<String, Object> dataMap = (Map<String, Object>) rawPlayersData.get(playerUUID).get(propertyKey);
                dataMap.put(key, entry.getValue());
                rawPlayersData.get(playerUUID).put(propertyKey, dataMap);
            } else {
                updatedData.put(entry.getKey().split("\\.")[1], entry.getValue());
                rawPlayersData.put(playerUUID, updatedData);
            }

        }

        for (Entry<String, Map<String, Object>> entry : rawPlayersData.entrySet()) {
            PRPlayer player = this.getSerializer().deserialize(entry.getValue(), PRPlayer.class);

            if (Objects.nonNull(player)) {
                this.addPlayer(player);
                success = true;
            }
        }

        if (!success) {
            this.setPlayers(new ArrayList<PRPlayer>());
        }
    }

    /**
     * Save all ranks using the storage managers storage solution.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void saveRanks() {
        if (this.getRanks().size() == 0)
            return;

        Gson gson = new Gson();

        Map<String, Object> ranks2 = new HashMap<String, Object>();

        for (PRRank r : this.getRanks()) {
            Map<String, Object> serializedRank = this.getSerializer().serialize(r);
            for (Entry<String, Object> entry : serializedRank.entrySet()) {
                if (entry.getValue().equals(r.getName())) {
                    serializedRank.remove(entry.getKey());
                    break;
                }
            }
            ranks2.put(r.getName(), serializedRank);
        }

        // List<Document> documents = new ArrayList<Document>();
        List<InsertOneModel<Document>> documents = new ArrayList<>();

        for (Entry<String, Object> entry : ranks2.entrySet()) {

            Map<String, Object> map = new HashMap<String, Object>();
            iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

            Document doc = null;
            boolean exists = false;
            for (InsertOneModel<Document> cachedDoc : documents) {
                if (cachedDoc.getDocument().get("keyname").equals(entry.getKey())) {
                    doc = cachedDoc.getDocument();
                    exists = true;
                    break;
                }
            }

            if (Objects.isNull(doc))
                doc = new Document("keyname", entry.getKey());

            for (Entry<String, Object> mapEntry : map.entrySet()) {
                // String dbKey = entry.getKey() + "." + mapEntry.getKey();
                String serializedValue = gson.toJson(mapEntry.getValue());

                doc.append(mapEntry.getKey().replaceAll("\\.", "_"), serializedValue);

            }
            if (!exists)
                documents.add(new InsertOneModel<>(doc));
        }

        this.ranksCollection.drop();
        this.ranksCollection.bulkWrite(documents);
    }

    /**
     * Save all players using the storage managers storage solution.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void savePlayers() {
        if (this.getPlayers().size() == 0)
            return;

        Gson gson = new Gson();

        Map<String, Object> players2 = new HashMap<String, Object>();

        for (PRPlayer p : this.getPlayers()) {
            Map<String, Object> serializedPlayer = this.getSerializer().serialize(p);
            for (Entry<String, Object> entry : serializedPlayer.entrySet()) {
                if (entry.getValue().equals(p.getUUID().toString())) {
                    serializedPlayer.remove(entry.getKey());
                    break;
                }
            }
            players2.put(p.getUUID().toString(), serializedPlayer);
        }

        // List<Document> documents = new ArrayList<Document>();
        List<InsertOneModel<Document>> documents = new ArrayList<>();

        for (Entry<String, Object> entry : players2.entrySet()) {

            Map<String, Object> map = new HashMap<String, Object>();
            iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

            Document doc = null;
            boolean exists = false;
            for (InsertOneModel<Document> cachedDoc : documents) {
                if (cachedDoc.getDocument().get("keyname").equals(entry.getKey())) {
                    doc = cachedDoc.getDocument();
                    exists = true;
                    break;
                }
            }

            if (Objects.isNull(doc))
                doc = new Document("keyname", entry.getKey());

            for (Entry<String, Object> mapEntry : map.entrySet()) {
                // String dbKey = entry.getKey() + "." + mapEntry.getKey();
                String serializedValue = gson.toJson(mapEntry.getValue());

                doc.append(mapEntry.getKey().replaceAll("\\.", "_"), serializedValue);

            }
            if (!exists)
                documents.add(new InsertOneModel<>(doc));
        }

        this.playersCollection.drop();
        this.playersCollection.bulkWrite(documents);
    }

    /**
     * Add a single rank to the storage managers storage solution.
     * 
     * @param rank
     */
    @Override
    public void saveRank(PRRank rank) {
        this.addRank(rank);
        this.saveRanks();
    }

    /**
     * Add a single player to the storage managers storage solution.
     * 
     * @param player
     */
    @Override
    public void savePlayer(PRPlayer player) {
        this.addPlayer(player);
        this.savePlayers();
    }

    /**
     * Delete all data from the storage manager.
     */
    @Override
    public void removeAllData() {
        db.drop();
    }

}
