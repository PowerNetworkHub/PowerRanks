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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

/**
 * Structure to store rank data.
 * 
 * @author svenar
 */
public class PRRank {

    private String name;
    private int weight;
    private long expires;
    private ArrayList<PRPermission> permissions;
    private Map<String, String> properties;

    /**
     * Get the name of this rank
     * 
     * @return String name of the rank
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this rank
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the priority of this rank
     * 
     * @return Integer weight for this rank
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Set the priority of this rank
     * 
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Get the time when the rank expires in the Unix epoch format
     * 
     * @return Unix epoch
     */
    public long getExpires() {
        return this.expires;
    }

    /**
     * Set when the rank expires in the Unix epoch format
     * 
     * @param expires
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * Get a list of all stored permissions in this rank
     * 
     * @return Java ArrayList with all PRPermission instances
     */
    public ArrayList<PRPermission> getPermissions() {
        return this.permissions;
    }

    /**
     * Overwrite all stored permissions with the provided Java ArrayList
     * 
     * @param permissions
     */
    public void setPermissions(ArrayList<PRPermission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Add a PRPermission instance to this rank
     * 
     * @param permission
     */
    public void addPermission(PRPermission permission) {
        if (Objects.isNull(this.permissions)) {
            this.permissions = new ArrayList<PRPermission>();
        }

        this.permissions.add(permission);
    }

    /**
     * Get a permission by a String permission node (Eg. permission.node.123)
     * 
     * @param name
     * @return PRPermission instance for the provided node, null if no PRPermission
     *         instance was found
     */
    public PRPermission getPermission(String name) {
        if (Objects.isNull(this.permissions)) {
            this.permissions = new ArrayList<PRPermission>();
        }

        for (PRPermission permission : this.permissions) {
            if (permission.getName().equals(name)) {
                return permission;
            }
        }
        return null;
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
