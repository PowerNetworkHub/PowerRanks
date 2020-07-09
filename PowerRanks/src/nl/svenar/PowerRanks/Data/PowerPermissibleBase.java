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
	}

	public Permissible getOldPermissible() {
		return oldPermissible;
	}

	public void setOldPermissible(Permissible oldPermissible) {
		this.oldPermissible = oldPermissible;
	}

	public boolean hasSuperPerm(String perm) {
		if (oldPermissible == null) {
			return super.hasPermission(perm);
		}

		return oldPermissible.hasPermission(perm);
	}

	@Override
	public boolean hasPermission(String permission) {
//		PowerRanks.log.info("[hasPermission] " + permission + ": " + oldPermissible.hasPermission(permission));
		if (permission == null) {
			throw new NullPointerException(permission);
		}

		return oldPermissible.hasPermission(permission) || (oldPermissible.hasPermission("*") && !plugin.playerDisallowedPermissions.get(player).contains(permission)) || checkPermissionWildcardTree(permission);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return hasPermission(permission.getName());
	}

	@Override
	public void recalculatePermissions() {
		if (oldPermissible == null) {
			super.recalculatePermissions();
			return;
		}

		oldPermissible.recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		if (oldPermissible == null) {
			return super.getEffectivePermissions();
		}

		return oldPermissible.getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
		if (oldPermissible == null) {
			return super.isOp();
		}

		return oldPermissible.isOp();
	}

	@Override
	public void setOp(boolean value) {
		if (oldPermissible == null) {
			super.setOp(value);
			return;
		}

		oldPermissible.setOp(value);
	}

	@Override
	public boolean isPermissionSet(String permission) {
//    	plugin.log.info("[isPermissionSet] " + permission + ": " + (oldPermissible.isPermissionSet(permission)));
		return oldPermissible.isPermissionSet(permission) || (oldPermissible.hasPermission("*") && !plugin.playerDisallowedPermissions.get(player).contains(permission));
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		return isPermissionSet(perm.getName());
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin);
		}

		return oldPermissible.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, ticks);
		}

		return oldPermissible.addAttachment(plugin, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, name, value);
		}

		return oldPermissible.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		if (oldPermissible == null) {
			return super.addAttachment(plugin, name, value, ticks);
		}

		return oldPermissible.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		if (oldPermissible == null) {
			super.removeAttachment(attachment);
			return;
		}

		oldPermissible.removeAttachment(attachment);
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
			if (oldPermissible.hasPermission(perm)) {
				return true;
			}
			permission_split[i] = "";
		}

		return false;
	}
}