package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class PowerPermissibleBase extends PermissibleBase {

	private PowerRanks plugin;
	private Player player;

	public PowerPermissibleBase(Player player, PowerRanks main) {
		super(player);
		this.player = player;
		this.plugin = main;
		PowerRanksVerbose.log("PowerPermissibleBase", "attached to player " + (player == null ? "null" : player.getName()));
	}
	
	/*
	 * === ----- Player has permission? ----- ===
	 */

	@Override
	public boolean hasPermission(Permission permission) {
		PowerRanksVerbose.log("hasPermission(Permission)", "hasPerm: " + permission.getName());
		return hasPermission(permission.getName());
	}

	@Override
	public boolean hasPermission(String permission) {
		ArrayList<String> permissions = plugin.getEffectivePlayerPermissions(player);

		boolean contains_wildcard = false;
		for (String p : generateWildcardList(permission)) {
			if (permissions.contains(p)) {
				contains_wildcard = true;
				break;
			}
		}

		PowerRanksVerbose.log("hasPermission", "");
		PowerRanksVerbose.log("hasPermission", "===== ---------- hasPermission ---------- =====");
		PowerRanksVerbose.log("hasPermission", "Player: " + player.getName());
		PowerRanksVerbose.log("hasPermission", "Permission: " + permission);
		PowerRanksVerbose.log("hasPermission", "Permissions: '" + String.join(", ", permissions) + "'");
		PowerRanksVerbose.log("hasPermission", "Is Disallowed: " + permissions.contains("-" + permission));
		PowerRanksVerbose.log("hasPermission", "Has *: " + permissions.contains("*"));
		PowerRanksVerbose.log("hasPermission", "Is Operator: " + player.isOp());
//		PowerRanksVerbose.log("hasPermission", "Return #3: " + super.hasPermission(permission));
		PowerRanksVerbose.log("hasPermission", "Is permission in list: " + permissions.contains(permission));
		PowerRanksVerbose.log("hasPermission", "Is in wildcard tree: " + contains_wildcard);
		PowerRanksVerbose.log("hasPermission", "===== ---------- hasPermission ---------- =====");
		PowerRanksVerbose.log("hasPermission", "");

		if (permissions.contains("-" + permission)) {
			return false;
		}

		if (permissions.contains("*") || player.isOp()) {
			return true;
		}

		try {
			
			return super.hasPermission(permission) || permissions.contains(permission) || contains_wildcard;
		} catch (Exception e) {
			return permissions.contains(permission) || contains_wildcard;
		}
//		
	}
	
	/*
	 * === ----- Recalculate all permissions ----- ===
	 */

	@Override
	public void recalculatePermissions() {
		PowerRanksVerbose.log("recalculatePermissions()", "called");
		super.recalculatePermissions();
		if (player != null) {
			// player.updateCommands();
		}
	}
	
	/*
	 * === ----- Get the effective permissions ----- ===
	 */

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		PowerRanksVerbose.log("getEffectivePermissions()", "called");
		return super.getEffectivePermissions();
	}
	
	/*
	 * === ----- Operator handling ----- ===
	 */

	@Override
	public boolean isOp() {
		PowerRanksVerbose.log("isOp()", "called");
		return super.isOp();
	}

	@Override
	public void setOp(boolean value) {
		PowerRanksVerbose.log("setOp(" + value + ")", "called");
		super.setOp(value);
	}
	
	/*
	 * === ----- Is Permission Set ----- ===
	 */

	@Override
	public boolean isPermissionSet(Permission perm) {
		boolean value = isPermissionSet(perm.getName());
		PowerRanksVerbose.log("isPermissionSet(" + perm + ")", "called, returned: " + value);
		return value;
	}

	@Override
	public boolean isPermissionSet(String permission) {
		boolean value = plugin.getEffectivePlayerPermissions(player).contains(permission);
		PowerRanksVerbose.log("isPermissionSet(" + permission + ")", "called, returned: " + value);
		return value;
	}

	/*
	 * === ----- Add Attachment ----- ===
	 */

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return super.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return super.addAttachment(plugin, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return super.addAttachment(plugin, name, value);

	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return super.addAttachment(plugin, name, value, ticks);
	}
	
	/*
	 * === ----- Remove Attachment ----- ===
	 */
	
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		super.removeAttachment(attachment);
	}
	
	/*
	 * === ----- Clear Permissions ----- ===
	 */
	
	@Override
	public synchronized void clearPermissions() {
		super.clearPermissions();
	}

	/*
	 * === ----- Internal functions ----- ===
	 */
	
	private ArrayList<String> generateWildcardList(String permission) {
		ArrayList<String> output = new ArrayList<String>();
		String[] permission_split = permission.split("\\.");

		permission_split = Util.array_pop(permission_split);
		for (int i = 0; i < permission_split.length + 1; i++) {
			if (permission_split.length == 0)
				break;
			output.add(String.join(".", permission_split) + ".*");
			permission_split = Util.array_pop(permission_split);
		}

		return output;
	}
}