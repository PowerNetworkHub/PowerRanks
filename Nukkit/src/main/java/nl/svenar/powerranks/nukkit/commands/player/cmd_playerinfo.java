package nl.svenar.powerranks.nukkit.commands.player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;

public class cmd_playerinfo extends PowerCommand {

	public cmd_playerinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 1) {
			String targetPlayerName = args[0];
			PRPlayer targetPlayer = PRCache.getPlayer(targetPlayerName);
			if (targetPlayer != null) {
				messagePlayerInfo(sender, targetPlayer, 0);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", targetPlayerName)
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			try {
				String targetPlayerName = args[0];
				int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
				PRPlayer targetPlayer = PRCache.getPlayer(targetPlayerName);
				if (targetPlayer != null) {
					messagePlayerInfo(sender, targetPlayer, page);
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", targetPlayerName)
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
			for (PRPlayer prPlayer : PRCache.getPlayers()) {
				tabcomplete.add(prPlayer.getName());
			}
		}

		return tabcomplete;
	}

	public void messagePlayerInfo(final CommandSender sender, final PRPlayer prPlayer, int page) {
		Player player = Util.getPlayerByName(prPlayer.getName());

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		long playerPlaytime = prPlayer.getPlaytime();

		final long days = TimeUnit.SECONDS.toDays(playerPlaytime);
		final long hours = TimeUnit.SECONDS.toHours(playerPlaytime)
				- TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(playerPlaytime));
		final long minutes = TimeUnit.SECONDS.toMinutes(playerPlaytime)
				- TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(playerPlaytime));
		final long seconds = TimeUnit.SECONDS.toSeconds(playerPlaytime)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(playerPlaytime));

		String playerPlaytimeFormatted = days > 0
				? String.format("%02d %s %02d:%02d:%02d", days, days == 1 ? "day" : "days", hours, minutes, seconds)
				: String.format("%02d:%02d:%02d", hours, minutes, seconds);

		String formatted_ranks = "";

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : prPlayer.getRanks()) {
			if (!playerRank.isDisabled()) {
				ranknames.add(playerRank.getName());
			}
		}

		for (String rankname : ranknames) {
			formatted_ranks += rankname + " ";
		}
		if (formatted_ranks.endsWith(" ")) {
			formatted_ranks = formatted_ranks.substring(0, formatted_ranks.length() - 1);
		}

		sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
				+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
				+ "===");
		if (player == null) {
			sender.sendMessage(TextFormat.GREEN + "Player not online, showing limited information.");
		}
		sender.sendMessage(TextFormat.GREEN + "UUID: " + TextFormat.DARK_GREEN + prPlayer.getUUID());
		if (player != null) {
			sender.sendMessage(TextFormat.GREEN + "Player name: " + TextFormat.DARK_GREEN + player.getDisplayName()
					+ (!player.getDisplayName().equals(player.getName())
							? (TextFormat.DARK_GREEN + " aka " + player.getName())
							: ""));
			sender.sendMessage(TextFormat.GREEN + "First joined (UTC): " + TextFormat.DARK_GREEN
					+ format.format(player.getFirstPlayed()));
			sender.sendMessage(
					TextFormat.GREEN + "Last joined (UTC): " + TextFormat.DARK_GREEN
							+ format.format(player.getLastPlayed()));
		} else {
			sender.sendMessage(TextFormat.GREEN + "Player name: " + TextFormat.DARK_GREEN + prPlayer.getName());
		}
		sender.sendMessage(TextFormat.GREEN + "Playtime: " + TextFormat.DARK_GREEN + playerPlaytimeFormatted);
		if (player != null) {
			sender.sendMessage(TextFormat.GREEN + "Chat format: " + TextFormat.RESET + getSampleChatFormat(player));
		}
		sender.sendMessage(TextFormat.GREEN + "Rank(s): " + TextFormat.DARK_GREEN + formatted_ranks);
		if (player != null) {
			sender.sendMessage(TextFormat.GREEN + "Effective Permissions: ");

			List<PRPermission> playerPermissions = prPlayer.getEffectivePermissions();
			int lines_per_page = sender instanceof Player ? 5 : 10;
			int last_page = playerPermissions.size() / lines_per_page;

			if (!(sender instanceof Player)) {
				page -= 1;
			}

			page = page < 0 ? 0 : page;
			page = page > last_page ? last_page : page;

			sender.sendMessage(TextFormat.AQUA + "Page " + TextFormat.BLUE + (page + 1) + TextFormat.AQUA + "/"
					+ TextFormat.BLUE + (last_page + 1));
			sender.sendMessage(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/pr" + " playerinfo "
					+ player.getName() + " " + TextFormat.BLUE
					+ (page + 2 > last_page + 1 ? last_page + 1 : page + 2));

			int line_index = 0;
			for (PRPermission permission : playerPermissions) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					sender.sendMessage(TextFormat.DARK_GREEN + "#" + (line_index + 1) + ". "
							+ (!permission.getValue() ? TextFormat.RED : TextFormat.GREEN) + permission.getName());
				}
				line_index += 1;
			}
		}

		sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
				+ TextFormat.BLUE + "===");
	}

	private String getSampleChatFormat(Player player) {
		String playersChatMessage = "message";

		String format = plugin.getConfigManager().getString("chat.format", "");

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : PRCache.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = PRCache.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

		String formatted_prefix = "";
		String formatted_suffix = "";
		String chatColor = ranks.size() > 0 ? ranks.get(0).getChatcolor() : "&f";
		String nameColor = ranks.size() > 0 ? ranks.get(0).getNamecolor() : "&f";
		String usertag = "";

		for (PRRank rank : ranks) {
			formatted_prefix += rank.getPrefix() + " ";
			formatted_suffix += rank.getSuffix() + " ";
		}

		if (formatted_prefix.endsWith(" ")) {
			formatted_prefix = formatted_prefix.substring(0, formatted_prefix.length() - 1);
		}

		if (formatted_suffix.endsWith(" ")) {
			formatted_suffix = formatted_suffix.substring(0, formatted_suffix.length() - 1);
		}

		PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
		Map<?, ?> availableUsertags = plugin.getUsertagStorage().getMap("usertags", new HashMap<String, String>());
		Set<String> playerUsertags = targetPlayer.getUsertags();

		for (String playerUsertag : playerUsertags) {
			String value = "";
			for (Entry<?, ?> entry : availableUsertags.entrySet()) {
				if (entry.getKey().toString().equalsIgnoreCase(playerUsertag)) {
					value = entry.getValue().toString();
				}
			}

			if (value.length() > 0) {
				usertag += (usertag.length() > 0 ? " " : "") + value;
			}
		}

		if (!player.hasPermission("powerranks.chat.chatcolor")) {
			playersChatMessage = playersChatMessage.replaceAll("(&[0-9a-fA-FiIjJrRlLmMnNoO])|(#[0-9a-fA-F]{6})", "");
		}
		String player_formatted_name = (nameColor.length() == 0 ? "&r" : nameColor) + player.getDisplayName();
		String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : chatColor) + playersChatMessage;

		format = PRUtil.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
				.put("suffix", formatted_suffix)
				.put("usertag", usertag)
				.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
				.put("world", player.getLevel().getName()).build(), '[', ']');

		format = plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, format, true, false, false);

		return format;
	}
}
