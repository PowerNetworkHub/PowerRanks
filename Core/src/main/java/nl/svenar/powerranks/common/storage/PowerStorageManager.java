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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nl.svenar.powerranks.common.serializer.PRSerializer;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

/**
 * Storage core that can be implemented in multiple storage methods (for
 * example, but not limited to: YAML and JSON)
 * 
 * @author svenar
 */
public abstract class PowerStorageManager {

    private List<PRRank> ranks = new ArrayList<PRRank>();
    private List<PRPlayer> players = new ArrayList<PRPlayer>();
    private PRSerializer serializer = new PRSerializer();

    public PowerStorageManager() {
    }

    /**
     * Get the type of the storage manager.
     * 
     * @return String storage type
     */
    public abstract String getType();

    /**
     * Get a PRSerializer instance
     * 
     * @return PRSerializer instance
     */
    public PRSerializer getSerializer() {
        return this.serializer;
    }

    /**
     * Load all objects corresponding to this storage manager.
     */
    public void loadAll() {
        this.loadRanks();
        this.loadPlayers();
    }

    /**
     * Save all objects corresponding to this storage manager.
     */
    public void saveAll() {
        saveRanks();
        savePlayers();
    }

    /**
     * Check if the storage method is connected. Returns true if a file base storage
     * is used.
     * 
     * Only used by external storage implementations like databases.
     * 
     * @return true if connected, false otherwise
     */
    public abstract boolean isConnected();

    /**
     * Load all ranks from the storage method, can be retrieved by calling
     * getRanks()
     */
    public abstract void loadRanks();

    /**
     * Load all players from the storage method, can be retrieved by calling
     * getPlayers()
     */
    public abstract void loadPlayers();

    /**
     * Save all ranks using the storage managers storage solution.
     */
    public abstract void saveRanks();

    /**
     * Save all players using the storage managers storage solution.
     */
    public abstract void savePlayers();

    /**
     * Add a single rank to the storage managers storage solution.
     * 
     * @param rank
     */
    public abstract void saveRank(PRRank rank);

    /**
     * Add a single player to the storage managers storage solution.
     * 
     * @param player
     */
    public abstract void savePlayer(PRPlayer player);

    /**
     * Delete all data from the storage manager.
     */
    public abstract void removeAllData();

    /**
     * Get all saved ranks
     * 
     * @return List of saved ranks
     */
    public List<PRRank> getRanks() {
        return this.ranks;
    }

    /**
     * Get a rank by name
     * 
     * @param rankName
     * @return PRRank object corresponding to the supplied rank name.
     */
    public PRRank getRank(String rankName) {
        for (PRRank obj : this.ranks) {
            if (obj.getName().equalsIgnoreCase(rankName)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Add a rank to the cache
     * 
     * @param rank
     */
    public void addRank(PRRank rank) {
        this.ranks.add(rank);
    }

    /**
     * Remove a rank from the cache
     * 
     * @param rank
     */
    public void removeRank(PRRank rank) {
        this.ranks.remove(rank);
    }

    /**
     * Overwrite the cached ranks list
     * 
     * @param ranks
     */
    public void setRanks(List<PRRank> ranks) {
        this.ranks = ranks;
    }

    /**
     * Clear the ranks cache
     */
    public void clearRanks() {
        this.ranks = new ArrayList<PRRank>();
    }

    /**
     * Get all saved players
     * 
     * @return List of saved players
     */
    public List<PRPlayer> getPlayers() {
        return this.players;
    }

    /**
     * Get a player by name
     * 
     * @param playerName
     * @return PRPlayer instance corresponding to the supplied player name.
     */
    public PRPlayer getPlayer(String playerName) {
        for (PRPlayer obj : this.players) {
            if (obj.getName().equalsIgnoreCase(playerName)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Get a player by UUID
     * 
     * @param playerName
     * @return PRPlayer instance corresponding to the supplied player name.
     */
    public PRPlayer getPlayer(UUID playerUUID) {
        for (PRPlayer obj : this.players) {
            if (obj.getUUID().toString().equals(playerUUID.toString())) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Add a player to the cache
     * 
     * @param player
     */
    public void addPlayer(PRPlayer player) {
        this.players.add(player);
    }

    /**
     * Remove a player from the cache
     * 
     * @param player
     */
    public void removePlayer(PRPlayer player) {
        this.players.remove(player);
    }

    /**
     * Overwrite the cached players list
     * 
     * @param players
     */
    public void setPlayers(List<PRPlayer> players) {
        this.players = players;
    }

    /**
     * Clear the players cache
     */
    public void clearPlayers() {
        this.players = new ArrayList<PRPlayer>();
    }

    /**
     * Iterate over a Java map generating a key, value pair where nested Java maps
     * become part of the key. (For example {rank1: {name: "rankname"}} becomes:
     * rank1.name=rankname)
     * 
     * out is the map where all key, value pairs are writen too. KeyPart is the
     * 'master key' that will be added to the beginning of every key.
     * 
     * @param out
     * @param map
     * @param keyPart
     */
    @SuppressWarnings("unchecked")
    public void iterate(Map<String, Object> out, Map<String, Object> map, String keyPart) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                iterate(out, (Map<String, Object>) entry.getValue(), keyPart + "." + entry.getKey());
            } else {
                String key = keyPart + "." + entry.getKey();
                key = key.replaceFirst("\\.", "");
                out.put(key, entry.getValue());
            }
        }
    }
}
