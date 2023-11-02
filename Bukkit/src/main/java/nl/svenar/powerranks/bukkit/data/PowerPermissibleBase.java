package nl.svenar.powerranks.bukkit.data;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import nl.svenar.powerranks.common.storage.PermissionRegistry;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;

public class PowerPermissibleBase extends PermissibleBase {

	private PowerRanks plugin;
	
	private Player player;

	private PermissionRegistry permissionRegistry;

	private PRPlayer prPlayer;

	public static Map<String, Integer> permissionCallCount = new HashMap<String, Integer>();

	public PowerPermissibleBase(Player player, PowerRanks plugin) {
		super(player);
		this.player = player;
		this.plugin = plugin;
		this.permissionRegistry = plugin.getPermissionRegistry();
		this.prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		if (prPlayer == null) {
			CacheManager.createPlayer(player);
			this.prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		}

		PowerRanksVerbose.log("PowerPermissibleBase",
				"attached to player " + (player == null ? "null" : player.getName()));

		recalculatePermissions();
	}

	@Override
	public boolean isOp() {
		PowerRanksVerbose.log("isOp()", "called");
		return super.isOp();
	}

	@Override
	public void setOp(boolean value) {
		super.setOp(value);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return isPermissionSet(perm.getName());
	}

	@Override
	public boolean isPermissionSet(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		permissionRegistry.queuePermission(name);

		PRPermission prPermission = getPRPermission(name);
		if (prPermission == null) {
			for (String wildCardPermissionName : PRUtil.generateWildcardList(name)) {
				prPermission = getPRPermission(wildCardPermissionName);
				if (prPermission != null) {
					break;
				}
			}
		}

		PowerRanksVerbose.log("isPermissionSet(String name)",
				"called with name: " + name + " (" + super.isPermissionSet(name) + ") - prPermission value: "
						+ (prPermission == null ? "null" : prPermission.getValue()));

		if (prPermission != null) {
			return prPermission.getValue();
		}

		return super.isPermissionSet(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return hasPermission(perm.getName());
	}

	@Override
	public boolean hasPermission(String inName) {
		if (inName == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		permissionRegistry.queuePermission(inName);

		if (permissionCallCount.get(inName) == null) {
			permissionCallCount.put(inName, 0);
		} else {
			permissionCallCount.put(inName, permissionCallCount.get(inName) + 1);
		}

		PRPermission prPermission = getPRPermission(inName);
		if (prPermission == null) {
			for (String wildCardPermissionName : PRUtil.generateWildcardList(inName)) {
				prPermission = getPRPermission(wildCardPermissionName);
				if (prPermission != null) {
					break;
				}
			}
		}

		boolean defaultHasPermission = false;
		try {
			defaultHasPermission = super.hasPermission(inName);
		} catch (NullPointerException e) {
			PowerRanksVerbose.log("hasPermission(String inName) failed", e.getMessage());
			super.recalculatePermissions();
		}

		PowerRanksVerbose.log("hasPermission(String inName)",
				"called with inName: " + inName + " (" + defaultHasPermission + ") - prPermission value: "
						+ (prPermission == null ? "null" : prPermission.getValue()));

		if (prPermission != null) {
			return prPermission.getValue();
		}
		return defaultHasPermission;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		PowerRanksVerbose.log("addAttachment(Plugin plugin)",
				"called with plugin: " + plugin.getName() + ", name: " + name + ", value: " + value);
		return super.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		PowerRanksVerbose.log("addAttachment(Plugin plugin)", "called with plugin: " + plugin.getName());
		return super.addAttachment(plugin);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		PowerRanksVerbose.log("removeAttachment(PermissionAttachment attachment)",
				"called with attachment permissions: ");
		for (Entry<String, Boolean> permissionAttachmentInfo : attachment.getPermissions().entrySet()) {
			PowerRanksVerbose.log("",
					"    " + permissionAttachmentInfo.getKey() + ": " + permissionAttachmentInfo.getValue());
		}
		try {
			super.removeAttachment(attachment);
		} catch (Exception e) {
			PowerRanksVerbose.log("removeAttachment(PermissionAttachment attachment) failed", e.getMessage());
		}
	}

	@Override
	public void recalculatePermissions() {
		PowerRanksVerbose.log("recalculatePermissions()", "called");
		super.recalculatePermissions();
	}

	public synchronized void clearPermissions() {
		PowerRanksVerbose.log("clearPermissions()", "called");
		super.clearPermissions();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		PowerRanksVerbose.log("addAttachment(Plugin plugin, String name, boolean value, int ticks)",
				"called with plugin: " + plugin.getName() + ", name: " + name + ", value: " + value + ", ticks: "
						+ ticks);
		return super.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		PowerRanksVerbose.log("addAttachment(Plugin plugin, int ticks)",
				"called with plugin: " + plugin.getName() + ", ticks: " + ticks);
		return super.addAttachment(plugin, ticks);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		PowerRanksVerbose.log("getEffectivePermissions()", "called");
		return super.getEffectivePermissions();
	}

	private PRPermission getPRPermission(String name) {
		PRPermission prPermission = null;

		boolean caseSensitive = PowerRanks.getConfigManager().getBool("general.case-sensitive-permissions", false);

		for (PRPermission permission : this.plugin.getEffectivePlayerPermissions(this.player)) {
			if ((caseSensitive && permission.getName().equals(name))
					|| (!caseSensitive && permission.getName().equalsIgnoreCase(name))) {
				prPermission = permission;
				break;
			}
		}

		return prPermission;
	}
}
