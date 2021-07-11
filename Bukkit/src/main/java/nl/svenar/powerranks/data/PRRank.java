package nl.svenar.powerranks.data;

import java.util.ArrayList;
import java.util.Collection;

import nl.svenar.powerranks.handlers.BaseDataHandler;

public class PRRank {

    private String name = "";
    private boolean is_default = false;
    private String prefix = "";
    private String suffix = "";
    private int weight = 0;
    private Collection<PRPermission> permissions = new ArrayList<PRPermission>();
    private Collection<String> inheritances = new ArrayList<String>();

    public PRRank(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefault(boolean is_default) {
        this.is_default = is_default;
    }

    public boolean getDefault() {
        return is_default;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Collection<PRPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<PRPermission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(PRPermission permission) {
        this.permissions.add(permission);
    }

    public void addPermission(String permissionnode) {
        PRPermission permission = new PRPermission(permissionnode);
        this.permissions.add(permission);
    }

    public PRPermission getPermission(String permissionName) {
        PRPermission permission = null;
        for (PRPermission rankPermission : getPermissions()) {
            if (rankPermission.getName().equals(permissionName)) {
                permission = rankPermission;
                break;
            }
        }
        return permission;
    }

    public boolean removePermission(String permissionName) {
        PRPermission permission = null;
        for (PRPermission rankPermission : getPermissions()) {
            if (rankPermission.getName().equals(permissionName)) {
                permission = rankPermission;
                break;
            }
        }
        this.permissions.remove(permission);
        return permission != null;
    }

    public boolean removePermission(PRPermission permission) {
        if (permission != null) {
            this.permissions.remove(permission);
        }
        return permission != null;
    }

    public Collection<String> getInheritedRanks() {
        return inheritances;
    }

    public void setInheritedRanks(Collection<String> inheritances) {
        this.inheritances = inheritances;
    }

    public void addInheritedRank(String inheritanceName) {
        if (this.getName().equalsIgnoreCase(inheritanceName)) {
            return;
        }
        this.inheritances.add(inheritanceName);
    }

    @Override
    public String toString() {
        String output = "";

        output += "{";
        output += "name: " + getName() + ", ";
        output += "weight: " + getWeight() + ", ";
        output += "prefix: '" + getPrefix() + "', ";
        output += "suffix: '" + getSuffix() + "', ";
        output += "permissions: {";
        for (PRPermission permission : getPermissions()) {
            output += permission.toString() + ", ";
        }
        output += "}, ";
        output += "inheritances: ";
        for (String inheritance : getInheritedRanks()) {
            output += BaseDataHandler.getRank(inheritance).toString() + ", ";
        }
        output += "}";

        output = output.replaceAll(", ]", "]").replaceAll(", }", "}");

        return output;
    }
}