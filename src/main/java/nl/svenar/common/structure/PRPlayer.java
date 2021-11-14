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

package nl.svenar.common.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;

/**
 * Structure to store player data.
 * 
 * @author svenar
 */
public class PRPlayer {

    private UUID uuid;
    private String name;
    private ArrayList<String> ranknames;
    private Map<String, String> properties;

    /**
     * Get the unique id of this player
     * 
     * @return Java UUID object for this player
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Set the unique id of this player
     * 
     * @param uuid
     */
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the name of this player
     * 
     * @return String name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this player
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get a list with all stored rank names
     * 
     * @return Java ArrayList with all stored rank names
     */
    public ArrayList<String> getRankNames() {
        return this.ranknames;
    }

    /**
     * Overwrite all stored rank names with the provided Java ArrayList
     * 
     * @param ranknames
     */
    public void setRankNames(ArrayList<String> ranknames) {
        this.ranknames = ranknames;
    }

    /**
     * Add the name of a rank to the player
     * 
     * @param rankname
     */
    public void addRankName(String rankname) {
        if (Objects.isNull(this.ranknames)) {
            this.ranknames = new ArrayList<String>();
        }

        this.ranknames.add(rankname);
    }

    /**
     * Check if this player instance has a specific rank
     * 
     * @param rankname
     * @return true if this player has that rank, false otherwise
     */
    public boolean hasRankName(String rankname) {
        if (Objects.isNull(this.ranknames)) {
            this.ranknames = new ArrayList<String>();
        }

        for (String rank : this.ranknames) {
            if (rank.equals(rankname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a key, value map with all stored properties
     * 
     * @return Java Map with all properties and its values
     */
    public Map<String, String> getProperties() {
        return this.properties;
    }

    /**
     * Overwrite all stored properties with the provided Java map
     * 
     * @param properties
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Add a new property to the stored properties and set its value
     * 
     * @param property
     * @param value
     */
    public void addProperty(String property, String value) {
        if (Objects.isNull(this.properties)) {
            this.properties = new HashMap<String, String>();
        }

        this.properties.put(property, value);
    }

    /**
     * Get a value of a stored property, returns null if a property with the
     * provided name is not found
     * 
     * @param property
     * @return String value of the property
     */
    public String getProperty(String property) {
        if (Objects.isNull(this.properties)) {
            this.properties = new HashMap<String, String>();
        }

        for (Entry<String, String> entry : this.properties.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
