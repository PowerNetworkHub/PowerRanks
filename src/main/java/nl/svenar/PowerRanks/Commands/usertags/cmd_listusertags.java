package nl.svenar.PowerRanks.Commands.usertags;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_listusertags extends PowerCommand {

	private Users users;

	public cmd_listusertags(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (!PowerRanks.plugin_hook_deluxetags) {
			if (args.length == 0) {
				displayList(sender, commandLabel, 0);
			} else if (args.length == 1) {
				int page = Integer.parseInt(args[0].replaceAll("[a-zA-Z]", ""));
				displayList(sender, commandLabel, page);
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments"));
			}
		} else {
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
		}

		return false;
	}

	private void displayList(CommandSender sender, String commandLabel, int page) {
		ArrayList<String> output_messages = new ArrayList<String>();

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");

		Set<String> items = this.users.getUserTags();

		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = items.size() / lines_per_page;

		if (!(sender instanceof Player)) {
			page -= 1;
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		if (sender instanceof Player) {
			String page_selector_tellraw = "tellraw " + sender.getName()
					+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
					+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
					+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
					+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags " + "%previous_page%"
					+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags " + "%previous_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags " + "%previous_page%"
					+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags" + "%next_page%"
					+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags " + "%next_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listusertags " + "%next_page%" + "\"}}]";

			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%",
					String.valueOf(last_page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%commandlabel%", commandLabel);

			output_messages.add(page_selector_tellraw);

			output_messages.add(ChatColor.AQUA + "Usertags:");

			// sender.sendMessage("[A] " + last_page + " " + lines_per_page);
		} else {
			output_messages.add(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/"
					+ ChatColor.BLUE + (last_page + 1));
			output_messages.add(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/" + commandLabel + " listusertags "
					+ " " + ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
		}

		int line_index = 0;
		for (String item : items) {
			if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
				String usertagValue = this.users.getUserTagValue(item);
				output_messages.add(ChatColor.DARK_GREEN + "#" + (line_index + 1) + ". " + ChatColor.GREEN + item + " "
						+ ChatColor.RESET + PowerRanks.chatColor(usertagValue, true));
			}
			line_index += 1;
		}

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
				+ ChatColor.BLUE + "===");

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
		return tabcomplete;
	}
}
