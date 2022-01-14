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
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

public class cmd_delrank extends PowerCommand {

	private Users users;

	public cmd_delrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);
			
			boolean commandAllowed = sender.hasPermission("powerranks.cmd.cmd_delrank");
			if (sender instanceof Player) {
				for (PRPermission permission : PowerRanks.getInstance()
						.getEffectivePlayerPermissions((Player) sender)) {
					if (permission.getName().equalsIgnoreCase("powerranks.cmd.cmd_delrank." + target_rank)) {
						commandAllowed = permission.getValue();
						break;
					}
				}
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					targetPlayer.removeRank(rank.getName());

					Messages.messageDelRankSuccessSender(sender, targetPlayer.getName(), rank.getName());
					if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
						Messages.messageDelRankSuccessTarget(Bukkit.getPlayer(targetPlayer.getUUID()), sender.getName(),
								rank.getName());
					}
				}
				// users.setGroup(sender instanceof Player ? (Player) sender : null, args[0],
				// target_rank, true);

			} else {
				Messages.noPermission(sender);
			}
		} else {
			if (sender.hasPermission("powerranks.cmd.cmd_delrank") || sender.hasPermission("powerranks.cmd.cmd_delrank.*")) {
				Messages.messageCommandUsageDel(sender);
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
			for (String rank : CacheManager.getPlayer(((Player) sender).getUniqueId().toString()).getRanks()) {
				tabcomplete.add(rank);
			}
		}

		return tabcomplete;
	}
}
