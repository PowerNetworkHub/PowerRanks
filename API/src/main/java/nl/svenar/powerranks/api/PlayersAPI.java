package nl.svenar.powerranks.api;

import java.util.Set;
import java.util.UUID;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;

public class PlayersAPI {

    // ================================================================================
    // ||                                                                            ||
    // ||                                GET PRPLAYER                                ||
    // ||                                                                            ||
    // ================================================================================
    
    /**
     * Get a PRPlayer object by UUID
     * 
     * @param UUID
     * @return PRPlayer
     */
    public PRPlayer get(UUID uuid) {
        return get(uuid.toString());
    }

    /**
     * Get a PRPlayer object by identiefier
     * 
     * @param identifier
     * @return PRPlayer
     */
    public PRPlayer get(String identifier) {
        return PRCache.getPlayer(identifier);
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                    NAME                                    ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Change the name of a PRPlayer
     * 
     * @param UUID
     * @param name
     * @return true if player is not null
     */
    public boolean setName(UUID uuid, String name) {
        return setName(uuid.toString(), name);
    }

    /**
     * Change the name of a PRPlayer
     * 
     * @param identifier
     * @param name
     * @return true if player is not null
     */
    public boolean setName(String identifier, String name) {
        return setName(get(identifier), name);
    }

    /**
     * Change the name of a PRPlayer
     * 
     * @param PRPlayer
     * @param name
     * @return true if player is not null
     */
    public boolean setName(PRPlayer player, String name) {
        if (player == null) {
            return false;
        }

        player.setName(name);
        return true;
    }

    /**
     * Get the name of a PRPlayer
     * 
     * @param UUID
     * @return String or null if player is null
     */
    public String getName(UUID uuid) {
        return getName(uuid.toString());
    }

    /**
     * Get the name of a PRPlayer
     * 
     * @param identifier
     * @return String or null if player is null
     */
    public String getName(String identifier) {
        return getName(get(identifier));
    }

    /**
     * Get the name of a PRPlayer
     * 
     * @param PRPlayer
     * @return String or null if player is null
     */
    public String getName(PRPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getName();
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                  NICKNAME                                  ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Change the nickname of a PRPlayer
     * 
     * @param UUID
     * @param nickname
     * @return true if player is not null
     */
    public boolean setNickName(UUID uuid, String nickname) {
        return setNickName(uuid.toString(), nickname);
    }

    /**
     * Change the nickname of a PRPlayer
     * 
     * @param identifier
     * @param nickname
     * @return true if player is not null
     */
    public boolean setNickName(String identifier, String nickname) {
        return setNickName(get(identifier), nickname);
    }

    /**
     * Change the nickname of a PRPlayer
     * 
     * @param PRPlayer
     * @param nickname
     * @return true if player is not null
     */
    public boolean setNickName(PRPlayer player, String nickname) {
        if (player == null) {
            return false;
        }

        player.setNickname(nickname);
        return true;
    }

    /**
     * Get the nickname of a PRPlayer
     * 
     * @param UUID
     * @return String or null if player is null
     */
    public String getNickName(UUID uuid) {
        return getNickName(uuid.toString());
    }

    /**
     * Get the nickname of a PRPlayer
     * 
     * @param identifier
     * @return String or null if player is null
     */
    public String getNickName(String identifier) {
        return getNickName(get(identifier));
    }

    /**
     * Get the nickname of a PRPlayer
     * 
     * @param PRPlayer
     * @return String or null if player is null
     */
    public String getNickName(PRPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getNickname();
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                  PLAYTIME                                  ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Change the playtime of a PRPlayer
     * 
     * @param UUID
     * @param playtime
     * @return true if player is not null
     */
    public boolean setPlaytime(UUID uuid, long playtime) {
        return setPlaytime(uuid.toString(), playtime);
    }

    /**
     * Change the playtime of a PRPlayer
     * 
     * @param identifier
     * @param playtime
     * @return true if player is not null
     */
    public boolean setPlaytime(String identifier, long playtime) {
        return setPlaytime(get(identifier), playtime);
    }

    /**
     * Change the playtime of a PRPlayer
     * 
     * @param PRPlayer
     * @param playtime
     * @return true if player is not null
     */
    public boolean setPlaytime(PRPlayer player, long playtime) {
        if (player == null) {
            return false;
        }

        player.setPlaytime(playtime);
        return true;
    }

    /**
     * Get the playtime of a PRPlayer
     * 
     * @param UUID
     * @return long or -1 if player is null
     */
    public long getPlaytime(UUID uuid) {
        return getPlaytime(uuid.toString());
    }

    /**
     * Get the playtime of a PRPlayer
     * 
     * @param identifier
     * @return long or -1 if player is null
     */
    public long getPlaytime(String identifier) {
        return getPlaytime(get(identifier));
    }

    /**
     * Get the playtime of a PRPlayer
     * 
     * @param PRPlayer
     * @return long or -1 if player is null
     */
    public long getPlaytime(PRPlayer player) {
        if (player == null) {
            return -1;
        }

        return player.getPlaytime();
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                   RANKS                                    ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Set the rank of a PRPlayer and remove all previous ranks
     * 
     * @param UUID
     * @param rankname
     * @return true if player is not null
     */
    public boolean setRank(UUID uuid, String rankname) {
        return setRank(uuid.toString(), PRCache.getRank(rankname));
    }

    /**
     * Set the rank of a PRPlayer and remove all previous ranks
     * 
     * @param identifier
     * @param rankname
     * @return true if player is not null
     */
    public boolean setRank(String identifier, String rankname) {
        return setRank(get(identifier), PRCache.getRank(rankname));
    }

    /**
     * Set the rank of a PRPlayer and remove all previous ranks
     * 
     * @param UUID
     * @param PRRank
     * @return true if player is not null
     */
    public boolean setRank(UUID uuid, PRRank rank) {
        return setRank(uuid.toString(), rank);
    }

    /**
     * Set the rank of a PRPlayer and remove all previous ranks
     * 
     * @param identifier
     * @param PRRank
     * @return true if player is not null
     */
    public boolean setRank(String identifier, PRRank rank) {
        return setRank(get(identifier), rank);
    }

    /**
     * Set the rank of a PRPlayer and remove all previous ranks
     * 
     * @param PRPlayer
     * @param PRRank
     * @return true if player is not null
     */
    public boolean setRank(PRPlayer player, PRRank rank) {
        if (player == null) {
            return false;
        }

        player.getRanks().clear();
        player.getRanks().add(new PRPlayerRank(rank));
        return true;
    }

    /**
     * Add a rank to a PRPlayer
     * 
     * @param UUID
     * @param rankname
     * @return true if the rank was added to the player, false if the rank already exists or player is null
     */
    public boolean addRank(UUID uuid, String rankname) {
        return addRank(uuid.toString(), PRCache.getRank(rankname));
    }

    /**
     * Add a rank to a PRPlayer
     * 
     * @param identifier
     * @param rankname
     * @return true if the rank was added to the player, false if the rank already exists or player is null
     */
    public boolean addRank(String identifier, String rankname) {
        return addRank(get(identifier), PRCache.getRank(rankname));
    }

    /**
     * Add a rank to a PRPlayer
     * 
     * @param UUID
     * @param PRRank
     * @return true if the rank was added to the player, false if the rank already exists or player is null
     */
    public boolean addRank(UUID uuid, PRRank rank) {
        return addRank(uuid.toString(), rank);
    }

    /**
     * Add a rank to a PRPlayer
     * 
     * @param identifier
     * @param PRRank
     * @return true if the rank was added to the player, false if the rank already exists or player is null
     */
    public boolean addRank(String identifier, PRRank rank) {
        return addRank(get(identifier), rank);
    }

    /**
     * Add a rank to a PRPlayer
     * 
     * @param PRPlayer
     * @param PRRank
     * @return true if the rank was added to the player, false if the rank already exists or player is null
     */
    public boolean addRank(PRPlayer player, PRRank rank) {
        if (player == null) {
            return false;
        }

        for (PRPlayerRank prPlayerRank : player.getRanks()) {
            if (prPlayerRank.getName().equalsIgnoreCase(rank.getName())) {
                return false;
            }
        }

        player.getRanks().add(new PRPlayerRank(rank));
        return true;
    }

    /**
     * Get all ranks of a PRPlayer
     * 
     * @param UUID
     * @return Set<PRPlayerRank> or null if player is null
     */
    public Set<PRPlayerRank> getRanks(UUID uuid) {
        return getRanks(uuid.toString());
    }

    /**
     * Get all ranks of a PRPlayer
     * 
     * @param identifier
     * @return Set<PRPlayerRank> or null if player is null
     */
    public Set<PRPlayerRank> getRanks(String identifier) {
        return getRanks(get(identifier));
    }

    /**
     * Get all ranks of a PRPlayer
     * 
     * @param PRPlayer
     * @return Set<PRPlayerRank> or null if player is null
     */
    public Set<PRPlayerRank> getRanks(PRPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getRanks();
    }

    /**
     * Remove a rank from a PRPlayer
     * 
     * @param UUID
     * @param rankname
     * @return true if the rank was removed from the player, false if the rank was not found or player is null
     */
    public boolean removeRank(UUID uuid, String rankname) {
        return removeRank(uuid.toString(), PRCache.getRank(rankname));
    }

    /**
     * Remove a rank from a PRPlayer
     * 
     * @param identifier
     * @param rankname
     * @return true if the rank was removed from the player, false if the rank was not found or player is null
     */
    public boolean removeRank(String identifier, String rankname) {
        return removeRank(get(identifier), PRCache.getRank(rankname));
    }

    /**
     * Remove a rank from a PRPlayer
     * 
     * @param UUID
     * @param PRRank
     * @return true if the rank was removed from the player, false if the rank was not found or player is null
     */
    public boolean removeRank(UUID uuid, PRRank rank) {
        return removeRank(uuid.toString(), rank);
    }

    /**
     * Remove a rank from a PRPlayer
     * 
     * @param identifier
     * @param PRRank
     * @return true if the rank was removed from the player, false if the rank was not found or player is null
     */
    public boolean removeRank(String identifier, PRRank rank) {
        return removeRank(get(identifier), rank);
    }

    /**
     * Remove a rank from a PRPlayer
     * 
     * @param PRPlayer
     * @param PRRank
     * @return true if the rank was removed from the player, false if the rank was not found or player is null
     */
    public boolean removeRank(PRPlayer player, PRRank rank) {
        if (player == null) {
            return false;
        }

        for (PRPlayerRank prPlayerRank : player.getRanks()) {
            if (prPlayerRank.getName().equalsIgnoreCase(rank.getName())) {
                player.getRanks().remove(prPlayerRank);
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a PRPlayer has a rank
     * 
     * @param UUID
     * @param rankname
     * @return true if the player has the rank, false if the player does not have the rank or player is null
     */
    public boolean hasRank(UUID uuid, String rankname) {
        return hasRank(uuid.toString(), PRCache.getRank(rankname));
    }

    /**
     * Check if a PRPlayer has a rank
     * 
     * @param identifier
     * @param rankname
     * @return true if the player has the rank, false if the player does not have the rank or player is null
     */
    public boolean hasRank(String identifier, String rankname) {
        return hasRank(get(identifier), PRCache.getRank(rankname));
    }

    /**
     * Check if a PRPlayer has a rank
     * 
     * @param UUID
     * @param PRRank
     * @return true if the player has the rank, false if the player does not have the rank or player is null
     */
    public boolean hasRank(UUID uuid, PRRank rank) {
        return hasRank(uuid.toString(), rank);
    }

    /**
     * Check if a PRPlayer has a rank
     * 
     * @param identifier
     * @param PRRank
     * @return true if the player has the rank, false if the player does not have the rank or player is null
     */
    public boolean hasRank(String identifier, PRRank rank) {
        return hasRank(get(identifier), rank);
    }

    /**
     * Check if a PRPlayer has a rank
     * 
     * @param PRPlayer
     * @param PRRank
     * @return true if the player has the rank, false if the player does not have the rank or player is null
     */
    public boolean hasRank(PRPlayer player, PRRank rank) {
        if (player == null) {
            return false;
        }

        for (PRPlayerRank prPlayerRank : player.getRanks()) {
            if (prPlayerRank.getName().equalsIgnoreCase(rank.getName())) {
                return true;
            }
        }

        return false;
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                 PERMISSIONS                                ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Add a permission to a PRPlayer
     * 
     * @param UUID
     * @param permissionNode
     * @param isAllowed
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(UUID uuid, String permissionNode, boolean isAllowed) {
        return addPermission(uuid.toString(), permissionNode, isAllowed);
    }

    /**
     * Add a permission to a PRPlayer
     * 
     * @param identifier
     * @param permissionNode
     * @param isAllowed
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(String identifier, String permissionNode, boolean isAllowed) {
        return addPermission(get(identifier), permissionNode, isAllowed);
    }

    /**
     * Add a permission to a PRPlayer
     * 
     * @param PRPlayer
     * @param permissionNode
     * @param isAllowed
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(PRPlayer player, String permissionNode, boolean isAllowed) {
        return addPermission(player, new PRPermission(permissionNode, isAllowed));
    }

    /**
     * Add a permission to a PRPlayer
     * 
     * @param UUID
     * @param PRPermission
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(UUID uuid, PRPermission permission) {
        return addPermission(uuid.toString(), permission);
    }

    /**
     * Add a permission to a PRPlayer
     * 
     * @param identifier
     * @param PRPermission
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(String identifier, PRPermission permission) {
        return addPermission(get(identifier), permission);
    }

    /**
     * Add a permission to a PRPlayer
     * 
     * @param PRPlayer
     * @param PRPermission
     * @return true if the permission was added, false if the permission already exists or player is null
     */
    public boolean addPermission(PRPlayer player, PRPermission permission) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equalsIgnoreCase(permission.getName())) {
                return false;
            }
        }

        player.getPermissions().add(permission);
        return true;
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param UUID
     * @param permissionNode
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(UUID uuid, String permissionNode) {
        return removePermission(uuid.toString(), permissionNode);
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param identifier
     * @param permissionNode
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(String identifier, String permissionNode) {
        return removePermission(get(identifier), permissionNode);
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param PRPlayer
     * @param permissionNode
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(PRPlayer player, String permissionNode) {
        return removePermission(player, new PRPermission(permissionNode, false));
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param UUID
     * @param PRPermission
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(UUID uuid, PRPermission permission) {
        return removePermission(uuid.toString(), permission);
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param identifier
     * @param PRPermission
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(String identifier, PRPermission permission) {
        return removePermission(get(identifier), permission);
    }

    /**
     * Remove a permission from a PRPlayer
     * 
     * @param PRPlayer
     * @param PRPermission
     * @return true if the permission was removed from the player, false if the permission was not found or player is null
     */
    public boolean removePermission(PRPlayer player, PRPermission permission) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equalsIgnoreCase(permission.getName())) {
                player.getPermissions().remove(prPermission);
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a PRPlayer has a permission
     * 
     * @param UUID
     * @param permissionNode
     * @return true if player has the permission, false if player does not have the permission or player is null
     */
    public boolean hasPermission(UUID uuid, String permissionNode) {
        return hasPermission(uuid.toString(), permissionNode);
    }

    /**
     * Check if a PRPlayer has a permission
     * 
     * @param identifier
     * @param permissionNode
     * @return true if player has the permission, false if player does not have the permission or player is null
     */
    public boolean hasPermission(String identifier, String permissionNode) {
        return hasPermission(get(identifier), permissionNode);
    }

    /**
     * Check if a PRPlayer has a permission
     * 
     * @param PRPlayer
     * @param permissionNode
     * @return true if player has the permission, false if player does not have the permission or player is null
     */
    public boolean hasPermission(PRPlayer player, String permissionNode) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equals(permissionNode)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a PRPlayer's permission is allowed
     * 
     * @param UUID
     * @param PRPermission
     * @return true if permission is allowed, false if permission is not allowed or not found
     */
    public boolean isPermissionAllowed(UUID uuid, String permissionNode) {
        return isPermissionAllowed(uuid.toString(), permissionNode);
    }

    /**
     * Check if a PRPlayer's permission is allowed
     * 
     * @param identifier
     * @param PRPermission
     * @return true if permission is allowed, false if permission is not allowed or not found
     */
    public boolean isPermissionAllowed(String identifier, String permissionNode) {
        return isPermissionAllowed(get(identifier), permissionNode);
    }

    /**
     * Check if a PRPlayer's permission is allowed
     * 
     * @param PRPlayer
     * @param PRPermission
     * @return true if permission is allowed, false if permission is not allowed or not found
     */
    public boolean isPermissionAllowed(PRPlayer player, String permissionNode) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equals(permissionNode)) {
                return prPermission.getValue();
            }
        }

        return false;
    }

    /**
     * Set a PRPlayer's permission to allowed
     * 
     * @param UUID
     * @param permissionNode
     * @return true if permission was set to allowed, false if permission was not found or player is null
     */
    public boolean setPermissionAllowed(UUID uuid, String permissionNode) {
        return setPermissionAllowed(uuid.toString(), permissionNode);
    }

    /**
     * Set a PRPlayer's permission to allowed
     * 
     * @param identifier
     * @param permissionNode
     * @return true if permission was set to allowed, false if permission was not found or player is null
     */
    public boolean setPermissionAllowed(String identifier, String permissionNode) {
        return setPermissionAllowed(get(identifier), permissionNode);
    }

    /**
     * Set a PRPlayer's permission to allowed
     * 
     * @param PRPlayer
     * @param permissionNode
     * @return true if permission was set to allowed, false if permission was not found or player is null
     */
    public boolean setPermissionAllowed(PRPlayer player, String permissionNode) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equals(permissionNode)) {
                prPermission.setValue(true);
                return true;
            }
        }

        return false;
    }

    /**
     * Set a PRPlayer's permission to disallowed
     * 
     * @param UUID
     * @param PRPermission
     * @return true if permission was set to disallowed, false if permission was not found or player is null
     */
    public boolean setPermissionDisallowed(UUID uuid, String permissionNode) {
        return setPermissionDisallowed(uuid.toString(), permissionNode);
    }

    /**
     * Set a PRPlayer's permission to disallowed
     * 
     * @param identifier
     * @param PRPermission
     * @return true if permission was set to disallowed, false if permission was not found or player is null
     */
    public boolean setPermissionDisallowed(String identifier, String permissionNode) {
        return setPermissionDisallowed(get(identifier), permissionNode);
    }

    /**
     * Set a PRPlayer's permission to disallowed
     * 
     * @param PRPlayer
     * @param PRPermission
     * @return true if permission was set to disallowed, false if permission was not found or player is null
     */
    public boolean setPermissionDisallowed(PRPlayer player, String permissionNode) {
        if (player == null) {
            return false;
        }

        for (PRPermission prPermission : player.getPermissions()) {
            if (prPermission.getName().equals(permissionNode)) {
                prPermission.setValue(false);
                return true;
            }
        }

        return false;
    }

    /**
     * Get all permissions of a PRPlayer
     * 
     * @param UUID
     * @return Set<PRPermission> or null if player is null
     */
    public Set<PRPermission> getPermissions(UUID uuid) {
        return getPermissions(uuid.toString());
    }

    /**
     * Get all permissions of a PRPlayer
     * 
     * @param identifier
     * @return Set<PRPermission> or null if player is null
     */
    public Set<PRPermission> getPermissions(String identifier) {
        return getPermissions(get(identifier));
    }

    /**
     * Get all permissions of a PRPlayer
     * 
     * @param PRPlayer
     * @return Set<PRPermission> or null if player is null
     */
    public Set<PRPermission> getPermissions(PRPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getPermissions();
    }

    // ================================================================================
    // ||                                                                            ||
    // ||                                  USERTAGS                                  ||
    // ||                                                                            ||
    // ================================================================================

    /**
     * Add a usertag to a PRPlayer
     * 
     * @param UUID
     * @param usertag
     * @return true if the usertag was added, false if the usertag already exists or player is null
     */
    public boolean addUsertag(UUID uuid, String usertag) {
        return addUsertag(uuid.toString(), usertag);
    }

    /**
     * Add a usertag to a PRPlayer
     * 
     * @param identifier
     * @param usertag
     * @return true if the usertag was added, false if the usertag already exists or player is null
     */
    public boolean addUsertag(String identifier, String usertag) {
        return addUsertag(get(identifier), usertag);
    }

    /**
     * Add a usertag to a PRPlayer
     * 
     * @param PRPlayer
     * @param usertag
     * @return true if the usertag was added, false if the usertag already exists or player is null
     */
    public boolean addUsertag(PRPlayer player, String usertag) {
        if (player == null) {
            return false;
        }

        for (String tag : player.getUsertags()) {
            if (tag.equalsIgnoreCase(usertag)) {
                return false;
            }
        }

        player.getUsertags().add(usertag);
        return true;
    }

    /**
     * Remove a usertag from a PRPlayer
     * 
     * @param UUID
     * @param usertag
     * @return true if the usertag was removed from the player, false if the usertag was not found or player is null
     */
    public boolean removeUsertag(UUID uuid, String usertag) {
        return removeUsertag(uuid.toString(), usertag);
    }

    /**
     * Remove a usertag from a PRPlayer
     * 
     * @param identifier
     * @param usertag
     * @return true if the usertag was removed from the player, false if the usertag was not found or player is null
     */
    public boolean removeUsertag(String identifier, String usertag) {
        return removeUsertag(get(identifier), usertag);
    }

    /**
     * Remove a usertag from a PRPlayer
     * 
     * @param PRPlayer
     * @param usertag
     * @return true if the usertag was removed from the player, false if the usertag was not found or player is null
     */
    public boolean removeUsertag(PRPlayer player, String usertag) {
        if (player == null) {
            return false;
        }

        for (String tag : player.getUsertags()) {
            if (tag.equalsIgnoreCase(usertag)) {
                player.getUsertags().remove(tag);
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a PRPlayer has a usertag
     * 
     * @param UUID
     * @param usertag
     * @return true if player has the usertag, false if player does not have the usertag or player is null
     */
    public boolean hasUsertag(UUID uuid, String usertag) {
        return hasUsertag(uuid.toString(), usertag);
    }

    /**
     * Check if a PRPlayer has a usertag
     * 
     * @param identifier
     * @param usertag
     * @return true if player has the usertag, false if player does not have the usertag or player is null
     */
    public boolean hasUsertag(String identifier, String usertag) {
        return hasUsertag(get(identifier), usertag);
    }

    /**
     * Check if a PRPlayer has a usertag
     * 
     * @param PRPlayer
     * @param usertag
     * @return true if player has the usertag, false if player does not have the usertag or player is null
     */
    public boolean hasUsertag(PRPlayer player, String usertag) {
        if (player == null) {
            return false;
        }

        for (String tag : player.getUsertags()) {
            if (tag.equalsIgnoreCase(usertag)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all usertags of a PRPlayer
     * 
     * @param UUID
     * @return Set<String> or null if player is null
     */
    public Set<String> getUsertags(UUID uuid) {
        return getUsertags(uuid.toString());
    }

    /**
     * Get all usertags of a PRPlayer
     * 
     * @param identifier
     * @return Set<String> or null if player is null
     */
    public Set<String> getUsertags(String identifier) {
        return getUsertags(get(identifier));
    }

    /**
     * Get all usertags of a PRPlayer
     * 
     * @param PRPlayer
     * @return Set<String> or null if player is null
     */
    public Set<String> getUsertags(PRPlayer player) {
        if (player == null) {
            return null;
        }

        return player.getUsertags();
    }
}
