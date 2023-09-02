package nl.svenar.powerranks.commands.test;

import java.util.ArrayList;
import java.util.Set;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class cmd_test extends PowerCommand {

	public cmd_test(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		
				Set<Permission> permissions = Bukkit.getServer().getPluginManager().getPermissions();
				for (Permission permission : permissions) {
					sender.sendMessage(permission.getName() + ": " + permission.getDefault().toString() + " (" + permission.getDescription() + ")");
				}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		return tabcomplete;
	}
}
