package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_delperm extends PowerCommand {

	private Users users;

	public cmd_delperm(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.delperm")) {
			if (args.length == 2) {
				final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
				final String permission = args[1];
				final boolean result = this.users.removePermission(rankname, permission);
				if (result) {
					if (rankname.equals("*")) {
						Messages.messageCommandPermissionRemovedFromAllRanks(sender, permission);
					} else {
						Messages.messageCommandPermissionRemoved(sender, permission, rankname);
					}
				} else {
					Messages.messageGroupNotFound(sender, rankname);
				}
			} else {
				Messages.messageCommandUsageDelperm(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getGroups()) {
				tabcomplete.add(rank);
			}
		}

		if (args.length == 2) {
			List<String> ranksPermissions = this.users.getPermissions(this.users.getRankIgnoreCase(args[0]));
			// for (Permission pai : Bukkit.getServer().getPermissions()) {
			for (String permission : ranksPermissions) {

				if (!tabcomplete.contains(permission)) {
					tabcomplete.add(permission);
				}
			}
		}

		return tabcomplete;
	}
}
