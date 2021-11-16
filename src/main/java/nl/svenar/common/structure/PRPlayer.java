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
import java.util.Objects;
import java.util.UUID;

/**
 * Structure to store player data.
 * 
 * @author svenar
 */
public class PRPlayer {

    private UUID uuid;
    private String name;
    private String rank;
    private ArrayList<PRSubrank> subranks;
    private ArrayList<PRPermission> permissions;
    private long playtime;

    public PRPlayer() {
        this.name = "";
        this.rank = "";
        this.subranks = new ArrayList<PRSubrank>();
        this.permissions = new ArrayList<PRPermission>();
        this.playtime = 0L;
    }

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
     * Get the rank of this player
     * 
     * @return String rank of the player
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * Set the rank of this player
     * 
     * @param rankname
     */
    public void setRank(String rankname) {
        this.rank = rankname;
    }

    /**
     * Get a list with all stored subrank names
     * 
     * @return Java ArrayList with all stored rank names
     */
    public ArrayList<PRSubrank> getSubRanks() {
        return this.subranks;
    }

    /**
     * Overwrite all stored subrank names with the provided Java ArrayList
     * 
     * @param subranks
     */
    public void setSubRanks(ArrayList<PRSubrank> subranks) {
        this.subranks = subranks;
    }

    /**
     * Add the name of a subrank to the player
     * 
     * @param rankname
     */
    public void addSubrank(PRSubrank subrank) {
        if (Objects.isNull(this.subranks)) {
            this.subranks = new ArrayList<PRSubrank>();
        }

        this.subranks.add(subrank);
    }

    /**
     * Remove a PRSubrank instance from this player
     * 
     * @param subrank
     */
    public void removeSubrank(PRSubrank subrank) {
        if (!this.subranks.contains(subrank)) {
            return;
        }

        this.subranks.remove(subrank);
    }

    /**
     * Check if this player instance has a specific subrank
     * 
     * @param subrank
     * @return true if this player has that rank, false otherwise
     */
    public boolean hasSubrank(String subrank) {
        if (Objects.isNull(this.subranks)) {
            this.subranks = new ArrayList<PRSubrank>();
        }

        for (PRSubrank rank : this.subranks) {
            if (rank.getName().equals(subrank)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of all stored permissions in this player
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
     * Add a PRPermission instance to this player
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
     * Remove a PRPermission instance from this player
     * 
     * @param permission
     */
    public void removePermission(PRPermission permission) {
        if (!this.permissions.contains(permission)) {
            return;
        }

        this.permissions.remove(permission);
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
     * Get the playtime of this player
     * 
     * @return Long playtime of the player
     */
    public long getPlaytime() {
        return this.playtime;
    }

    /**
     * Set the playtime of this player
     * 
     * @param playtime
     */
    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }
}
