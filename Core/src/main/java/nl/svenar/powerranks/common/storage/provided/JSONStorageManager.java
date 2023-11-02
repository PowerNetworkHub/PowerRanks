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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

/**
 * JSON storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class JSONStorageManager extends PowerStorageManager {

    private File ranksFile;
    
    private File playersFile;

    /**
     * Initialize this storage method by creating the required files
     * 
     * @param directory
     * @param ranksFileName
     * @param playersFileName
     */
    public JSONStorageManager(String directory, String ranksFileName, String playersFileName) {
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
        return "JSON";
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
        boolean success = false;

        this.setRanks(new ArrayList<PRRank>());

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.ranksFile)))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String jsonData = resultStringBuilder.toString();

        if (jsonData.length() > 0) {
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();

            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonData, mapType);

            for (Entry<String, Object> entry : data.entrySet()) {

                if (entry.getValue() instanceof LinkedTreeMap) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) entry.getValue();
                    map.put("name", entry.getKey());

                    PRRank rank = this.getSerializer().deserialize(map, PRRank.class);
                    this.addRank(rank);
                    success = true;
                }

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
        boolean success = false;

        this.setPlayers(new ArrayList<PRPlayer>());

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.playersFile)))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String jsonData = resultStringBuilder.toString();

        if (jsonData.length() > 0) {
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();

            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonData, mapType);

            for (Entry<String, Object> entry : data.entrySet()) {

                if (entry.getValue() instanceof LinkedTreeMap) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) entry.getValue();
                    map.put("uuid", UUID.fromString(entry.getKey()));

                    PRPlayer player = this.getSerializer().deserialize(map, PRPlayer.class);
                    this.addPlayer(player);
                    success = true;
                }

            }
        }

        if (!success) {
            this.setPlayers(new ArrayList<PRPlayer>());
        }
    }

    public Map<String, Object> getRanksAsMap() {
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

        return ranks2;
    }

    public String getRanksAsJSON(boolean pretty) {
        Gson gson = null;

        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            gson = new Gson();
        }

        Map<String, Object> ranks2 = getRanksAsMap();

        return gson.toJson(ranks2);
    }

    public Map<String, Object> getPlayersAsMap() {
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

        return players2;
    }

    public String getPlayersAsJSON(boolean pretty) {
        Gson gson = null;

        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            gson = new Gson();
        }

        Map<String, Object> players2 = getPlayersAsMap();

        return gson.toJson(players2);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PRRank> getRanksFromJSON(LinkedTreeMap<?, ?> ranks) {
        ArrayList<PRRank> newRanks = new ArrayList<PRRank>();

        for (Entry<?, ?> entry : ranks.entrySet()) {
            String rankName = (String) entry.getKey();
            LinkedTreeMap<?, ?> rankData = (LinkedTreeMap<?, ?>) entry.getValue();

            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) rankData;
            map.put("name", rankName);
            PRRank newRank = this.getSerializer().deserialize(map, PRRank.class);

            newRanks.add(newRank);
        }

        return newRanks;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<PRPlayer> getPlayersFromJSON(LinkedTreeMap<?, ?> players) {
        ArrayList<PRPlayer> newPlayers = new ArrayList<PRPlayer>();

        for (Entry<?, ?> entry : players.entrySet()) {
            String playerUUID = (String) entry.getKey();
            LinkedTreeMap<?, ?> playerData = (LinkedTreeMap<?, ?>) entry.getValue();

            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) playerData;
            map.put("uuid", UUID.fromString(playerUUID));
            PRPlayer newPlayer = this.getSerializer().deserialize(map, PRPlayer.class);

            newPlayers.add(newPlayer);
        }

        return newPlayers;
    }

    /**
     * Save all ranks using the storage managers storage solution.
     */
    @Override
    public void saveRanks() {
        String jsonData = getRanksAsJSON(true);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.ranksFile));
            writer.write(jsonData);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Save all players using the storage managers storage solution.
     */
    @Override
    public void savePlayers() {
        String jsonData = getPlayersAsJSON(true);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.playersFile));
            writer.write(jsonData);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
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
