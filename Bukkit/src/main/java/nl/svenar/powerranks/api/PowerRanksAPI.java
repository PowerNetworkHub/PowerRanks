package nl.svenar.powerranks.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.World;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class PowerRanksAPI {

    private PowerRanks plugin;
    private String version = "2.0";

    public enum POWERRANKS_API_STATE {
        RANK_CREATE_SUCCESSFULLY, RANK_CREATE_FAILED_INVALID_CHARACTERS, RANK_CREATE_FAILED_NAME_ALREADY_EXISTS,
        RANK_DELETE_SUCCESSFULLY, RANK_FAILED_NAME_NOT_FOUND, RANK_FAILED_CANNOT_DELETE_DEFAULT_RANK,
        RANK_SET_WEIGHT_SUCCESSFULLY, RANK_SET_PREFIX_SUCCESSFULLY, RANK_SET_SUFFIX_SUCCESSFULLY,
        RANK_PERMISSION_ADD_SUCCESSFULLY, RANK_PERMISSION_ADD_FAILED_PERMISSION_ALREADY_EXISTS,
        RANK_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY, RANK_FAILED_PERMISSION_NOT_FOUND,
        RANK_PERMISSION_REMOVED_SUCCESSFULLY, RANK_PERMISSION_FAILED_WORLD_NOT_FOUND,
        RANK_PERMISSION_FAILED_WORLD_ALREADY_EXISTS, RANK_PERMISSION_FAILED_WORLD_DOES_NOT_EXIST,
        RANK_PERMISSION_WORLD_ADDED_SUCCESSFULLY, RANK_PERMISSION_WORLD_REMOVED_SUCCESSFULLY,
        RANK_PERMISSION_WORLD_CHANGE_ALLOWED_SUCCESSFULLY, PLAYER_FAILED_NAME_NOT_FOUND, PLAYER_FAILED_ALREADY_HAS_RANK,
        PLAYER_SUCCESSFULLY_SET_RANK, PLAYER_SUCCESSFULLY_ADDED_RANK, PLAYER_SUCCESSFULLY_REMOVED_RANK,
        PLAYER_FAILED_DOES_NOT_HAVE_RANK, PLAYER_SUCCESSFULLY_REMOVED_ALL_RANKS, PLAYER_FAILED_ALREADY_HAS_PERMISSION,
        PLAYER_SUCCESSFULLY_ADDED_PERMISSION, PLAYER_FAILED_DOES_NOT_HAVE_PERMISSION, PLAYER_SUCCESSFULLY_REMOVED_PERMISSION, PLAYER_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY;
    }

    /**
     * Constructor Initialize the PowerRanksAPI
     */
    public PowerRanksAPI() {
        this.plugin = PowerRanks.getInstance();
    }

    /**
     * Get the name of the plugin
     * 
     * @return plugin name
     */
    public String getName() {
        return this.plugin.getName();
    }

    /**
     * Get the current API version
     * 
     * @return PowerRanks API version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the default PRRank, can be null in rare cases if the default rank is
     * missing
     * 
     * @param rankName
     * @return PRRank if rank exists, otherwise null
     */
    public PRRank getDefaultRank() {
        return this.getRank("default");
    }

    /**
     * Get a specific PRRank by name
     * 
     * @param rankName
     * @return PRRank if rank exists, otherwise null
     */
    public PRRank getRank(String rankName) {
        return BaseDataHandler.getRank(rankName);
    }

    /**
     * Get a collection of all registered ranks, sorted by weight
     * 
     * @return a collection of all ranks
     */
    public Collection<PRRank> getRanks() {
        List<PRRank> ranks = new ArrayList<PRRank>();
        for (PRRank rank : BaseDataHandler.getRanks()) {
            ranks.add(rank);
        }
        Collections.sort(ranks, (left, right) -> left.getWeight() - right.getWeight());
        return ranks;
    }

    /**
     * Get a list of all registered rank names
     * 
     * @returna collection of all rank names
     */
    public Collection<String> getRankNames() {
        List<String> ranks = new ArrayList<String>();
        for (PRRank rank : this.getRanks()) {
            ranks.add(rank.getName());
        }
        return ranks;
    }

    /**
     * Get a specific PRPlayer by name
     * 
     * @param playerName
     * @return PRPlayer if player exists, otherwise null
     */
    public PRPlayer getPlayer(String playerName) {
        return BaseDataHandler.getPlayer(playerName);
    }

    /**
     * Get a collection of all registered players
     * 
     * @return a collection of all players
     */
    public Collection<PRPlayer> getPlayers() {
        return BaseDataHandler.getPlayers();
    }

    /**
     * Get a list of all registered player names
     * 
     * @returna collection of all player names
     */
    public Collection<String> getPlayerNames() {
        List<String> players = new ArrayList<String>();
        for (PRPlayer player : this.getPlayers()) {
            players.add(player.getName());
        }
        return players;
    }

    /**
     * Create a new rank
     * 
     * @param rankName
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE createRank(String rankName) {
        String rankNameRegex = "[^a-zA-Z0-9]";

        if (BaseDataHandler.getRank(rankName) == null) {
            Pattern pattern = Pattern.compile(rankNameRegex);
            Matcher matcher = pattern.matcher(rankName);

            if (matcher.find()) {
                return POWERRANKS_API_STATE.RANK_CREATE_FAILED_INVALID_CHARACTERS;
            } else {
                PRRank newRank = new PRRank(rankName);
                BaseDataHandler.addRank(newRank);

                return POWERRANKS_API_STATE.RANK_CREATE_SUCCESSFULLY;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_CREATE_FAILED_NAME_ALREADY_EXISTS;
        }
    }

    /**
     * Delete an existing rank
     * 
     * @param rankName
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE deleteRank(String rankName) {
        if (!rankName.equals("default")) {
            if (BaseDataHandler.getRank(rankName) != null) {

                PRRank rank = BaseDataHandler.getRank(rankName);
                BaseDataHandler.removeRank(rank);

                return POWERRANKS_API_STATE.RANK_DELETE_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_CANNOT_DELETE_DEFAULT_RANK;
        }
    }

    /**
     * Change a ranks weight
     * 
     * @param rankName
     * @param weight
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE setRankWeight(String rankName, int weight) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            rank.setWeight(weight);

            return POWERRANKS_API_STATE.RANK_SET_WEIGHT_SUCCESSFULLY;
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Get the weight of a rank
     * 
     * @param rankName
     * @return Integer
     */
    public int getRankWeight(String rankName) {
        PRRank rank = BaseDataHandler.getRank(rankName);
        return rank != null ? rank.getWeight() : 0;
    }

    /**
     * Change a ranks prefix
     * 
     * @param rankName
     * @param newPrefix
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE setRankPrefix(String rankName, String newPrefix) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            rank.setPrefix(newPrefix);

            return POWERRANKS_API_STATE.RANK_SET_PREFIX_SUCCESSFULLY;
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Get the prefix of a rank
     * 
     * @param rankName
     * @return String with the prefix of a rank, empty string if the rank is not
     *         found
     */
    public String getRankPrefix(String rankName) {
        PRRank rank = BaseDataHandler.getRank(rankName);
        return rank != null ? rank.getPrefix() : "";
    }

    /**
     * Change a ranks suffix
     * 
     * @param rankName
     * @param newSuffix
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE setRankSuffix(String rankName, String newSuffix) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            rank.setSuffix(newSuffix);

            return POWERRANKS_API_STATE.RANK_SET_SUFFIX_SUCCESSFULLY;
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Get the suffix of a rank
     * 
     * @param rankName
     * @return String with the suffix of a rank, empty string if the rank is not
     *         found
     */
    public String getRankSuffix(String rankName) {
        PRRank rank = BaseDataHandler.getRank(rankName);
        return rank != null ? rank.getSuffix() : "";
    }

    /**
     * Get all permissions of a rank in the PRPermission format
     * 
     * @param rankName
     * @return PRPermission collection
     */
    public Collection<PRPermission> getRankPermissions(String rankName) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);

            return rank.getPermissions();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Add a permission to a rank When the permission is a string, allowed is set to
     * true and the permission will be active in all worlds
     * 
     * @param rankName
     * @param permission
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE addRankPermission(String rankName, String permission) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            boolean rankHasPermission = false;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    rankHasPermission = true;
                    break;
                }
            }
            if (!rankHasPermission) {
                rank.addPermission(permission);

                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_FAILED_PERMISSION_ALREADY_EXISTS;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Add a permission to a rank When the permission is a string and allowed is
     * given, the permission will be active in all worlds
     * 
     * @param rankName
     * @param permission
     * @param allowed
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE addRankPermission(String rankName, String permission, boolean allowed) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            boolean rankHasPermission = false;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    rankHasPermission = true;
                    break;
                }
            }
            if (!rankHasPermission) {
                rank.addPermission(permission);
                rank.getPermission(permission).setAllowed(allowed);

                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_FAILED_PERMISSION_ALREADY_EXISTS;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Add a permission to a rank
     * 
     * @param rankName
     * @param permission
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE addRankPermission(String rankName, PRPermission permission) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            if (!rank.getPermissions().contains(permission)) {
                rank.addPermission(permission);

                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_PERMISSION_ADD_FAILED_PERMISSION_ALREADY_EXISTS;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Remove a permission node from a rank
     * 
     * @param rankName
     * @param permission
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE removeRankPermission(String rankName, String permission) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                rank.removePermission(targetPermission);

                return POWERRANKS_API_STATE.RANK_PERMISSION_REMOVED_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Remove a permission node from a rank
     * 
     * @param rankName
     * @param permission
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE removeRankPermission(String rankName, PRPermission permission) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            if (permission != null) {
                rank.removePermission(permission);

                return POWERRANKS_API_STATE.RANK_PERMISSION_REMOVED_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Change if a permission should be allowed or denied on a rank
     * 
     * @param rankName
     * @param permission
     * @param allowed
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE setRankPermissionAllowed(String rankName, String permission, boolean allowed) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                targetPermission.setAllowed(allowed);
                return POWERRANKS_API_STATE.RANK_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY;
            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Check if a permission is allowed
     * 
     * @param rankName
     * @param permission
     * @return if permission is allowed on a certain rank
     */
    public boolean isRankPermissionAllowed(String rankName, String permission) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                return targetPermission.isAllowed(null);
            }
        }
        return false;
    }

    /**
     * Check if a permission is allowed
     * 
     * @param rankName
     * @param permission
     * @return if permission is allowed on a certain rank in a certain world
     */
    public boolean isRankPermissionAllowed(String rankName, String permission, World world) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                return targetPermission.isAllowed(world);
            }
        }
        return false;
    }

    public POWERRANKS_API_STATE addWorldToRankPermission(String rankName, String permission, String worldName) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                World world = Bukkit.getServer().getWorld(worldName);
                if (world != null) {
                    if (!targetPermission.getWorlds().contains(world.getName())) {
                        targetPermission.addWorld(world.getName());
                        return POWERRANKS_API_STATE.RANK_PERMISSION_WORLD_ADDED_SUCCESSFULLY;
                    } else {
                        return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_ALREADY_EXISTS;
                    }
                } else {
                    return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_NOT_FOUND;
                }

            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    public POWERRANKS_API_STATE removeWorldFromRankPermission(String rankName, String permission, String worldName) {
        if (BaseDataHandler.getRank(rankName) != null) {
            PRRank rank = BaseDataHandler.getRank(rankName);
            PRPermission targetPermission = null;
            for (PRPermission rankPermission : rank.getPermissions()) {
                if (rankPermission.getName().equals(permission)) {
                    targetPermission = rankPermission;
                    break;
                }
            }
            if (targetPermission != null) {
                World world = Bukkit.getServer().getWorld(worldName);
                if (world != null) {
                    if (targetPermission.getWorlds().contains(world.getName())) {
                        targetPermission.addWorld(world.getName());
                        return POWERRANKS_API_STATE.RANK_PERMISSION_WORLD_REMOVED_SUCCESSFULLY;
                    } else {
                        return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_DOES_NOT_EXIST;
                    }
                } else {
                    return POWERRANKS_API_STATE.RANK_PERMISSION_FAILED_WORLD_NOT_FOUND;
                }

            } else {
                return POWERRANKS_API_STATE.RANK_FAILED_PERMISSION_NOT_FOUND;
            }
        } else {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }
    }

    /**
     * Remove all ranks from the player and add the default and the specified rank
     * 
     * @param playerName
     * @param rankName
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE setPlayerRank(String playerName, String rankName) {
        PRPlayer player = BaseDataHandler.getPlayer(playerName);
        PRRank rank = BaseDataHandler.getRank(rankName);

        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        if (rank == null) {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }

        if (!player.getRanks().contains(rank) || rankName.equals("default")) {
            player.setRanks(new ArrayList<PRRank>());
            PRRank defaultRank = BaseDataHandler.getRank("default");
            player.addRank(defaultRank);
            player.addRank(rank);
            return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_SET_RANK;
        } else {
            return POWERRANKS_API_STATE.PLAYER_FAILED_ALREADY_HAS_RANK;
        }
    }

    /**
     * Add a rank to a player
     * 
     * @param playerName
     * @param rankName
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE addPlayerRank(String playerName, String rankName) {
        PRPlayer player = BaseDataHandler.getPlayer(playerName);
        PRRank rank = BaseDataHandler.getRank(rankName);

        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        if (rank == null) {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }

        if (!player.getRanks().contains(rank)) {
            player.addRank(rank);
            return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_ADDED_RANK;
        } else {
            return POWERRANKS_API_STATE.PLAYER_FAILED_ALREADY_HAS_RANK;
        }
    }

    /**
     * Remove a rank from a player
     * 
     * @param playerName
     * @param rankName
     * @return PowerRanks API state
     */
    public POWERRANKS_API_STATE removePlayerRank(String playerName, String rankName) {
        PRPlayer player = BaseDataHandler.getPlayer(playerName);
        PRRank rank = BaseDataHandler.getRank(rankName);

        if (rankName.equals("default")) {
            return POWERRANKS_API_STATE.RANK_FAILED_CANNOT_DELETE_DEFAULT_RANK;
        }

        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        if (rank == null) {
            return POWERRANKS_API_STATE.RANK_FAILED_NAME_NOT_FOUND;
        }

        if (player.getRanks().contains(rank)) {
            player.removeRank(rank);
            return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_REMOVED_RANK;
        } else {
            return POWERRANKS_API_STATE.PLAYER_FAILED_DOES_NOT_HAVE_RANK;
        }
    }

    public POWERRANKS_API_STATE clearRanks(String playerName) {
        PRPlayer player = BaseDataHandler.getPlayer(playerName);

        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        player.setRanks(new ArrayList<PRRank>());
        PRRank defaultRank = BaseDataHandler.getRank("default");
        player.addRank(defaultRank);

        return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_REMOVED_ALL_RANKS;
    }

    public Collection<PRPermission> getPlayerPermissions(PRPlayer player) {
        if (player != null) {
            return player.getPermissions();
        } else {
            return Collections.emptyList();
        }
    }

    public POWERRANKS_API_STATE addPlayerPermission(String playerName, String permissionNode) {
        PRPlayer player = PowerRanks.getAPI().getPlayer(playerName);
        return addPlayerPermission(player, permissionNode);
    }

    public POWERRANKS_API_STATE addPlayerPermission(PRPlayer player, String permissionNode) {
        return addPlayerPermission(player, permissionNode, true);
    }

    public POWERRANKS_API_STATE addPlayerPermission(String playerName, String permissionNode, boolean allowed) {
        PRPlayer player = PowerRanks.getAPI().getPlayer(playerName);
        return addPlayerPermission(player, permissionNode, allowed);
    }

    public POWERRANKS_API_STATE addPlayerPermission(PRPlayer player, String permissionNode, boolean allowed) {
        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        boolean playerHasPermission = false;
        for (PRPermission playerPermission : player.getPermissions()) {
            if (playerPermission.getName().equals(permissionNode)) {
                playerHasPermission = true;
                break;
            }
        }

        if (playerHasPermission) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_ALREADY_HAS_PERMISSION;
        }

        PRPermission permission = new PRPermission(permissionNode);
        permission.setAllowed(allowed);
        player.addPermission(permission);
        return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_ADDED_PERMISSION;
    }

    public POWERRANKS_API_STATE removePlayerPermission(PRPlayer player, PRPermission permissionNode) {
        return removePlayerPermission(player, permissionNode.getName());
    }

    public POWERRANKS_API_STATE removePlayerPermission(String playerName, PRPermission permissionNode) {
        return removePlayerPermission(BaseDataHandler.getPlayer(playerName), permissionNode.getName());
    }

    public POWERRANKS_API_STATE removePlayerPermission(String playerName, String permissionNode) {
        return removePlayerPermission(BaseDataHandler.getPlayer(playerName), permissionNode);
    }

    public POWERRANKS_API_STATE removePlayerPermission(PRPlayer player, String permissionNode) {
        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        PRPermission permissionToRemove = null;
        for (PRPermission playerPermission : player.getPermissions()) {
            if (playerPermission.getName().equals(permissionNode)) {
                permissionToRemove = playerPermission;
                break;
            }
        }

        if (permissionToRemove == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_DOES_NOT_HAVE_PERMISSION;
        }

        player.getPermissions().remove(permissionToRemove);

        return POWERRANKS_API_STATE.PLAYER_SUCCESSFULLY_REMOVED_PERMISSION;
    }

    public POWERRANKS_API_STATE setPlayerPermissionAllowed(String playerName, String permissionNode, boolean allowed) {
        return setPlayerPermissionAllowed(BaseDataHandler.getPlayer(playerName), permissionNode, allowed);
    }

    public POWERRANKS_API_STATE setPlayerPermissionAllowed(PRPlayer player, String permissionNode, boolean allowed) {
        if (player == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_NAME_NOT_FOUND;
        }

        PRPermission permissionToChange = null;
        for (PRPermission playerPermission : player.getPermissions()) {
            if (playerPermission.getName().equals(permissionNode)) {
                permissionToChange = playerPermission;
                break;
            }
        }

        if (permissionToChange == null) {
            return POWERRANKS_API_STATE.PLAYER_FAILED_DOES_NOT_HAVE_PERMISSION;
        }

        permissionToChange.setAllowed(allowed);

        return POWERRANKS_API_STATE.PLAYER_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY;
    }
}