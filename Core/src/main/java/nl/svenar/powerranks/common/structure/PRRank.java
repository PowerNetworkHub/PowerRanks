package nl.svenar.powerranks.common.structure;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Structure to store rank data.
 * 
 * @author svenar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PRRank {

    private String name;
    private boolean isDefault;
    private ArrayList<PRPermission> permissions;
    private ArrayList<String> inheritances;
    private PRChatFormat chatFormat;
    private PREconomyDetails economyDetails;
    private int weight;

    public PRRank() {
        name = "";
        isDefault = false;
        permissions = new ArrayList<PRPermission>();
        inheritances = new ArrayList<String>();
        chatFormat = new PRChatFormat();
        economyDetails = new PREconomyDetails();
        weight = 0;
    }

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
     * Check the default value of this rank
     * 
     * @return true if this rank will be granted to new players
     */
    public boolean isDefault() {
        return this.isDefault;
    }

    /**
     * Change the default value of this rank
     * When true this rank will be granted to new players
     * 
     * @param isDefault
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
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
     * Remove a PRPermission instance from this rank
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
     * Get a list of all stored inheritances in this rank
     * 
     * @return Java ArrayList with all inheritance names
     */
    public ArrayList<String> getInheritances() {
        return this.inheritances;
    }

    /**
     * Overwrite all stored inheritances with the provided Java ArrayList
     * 
     * @param inheritances
     */
    public void setInheritances(ArrayList<String> inheritanceNames) {
        this.inheritances = inheritanceNames;
    }

    /**
     * Add a inheritance to this rank
     * 
     * @param inheritanceName
     */
    public void addInheritance(String inheritanceName) {
        if (Objects.isNull(this.inheritances)) {
            this.inheritances = new ArrayList<String>();
        }

        this.inheritances.add(inheritanceName);
    }

    /**
     * Remove an inheritance from this rank
     * 
     * @param inheritanceName
     */
    public void removeInheritance(String inheritanceName) {
        if (Objects.isNull(this.inheritances)) {
            this.inheritances = new ArrayList<String>();
        }

        if (this.inheritances.contains(inheritanceName)) {
            this.inheritances.remove(inheritanceName);
        }
    }

    /**
     * Get the chat format details for this rank.
     * @return PRChatFormat instance
     */
    public PRChatFormat getChatFormat() {
        return this.chatFormat;
    }

    /**
     * Set the chat format details for this rank.
     * @param chatFormat
     */
    public void setChatFormat(PRChatFormat chatFormat) {
        this.chatFormat = chatFormat;
    }

    /**
     * Change the chat prefix for this rank
     * 
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.chatFormat.setPrefix(prefix);
    }

    /**
     * Get the chat prefix for this rank
     * 
     * @return prefix
     */
    public String getPrefix() {
        return this.chatFormat.getPrefix();
    }

    /**
     * Change the chat suffix for this rank
     * 
     * @param suffix
     */
    public void setSuffix(String suffix) {
        this.chatFormat.setSuffix(suffix);
    }

    /**
     * Get the chat prefix for this rank
     * 
     * @return suffix
     */
    public String getSuffix() {
        return this.chatFormat.getSuffix();
    }

    /**
     * Change the name color for this rank
     * 
     * @param namecolor
     */
    public void setNamecolor(String namecolor) {
        this.chatFormat.setNamecolor(namecolor);
    }

    /**
     * Get the name color for this rank
     * 
     * @return name color
     */
    public String getNamecolor() {
        return this.chatFormat.getNamecolor();
    }

    /**
     * Change the chat color for this rank
     * 
     * @param chatcolor
     */
    public void setChatcolor(String chatcolor) {
        this.chatFormat.setChatcolor(chatcolor);
    }

    /**
     * Get the chat color for this rank
     * 
     * @return chat color
     */
    public String getChatcolor() {
        return this.chatFormat.getChatcolor();
    }

    /**
     * Get the economy details for this rank.
     * @return PREconomyDetails instance
     */
    public PREconomyDetails getEconomyDetails() {
        return this.economyDetails;
    }

    /**
     * Set the economy details for this rank.
     * @param economyDetails
     */
    public void setEconomyDetails(PREconomyDetails economyDetails) {
        this.economyDetails = economyDetails;
    }

    /**
     * Get a list of all stored buyable ranks in this rank
     * 
     * @return Java ArrayList with all buyable rank names
     */
    public ArrayList<String> getBuyableRanks() {
        return this.economyDetails.getBuyableRanks();
    }

    /**
     * Overwrite all stored buyable ranks with the provided Java ArrayList
     * 
     * @param economyBuyable
     */
    public void setBuyableRanks(ArrayList<String> buyableRankNames) {
        this.economyDetails.setBuyableRanks(buyableRankNames);
    }

    /**
     * Add a buyable rank to this rank
     * 
     * @param buyableRankName
     */
    public void addBuyableRank(String buyableRankName) {
        this.economyDetails.addBuyableRank(buyableRankName);
    }

    /**
     * Remove an buyable rank from this rank
     * 
     * @param buyableRankName
     */
    public void removeBuyableRank(String buyableRankName) {
        this.economyDetails.removeBuyableRank(buyableRankName);
    }

    /**
     * Get the buy cost of this rank
     * 
     * @return String buy cost of the rank
     */
    public float getBuyCost() {
        return this.economyDetails.getCost();
    }

    /**
     * Set the buy cost of this rank
     * 
     * @param economyCost
     */
    public void setBuyCost(float economyCost) {
        this.economyDetails.setCost(economyCost);
    }

    /**
     * Get the buy description of this rank
     * 
     * @return String buy description of the rank
     */
    public String getBuyDescription() {
        return this.economyDetails.getDescription();
    }

    /**
     * Set the buy description of this rank
     * 
     * @param description
     */
    public void setBuyDescription(String description) {
        this.economyDetails.setDescription(description);
    }

    /**
     * Get the buy command of this rank
     * 
     * @return String buy command of the rank
     */
    public String getBuyCommand() {
        return this.economyDetails.getBuyCommand();
    }

    /**
     * Set the buy command of this rank
     * 
     * @param buyCommand
     */
    public void setBuyCommand(String buyCommand) {
        this.economyDetails.setBuyCommand(buyCommand);
    }

    /**
     * Get the weight of this rank
     * 
     * @return Integer weight of the rank
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Set the weight of this rank
     * 
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        String output = "name:" + name
        + ", isDefault:" + isDefault
        + ", permissions:[<<PERMISSIONS>>]"
        + ", inheritances:[<<INHERITANCES>>]"
        + ", chatPrefix:" + chatFormat.getPrefix()
        + ", chatSuffix:" + chatFormat.getSuffix()
        + ", chatNamecolor:" + chatFormat.getNamecolor()
        + ", chatChatcolor:" + chatFormat.getChatcolor()
        + ", economyBuyable:" + economyDetails.getBuyableRanks()
        + ", economyCost:" + economyDetails.getCost()
        + ", economyDescription:" + economyDetails.getDescription()
        + ", economyBuyCommand:" + economyDetails.getBuyCommand()
        + ", buyableRanks:[<<BUYABLERANKS>>]"
        + ", weight:" + weight;

        String permissions = "";
        for (PRPermission permission : getPermissions()) {
            permissions += permission.toString() + ";";
        }
        permissions = permissions.length() > 0 ? permissions.substring(0, permissions.length() - 1) : "";

        String inheritances = "";
        for (String inheritance : getInheritances()) {
            inheritances += inheritance + ";";
        }
        inheritances = inheritances.length() > 0 ? inheritances.substring(0, inheritances.length() - 1) : "";

        String buyableRanks = "";
        for (String buyable : getBuyableRanks()) {
            buyableRanks += buyable + ";";
        }
        buyableRanks = buyableRanks.length() > 0 ? buyableRanks.substring(0, buyableRanks.length() - 1) : "";

        output = output.replaceAll("<<PERMISSIONS>>", permissions);
        output = output.replaceAll("<<INHERITANCES>>", inheritances);
        output = output.replaceAll("<<BUYABLERANKS>>", buyableRanks);

        return output;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + toString().hashCode();
        return result;
    }
}