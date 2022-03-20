package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

public class cmd_listdefaultranks extends PowerCommand {

	public cmd_listdefaultranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			List<PRRank> ranks = CacheManager.getDefaultRanks();
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
					+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
					+ "===");
			// sender.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " +
			// ChatColor.GREEN + ranks.size());
			sender.sendMessage(ChatColor.AQUA + "Ranks (" + ranks.size() + "):");
			int index = 0;

			ranks = new ArrayList<>(new HashSet<>(ranks));
			ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

			for (PRRank rank : ranks) {
				index++;
				sender.sendMessage(
						ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GRAY + "(" + rank.getWeight()
								+ ") " + ChatColor.GREEN + rank.getName() + ChatColor.RESET + " "
								+ PowerRanks.chatColor(rank.getPrefix(), true));
			}
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
					+ ChatColor.BLUE + "===");
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		return tabcomplete;
	}
}
