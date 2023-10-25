package nl.svenar.powerranks.bukkit.commands.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.LanguageManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_help extends PowerCommand {

	public cmd_help(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"%color_command_allowed%\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
		String page_selector_tellraw = "tellraw " + sender.getName()
				+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
				+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
				+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
				+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr"
				+ " help " + "%previous_page%"
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%" + "\"}}]";

		ArrayList<String> help_messages = new ArrayList<String>();

		LanguageManager languageManager = PowerRanks.getLanguageManager();
		// PowerConfigManager languageManager = PowerRanks.getOldLanguageManager();
		List<String> lines = languageManager.getKeys("commands");

		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = lines.size() / lines_per_page;

		int page = 0;
		if (args.length > 0) {
			page = args[0].replaceAll("[a-zA-Z]", "").length() > 0 ? Integer.parseInt(args[0].replaceAll("[a-zA-Z]", "")) : 0;

			if (!(sender instanceof Player)) {
				page -= 1;
			}
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%", String.valueOf(last_page + 1));

		if (lines != null) {
			if (sender instanceof Player) {
				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
								.replaceAll("%plugin%", PowerRanks.pdf.getName())
								.replaceAll("%player%", sender.getName()));
				help_messages.add(page_selector_tellraw);
				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"Arguments: \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"blue\"},{\"text\":\"optional\",\"color\":\"aqua\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Arguments between [] are not required.\"}},{\"text\":\"]\",\"color\":\"blue\"},{\"text\":\" <\",\"color\":\"blue\"},{\"text\":\"required\",\"color\":\"aqua\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Arguments between <> are required.\"}},{\"text\":\">\",\"color\":\"blue\"}]"
								.replaceAll("%player%", sender.getName()));
			} else {
				help_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
						+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
						+ "===");
				help_messages.add(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/"
						+ ChatColor.BLUE + (last_page + 1));
				help_messages.add(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/" + commandLabel + " help "
						+ ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
			}

			int line_index = 0;
			for (String section : lines) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					String help_command = section + " " + languageManager.getUnformattedMessage("commands." + section + ".arguments");
					String help_description = languageManager.getUnformattedMessage("commands." + section + ".description");
					if (sender instanceof Player) {
						help_messages.add(tellrawbase.replaceAll("%arg%", help_command)
								.replaceAll("%help%", help_description).replaceAll("%player%", sender.getName())
								.replaceAll("%cmd%", commandLabel).replaceAll("%color_command_allowed%",
										sender.hasPermission("powerranks.cmd." + section) ? "green" : "red"));
					} else {
						help_messages.add(ChatColor.BLACK + "[" + ChatColor.GREEN + "/" + commandLabel + " "
								+ help_command + ChatColor.BLACK + "] " + ChatColor.DARK_GREEN + help_description);
					}
				}
				line_index += 1;
			}
		}

		if (sender instanceof Player) {
			help_messages.add(
					"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
							.replaceAll("%player%", sender.getName()));
		} else {
			help_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-----------------------------"
					+ ChatColor.BLUE + "===");
		}

		if (plugin != null)
			for (String msg : help_messages)
				if (sender instanceof Player) {
					plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);
				} else {
					sender.sendMessage(msg);
				}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
