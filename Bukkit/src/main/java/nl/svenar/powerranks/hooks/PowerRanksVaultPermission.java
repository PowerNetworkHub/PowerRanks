package nl.svenar.powerranks.hooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.permission.Permission;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPermissible;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class PowerRanksVaultPermission extends Permission {

    private PowerRanks plugin;

    public PowerRanksVaultPermission(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return this.plugin.getDescription().getName();
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.isEnabled();
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return true;
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }

    @Override
    public boolean playerHas(String world, OfflinePlayer player, String permission) {
        PRPlayer prPlayer = getPlayer(player);
        PRPermissible playerPermissible = prPlayer.getPermissible();
        if (playerPermissible != null) {
            return playerPermissible.hasPermission(permission);
        }
        return false;
    }

    @Override
    public boolean playerAdd(String world, OfflinePlayer player, String permission) {
        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            PRPermission prPermission = new PRPermission(permission);
            prPlayer.addPermission(prPermission);
        }

        return prPlayer != null;
    }

    @Override
    public boolean playerRemove(String world, OfflinePlayer player, String permission) {
        PRPlayer prPlayer = getPlayer(player);
        PRPermission permissionToRemove = null;

        if (prPlayer != null) {
            for (PRPermission prPermission : prPlayer.getPermissions()) {
                if (prPermission.getName().equals(permission)) {
                    permissionToRemove = prPermission;
                    break;
                }
            }
        }

        if (permissionToRemove != null) {
            Collection<PRPermission> prPermissions = prPlayer.getPermissions();
            prPermissions.remove(permissionToRemove);
            prPlayer.setPermissions(prPermissions);
        }

        return permissionToRemove != null;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        PRRank rank = getRank(group);
        if (rank != null) {
            for (PRPermission prPermission : rank.getPermissions()) {
                if (prPermission.getName().equals(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        PRRank rank = getRank(group);
        if (rank != null) {
            PRPermission prPermission = new PRPermission(permission);
            rank.addPermission(prPermission);
        }
        return rank != null;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        PRRank rank = getRank(group);
        PRPermission permissionToRemove = null;

        if (rank != null) {
            Collection<PRPermission> prPermissions = rank.getPermissions();

            for (PRPermission prPermission : rank.getPermissions()) {
                if (prPermission.getName().equals(permission)) {
                    permissionToRemove = prPermission;
                    break;
                }
            }

            if (permissionToRemove != null) {
                prPermissions.remove(permissionToRemove);
                rank.setPermissions(prPermissions);
            }
        }
        return permissionToRemove != null;
    }

    @Override
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            Collection<PRRank> playerRanks = prPlayer.getRanks();
            for (PRRank rank : playerRanks) {
                if (rank.getName().equals(group)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
        PRRank rankToAdd = null;

        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            Collection<PRRank> playerRanks = prPlayer.getRanks();

            for (PRRank rank : playerRanks) {
                if (rank.getName().equals(group)) {
                    rankToAdd = rank;
                    break;
                }
            }

            if (rankToAdd != null) {
                playerRanks.add(rankToAdd);
                prPlayer.setRanks(playerRanks);
            }
        }

        return rankToAdd != null;
    }

    @Override
    public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
        PRRank rankToRemove = null;

        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            Collection<PRRank> playerRanks = prPlayer.getRanks();

            for (PRRank rank : playerRanks) {
                if (rank.getName().equals(group)) {
                    rankToRemove = rank;
                    break;
                }
            }

            if (rankToRemove != null) {
                playerRanks.remove(rankToRemove);
                prPlayer.setRanks(playerRanks);
            }
        }

        return rankToRemove != null;
    }

    @Override
    public String[] getPlayerGroups(String world, OfflinePlayer player) {
        String[] ranks = null;

        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            Collection<PRRank> playerRanks = prPlayer.getRanks();

            ranks = new String[playerRanks.size()];
            int index = 0;
            for (PRRank rank : playerRanks) {
                ranks[index] = rank.getName();
                index++;
            }
        }
        return ranks == null ? new String[0] : ranks;
    }

    @Override
    public String getPrimaryGroup(String world, OfflinePlayer player) {
        PRPlayer prPlayer = getPlayer(player);

        if (prPlayer != null) {
            List<PRRank> playerRanks = new ArrayList<PRRank>();
            for (PRRank rank : prPlayer.getRanks()) {
                playerRanks.add(rank);
            }
            Collections.sort(playerRanks, (left, right) -> left.getWeight() - right.getWeight());
            return playerRanks.get(playerRanks.size() - 1).getName();
        }
        return "default";
    }

    @Override
    public String[] getGroups() {
        Collection<PRRank> ranks = BaseDataHandler.getRanks();
        String[] groups = new String[ranks.size()];
        int index = 0;
        for (PRRank rank : ranks) {
            groups[index] = rank.getName();
            index++;
        }
        return groups;
    }

    // -- Deprecated methods

    @Deprecated
    private OfflinePlayer playerFromName(String name) {
        return this.plugin.getServer().getOfflinePlayer(name);
    }

    @Override
    public boolean playerHas(String world, String name, String permission) {
        return playerHas(world, playerFromName(name), permission);
    }

    @Override
    public boolean playerAdd(String world, String name, String permission) {
        return playerAdd(world, playerFromName(name), permission);
    }

    @Override
    public boolean playerRemove(String world, String name, String permission) {
        return playerRemove(world, playerFromName(name), permission);
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {
        return playerInGroup(world, playerFromName(player), group);
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        return playerAddGroup(world, playerFromName(player), group);
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        return playerRemoveGroup(world, playerFromName(player), group);
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {
        return getPlayerGroups(world, playerFromName(player));
    }

    @Override
    public String getPrimaryGroup(String world, String player) {
        return getPrimaryGroup(world, playerFromName(player));
    }

    // -- PowerRanks stuff

    private PRPlayer getPlayer(OfflinePlayer player) {
        PRPlayer prPlayer = null;
        for (PRPlayer cachedPlayer : BaseDataHandler.getPlayers()) {
            if (cachedPlayer.getName().equals(player.getName())) {
                prPlayer = cachedPlayer;
                break;
            }
        }

        return prPlayer;
    }

    private PRRank getRank(String rankName) {
        PRRank prRank = null;
        for (PRRank rank : BaseDataHandler.getRanks()) {
            if (rank.getName().equals(rankName)) {
                prRank = rank;
                break;
            }
        }

        return prRank;
    }
}
