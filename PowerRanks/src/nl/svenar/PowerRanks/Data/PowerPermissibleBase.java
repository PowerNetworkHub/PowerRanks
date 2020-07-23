package nl.svenar.PowerRanks.Data;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import nl.svenar.PowerRanks.PowerRanks;

public class PowerPermissibleBase extends PermissibleBase {

	private Permissible oldPermissible = new PermissibleBase(null);
	private PowerRanks plugin;
	private Player player;

	public PowerPermissibleBase(Player player, PowerRanks main) {
		super(player);
		this.player = player;
		this.plugin = main;
		PowerRanksVerbose.log("PowerPermissibleBase", "created");
	}

	public Permissible getOldPermissible() {
		PowerRanksVerbose.log("getOldPermissible", "called");
		return oldPermissible;
	}

	public void setOldPermissible(Permissible oldPermissible) {
		this.oldPermissible = oldPermissible;
		PowerRanksVerbose.log("setOldPermissible", "called");
	}

	public boolean hasSuperPerm(String perm) {
		if (oldPermissible == null) {
			return super.hasPermission(perm);
		}

		PowerRanksVerbose.log("hasSuperPerm", "hasPerm: " + oldPermissible.hasPermission(perm));
		
		return oldPermissible.hasPermission(perm);
	}

	@Override
	public boolean hasPermission(String permission) {
//		PowerRanks.log.info("-----------------------------");
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + oldPermissible.hasPermission(permission));
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + oldPermissible.hasPermission("*"));
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + !plugin.playerDisallowedPermissions.get(player).contains(permission));
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + (oldPermissible.hasPermission("*") && !plugin.playerDisallowedPermissions.get(player).contains(permission)));
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + checkPermissionWildcardTree(permission));
//		PowerRanks.log.info("-----------------------------");
		if (permission == null) {
			throw new NullPointerException(permission);
		}
		
		boolean hasPerm = oldPermissible.hasPermission(permission);
		boolean isDisallowed = plugin.playerDisallowedPermissions.get(player).contains(permission);
		boolean hasAllPerms = oldPermissible.hasPermission("*");
		boolean hasWildcardTree = checkPermissionWildcardTree(permission);
		
		PowerRanksVerbose.log("hasPermission(String)", "hasPerm: " + hasPerm);
		PowerRanksVerbose.log("hasPermission(String)", "isDisallowed: " + isDisallowed);
		PowerRanksVerbose.log("hasPermission(String)", "hasAllPerms: " + hasAllPerms);
		PowerRanksVerbose.log("hasPermission(String)", "hasWildcardTree: " + hasWildcardTree);
		
		// (hasPerm && !isDisallowed) || (hasAllPerms && !isDisallowed) || (hasWildcardTree && !isDisallowed)

		return (hasPerm && !isDisallowed) || (hasAllPerms && !isDisallowed) || (hasWildcardTree && !isDisallowed);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		PowerRanksVerbose.log("hasPermission(Permission)", "hasPerm: " + permission.getName());
		return hasPermission(permission.getName());
	}

	@Override
	public void recalculatePermissions() {
		if (oldPermissible == null) {
			super.recalculatePermissions();
			return;
		}

		oldPermissible.recalculatePermissions();
		
		PowerRanksVerbose.log("recalculatePermissions", "Permissions recalculated");
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		if (oldPermissible == null) {
			return super.getEffectivePermissions();
		}

		PowerRanksVerbose.log("getEffectivePermissions", "called");
		return oldPermissible.getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
		if (oldPermissible == null) {
			return super.isOp();
		}

		PowerRanksVerbose.log("isOp", "is op: " + oldPermissible.isOp());
		return oldPermissible.isOp();
	}

	@Override
	public void setOp(boolean value) {
		if (oldPermissible == null) {
			super.setOp(value);
			return;
		}
		
		PowerRanksVerbose.log("setOp", "set op: " + value);

		oldPermissible.setOp(value);
	}

	@Override
	public boolean isPermissionSet(String permission) {
//    	plugin.log.info("[isPermissionSet] " + permission + ": " + (oldPermissible.isPermissionSet(permission)));
//		return oldPermissible.isPermissionSet(permission) || (oldPermissible.hasPermission("*") && !plugin.playerDisallowedPermissions.get(player).contains(permission));
		boolean hasPerm = oldPermissible.hasPermission(permission);
		boolean isDisallowed = plugin.playerDisallowedPermissions.get(player).contains(permission);
		boolean hasAllPerms = oldPermissible.hasPermission("*");
		boolean hasWildcardTree = checkPermissionWildcardTree(permission);
		
		PowerRanksVerbose.log("isPermissionSet(String)", "hasPerm: " + hasPerm);
		PowerRanksVerbose.log("isPermissionSet(String)", "isDisallowed: " + isDisallowed);
		PowerRanksVerbose.log("isPermissionSet(String)", "hasAllPerms: " + hasAllPerms);
		PowerRanksVerbose.log("isPermissionSet(String)", "hasWildcardTree: " + hasWildcardTree);
		
		return (hasPerm && !isDisallowed) || (hasAllPerms && !isDisallowed) || (hasWildcardTree && !isDisallowed);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		PowerRanksVerbose.log("isPermissionSet(Permission)", "IsSet: " + perm.getName());
		return isPermissionSet(perm.getName());
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin);
		}
		
		PowerRanksVerbose.log("addAttachment(plugin)", "PermissionAttachment added");

		return oldPermissible.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, ticks);
		}
		
		PowerRanksVerbose.log("addAttachment(plugin, ticks)", "PermissionAttachment added");

		return oldPermissible.addAttachment(plugin, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, name, value);
		}

		PowerRanksVerbose.log("addAttachment(plugin, name, value)", "PermissionAttachment added");
		
		return oldPermissible.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, name, value, ticks);
		}
		
		PowerRanksVerbose.log("addAttachment(plugin, name, value, ticks)", "PermissionAttachment added");

		return oldPermissible.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		if (oldPermissible == null) {
			super.removeAttachment(attachment);
			return;
		}

		oldPermissible.removeAttachment(attachment);
		
		PowerRanksVerbose.log("removeAttachment", "PermissionAttachment removed");
	}

	@Override
	public synchronized void clearPermissions() {
		if (oldPermissible == null) {
			super.clearPermissions();
			return;
		}

		if (oldPermissible instanceof PermissibleBase) {
			PermissibleBase base = (PermissibleBase) oldPermissible;
			base.clearPermissions();
		}
		
		PowerRanksVerbose.log("clearPermissions", "Permissions cleared");
	}

	private boolean checkPermissionWildcardTree(String permission) {
		String[] permission_split = permission.split("\\.");
		if (permission_split.length == 0)
			return false;

		for (int i = permission_split.length - 1; i >= 0; i--) {
			String perm = String.join(".", permission_split);
			if (perm != null && perm.length() > 0) {
				while (perm.charAt(perm.length() - 1) == '.') {
					perm = perm.substring(0, perm.length() - 1);
				}
			}
			perm += ".*";
			PowerRanksVerbose.log("checkPermissionWildcardTree", "Checking: " + perm);
			if (oldPermissible.hasPermission(perm)) {
				return true;
			}
			permission_split[i] = "";
		}

		return false;
	}
}