package nl.svenar.powerranks.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.ErrorManager;
import nl.svenar.powerranks.utils.NMSUtils;
import nl.svenar.powerranks.utils.PermissionUtils;

public class PRPlayer {

    private Player player = null;
    private UUID uuid = null;
    private String name = "";
    private Collection<PRPermission> permissions = new ArrayList<PRPermission>();
    private Collection<PRRank> ranks = new ArrayList<PRRank>();
    private PRPermissible playerPermissible = null;
    private PermissionAttachment permissionAttachment = null;

    public PRPlayer(Player player) {
        this.setPlayer(player);
        this.setName(player.getName());
        this.setUuid(player.getUniqueId());
        
        for (PRRank rank : BaseDataHandler.getRanks()) {
            if (rank.getDefault()) {
                this.addRank(rank);
            }
        }
    }

    public PRPlayer() {
        for (PRRank rank : BaseDataHandler.getRanks()) {
            if (rank.getDefault()) {
                this.addRank(rank);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Collection<PRPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<PRPermission> permissions) {
        this.permissions = permissions;
        this.updatePermissions();
    }

    public void addPermission(PRPermission permission) {
        this.permissions.add(permission);
        this.updatePermissions();
    }

    public Collection<PRRank> getRanks() {
        return ranks;
    }

    public Collection<String> getRankNames() {
        List<String> ranks = new ArrayList<String>();
        
        for (PRRank playerRank : this.getRanks()) {
            ranks.add(playerRank.getName());
        }

        return ranks;
    }

    public void setRanks(Collection<PRRank> ranks) {
        this.ranks = ranks;
    }

    public void addRank(PRRank rank) {
        if (rank == null) {
            return;
        }
        for (PRRank existingRank : this.getRanks()) {
            if (existingRank.getName().equalsIgnoreCase(rank.getName())) {
                return;
            }
        }
        this.ranks.add(rank);
    }

    public void removeRank(PRRank rank) {
        if (rank == null) {
            return;
        }
        if (!getRanks().contains(rank)) {
            return;
        }
        this.ranks.remove(rank);
    }

    public static UUID stringToUUID(String str) {
        return UUID.fromString(str);
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public void injectPermissible() {
        if (!this.isOnline() || this.isPermissibleInjected()) {
            return;
        }

        try {
            Field permField = NMSUtils.obcClass("entity.CraftHumanEntity").getDeclaredField("perm");
            permField.setAccessible(true);

            Class<? extends Object> oldPermissible = permField.get(this.getPlayer()).getClass();
            if (!oldPermissible.getName().equals("org.bukkit.permissions.PermissibleBase") && !oldPermissible.getName().equals("nl.svenar.powerranks.data.PRPermissible")) {
                PowerRanks.getInstance().getLogger().warning("Player '" + getName() + "' is already injected with a custom permissible '" + oldPermissible.getName() + "'!");
                PowerRanks.getInstance().getLogger().warning("This is most likely caused by a different permission management plugin.");
                PowerRanks.getInstance().getLogger().warning("Please make sure that " + PowerRanks.getInstance().getDescription().getName() + " is the only permission management plugin installed on your server!");
            }
            
            playerPermissible = new PRPermissible(this);
            permField.set(this.getPlayer(), playerPermissible);
            permField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.permissionAttachment = this.player.addAttachment(PowerRanks.getInstance());
        this.updatePermissions();
    }

    public boolean isPermissibleInjected() {
        try {

            Field permField = NMSUtils.obcClass("entity.CraftHumanEntity").getDeclaredField("perm");
            permField.setAccessible(true);
            PermissibleBase permissible = (PermissibleBase) permField.get(this.getPlayer());
            permField.setAccessible(false);

            return permissible instanceof PRPermissible;
        } catch (Exception e) {
            ErrorManager.logError("An error occured while check player " + this.getPlayer().getName() + "'s ("
                    + this.getUuid().toString() + ") permissible base! Check error below for more information.");
            e.printStackTrace();
        }
        return false;
    }

    public PRPermissible getPermissible() {
        return this.playerPermissible;
    }

    public Collection<PRPermission> getAllPermissions() {
        Collection<PRPermission> permissions = new ArrayList<PRPermission>();
        List<PRRank> allApplicableRanks = new ArrayList<PRRank>();

        // ADD ALL RANKS AND ITS INHERITANCES //
        for (PRRank rank : getRanks()) {
            allApplicableRanks.add(rank);

            // ADD INHERITANCE IF NOT ALREADY IN LIST //
            for (String inheritanceName : rank.getInheritedRanks()) {
                PRRank inheritance = BaseDataHandler.getRank(inheritanceName);

                PRRank existingRank = null;

                for (PRRank currentListedRank : allApplicableRanks) {
                    if (inheritance.getName().equals(currentListedRank.getName())) {
                        existingRank = currentListedRank;
                        break;
                    }
                }

                if (existingRank == null) {
                    allApplicableRanks.add(inheritance);
                }
            }
            // ADD INHERITANCE IF NOT ALREADY IN LIST //
        }
        // ADD ALL RANKS AND ITS INHERITANCES //

        // Making sure that all ranks are sorted correctly
        Collections.sort(allApplicableRanks, (left, right) -> left.getWeight() - right.getWeight());

        for (PRRank rank : allApplicableRanks) {
            for (PRPermission permission : rank.getPermissions()) {
                PRPermission existingPermission = null;

                for (PRPermission currentListedPermission : permissions) {
                    if (permission.getName().equals(currentListedPermission.getName())) {
                        existingPermission = currentListedPermission;
                        break;
                    }
                }

                if (existingPermission != null) {
                    permissions.remove(existingPermission);
                }

                permissions.add(permission);
            }
        }

        // CHECK PLAYERS OWN PERMISSIONS //
        for (PRPermission permission : getPermissions()) {
            PRPermission existingPermission = null;

            for (PRPermission currentListedPermission : permissions) {
                if (permission.getName().equals(currentListedPermission.getName())) {
                    existingPermission = currentListedPermission;
                    break;
                }
            }

            if (existingPermission != null) {
                permissions.remove(existingPermission);
            }

            permissions.add(permission);
        }
        // CHECK PLAYERS OWN PERMISSIONS //

        // MULTI DIMENSIONAL PERMISSIONS //
        List<PRPermission> permissionsToRemove = new ArrayList<PRPermission>();

        for (PRPermission permission : permissions) {
            if (PermissionUtils.isMultiDimensional(permission.getName())) {
                permissionsToRemove.add(permission);
            }
        }

        for (PRPermission permission : permissionsToRemove) {
            permissions.remove(permission);
            for (String multiDimensionalPermission : PermissionUtils.generateMultiDimensionalPermissions(permission.getName())) {
                PRPermission newPermission = new PRPermission(multiDimensionalPermission);
                newPermission.setAllowed(permission.isAllowed(null));
                newPermission.setWorlds(permission.getWorlds());
                permissions.add(newPermission);
            }
        }
        // MULTI DIMENSIONAL PERMISSIONS //

        return permissions;
    }

    public boolean isPermissionAllowed(String permission, World world) {
        for (PRPermission prPermission : getAllPermissions()) {
            if (prPermission.getPermission().getName().equals(permission)) {
                return prPermission.isAllowed(world);
            }
        }
        return false;
    }

    public boolean isPermissionAllowed(Permission permission, World world) {
        return isPermissionAllowed(permission.getName(), world);
    }

    public void updatePermissions() {
        if (this.permissionAttachment != null) {
            for (PRPermission permission : getAllPermissions()) {
                this.permissionAttachment.setPermission(permission.getPermission(), permission.isAllowed(null));
            }
            this.player.updateCommands();
        }
    }

    public boolean isOP() {
        return this.getPlayer() != null ? this.getPlayer().isOp() : false;
    }

    @Override
    public String toString() {
        String output = "";

        output += "{";
        output += "uuid: " + getUuid() + ", ";
        output += "name: " + getName() + ", ";
        output += "ranks: ";
        for (PRRank rank : getRanks()) {
            output += rank.toString() + ", ";
        }
        output += "permissions: {";
        for (PRPermission permission : getPermissions()) {
            output += permission.toString() + ", ";
        }
        output += "}";
        output += "}";

        output = output.replaceAll(", ]", "]").replaceAll(", }", "}");

        return output;
    }
}
