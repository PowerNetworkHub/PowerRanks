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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

/**
 * SQLite storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class SQLiteStorageManager extends PowerStorageManager {

    private File ranksFile, playersFile;

    private Connection ranksSQLConnection, playersSQLConnection;

    /**
     * Initialize this storage method by creating the required files
     * 
     * @param directory
     * @param ranksFileName
     * @param playersFileName
     */
    public SQLiteStorageManager(String directory, String ranksFileName, String playersFileName) {
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

        this.setupTables();

        try {
            this.loadRanks();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.loadPlayers();
    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public String getType() {
        return "SQLITE";
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
     * Create tables to storage data in
     */
    private void setupTables() {
        this.setupTableRanks();
        this.setupTablePlayers();
    }

    /**
     * Create tables to storage ranks inside of
     */
    private void setupTableRanks() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.ranksSQLConnection = DriverManager.getConnection("jdbc:sqlite:" + this.ranksFile);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(this.ranksSQLConnection)) {
            String query = SQLCreateTable(this.ranksFile.getName().split("\\.")[0]);
            try {
                int result = this.ranksSQLConnection.createStatement().executeUpdate(query);
                checkSQLResult(result, query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create tables to storage players inside of
     */
    private void setupTablePlayers() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.playersSQLConnection = DriverManager.getConnection("jdbc:sqlite:" + this.playersFile);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (Objects.nonNull(this.playersSQLConnection)) {
            String query = SQLCreateTable(this.playersFile.getName().split("\\.")[0]);
            try {
                int result = this.playersSQLConnection.createStatement().executeUpdate(query);
                checkSQLResult(result, query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

        try {
            String query = this.SQLSelectAllInTable(this.ranksFile.getName().split("\\.")[0]);
            Statement st = this.ranksSQLConnection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String key = rs.getString("keyname");
                String rawValue = rs.getString("val");

                Type mapType = new TypeToken<Object>() {
                }.getType();

                Object data = gson.fromJson(rawValue, mapType);

                rawDBData.put(key, data);
            }
        } catch (SQLException e) {
            success = false;
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

        try {
            String query = this.SQLSelectAllInTable(this.playersFile.getName().split("\\.")[0]);
            Statement st = this.playersSQLConnection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String key = rs.getString("keyname");
                String rawValue = rs.getString("val");

                Type mapType = new TypeToken<Object>() {
                }.getType();

                Object data = gson.fromJson(rawValue, mapType);

                rawDBData.put(key, data);
            }
        } catch (SQLException e) {
            success = false;
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

        try {
            String query = SQLDeleteAllInTable(this.ranksFile.getName().split("\\.")[0]);
            int result = this.ranksSQLConnection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = null;
        try {
            this.ranksSQLConnection.setAutoCommit(false);
            stmt = this.ranksSQLConnection
                    .prepareStatement(SQLInsertOrUpdateKV(this.ranksFile.getName().split("\\.")[0]));
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        for (Entry<String, Object> entry : ranks2.entrySet()) {

            Map<String, Object> map = new HashMap<String, Object>();
            iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

            for (Entry<String, Object> mapEntry : map.entrySet()) {
                String dbKey = entry.getKey() + "." + mapEntry.getKey();
                String serializedValue = gson.toJson(mapEntry.getValue());

                try {
                    stmt.clearParameters();
                    stmt.setString(1, dbKey);
                    stmt.setString(2, serializedValue);
                    stmt.setString(3, serializedValue);
                    stmt.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            stmt.executeBatch();
            this.ranksSQLConnection.commit();
            this.ranksSQLConnection.setAutoCommit(true);
        } catch (SQLException e) {
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

        PreparedStatement stmt = null;
        try {
            this.playersSQLConnection.setAutoCommit(false);
            stmt = this.playersSQLConnection
                    .prepareStatement(SQLInsertOrUpdateKV(this.playersFile.getName().split("\\.")[0]));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = SQLDeleteAllInTable(this.playersFile.getName().split("\\.")[0]);
            int result = this.playersSQLConnection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        for (Entry<String, Object> entry : players2.entrySet()) {

            Map<String, Object> map = new HashMap<String, Object>();
            iterate(map, (LinkedHashMap<String, Object>) entry.getValue(), "");

            for (Entry<String, Object> mapEntry : map.entrySet()) {
                String dbKey = entry.getKey() + "." + mapEntry.getKey();
                String serializedValue = gson.toJson(mapEntry.getValue());

                try {
                    stmt.clearParameters();
                    stmt.setString(1, dbKey);
                    stmt.setString(2, serializedValue);
                    stmt.setString(3, serializedValue);
                    stmt.addBatch();
                    // String query = SQLInsertOrUpdateKV(this.ranksFile.getName().split("\\.")[0],
                    // dbKey,
                    // serializedValue);
                    // int result = this.ranksSQLConnection.createStatement().executeUpdate(query);
                    // checkSQLResult(result, query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            stmt.executeBatch();
            this.playersSQLConnection.commit();
            this.playersSQLConnection.setAutoCommit(true);
        } catch (SQLException e) {
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
     * Create a table if it does not exist yet
     * 
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLCreateTable(String tableName) {
        return "CREATE TABLE IF NOT EXISTS `" + tableName
                + "` (`keyname` VARCHAR(256) NOT NULL UNIQUE, `val` LONGTEXT NOT NULL , UNIQUE(`keyname`));";
    }

    /**
     * Insert or update data in a table
     * 
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLInsertOrUpdateKV(String tableName) {
        return "INSERT INTO `" + tableName + "` (keyname, val) VALUES (?, ?) ON CONFLICT(keyname) DO UPDATE SET val=?;";
    }

    /**
     * Select all data in a table
     * 
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLSelectAllInTable(String tableName) {
        return "SELECT * FROM `" + tableName + "`;";
    }

    /**
     * Delete all data in a table
     * 
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLDeleteAllInTable(String tableName) {
        return "DELETE FROM `" + tableName + "`;";
    }

    /**
     * Check if an SQL update succeeded
     * 
     * @param result
     * @param query
     * @throws SQLException
     */
    private void checkSQLResult(int result, String query) throws SQLException {
        if (result < 0) {
            throw new SQLException("Failed to execute SQL query (" + query + ")");
        }
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
