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

package nl.svenar.powerranks.common.storage;

/**
 * Basic SQL connection data structure to store common connection information.
 * 
 * @author svenar
 */
public class PowerSQLConfiguration {
    
    private String host;

    private int port;

    private String database;

    private String username;

    private String password;

    private boolean useSSL;

    private String tableRanks;

    private String tablePlayers;

    private String tableMessages;

    private boolean silentErrors;

    /**
     * Constructor to initialize SQL connection data
     * 
     * @param host
     * @param port
     * @param database
     * @param username
     * @param password
     * @param useSSL
     * @param tableRanks
     * @param tablePlayers
     * @param tableMessages
     */
    public PowerSQLConfiguration() {
        this.host = "";
        this.port = 0;
        this.database = "";
        this.username = "";
        this.password = "";
        this.useSSL = false;
        this.tableRanks = "";
        this.tablePlayers = "";
        this.tableMessages = "";
        this.silentErrors = false;
    }

    /**
     * Set the database host (ip or hostname)
     * 
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 
     * @return Database host (ip or hostname)
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Set the database port
     * 
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 
     * @return Database port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Set the database name
     * 
     * @param database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * 
     * @return Database name
     */
    public String getDatabase() {
        return this.database;
    }

    /**
     * Set the database username
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return Database username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the database password
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return Database password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set whether to use SSL for connecting
     * 
     * @param useSSL
     */
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    /**
     * 
     * @return Whether to use SSL for connecting
     */
    public boolean isUsingSSL() {
        return this.useSSL;
    }

    /**
     * Set the name used to store all rank data in
     * 
     * @param tableRanks
     */
    public void setTableRanks(String tableRanks) {
        this.tableRanks = tableRanks;
    }

    /**
     * Get the name used to store all rank data in
     * 
     * @return Table name for ranks
     */
    public String getTableRanks() {
        return this.tableRanks;
    }

    /**
     * Set the name used to store all player data in
     * 
     * @param tablePlayers
     */
    public void setTablePlayers(String tablePlayers) {
        this.tablePlayers = tablePlayers;
    }

    /**
     * Get the name used to store all player data in
     * 
     * @return Table name for players
     */
    public String getTablePlayers() {
        return this.tablePlayers;
    }

    /**
     * Set the name used to store all message data in
     * 
     * @param tableMessages
     */
    public void setTableMessages(String tableMessages) {
        this.tableMessages = tableMessages;
    }

    /**
     * Get the name used to store all message data in
     * 
     * @return Table name for messages
     */
    public String getTableMessages() {
        return this.tableMessages;
    }

    /**
     * Set whether to silently ignore errors
     * 
     * @param silentErrors
     */
    public void setSilentErrors(boolean silentErrors) {
        this.silentErrors = silentErrors;
    }

    /**
     * Get whether to silently ignore errors
     * 
     * @return Whether to silently ignore errors
     */
    public boolean silentErrors() {
        return this.silentErrors;
    }
}
