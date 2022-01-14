package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.structure.PRPermission;

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
		if (permission.getName().toLowerCase().contains(PowerRanksVerbose.getFilter().toLowerCase())) {
			PowerRanksVerbose.log("hasPermission(Permission)", "hasPerm: " + permission.getName());
		}
		return hasPermission(permission.getName());
	}

	@Override
	public boolean hasPermission(String permission) {
		ArrayList<PRPermission> permissions = plugin.getEffectivePlayerPermissions(player);
		ArrayList<String> wildcardPermissions = Util.generateWildcardList(permission);

		boolean containsWildcard = false;
		boolean checkedWildcard = false;

		// for (String p : wildcardPermissions) {
		// 	PowerRanksVerbose.log("wc", p);
		// }

		// for (String p : generateWildcardList(permission)) {
		// 	for (PRPermission perm : permissions) {
		// 		if (perm.getName().equals(p)) {
		// 			contains_wildcard = true;
		// 			break;
		// 		}
		// 	}
		// }

		boolean disallowed = false;

		for (PRPermission prPermission : permissions) {
			if (prPermission.getName().equals(permission)) {
				disallowed = !prPermission.getValue();
				break;
			}
		}

		if (!disallowed) {
			checkedWildcard = true;
			for (PRPermission perm : permissions) {

				if (wildcardPermissions.contains(perm.getName())) {
					containsWildcard = true;
					disallowed = !perm.getValue();
					break;
				}
			}
		}

		if (permission.toLowerCase().contains(PowerRanksVerbose.getFilter().toLowerCase())) {
			PowerRanksVerbose.log("hasPermission", "");
			PowerRanksVerbose.log("hasPermission", "===== ---------- hasPermission ---------- =====");
			PowerRanksVerbose.log("hasPermission", "Player: " + player.getName());
			PowerRanksVerbose.log("hasPermission", "Permission: " + permission);
			PowerRanksVerbose.log("hasPermission", "Permissions: '" + String.join(", ", getAllPermissionsFormatted(permissions)) + "'");
			PowerRanksVerbose.log("hasPermission", "Is Disallowed: " + disallowed);
			PowerRanksVerbose.log("hasPermission", "Has *: " + getAllPermissions(permissions).contains("*"));
			PowerRanksVerbose.log("hasPermission", "Is Operator: " + player.isOp());
	//		PowerRanksVerbose.log("hasPermission", "Return #3: " + super.hasPermission(permission));
			PowerRanksVerbose.log("hasPermission", "Is permission in list: " + getAllPermissions(permissions).contains(permission));
			PowerRanksVerbose.log("hasPermission", "Is in wildcard tree: " + (checkedWildcard ? containsWildcard : "unchecked"));
			PowerRanksVerbose.log("hasPermission", "===== ---------- hasPermission ---------- =====");
			PowerRanksVerbose.log("hasPermission", "");
		}

		if (disallowed) {
			return false;
		}

		if (getAllPermissions(permissions).contains("*") || player.isOp()) {
			return true;
		}

		try {
			return super.hasPermission(permission) || getAllowedPermissions(permissions).contains(permission) || !disallowed;
		} catch (Exception e) {
			return getAllowedPermissions(permissions).contains(permission);
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
		if (perm.getName().toLowerCase().contains(PowerRanksVerbose.getFilter().toLowerCase())) {
			PowerRanksVerbose.log("isPermissionSet(" + perm + ")", "called, returned: " + value);
		}
		return value;
	}

	@Override
	public boolean isPermissionSet(String permission) {
		PRPermission prPermission = null;
		for (PRPermission perm : plugin.getEffectivePlayerPermissions(player)) {
			if (perm.getName().equals(permission)) {
				prPermission = perm;
				break;
			}
		}

		if (Objects.isNull(prPermission)) {
			return false;
		}
		
		// boolean value = plugin.getEffectivePlayerPermissions(player).contains(permission);
		boolean value = prPermission.getValue();
		if (permission.toLowerCase().contains(PowerRanksVerbose.getFilter().toLowerCase())) {
			PowerRanksVerbose.log("isPermissionSet(" + permission + ")", "called, returned: " + value);
		}
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

	private ArrayList<String> getAllowedPermissions(ArrayList<PRPermission> permissions) {
		ArrayList<String> output = new ArrayList<String>();
		for (PRPermission permission : permissions) {
			if (permission.getValue()) {
				output.add(permission.getName());
			}
		}
		return output;
	}

	private ArrayList<String> getAllPermissions(ArrayList<PRPermission> permissions) {
		ArrayList<String> output = new ArrayList<String>();
		for (PRPermission permission : permissions) {
			output.add(permission.getName());
		}
		return output;
	}

	private ArrayList<String> getAllPermissionsFormatted(ArrayList<PRPermission> permissions) {
		ArrayList<String> output = new ArrayList<String>();
		for (PRPermission permission : permissions) {
			output.add(permission.getName() + ":" + permission.getValue());
		}
		return output;
	}
}
