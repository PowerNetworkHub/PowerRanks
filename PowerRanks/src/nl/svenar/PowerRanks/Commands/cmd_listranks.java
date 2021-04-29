package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_listranks extends PowerCommand {

	private Users users;

	public cmd_listranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.listranks")) {
			Set<String> ranks = users.getGroups();
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
			// sender.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " + ChatColor.GREEN + ranks.size());
			sender.sendMessage(ChatColor.AQUA + "Ranks (" + ranks.size() + "):");
			int index = 0;
			for (String rank : ranks) {
				index++;
				sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + rank + ChatColor.RESET + " " + PowerRanks.chatColor(users.getPrefix(rank), true));
			}
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
