package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_rankinfo extends PowerCommand {

	public cmd_rankinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 1) {
			String target_rank_name = args[0];
			PRRank target_rank = PRCache.getRank(target_rank_name);
			if (target_rank != null) {
				messageRankInfo(sender, target_rank, 0);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						plugin.getLanguageManager().getFormattedMessage(
								"general.rank-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", target_rank_name)
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			try {
				String target_rank_name = args[0];
				int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
				PRRank target_rank = PRCache.getRank(target_rank_name);
				if (target_rank != null) {
					messageRankInfo(sender, target_rank, page);
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager().getFormattedMessage(
									"general.rank-not-found"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", target_rank_name)
									.build(),
							'[', ']'));
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(
						plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			}
		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
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

	public void messageRankInfo(CommandSender sender, PRRank rank, int page) {

		String formatted_inheritances = "";
		for (String rankname : rank.getInheritances()) {
			formatted_inheritances += rankname + " ";
		}
		if (formatted_inheritances.endsWith(" ")) {
			formatted_inheritances = formatted_inheritances.substring(0, formatted_inheritances.length() - 1);
		}

		String formatted_buyableranks = "";
		for (String rankname : rank.getBuyableRanks()) {
			formatted_inheritances += rankname + " ";
		}
		if (formatted_inheritances.endsWith(" ")) {
			formatted_inheritances = formatted_inheritances.substring(0, formatted_inheritances.length() - 1);
		}

		sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
				+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
				+ "===");
		sender.sendMessage(TextFormat.GREEN + "Name: " + TextFormat.DARK_GREEN + rank.getName());
		sender.sendMessage(TextFormat.GREEN + "Weight: " + TextFormat.DARK_GREEN + rank.getWeight());
		sender.sendMessage(
				TextFormat.GREEN + "Prefix: " + TextFormat.RESET + plugin.getPowerColor()
						.format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getPrefix(), true, false, false));
		sender.sendMessage(
				TextFormat.GREEN + "Suffix: " + TextFormat.RESET + plugin.getPowerColor()
						.format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getSuffix(), true, false, false));
		sender.sendMessage(TextFormat.GREEN + "Chat format: " + TextFormat.RESET
				+ getSampleChatFormat(null, sender.getName(), "world", rank));
		sender.sendMessage(TextFormat.YELLOW + "Buyable ranks: " + TextFormat.GOLD + formatted_buyableranks);
		sender.sendMessage(TextFormat.YELLOW + "Buy cost: " + TextFormat.GOLD + rank.getBuyCost());
		sender.sendMessage(TextFormat.YELLOW + "Buy description: " + TextFormat.GOLD + rank.getBuyDescription());
		sender.sendMessage(TextFormat.YELLOW + "Buy command: " + TextFormat.GOLD + rank.getBuyCommand());
		sender.sendMessage(TextFormat.GREEN + "Inheritance(s): " + TextFormat.DARK_GREEN + formatted_inheritances);
		sender.sendMessage(TextFormat.GREEN + "Effective Permissions: ");

		ArrayList<PRPermission> playerPermissions = rank.getPermissions();
		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = playerPermissions.size() / lines_per_page;

		if (!(sender instanceof Player)) {
			page -= 1;
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		sender.sendMessage(TextFormat.AQUA + "Page " + TextFormat.BLUE + (page + 1) + TextFormat.AQUA + "/"
				+ TextFormat.BLUE + (last_page + 1));
		sender.sendMessage(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/pr" + " rankinfo "
				+ rank.getName() + " " + TextFormat.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));

		int line_index = 0;
		for (PRPermission permission : playerPermissions) {
			if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
				sender.sendMessage(TextFormat.DARK_GREEN + "#" + (line_index + 1) + ". "
						+ (!permission.getValue() ? TextFormat.RED : TextFormat.GREEN) + permission.getName());
			}
			line_index += 1;
		}

		sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
				+ TextFormat.BLUE + "===");
	}

	private String getSampleChatFormat(CommandSender sender, String name, String world, PRRank rank) {
		String playersChatMessage = "message";

		String format = plugin.getConfigManager().getString("chat.format", "");

		String formatted_prefix = "";
		String formatted_suffix = "";
		String chatColor = rank.getChatcolor();
		String nameColor = rank.getNamecolor();
		String usertag = "";

		formatted_prefix += rank.getPrefix();
		formatted_suffix += rank.getSuffix();

		String player_formatted_name = (nameColor.length() == 0 ? "&r" : nameColor) + name;
		String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : chatColor) + playersChatMessage;

		format = PRUtil.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
				.put("suffix", formatted_suffix)
				.put("usertag", usertag)
				.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
				.put("world", world).build(), '[', ']');

		format = plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, rank.getSuffix(), true, false, false);

		return format;
	}
}
