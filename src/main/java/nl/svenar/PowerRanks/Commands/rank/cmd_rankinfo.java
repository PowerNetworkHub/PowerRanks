package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.common.structure.PRRank;

public class cmd_rankinfo extends PowerCommand {


	public cmd_rankinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
		if (sender.hasPermission("powerranks.cmd.rankinfo")) {
			if (args.length == 1) {
				String target_rank_name = args[0];
				PRRank target_rank = CacheManager.getRank(target_rank_name);
				if (target_rank != null) {
					Messages.messageRankInfo(sender, target_rank, 0);
				} else {
					Messages.messageGroupNotFound(sender, target_rank_name);
				}
			} else if (args.length == 2) {
				String target_rank_name = args[0];
				int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
				PRRank target_rank = CacheManager.getRank(target_rank_name);
				if (target_rank != null) {
					Messages.messageRankInfo(sender, target_rank, page);
				} else {
					Messages.messageGroupNotFound(sender, target_rank_name);
				}
			} else {
				Messages.messageCommandUsageRankinfo(sender);
			}
		} else {
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRRank rank : CacheManager.getRanks()) {
				tabcomplete.add(rank.getName());
			}
		}

		return tabcomplete;
	}
}
