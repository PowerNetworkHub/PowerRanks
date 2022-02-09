package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

public class cmd_addownrank extends PowerCommand {

	private Users users;

	public cmd_addownrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
		if (args.length == 1) {
			String target_rank = users.getRankIgnoreCase(args[0]);
			
			boolean commandAllowed = sender.hasPermission("powerranks.cmd.addrank") || sender.hasPermission("powerranks.cmd.addownrank");
			if (sender instanceof Player) {
				for (PRPermission permission : PowerRanks.getInstance()
						.getEffectivePlayerPermissions((Player) sender)) {
					if (permission.getName().equalsIgnoreCase("powerranks.cmd.addrank." + target_rank)) {
						commandAllowed = permission.getValue();
						break;
					}
				}
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(sender.getName());
				if (rank != null && targetPlayer != null) {
					targetPlayer.addRank(rank.getName());

					// Messages.messageSetRankSuccessSender(sender, targetPlayer.getName(), rank.getName());
					Messages.messageAddRankSuccessTarget((Player) sender, sender.getName(), rank.getName());
				}
			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		} else {
			if (sender.hasPermission("powerranks.cmd.addownrank") || sender.hasPermission("powerranks.cmd.addrank.*")) {
				Messages.messageCommandUsageAddown(sender);
			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}
		
		return tabcomplete;
	}
}
