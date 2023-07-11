package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;

public class cmd_listranks extends PowerCommand {

	private Users users;

	public cmd_listranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		List<PRRank> ranks = users.getGroups();
		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
		// sender.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " +
		// ChatColor.GREEN + ranks.size());
		sender.sendMessage(ChatColor.AQUA + "Ranks (" + ranks.size() + "):");
		int index = 0;

		ranks = new ArrayList<>(new HashSet<>(ranks));
		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		for (PRRank rank : ranks) {
			index++;
			sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GRAY + "(" + rank.getWeight()
					+ ") " + ChatColor.GREEN + rank.getName() + ChatColor.RESET + " "
					+ PowerRanks.chatColor(rank.getPrefix(), true));
		}
		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
				+ ChatColor.BLUE + "===");

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
