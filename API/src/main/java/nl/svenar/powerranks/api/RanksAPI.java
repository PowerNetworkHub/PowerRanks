package nl.svenar.powerranks.api;

import java.util.ArrayList;
import java.util.List;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;

public class RanksAPI {

    /**
     * Create a new rank
     * 
     * @param name
     * @return PRRank or null if the rank already exists
     */
    public PRRank create(String name) {
        return PRCache.createRank(name);
    }

    /**
     * Get a rank by name
     * 
     * @param name
     * @return PRRank or null if the rank does not exists
     */
    public PRRank get(String name) {
        return PRCache.getRank(name);
    }

    /**
     * Get all ranks
     * 
     * @return List<PRRank>
     */
    public List<PRRank> getRanks() {
        return PRCache.getRanks();
    }

    /**
     * Delete a rank by name
     * 
     * @param name
     * @return true if the rank was deleted, false if the rank does not exists
     */
    public boolean delete(String name) {
        try {
            PRRank rank = get(name);
            PRCache.removeRank(rank);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Check if a rank exists
     * 
     * @param name
     * @return true if the rank exists, false if the rank does not exists
     */
    public boolean exists(String name) {
        return get(name) != null;
    }

    /**
     * Rename a rank
     * 
     * @param oldname
     * @param newname
     * @return true if the rank was renamed, false if the rank does not exists or the new name already exists
     */
    public boolean rename(String oldname, String newname) {
        if (!exists(oldname) || exists(newname)) {
            return false;
        }

        PRRank rank = get(oldname);
        rank.setName(newname);

        return true;
    }

    /**
     * Set the prefix of a rank
     * @param name
     * @param prefix
     * @return true if the prefix was set, false if the rank does not exists
     */
    public boolean setPrefix(String name, String prefix) {
        return setPrefix(get(name), prefix);
    }

    /**
     * Set the prefix of a rank
     * @param rank
     * @param prefix
     * @return true if the prefix was set, false if the rank does not exists
     */
    public boolean setPrefix(PRRank rank, String prefix) {
        if (rank == null) {
            return false;
        }

        rank.setPrefix(prefix);

        return true;
    }

    /**
     * Get the prefix of a rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getPrefix(String name) {
        return getPrefix(get(name));
    }

    /**
     * Get the prefix of a rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getPrefix(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getPrefix();
    }

    /**
     * Set the suffix of a rank
     * @param name
     * @param suffix
     * @return true if the suffix was set, false if the rank does not exists
     */
    public boolean setSuffix(String name, String suffix) {
        return setSuffix(get(name), suffix);
    }

    /**
     * Set the suffix of a rank
     * @param rank
     * @param suffix
     * @return true if the suffix was set, false if the rank does not exists
     */
    public boolean setSuffix(PRRank rank, String suffix) {
        if (rank == null) {
            return false;
        }

        rank.setSuffix(suffix);

        return true;
    }

    /**
     * Get the suffix of a rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getSuffix(String name) {
        return getSuffix(get(name));
    }

    /**
     * Get the suffix of a rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getSuffix(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getSuffix();
    }

    /**
     * Set the weight of a rank
     * @param name
     * @param weight
     * @return true if the weight was set, false if the rank does not exists
     */
    public boolean setWeight(String name, int weight) {
        return setWeight(get(name), weight);
    }

    /**
     * Set the weight of a rank
     * @param rank
     * @param weight
     * @return true if the weight was set, false if the rank does not exists
     */
    public boolean setWeight(PRRank rank, int weight) {
        if (rank == null) {
            return false;
        }

        rank.setWeight(weight);

        return true;
    }

    /**
     * Get the weight of a rank
     * @param name
     * @return int or -1 if the rank does not exists
     */
    public int getWeight(String name) {
        return getWeight(get(name));
    }

    /**
     * Get the weight of a rank
     * @param rank
     * @return int or -1 if the rank does not exists
     */
    public int getWeight(PRRank rank) {
        if (rank == null) {
            return -1;
        }

        return rank.getWeight();
    }

    /**
     * Set the default state of a rank
     * @param name
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setDefault(String name, boolean isDefault) {
        return setDefault(get(name), isDefault);
    }

    /**
     * Set the default state of a rank
     * @param rank
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setDefault(PRRank rank, boolean isDefault) {
        if (rank == null) {
            return false;
        }

        rank.setDefault(isDefault);

        return true;
    }

    /**
     * Get the default state of a rank
     * @param name
     * @return boolean or false if the rank does not exists
     */
    public boolean isDefault(String name) {
        return isDefault(get(name));
    }

    /**
     * Get the default state of a rank
     * @param rank
     * @return boolean or false if the rank does not exists
     */
    public boolean isDefault(PRRank rank) {
        if (rank == null) {
            return false;
        }

        return rank.isDefault();
    }

    /**
     * Set the name color for a player of a rank
     * @param name
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setNameColor(String name, String nameColor) {
        return setNameColor(get(name), nameColor);
    }

    /**
     * Set the name color for a player of a rank
     * @param rank
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setNameColor(PRRank rank, String nameColor) {
        if (rank == null) {
            return false;
        }

        rank.setNamecolor(nameColor);

        return true;
    }

    /**
     * Get the name color for a player of a rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getNameColor(String name) {
        return getNameColor(get(name));
    }

    /**
     * Get the name color for a player of a rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getNameColor(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getNamecolor();
    }

    /**
     * Set the chat color for a player of a rank
     * @param name
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setChatColor(String name, String chatColor) {
        return setChatColor(get(name), chatColor);
    }

    /**
     * Set the chat color for a player of a rank
     * @param rank
     * @param isDefault
     * @return true if the default state was set, false if the rank does not exists
     */
    public boolean setChatColor(PRRank rank, String chatColor) {
        if (rank == null) {
            return false;
        }

        rank.setChatcolor(chatColor);

        return true;
    }

    /**
     * Get the chat color for a player of a rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getChatColor(String name) {
        return getChatColor(get(name));
    }

    /**
     * Get the chat color for a player of a rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getChatColor(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getChatcolor();
    }

    /**
     * Add a rank as inheritance to a rank
     * @param name
     * @param inherit
     * @return true if the inheritance was added, false if the rank does not exists or the inheritance already exists
     */
    public boolean addInheritance(String name, String inherit) {
        return addInheritance(get(name), inherit);
    }

    /**
     * Add a rank as inheritance to a rank
     * @param name
     * @param inherit
     * @return true if the inheritance was added, false if the rank does not exists or the inheritance already exists
     */
    public boolean addInheritance(PRRank rank, String inherit) {
        if (rank == null) {
            return false;
        }

        for (String prPermission : rank.getInheritances()) {
            if (prPermission.equalsIgnoreCase(inherit)) {
                return false;
            }
        }

        rank.getInheritances().add(inherit);

        return true;
    }

    /**
     * Remove a rank as inheritance from a rank
     * @param name
     * @param inherit
     * @return true if the inheritance was removed, false if the rank does not exists or the inheritance does not exists
     */
    public boolean removeInheritance(String name, String inherit) {
        return removeInheritance(get(name), inherit);
    }

    /**
     * Remove a rank as inheritance from a rank
     * @param name
     * @param inherit
     * @return true if the inheritance was removed, false if the rank does not exists or the inheritance does not exists
     */
    public boolean removeInheritance(PRRank rank, String inherit) {
        if (rank == null) {
            return false;
        }

        boolean found = false;
        for (String prPermission : rank.getInheritances()) {
            if (prPermission.equalsIgnoreCase(inherit)) {
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        rank.getInheritances().remove(inherit);

        return true;
    }

    /**
     * Get all inheritances of a rank
     * @param name
     * @return List<PRRank> or null if the rank does not exists
     */
    public List<PRRank> getInheritances(String name) {
        return getInheritances(get(name));
    }

    /**
     * Get all inheritances of a rank
     * @param rank
     * @return List<PRRank> or null if the rank does not exists
     */
    public List<PRRank> getInheritances(PRRank rank) {
        if (rank == null) {
            return null;
        }

        List<PRRank> inheritances = new ArrayList<PRRank>();
        for (String inherit : rank.getInheritances()) {
            PRRank prRank = get(inherit);
            if (prRank != null) {
                inheritances.add(prRank);
            }
        }

        return inheritances;
    }

    /**
     * Add a permission to a rank
     * @param name
     * @param permissionNode
     * @param allowed
     * @return true if the permission was added, false if the rank does not exists or the permission already exists
     */
    public boolean addPermission(String name, String permissionNode, boolean allowed) {
        return addPermission(get(name), new PRPermission(permissionNode, allowed));
    }

    /**
     * Add a permission to a rank
     * @param rank
     * @param permissionNode
     * @param allowed
     * @return true if the permission was added, false if the rank does not exists or the permission already exists
     */
    public boolean addPermission(PRRank rank, String permissionNode, boolean allowed) {
        return addPermission(rank, new PRPermission(permissionNode, allowed));
    }

    /**
     * Add a permission to a rank
     * @param name
     * @param permission
     * @return true if the permission was added, false if the rank does not exists or the permission already exists
     */
    public boolean addPermission(String name, PRPermission permission) {
        return addPermission(get(name), permission);
    }

    /**
     * Add a permission to a rank
     * @param rank
     * @param permission
     * @return true if the permission was added, false if the rank does not exists or the permission already exists
     */
    public boolean addPermission(PRRank rank, PRPermission permission) {
        if (rank == null) {
            return false;
        }

        for (PRPermission prPermission : rank.getPermissions()) {
            if (prPermission.getName().equals(permission.getName())) {
                return false;
            }
        }

        rank.getPermissions().add(permission);

        return true;
    }

    /**
     * Remove a permission from a rank
     * @param name
     * @param permissionNode
     * @return true if the permission was removed, false if the rank does not exists or the permission does not exists
     */
    public boolean removePermission(String name, String permission) {
        return removePermission(get(name), permission);
    }

    /**
     * Remove a permission from a rank
     * @param rank
     * @param permissionNode
     * @return true if the permission was removed, false if the rank does not exists or the permission does not exists
     */
    public boolean removePermission(PRRank rank, String permission) {
        if (rank == null) {
            return false;
        }

        PRPermission foundPermission = null;
        for (PRPermission prPermission : rank.getPermissions()) {
            if (prPermission.getName().equals(permission)) {
                foundPermission = prPermission;
                break;
            }
        }

        if (foundPermission == null) {
            return false;
        }

        rank.getPermissions().remove(foundPermission);

        return true;
    }

    /**
     * Get all permissions of a rank
     * @param name
     * @return List<PRPermission> or null if the rank does not exists
     */
    public List<PRPermission> getPermissions(String name) {
        return getPermissions(get(name));
    }

    /**
     * Get all permissions of a rank
     * @param rank
     * @return List<PRPermission> or null if the rank does not exists
     */
    public List<PRPermission> getPermissions(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getPermissions();
    }

    /**
     * Add a rank to a rank as buyable
     * @param name
     * @param buyableRankName
     * @return true if the buyable rank was added, false if the rank does not exists or the buyable rank already exists
     */
    public boolean addBuyableRank(String name, String buyableRankName) {
        return addBuyableRank(get(name), get(buyableRankName));
    }

    /**
     * Add a rank to a rank as buyable
     * @param name
     * @param buyableRankName
     * @return true if the buyable rank was added, false if the rank does not exists or the buyable rank already exists
     */
    public boolean addBuyableRank(PRRank rank, String buyableRankName) {
        return addBuyableRank(rank, get(buyableRankName));
    }

    /**
     * Add a rank to a rank as buyable
     * @param name
     * @param buyableRank
     * @return true if the buyable rank was added, false if the rank does not exists or the buyable rank already exists
     */
    public boolean addBuyableRank(String name, PRRank buyableRank) {
        return addBuyableRank(get(name), buyableRank);
    }

    /**
     * Add a rank to a rank as buyable
     * @param rank
     * @param buyableRank
     * @return true if the buyable rank was added, false if the rank does not exists or the buyable rank already exists
     */
    public boolean addBuyableRank(PRRank rank, PRRank buyableRank) {
        if (rank == null) {
            return false;
        }

        for (String prBuyableRank : rank.getBuyableRanks()) {
            if (prBuyableRank.equalsIgnoreCase(buyableRank.getName())) {
                return false;
            }
        }

        rank.getBuyableRanks().add(buyableRank.getName());

        return true;
    }

    /**
     * Remove a rank from a rank as buyable
     * @param name
     * @param buyableRankName
     * @return true if the buyable rank was removed, false if the rank does not exists or the buyable rank does not exists
     */
    public boolean removeBuyableRank(String name, String buyableRankName) {
        return removeBuyableRank(get(name), get(buyableRankName));
    }

    /**
     * Remove a rank from a rank as buyable
     * @param name
     * @param buyableRankName
     * @return true if the buyable rank was removed, false if the rank does not exists or the buyable rank does not exists
     */
    public boolean removeBuyableRank(PRRank rank, String buyableRankName) {
        return removeBuyableRank(rank, get(buyableRankName));
    }

    /**
     * Remove a rank from a rank as buyable
     * @param name
     * @param buyableRank
     * @return true if the buyable rank was removed, false if the rank does not exists or the buyable rank does not exists
     */
    public boolean removeBuyableRank(String name, PRRank buyableRank) {
        return removeBuyableRank(get(name), buyableRank);
    }

    /**
     * Remove a rank from a rank as buyable
     * @param rank
     * @param buyableRank
     * @return true if the buyable rank was removed, false if the rank does not exists or the buyable rank does not exists
     */
    public boolean removeBuyableRank(PRRank rank, PRRank buyableRank) {
        if (rank == null) {
            return false;
        }

        boolean found = false;
        for (String prBuyableRank : rank.getBuyableRanks()) {
            if (prBuyableRank.equalsIgnoreCase(buyableRank.getName())) {
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        rank.getBuyableRanks().remove(buyableRank.getName());

        return true;
    }

    /**
     * Get all buyable ranks of a rank
     * @param name
     * @return List<PRRank> or null if the rank does not exists
     */
    public List<PRRank> getBuyableRanks(String name) {
        return getBuyableRanks(get(name));
    }

    /**
     * Get all buyable ranks of a rank
     * @param rank
     * @return List<PRRank> or null if the rank does not exists
     */
    public List<PRRank> getBuyableRanks(PRRank rank) {
        if (rank == null) {
            return null;
        }

        List<PRRank> buyableRanks = new ArrayList<PRRank>();
        for (String buyableRank : rank.getBuyableRanks()) {
            PRRank prRank = get(buyableRank);
            if (prRank != null) {
                buyableRanks.add(prRank);
            }
        }

        return buyableRanks;
    }

    /**
     * Set the buy cost of a rank
     * @param name
     * @param cost
     * @return true if the buy cost was set, false if the rank does not exists
     */
    public boolean setBuyCost(String name, float cost) {
        return setBuyCost(get(name), cost);
    }

    /**
     * Set the buy cost of a rank
     * @param rank
     * @param cost
     * @return true if the buy cost was set, false if the rank does not exists
     */
    public boolean setBuyCost(PRRank rank, float cost) {
        if (rank == null) {
            return false;
        }

        rank.setBuyCost(cost);

        return true;
    }

    /**
     * Get the buy cost of a rank
     * @param name
     * @return float or -1 if the rank does not exists
     */
    public float getBuyCost(String name) {
        return getBuyCost(get(name));
    }

    /**
     * Get the buy cost of a rank
     * @param rank
     * @return float or -1 if the rank does not exists
     */
    public float getBuyCost(PRRank rank) {
        if (rank == null) {
            return -1;
        }

        return rank.getBuyCost();
    }

    /**
     * Set the buy description of a rank
     * @param name
     * @param description
     * @return true if the buy description was set, false if the rank does not exists
     */
    public boolean setBuyDescription(String name, String description) {
        return setBuyDescription(get(name), description);
    }

    /**
     * Set the buy description of a rank
     * @param rank
     * @param description
     * @return true if the buy description was set, false if the rank does not exists
     */
    public boolean setBuyDescription(PRRank rank, String description) {
        if (rank == null) {
            return false;
        }

        rank.setBuyDescription(description);

        return true;
    }

    /**
     * Get the buy description of a rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getBuyDescription(String name) {
        return getBuyDescription(get(name));
    }

    /**
     * Get the buy description of a rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getBuyDescription(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getBuyDescription();
    }

    /**
     * Set the buy command of a rank
     * This command will be executed when a player buys the rank
     * @param name
     * @param command
     * @return true if the buy command was set, false if the rank does not exists
     */
    public boolean setBuyCommand(String name, String command) {
        return setBuyCommand(get(name), command);
    }

    /**
     * Set the buy command of a rank
     * This command will be executed when a player buys the rank
     * @param rank
     * @param command
     * @return true if the buy command was set, false if the rank does not exists
     */
    public boolean setBuyCommand(PRRank rank, String command) {
        if (rank == null) {
            return false;
        }

        rank.setBuyCommand(command);

        return true;
    }

    /**
     * Get the buy command of a rank
     * This command will be executed when a player buys the rank
     * @param name
     * @return String or null if the rank does not exists
     */
    public String getBuyCommand(String name) {
        return getBuyCommand(get(name));
    }

    /**
     * Get the buy command of a rank
     * This command will be executed when a player buys the rank
     * @param rank
     * @return String or null if the rank does not exists
     */
    public String getBuyCommand(PRRank rank) {
        if (rank == null) {
            return null;
        }

        return rank.getBuyCommand();
    }
}
