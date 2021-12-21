package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

public class cmd_setrank extends PowerCommand {

	private Users users;

	public cmd_setrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);
			if (sender.hasPermission("powerranks.cmd.setrank")
					|| sender.hasPermission("powerranks.cmd.setrank." + target_rank.toLowerCase())) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					targetPlayer.setRank(rank.getName());

					Messages.messageSetRankSuccessSender(sender, targetPlayer.getName(), rank.getName());
					if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
						Messages.messageSetRankSuccessTarget(Bukkit.getPlayer(targetPlayer.getUUID()), sender.getName(), rank.getName());
					}
				}
				// users.setGroup(sender instanceof Player ? (Player) sender : null, args[0],
				// target_rank, true);

			} else {
				Messages.noPermission(sender);
			}
		} else {
			if (sender.hasPermission("powerranks.cmd.setrank") || sender.hasPermission("powerranks.cmd.setrank.*")) {
				Messages.messageCommandUsageSet(sender);
			} else {
				Messages.noPermission(sender);
			}
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

		if (args.length == 2) {
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}

		return tabcomplete;
	}
}
