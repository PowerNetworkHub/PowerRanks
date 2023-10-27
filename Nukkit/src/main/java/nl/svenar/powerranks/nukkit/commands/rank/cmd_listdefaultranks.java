package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_listdefaultranks extends PowerCommand {

	public cmd_listdefaultranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 0) {
			Set<PRRank> ranks = PRCache.getDefaultRanks();
			sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
					+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
					+ "===");
			// sender.sendMessage(TextFormat.DARK_GREEN + "Number of ranks: " +
			// TextFormat.GREEN + ranks.size());
			sender.sendMessage(TextFormat.AQUA + "Ranks (" + ranks.size() + "):");
			int index = 0;

			List<PRRank> sortedRanks = new ArrayList<>(ranks);
			PRUtil.sortRanksByWeight(sortedRanks);
			PRUtil.reverseRanks(sortedRanks);

			for (PRRank rank : sortedRanks) {
				index++;
				sender.sendMessage(
						TextFormat.DARK_GREEN + "#" + index + ". " + TextFormat.GRAY + "(" + rank.getWeight()
								+ ") " + TextFormat.GREEN + rank.getName() + TextFormat.RESET + " "
								+ plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getPrefix(), true, false, false));
			}
			sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
					+ TextFormat.BLUE + "===");
		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		return tabcomplete;
	}
}
