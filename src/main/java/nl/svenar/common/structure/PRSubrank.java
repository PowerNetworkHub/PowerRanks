package nl.svenar.common.structure;

import java.util.ArrayList;
import java.util.Objects;

public class PRSubrank {

    private String rank;
    private ArrayList<String> worlds;
    private boolean usePrefix;
    private boolean useSuffix;
    private boolean usePermissions;

    public PRSubrank() {
        rank = "";
        worlds = new ArrayList<String>();
        usePrefix = true;
        useSuffix = true;
        usePermissions = true;

        worlds.add("All");
    }

    /**
     * Get the name of the rank in this subrank
     * 
     * @return String name of the rank
     */
    public String getName() {
        return this.rank;
    }

    /**
     * Set the name of the rank in this subrank
     * 
     * @param rank
     */
    public void setName(String rank) {
        this.rank = rank;
    }

    /**
     * Get a list of all stored worlds in this subrank
     * 
     * @return Java ArrayList with all String instances
     */
    public ArrayList<String> getWorlds() {
        return this.worlds;
    }

    /**
     * Overwrite all stored worlds with the provided Java ArrayList
     * 
     * @param worlds
     */
    public void setWorlds(ArrayList<String> worlds) {
        this.worlds = worlds;
    }

    /**
     * Add a String instance to this subrank
     * 
     * @param world
     */
    public void addWorld(String world) {
        if (Objects.isNull(this.worlds)) {
            this.worlds = new ArrayList<String>();
        }

        this.worlds.add(world);
    }

    /**
     * Remove a String instance from this subrank
     * 
     * @param world
     */
    public void removeWorld(String world) {
        if (!this.worlds.contains(world)) {
            return;
        }

        this.worlds.remove(world);
    }

    /**
     * Get a world by name no matter the casing, returns null if no world is found
     * 
     * @param name
     * @return String
     */
    public String getWorld(String name) {
        if (Objects.isNull(this.worlds)) {
            this.worlds = new ArrayList<String>();
        }

        for (String world : this.worlds) {
            if (world.equals(name)) {
                return world;
            }
        }

        for (String world : this.worlds) {
            if (world.equalsIgnoreCase(name)) {
                return world;
            }
        }
        return null;
    }

    /**
     * Get if prefix is being used on this subrank
     * 
     * @return true if allowed, false otherwise
     */
    public boolean getUsingPrefix() {
        return this.usePrefix;
    }

    /**
     * Set if prefix is being used on this subrank
     * 
     * @param value
     */
    public void setUsingPrefix(boolean value) {
        this.usePrefix = value;
    }

    /**
     * Get if suffix is being used on this subrank
     * 
     * @return true if allowed, false otherwise
     */
    public boolean getUsingSuffix() {
        return this.useSuffix;
    }

    /**
     * Set if suffix is being used on this subrank
     * 
     * @param value
     */
    public void setUsingSuffix(boolean value) {
        this.useSuffix = value;
    }

    /**
     * Get if permissions is being used on this subrank
     * 
     * @return true if allowed, false otherwise
     */
    public boolean getUsingPermissions() {
        return this.usePermissions;
    }

    /**
     * Set if permissions is being used on this subrank
     * 
     * @param value
     */
    public void setUsingPermissions(boolean value) {
        this.usePermissions = value;
    }
}
