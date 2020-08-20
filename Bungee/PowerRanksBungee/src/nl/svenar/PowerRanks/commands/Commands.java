package nl.svenar.PowerRanks.commands;

import java.util.Arrays;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import nl.svenar.PowerRanks.PowerRanks;

public class Commands extends Command {

	private static HashMap<String, PowerRanksCommand> powerranks_commands = new HashMap<>();

	public Commands(String name) {
		super(name);
		new cmd_help();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------").create());
			sender.sendMessage(new ComponentBuilder(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.").create());
			sender.sendMessage(new ComponentBuilder().create());
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + PowerRanks.pdf.getAuthor()).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion()).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + PowerRanks.website_url).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar").create());
			sender.sendMessage(new ComponentBuilder(ChatColor.DARK_AQUA + "--------------------------").create());
		} else {
			String command = args[0];
			PowerRanksCommand command_handler = get_powerranks_command(command);
			if (command_handler != null) {
				command_handler.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
			} else {
				sender.sendMessage(new ComponentBuilder("Unknown Command").color(ChatColor.DARK_RED).create());
			}
		}
	}
	
	public static PowerRanksCommand get_powerranks_command(String command_name) {
		return powerranks_commands.get(command_name.toLowerCase());
	}

	public static void add_powerranks_command(String command_name, PowerRanksCommand command_handler) {
		powerranks_commands.put(command_name.toLowerCase(), command_handler);
	}
}
