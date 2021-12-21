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

package nl.svenar.common.storage;

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
     */
    public PowerSQLConfiguration(String host, int port, String database, String username, String password,
            boolean useSSL,
            String tableRanks, String tablePlayers) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.useSSL = useSSL;
        this.tableRanks = tableRanks;
        this.tablePlayers = tablePlayers;
    }

    /**
     * 
     * @return Database host (ip or hostname)
     */
    public String getHost() {
        return this.host;
    }

    /**
     * 
     * @return Database port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * 
     * @return Database name
     */
    public String getDatabase() {
        return this.database;
    }

    /**
     * 
     * @return Database username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 
     * @return Database password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 
     * @return Whether to use SSL for connecting
     */
    public boolean isUsingSSL() {
        return this.useSSL;
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
     * Get the name used to store all player data in
     * 
     * @return Table name for players
     */
    public String getTablePlayers() {
        return this.tablePlayers;
    }
}
