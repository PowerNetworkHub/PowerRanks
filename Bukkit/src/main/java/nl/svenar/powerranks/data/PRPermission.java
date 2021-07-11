package nl.svenar.powerranks.data;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.permissions.Permission;

public class PRPermission {
    
    private Permission permission = null;
    private boolean allowed = true;
    private Collection<String> worlds = new ArrayList<String>();

    public PRPermission(Permission permission) {
        this.setPermission(permission);
    }

    public PRPermission(String permissionnode) {
        this.setPermission(permissionnode);
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setPermission(String permissionnode) {
        Permission permission = new Permission(permissionnode);
        this.permission = permission;
    }

    public String getName() {
        return this.permission.getName();
    }

    public boolean isAllowed(World targetWorld) {
        boolean inWorld = false;

        if (targetWorld != null) {
            for (String worldName : getWorlds()) {
                World world = Bukkit.getServer().getWorld(worldName);
                if (world != null) {
                    if (world.getUID().toString().equals(targetWorld.getUID().toString())) {
                        inWorld = true;
                        break;
                    }
                }
            }
        }

        return getWorlds().size() == 0 || targetWorld == null ? allowed : inWorld && allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public Collection<String> getWorlds() {
        return worlds;
    }

    public void setWorlds(Collection<String> worlds) {
        this.worlds = worlds;
    }

    public void addWorld(String worldName) {
        this.worlds.add(worldName);
    }

    @Override
    public String toString() {
        String output = "";

        output += "name: " + getName() + ", ";
        output += "options: " + "{";
        output += "allowed: " + isAllowed(null) + ", ";
        output += "worlds: " + "[";
        for (String world : getWorlds()) {
            output += world + ", ";
        }
        output += "]";
        output += "}";

        output = output.replaceAll(", ]", "]").replaceAll(", }", "}");

        return output;
    }
}
