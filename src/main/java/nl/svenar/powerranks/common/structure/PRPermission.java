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

package nl.svenar.powerranks.common.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

/**
 * Structure to store permission data.
 * 
 * @author svenar
 */
public class PRPermission {

    private String name;
    private boolean value;
    private long expires;
    private Map<String, String> properties;

    /**
     * Get the permission node
     * 
     * @return String with the permission node
     */
    public String getName() {
        return this.name;
    }

    /**
     * Update the permissoin node
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get ifa permission is allowed or not
     * 
     * @return true if allowed, false otherwise
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Change if a permission is allowed or not
     * 
     * @param value
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Get the time when the permission expires in the Unix epoch format
     * 
     * @return Unix epoch
     */
    public long getExpires() {
        return this.expires;
    }

    /**
     * Set when the permission expires in the Unix epoch format
     * 
     * @param expires
     */
    public void setExpires(long expires) {
        this.expires = expires;
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
