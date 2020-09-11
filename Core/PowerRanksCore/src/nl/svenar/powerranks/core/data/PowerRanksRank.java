package nl.svenar.powerranks.core.data;

import java.util.ArrayList;

public class PowerRanksRank {
	
	private String name = "";
	private String prefix = "";
	private String suffix = "";
	private String chat_color = "";
	private String name_color = "";
	private String level_promote = "";
	private String level_demote = "";
	private int weight = 0;
	private ArrayList<String> permissions = new ArrayList<String>();
	private ArrayList<String> inheritances = new ArrayList<String>();
	
	/**
	 * constructor
	 * 
	 * @param name
	 */
	public PowerRanksRank(String name) {
		set_name(name);
	}
	
	/**
	 * @return the name of this rank
	 */
	public String get_name() {
		return name;
	}
	
	/**
	 * @param name, the name to set
	 */
	public void set_name(String name) {
		this.name = name;
	}
	
	/**
	 * @return the prefix
	 */
	public String get_prefix() {
		return prefix;
	}
	
	/**
	 * @param prefix, the prefix to set
	 */
	public void set_prefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * @return the suffix
	 */
	public String get_suffix() {
		return suffix;
	}
	
	/**
	 * @param suffix, the suffix to set
	 */
	public void set_suffix(String suffix) {
		this.suffix = suffix;
	}
	
	/**
	 * @return the chat color
	 */
	public String get_chat_color() {
		return chat_color;
	}
	
	/**
	 * @param chat_color, the chat color to set
	 */
	public void set_chat_color(String chat_color) {
		this.chat_color = chat_color;
	}

	/**
	 * @return the name color
	 */
	public String get_name_color() {
		return name_color;
	}

	/**
	 * @param name_color, the name color to set
	 */
	public void set_name_color(String name_color) {
		this.name_color = name_color;
	}

	/**
	 * @return the level promote
	 */
	public String get_level_promote() {
		return level_promote;
	}

	/**
	 * @param level_promote, the level promote to set
	 */
	public void set_level_promote(String level_promote) {
		this.level_promote = level_promote;
	}

	/**
	 * @return the level_demote
	 */
	public String get_level_demote() {
		return level_demote;
	}

	/**
	 * @param level_demote, the level_demote to set
	 */
	public void set_level_demote(String level_demote) {
		this.level_demote = level_demote;
	}

	/**
	 * @return the permissions
	 */
	public ArrayList<String> get_permissions() {
		return permissions;
	}

	/**
	 * @param permissions, the permissions to set
	 */
	public void set_permissions(ArrayList<String> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * @param permission, the permission to add
	 */
	public void add_permission(String permission) {
		this.permissions.add(permission);
	}
	
	/**
	 * @param permission, the permission to remove
	 */
	public void remove_permission(String permission) {
		this.permissions.remove(permission);
	}

	/**
	 * @return the inheritances
	 */
	public ArrayList<String> get_inheritances() {
		return inheritances;
	}

	/**
	 * @param inheritances, the inheritances to set
	 */
	public void set_inheritances(ArrayList<String> inheritances) {
		this.inheritances = inheritances;
	}
	
	/**
	 * @param inheritance, the inheritance to add
	 */
	public void add_inheritance(String inheritance) {
		this.inheritances.add(inheritance);
	}
	
	/**
	 * @param inheritance, the inheritance to remove
	 */
	public void remove_inheritance(String inheritance) {
		this.inheritances.remove(inheritance);
	}

	/**
	 * @return the weight
	 */
	public int get_weight() {
		return weight;
	}

	/**
	 * @param weight, the weight to set
	 */
	public void set_weight(int weight) {
		this.weight = weight;
	}

	/**
	 * @param permission, the permission to check
	 */
	public boolean has_permission(String permission) {
		return get_permissions().contains(permission) || get_permissions().contains("-" + permission);
	}
	
	/**
	 * @param permission, the permission to check
	 */
	public boolean is_permission_allowed(String permission) {
		return !permission.contains("-");
	}
}