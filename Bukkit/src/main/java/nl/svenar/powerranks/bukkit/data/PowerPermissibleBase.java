package nl.svenar.powerranks.bukkit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;

public class PowerPermissibleBase extends PermissibleBase {

	private PRPlayer prPlayer;

	public static Map<String, Integer> permissionCallCount = new HashMap<String, Integer>();

	/**
	 * Constructor for PowerPermissibleBase.
	 * sets up the permissible base for a PowerRanks player.
	 * 
	 * @param player
	 * @param plugin
	 */
	public PowerPermissibleBase(Player player, PowerRanks plugin) {
		super(player);
		this.prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		if (this.prPlayer == null) {
			CacheManager.createPlayer(player);
			this.prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		}

		recalculatePermissions();
	}

	/**
	 * Default behaviour, return server op status for the player.
	 * 
	 * @return true if the player is op, false
	 */
	@Override
	public boolean isOp() {
		return super.isOp();
	}

	/**
	 * Sets the op status for the player.
	 * 
	 * @param value
	 */
	@Override
	public void setOp(boolean value) {
		super.setOp(value);
	}

	/**
	 * Checks if a permission is set for the player.
	 * 
	 * @param perm
	 * @return true if the permission is set, false otherwise
	 */
	@Override
	public boolean isPermissionSet(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return isPermissionSet(perm.getName());
	}

	/**
	 * Checks if a permission is set for the player.
	 * 
	 * @param name
	 * @return true if the permission is set, false otherwise
	 */
	@Override
	public boolean isPermissionSet(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		PRPermission prPermission = this.prPlayer.getPermission(name, true);

		if (prPermission != null) {
			return prPermission.getValue();
		}

		return super.isPermissionSet(name);
	}

	/**
	 * Checks if the player has a specific permission.
	 * 
	 * @param perm
	 * @return true if the player has the permission, false otherwise
	 */
	@Override
	public boolean hasPermission(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return hasPermission(perm.getName());
	}

	/**
	 * Checks if the player has a specific permission.
	 * 
	 * @param inName
	 * @return true if the player has the permission, false otherwise
	 */
	@Override
	public boolean hasPermission(String inName) {
		if (inName == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		if (permissionCallCount.get(inName) == null) {
			permissionCallCount.put(inName, 0);
		} else {
			permissionCallCount.put(inName, permissionCallCount.get(inName) + 1);
		}

		PRPermission prPermission = this.prPlayer.getPermission(inName, true);

		if (prPermission != null) {
			return prPermission.getValue();
		}

		boolean defaultHasPermission = false;
		try {
			defaultHasPermission = super.hasPermission(inName);
		} catch (NullPointerException e) {
			super.recalculatePermissions();
		}
		return defaultHasPermission;
	}

	/**
	 * Adds a permission attachment for the player.
	 * 
	 * @param plugin
	 * @param name
	 * @param value
	 * @return the PermissionAttachment object
	 */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return super.addAttachment(plugin, name, value);
	}

	/**
	 * Adds a permission attachment for the player.
	 * 
	 * @param plugin
	 * @return the PermissionAttachment object
	 */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return super.addAttachment(plugin);
	}

	/**
	 * Removes a permission attachment for the player.
	 * 
	 * @param attachment
	 */
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		try {
			super.removeAttachment(attachment);
		} catch (Exception e) {
		}
	}

	/**
	 * Recalculates the permissions for the player.
	 */
	@Override
	public void recalculatePermissions() {
		this.prPlayer.recalculateEffectivePermissions();
		super.recalculatePermissions();
	}

	/**
	 * Clears all permissions for the player.
	 */
	public synchronized void clearPermissions() {
		super.clearPermissions();
	}

	/**
	 * Adds a permission attachment for the player with a specified duration.
	 * 
	 * @param plugin
	 * @param name
	 * @param value
	 * @param ticks
	 * @return the PermissionAttachment object
	 */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return super.addAttachment(plugin, name, value, ticks);
	}

	/**
	 * Adds a permission attachment for the player with a specified duration.
	 * 
	 * @param plugin
	 * @param ticks
	 * @return the PermissionAttachment object
	 */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return super.addAttachment(plugin, ticks);
	}

	/**
	 * Gets the effective permissions for the player.
	 * 
	 * @return a Set of PermissionAttachmentInfo objects representing the effective
	 *         permissions
	 */
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return super.getEffectivePermissions();
	}
}