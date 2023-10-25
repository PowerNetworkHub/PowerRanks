package nl.svenar.powerranks.bukkit.commands.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.PowerPermissibleBase;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_test extends PowerCommand {

	public cmd_test(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		
				// Set<Permission> permissions = Bukkit.getServer().getPluginManager().getPermissions();
				// for (Permission permission : permissions) {
				// 	sender.sendMessage(permission.getName() + ": " + permission.getDefault().toString() + " (" + permission.getDescription() + ")");
				// }

				if (sender instanceof Player) {
					List<PRPermission> playerPermissions = PowerRanks.getInstance().getEffectivePlayerPermissions((Player) sender);
					for (PRPermission permission : playerPermissions) {
						sender.sendMessage(permission.getName() + ": " + permission.getValue());
					}
				} else {
					Map<String, Integer> permissionCallCount = PowerPermissibleBase.permissionCallCount;
					// sort from high to low value
					permissionCallCount = sortByValue(permissionCallCount, false);
					int index = 0;
					for (Entry<String, Integer> entry : permissionCallCount.entrySet()) {
						if (index >= 20)
							break;
						sender.sendMessage(entry.getKey() + ": " + entry.getValue());
						index++;
					}
				}

		return false;
	}

	private Map<String, Integer> sortByValue(Map<String, Integer> permissionCallCount, boolean asc) {
		// sort from high to low value
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(permissionCallCount.entrySet());
		java.util.Collections.sort(list, new java.util.Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if (asc) {
					return (o1.getValue()).compareTo(o2.getValue());
				} else {
					return (o2.getValue()).compareTo(o1.getValue());
				}
			}
		});
		// return
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		return tabcomplete;
	}
}
