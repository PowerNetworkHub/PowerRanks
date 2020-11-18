package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class cmd_help extends PowerCommand {

	public cmd_help(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, boolean subcommandFailed) {
		if (sender instanceof Player) {
			if (sender.hasPermission("powerranks.cmd.help")) {
				String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"%color_command_allowed%\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
				String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
						+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%previous_page%"
						+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%previous_page%"
						+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%previous_page%"
						+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%next_page%"
						+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%next_page%"
						+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + commandLabel + " help " + "%next_page%" + "\"}}]";

				ArrayList<String> help_messages = new ArrayList<String>();

				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
								.replaceAll("%plugin%", PowerRanks.pdf.getName()).replaceAll("%player%", sender.getName()));

				YamlConfiguration langYaml = PowerRanks.loadLangFile();
				ConfigurationSection lines = langYaml.getConfigurationSection("commands.help");

				int page = 0;
				int lines_per_page = 5;
				if (args.length > 0) {
					page = Integer.parseInt(args[0].replaceAll("[a-zA-Z]", ""));
				}

				page = page < 0 ? 0 : page;
				page = page > lines.getKeys(false).size() / lines_per_page ? lines.getKeys(false).size() / lines_per_page : page;

				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));

				if (lines != null) {
					help_messages.add(page_selector_tellraw);
					help_messages.add("tellraw %player% {\"text\":\"[optional] <required>\",\"color\":\"dark_aqua\"}".replaceAll("%player%", sender.getName()));
					int line_index = 0;
					for (String section : lines.getKeys(false)) {
						if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
							String help_command = langYaml.getString("commands.help." + section + ".command");
							String help_description = langYaml.getString("commands.help." + section + ".description");
							help_messages.add(tellrawbase.replaceAll("%arg%", help_command).replaceAll("%help%", help_description).replaceAll("%player%", sender.getName()).replaceAll("%cmd%", commandLabel)
									.replaceAll("%color_command_allowed%", sender.hasPermission("powerranks.cmd." + section) ? "green" : "red"));
						}
						line_index += 1;
					}
				}

				help_messages.add(
						"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]".replaceAll("%player%", sender.getName()));

				if (plugin != null)
					for (String msg : help_messages)
						plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);

			} else {
				sender.sendMessage(plugin.plp + ChatColor.DARK_RED + "You do not have permission to execute this command");
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");

			YamlConfiguration langYaml = PowerRanks.loadLangFile();

			ConfigurationSection lines = langYaml.getConfigurationSection("commands.help");
			if (lines != null) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[Optional] <Required>");
				String prefix = langYaml.getString("general.prefix");
				for (String section : lines.getKeys(false)) {
					String line = "&a/" + commandLabel + " " + langYaml.getString("commands.help." + section + ".command") + "&2 - " + langYaml.getString("commands.help." + section + ".description");
					line = Util.replaceAll(line, "%base_cmd%", "/" + commandLabel);
					line = Util.replaceAll(line, "%plugin_prefix%", prefix);
					line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
					String msg = PowerRanks.chatColor(line, true);
					if (msg.length() > 0)
						sender.sendMessage(msg);
				}
			}

			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");
		}

		return false;
	}

}
