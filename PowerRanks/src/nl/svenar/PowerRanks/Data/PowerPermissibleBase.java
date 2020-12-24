package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class PowerPermissibleBase extends PermissibleBase {

	private PowerRanks plugin;
	private Player player;

	public PowerPermissibleBase(Player player, PowerRanks main) {
		super(player);
		this.player = player;
		this.plugin = main;
		PowerRanksVerbose.log("PowerPermissibleBase", "created");
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
        PowerRanksVerbose.log("hasPermission", "Return: " + (!permissions.contains("-" + permission) && (permissions.contains("*") || player.isOp() || super.hasPermission(permission) || permissions.contains(permission) || contains_wildcard)));
        PowerRanksVerbose.log("hasPermission", "===== ---------- hasPermission ---------- =====");
        PowerRanksVerbose.log("hasPermission", "");

        if (permissions.contains("-" + permission)){
            return false;
        }
        
        if (permissions.contains("*") || player.isOp()){
            return true;
        }
        
        return super.hasPermission(permission) || permissions.contains(permission) || contains_wildcard;
    }

	private ArrayList<String> generateWildcardList(String permission) {
		ArrayList<String> output = new ArrayList<String>();
		String[] permission_split = permission.split("\\.");
		
		permission_split = Util.array_pop(permission_split);
		for (int i = 0; i < permission_split.length + 1; i++) {
			output.add(String.join(".", permission_split) + ".*");
			permission_split = Util.array_pop(permission_split);
		}
		
		return output;
	}
}