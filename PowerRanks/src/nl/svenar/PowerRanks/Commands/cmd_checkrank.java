package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_checkrank extends PowerCommand {

	private Users users;

	public cmd_checkrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.checkrank")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					String rankname = this.users.getGroup((Player) sender);
					Messages.messageCommandCheckrankResponse(sender, sender.getName(), rankname);
				} else {
					Messages.messageConsoleNotAPlayer(sender);
				}
			} else if (args.length == 1) {
				Player targetPlayer = Util.getPlayerByName(args[0]);
				if (targetPlayer != null) {
					String rankname = this.users.getGroup(targetPlayer);
					Messages.messageCommandCheckrankResponse(sender, targetPlayer.getName(), rankname);
				} else {
					Messages.messagePlayerNotFound(sender, args[0]);
				}
			} else {
				Messages.messageCommandUsageCheck(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		
		if (args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
