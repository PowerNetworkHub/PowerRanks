package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_listpermissions extends PowerCommand {


	public cmd_listpermissions(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		String rankName = "";
		List<String> ranks = new ArrayList<String>();
		for (PRRank rank : PRCache.getRanks()) {
			ranks.add(rank.getName());
		}
		if (args.length == 1) {
			rankName = args[0];

			if (ranks.contains(rankName)) {
				// Messages.listRankPermissions(sender, s, rankName, 0);
				displayRankPermissions(sender, rankName, commandLabel, 0);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						plugin.getLanguageManager().getFormattedMessage(
								"general.rank-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", args[0])
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			rankName = args[0];
			if (ranks.contains(rankName)) {
				int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
				// Messages.listRankPermissions(sender, s, rankName, page);
				displayRankPermissions(sender, rankName, commandLabel, page);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						plugin.getLanguageManager().getFormattedMessage(
								"general.rank-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", args[0])
								.build(),
						'[', ']'));
			}
		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	private void displayRankPermissions(CommandSender sender, String rankName, String commandLabel, int page) {
		int targetPage = page;
		ArrayList<String> outputMessages = new ArrayList<String>();

		outputMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
				+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE + "===");

		PRRank rank = PRCache.getRank(rankName);
		List<PRPermission> ranksPermissions = rank.getPermissions();

		int linesPerPage = sender instanceof Player ? 5 : 10;
		int last_page = ranksPermissions.size() / linesPerPage;

		if (!(sender instanceof Player)) {
			targetPage -= 1;
		}

		targetPage = targetPage < 0 ? 0 : targetPage;
		targetPage = targetPage > last_page ? last_page : targetPage;

		if (sender instanceof Player) {
			String pageSelectorTellraw = "tellraw " + sender.getName()
					+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
					+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
					+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
					+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%next_page%"
					+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%next_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listpermissions %rankname% " + "%next_page%" + "\"}}]";

			pageSelectorTellraw = pageSelectorTellraw.replaceAll("%next_page%", String.valueOf(targetPage + 1));
			pageSelectorTellraw = pageSelectorTellraw.replaceAll("%previous_page%", String.valueOf(targetPage - 1));
			pageSelectorTellraw = pageSelectorTellraw.replaceAll("%last_page%",
					String.valueOf(last_page + 1));
			pageSelectorTellraw = pageSelectorTellraw.replaceAll("%rankname%", rankName);
			pageSelectorTellraw = pageSelectorTellraw.replaceAll("%commandlabel%", commandLabel);

			outputMessages.add(pageSelectorTellraw);

			outputMessages.add(TextFormat.AQUA + "Permissions:");

			// sender.sendMessage("[A] " + last_page + " " + linesPerPage);
		} else {
			outputMessages.add(TextFormat.AQUA + "Page " + TextFormat.BLUE + (targetPage + 1) + TextFormat.AQUA + "/"
					+ TextFormat.BLUE + (last_page + 1));
			outputMessages
					.add(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/" + commandLabel + " listpermissions "
							+ rankName + " " + TextFormat.BLUE + (targetPage + 2 > last_page + 1 ? last_page + 1 : targetPage + 2));
		}

		int lineIndex = 0;
		for (PRPermission permission : ranksPermissions) {
			if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
				outputMessages.add(TextFormat.DARK_GREEN + "#" + (lineIndex + 1) + ". "
						+ (permission.getValue() ? TextFormat.GREEN : TextFormat.RED) + permission.getName());
			}
			lineIndex += 1;
		}

		outputMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
				+ TextFormat.BLUE + "===");

		if (plugin != null) {
			for (String msg : outputMessages) {
				if (msg.startsWith("tellraw")) {
					plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);
				} else {
					sender.sendMessage(msg);
				}
			}
		}
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRRank rank : PRCache.getRanks()) {
				tabcomplete.add(rank.getName());
			}
		}

		return tabcomplete;
	}
}
