package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.manager.LanguageManager;

public class cmd_help extends PowerCommand {

	public cmd_help(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		ArrayList<String> help_messages = new ArrayList<String>();

		LanguageManager languageManager = plugin.getLanguageManager();
		List<String> lines = languageManager.getKeys("commands");

		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = lines.size() / lines_per_page;

		int page = 0;
		if (args.length > 0) {
			page = args[0].replaceAll("[a-zA-Z]", "").length() > 0
					? Integer.parseInt(args[0].replaceAll("[a-zA-Z]", ""))
					: 0;

			if (!(sender instanceof Player)) {
				page -= 1;
			}
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		if (lines != null) {
			help_messages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
					+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
					+ "===");
			help_messages.add(TextFormat.AQUA + "Page " + TextFormat.BLUE + (page + 1) + TextFormat.AQUA + "/"
					+ TextFormat.BLUE + (last_page + 1));
			help_messages.add(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/" + commandLabel + " help "
					+ TextFormat.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));

			int line_index = 0;
			for (String section : lines) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					String help_command = section + " "
							+ languageManager.getUnformattedMessage("commands." + section + ".arguments");
					String help_description = languageManager
							.getUnformattedMessage("commands." + section + ".description");
					help_messages.add(TextFormat.BLACK + "[" + TextFormat.GREEN + "/" + commandLabel + " "
							+ help_command + TextFormat.BLACK + "] " + TextFormat.DARK_GREEN + help_description);
				}
				line_index += 1;
			}
		}

		help_messages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "-----------------------------"
				+ TextFormat.BLUE + "===");

		if (plugin != null)
			for (String msg : help_messages)
				sender.sendMessage(msg);

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
