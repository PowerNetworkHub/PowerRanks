package nl.svenar.PowerRanks.Commands.rank;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;

public class cmd_rank_handler extends PowerCommand {

	public cmd_rank_handler(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args, boolean subcommandFailed) {
		if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
		} else {
			if (sender instanceof Player) {
				Messages.noPermission((Player) sender);
			}
		}

		return false;
	}

}
