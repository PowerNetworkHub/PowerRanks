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

package nl.svenar.common.storage.provided;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

/**
 * PSM (Power Storage Method) storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class PSMStorageManager extends PowerStorageManager {

    private File ranksFile, playersFile;

    /**
     * Initialize this storage method by creating the required files
     * 
     * @param directory
     * @param ranksFileName
     * @param playersFileName
     */
    public PSMStorageManager(String directory, String ranksFileName, String playersFileName) {
        File targetDir = new File(directory);
        this.ranksFile = new File(targetDir, ranksFileName);
        this.playersFile = new File(targetDir, playersFileName);

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        if (!this.ranksFile.exists()) {
            try {
                this.ranksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.playersFile.exists()) {
            try {
                this.playersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.loadRanks();
        this.loadPlayers();
    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public String getType() {
        return "PSM";
    }

    /**
     * Check if the storage method is connected. Returns true if a file base storage
     * is used.
     * 
     * Only used by external storage implementations like databases.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return true;
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

        try (BufferedReader br = new BufferedReader(new FileReader(this.ranksFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("=");
                rawDBData.put(lineSplit[0], gson.fromJson(lineSplit[1], mapType));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        // this.setPlayers(new ArrayList<PRPlayer>());

        Gson gson = new Gson();

        boolean success = false;

        this.setPlayers(new ArrayList<PRPlayer>());

        Map<String, Object> rawDBData = new HashMap<String, Object>();

        Type mapType = new TypeToken<Object>() {
        }.getType();

        try (BufferedReader br = new BufferedReader(new FileReader(this.playersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("=");
                rawDBData.put(lineSplit[0], gson.fromJson(lineSplit[1], mapType));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        Gson gson = new Gson();

        String buffer = "";

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
        try {
            FileWriter fw = new FileWriter(this.ranksFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Entry<String, Object> entry : ranks2.entrySet()) {

                Map<String, Object> map = new HashMap<String, Object>();
                iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

                for (Entry<String, Object> mapEntry : map.entrySet()) {
                    String dbKey = entry.getKey() + "." + mapEntry.getKey();
                    String serializedValue = gson.toJson(mapEntry.getValue());

                    buffer = dbKey;
                    buffer += "=";
                    buffer += serializedValue;
                    buffer += "\n";
                    bw.write(buffer);

                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all players using the storage managers storage solution.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void savePlayers() {
        Gson gson = new Gson();

        String buffer = "";

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

        try {
            FileWriter fw = new FileWriter(this.playersFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Entry<String, Object> entry : players2.entrySet()) {

                Map<String, Object> map = new HashMap<String, Object>();
                iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

                for (Entry<String, Object> mapEntry : map.entrySet()) {
                    String dbKey = entry.getKey() + "." + mapEntry.getKey();
                    String serializedValue = gson.toJson(mapEntry.getValue());

                    buffer = dbKey;
                    buffer += "=";
                    buffer += serializedValue;
                    buffer += "\n";
                    bw.write(buffer);
                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        this.ranksFile.delete();
        this.playersFile.delete();
    }
}
