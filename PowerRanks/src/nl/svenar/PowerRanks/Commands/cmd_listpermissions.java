package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util;

public class cmd_listpermissions extends PowerCommand {

	private Users users;

	public cmd_listpermissions(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.listpermissions")) {
			String rankName = "";
			if (args.length == 1) {
				rankName = users.getRankIgnoreCase(args[0]);
				if (users.getGroups().contains(rankName)) {
					// Messages.listRankPermissions(sender, s, rankName, 0);
					displayRankPermissions(sender, rankName, commandLabel, 0);
				} else {
					Messages.messageGroupNotFound(sender, args[0]);
				}
			} else if (args.length == 2) {
				rankName = users.getRankIgnoreCase(args[0]);
				if (users.getGroups().contains(rankName)) {
					int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
					// Messages.listRankPermissions(sender, s, rankName, page);
					displayRankPermissions(sender, rankName, commandLabel, page);
				} else {
					Messages.messageGroupNotFound(sender, args[0]);
				}
			} else {
				Messages.messageCommandUsageListPermissions(sender);
			}

		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	private void displayRankPermissions(CommandSender sender, String rankName, String commandLabel, int page) {
		ArrayList<String> output_messages = new ArrayList<String>();

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");

		List<String> ranksPermissions = this.users.getPermissions(rankName);

		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = ranksPermissions.size() / lines_per_page;


		if (!(sender instanceof Player)) {
			page -= 1;
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		if (sender instanceof Player) {
			String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%" + "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
				+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}" + ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%next_page%"
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%next_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "%commandlabel%" + " listpermissions %rankname% " + "%next_page%" + "\"}}]";
			
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%", String.valueOf(last_page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%rankname%", rankName);
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%commandlabel%", commandLabel);

			output_messages.add(page_selector_tellraw);

			output_messages.add(ChatColor.AQUA + "Permissions:");

			// sender.sendMessage("[A] " + last_page + " " + lines_per_page);
		} else {
			output_messages.add(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/" + ChatColor.BLUE + (last_page + 1));
			output_messages.add(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/" + commandLabel + " listpermissions " + rankName + " " + ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
		}

		int line_index = 0;
		for (String permission : ranksPermissions) {
			if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
				output_messages.add(ChatColor.DARK_GREEN + "#" + (line_index + 1) + ". " + (permission.startsWith("-") ? ChatColor.RED : ChatColor.GREEN) + permission);
			}
			line_index += 1;
		}

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");

		if (plugin != null) {
			for (String msg : output_messages) {
				if (msg.startsWith("tellraw")) {
					plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);
				} else {
					sender.sendMessage(msg);
				}
			}
		}
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getGroups()) {
				tabcomplete.add(rank);
			}
		}

		return tabcomplete;
	}
}
