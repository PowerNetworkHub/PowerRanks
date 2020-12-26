package nl.svenar.PowerRanks.Commands.rank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.PowerRanks.Commands.PowerSubCommand;
import nl.svenar.PowerRanks.Data.Messages;

public class cmd_rank_set extends PowerSubCommand {

	public cmd_rank_set(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onSubCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (verifyPermission(sender, "powerranks.command.rank.set")) {
		} else {
			if (sender instanceof Player) {
				Messages.noPermission((Player) sender);
			}
		}

		return false;
	}

}
