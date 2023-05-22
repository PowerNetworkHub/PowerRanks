package nl.svenar.powerranks.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommandHandler;

public class ChatTabExecutor implements TabCompleter {

	private static ArrayList<String> addon_commands = new ArrayList<String>();

	public ChatTabExecutor(PowerRanks plugin) {
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<String>();

		ArrayList<String> commands_list = new ArrayList<String>();
		
		for (String arg : PowerCommandHandler.handle_tab_complete(sender, args[0], Arrays.copyOfRange(args, 1, args.length))) {
			commands_list.add(arg);
		}

		if (args.length == 1) {
			for (String command : addon_commands) {
				if (command.toLowerCase().contains(args[0].toLowerCase()))
					commands_list.add(command);
			}
		}

		for (String command : commands_list) {
			if (command.toLowerCase().contains(args[args.length - 1].toLowerCase()))
				list.add(command);
		}

		return list;
	}

	public static void addAddonCommand(String command) {
		if (!addon_commands.contains(command.toLowerCase()))
			addon_commands.add(command.toLowerCase());
	}
}