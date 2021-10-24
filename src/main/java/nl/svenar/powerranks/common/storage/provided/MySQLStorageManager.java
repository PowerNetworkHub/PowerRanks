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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

/**
 * MySQL storage manager implementation using PowerStorageManager as base.
 * 
 * @author svenar
 */
public class MySQLStorageManager extends PowerStorageManager {

    private Connection connection;
    private PowerSQLConfiguration sqlConfig;

    /**
     * Initialize this storage method by connecting to the external database
     * 
     * @param sqlConfig
     * @param hideErrors
     */
    public MySQLStorageManager(PowerSQLConfiguration sqlConfig, boolean hideErrors) {
        this.sqlConfig = sqlConfig;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                if (!hideErrors)
                    ex.printStackTrace();
            }
        }

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + sqlConfig.getHost() + ":" + sqlConfig.getPort() + "?autoReconnect=true"
                            + "&useSSL=false" + "&rewriteBatchedStatements=true",
                    sqlConfig.getUsername(), sqlConfig.getPassword());

        } catch (SQLException e) {
            if (!hideErrors)
                e.printStackTrace();
        }

        if (this.isConnected()) {
            this.setupDatabase();
            this.setupTables();
        }
    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public String getType() {
        return "MYSQL";
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
        try {
            return Objects.isNull(this.connection) ? false : !this.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Create the database
     */
    private void setupDatabase() {
        try {
            String query = SQLCreateDatabase(this.sqlConfig.getDatabase());
            int result = this.connection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tables to storage data in inside of the database
     */
    private void setupTables() {

        try {
            String query = SQLCreateDatabase(this.sqlConfig.getDatabase());
            int result = this.connection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);

            query = SQLCreateTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTableRanks());
            result = this.connection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);

            query = SQLCreateTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTablePlayers());
            result = this.connection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);
        } catch (SQLException e) {
            e.printStackTrace();
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
            String query = this.SQLSelectAllInTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTableRanks());
            Statement st = this.connection.createStatement();
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
            String query = this.SQLSelectAllInTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTablePlayers());
            Statement st = this.connection.createStatement();
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
            String query = SQLDeleteAllInTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTableRanks());
            int result = this.connection.createStatement().executeUpdate(query);
            checkSQLResult(result, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement stmt = null;
        try {
            this.connection.setAutoCommit(false);
            stmt = this.connection
                    .prepareStatement(SQLInsert(this.sqlConfig.getDatabase(), this.sqlConfig.getTableRanks()));
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
                    // stmt.setString(3, serializedValue);
                    stmt.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            stmt.executeBatch();
            this.connection.commit();
            this.connection.setAutoCommit(true);
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
            this.connection.setAutoCommit(false);
            stmt = this.connection
                    .prepareStatement(SQLInsert(this.sqlConfig.getDatabase(), this.sqlConfig.getTablePlayers()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = SQLDeleteAllInTable(this.sqlConfig.getDatabase(), this.sqlConfig.getTablePlayers());
            int result = this.connection.createStatement().executeUpdate(query);
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
                    // stmt.setString(3, serializedValue);
                    stmt.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            stmt.executeBatch();
            this.connection.commit();
            this.connection.setAutoCommit(true);
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
     * Create a database if it does not exist yet
     * 
     * @param databaseName
     * @return String containing an SQL query
     */
    private String SQLCreateDatabase(String databaseName) {
        return "CREATE DATABASE IF NOT EXISTS " + databaseName;
    }

    /**
     * Create a table if it does not exist yet
     * 
     * @param databaseName
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLCreateTable(String databaseName, String tableName) {
        return "CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`" + tableName
                + "` (`keyname` VARCHAR(256) NOT NULL UNIQUE, `val` LONGTEXT NOT NULL , UNIQUE(`keyname`));";
    }

    /**
     * Insert data into a table
     * 
     * @param databaseName
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLInsert(String databaseName, String tableName) {
        return "INSERT INTO " + (databaseName.length() > 0 ? "`" + databaseName + "`." : "") + "`" + tableName
                + "` (keyname, val) VALUES (?, ?)";
    }

    /**
     * Select all data in a table
     * 
     * @param databaseName
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLSelectAllInTable(String databaseName, String tableName) {
        return "SELECT * FROM `" + databaseName + "`.`" + tableName + "`;";
    }

    /**
     * Delete all data in a table
     * 
     * @param databaseName
     * @param tableName
     * @return String containing an SQL query
     */
    private String SQLDeleteAllInTable(String databaseName, String tableName) {
        return "DELETE FROM `" + databaseName + "`.`" + tableName + "`;";
    }

    /**
     * Delete a database
     * 
     * @param databaseName
     * @return String containing an SQL query
     */
    private String SQLDropDatabase(String databaseName) {
        return "DROP DATABASE IF EXISTS " + databaseName;
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
        if (this.isConnected()) {
            try {
                String query = SQLDropDatabase(this.sqlConfig.getDatabase());
                int result = this.connection.createStatement().executeUpdate(query);
                checkSQLResult(result, query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
