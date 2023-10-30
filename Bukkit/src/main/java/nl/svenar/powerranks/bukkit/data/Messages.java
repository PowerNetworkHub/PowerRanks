package nl.svenar.powerranks.bukkit.data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.AddonsManager;
import nl.svenar.powerranks.bukkit.addons.DownloadableAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksPlayer;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.external.VaultHook;
import nl.svenar.powerranks.bukkit.util.Util;

import com.google.common.collect.ImmutableMap;

import me.clip.placeholderapi.PlaceholderAPI;

public class Messages {
	private static PowerRanks powerRanks = null;

	public Messages(PowerRanks powerRanks) {
		Messages.powerRanks = powerRanks;
	}

	public static void messageNoArgs(final CommandSender console) {
		console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		console.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For a list of commands.");
		console.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		console.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors().get(0));
		console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
		console.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
		console.sendMessage(ChatColor.DARK_GREEN + "Support me on: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
		console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageStats(CommandSender sender) {
		Users users = new Users(null);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		int addonCount = 0;
		for (Entry<File, Boolean> prAddon : AddonsManager.loadedAddons.entrySet()) {
			if (prAddon.getValue() == true)
				addonCount++;
		}
		Instant current_time = Instant.now();

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Server version: " + ChatColor.DARK_GREEN + Bukkit.getVersion() + " | "
				+ Bukkit.getServer().getBukkitVersion());
		sender.sendMessage(
				ChatColor.GREEN + "Java version: " + ChatColor.DARK_GREEN + System.getProperty("java.version"));
		sender.sendMessage(ChatColor.GREEN + "Storage method: " + ChatColor.DARK_GREEN
				+ PowerRanks.getConfigManager().getString("storage.type", "yaml").toUpperCase());
		sender.sendMessage(ChatColor.GREEN + "Uptime: " + ChatColor.DARK_GREEN
				+ format.format(Duration.between(PowerRanks.powerranks_start_time, current_time).toMillis()));
		sender.sendMessage(
				ChatColor.GREEN + "PowerRanks Version: " + ChatColor.DARK_GREEN + PowerRanks.pdf.getVersion());
		sender.sendMessage(ChatColor.GREEN + "Registered ranks: " + ChatColor.DARK_GREEN + users.getGroups().size());
		sender.sendMessage(
				ChatColor.GREEN + "Registered players: " + ChatColor.DARK_GREEN + users.getCachedPlayers().size());
		sender.sendMessage(ChatColor.GREEN + "Registered addons: " + ChatColor.DARK_GREEN + addonCount);

		boolean hex_color_supported = false;
		try {
			"#FF0000a".replace("#FF0000", net.md_5.bungee.api.ChatColor.of("#FF0000") + "");
			hex_color_supported = true;
		} catch (Exception | NoSuchMethodError e) {
			hex_color_supported = false;
		}
		sender.sendMessage(ChatColor.GREEN + "RGB colors: "
				+ (hex_color_supported ? ChatColor.DARK_GREEN + "" : ChatColor.DARK_RED + "un") + "supported");
		sender.sendMessage(ChatColor.GREEN + "Bungeecord: "
				+ (PowerRanks.getInstance().getBungeecordManager().isReady() ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Connected servers: "
				+ ChatColor.DARK_GREEN + PowerRanks.getInstance().getBungeecordManager().getServerCount());

		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
		sender.sendMessage(ChatColor.GREEN + "- Vault Economy: "
				+ (PowerRanks.vaultEconomyEnabled ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions: "
				+ (PowerRanks.vaultPermissionsEnabled ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI: "
				+ (PowerRanks.getPlaceholderapiExpansion() != null ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags: "
				+ (PowerRanks.plugin_hook_deluxetags ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- NametagEdit: "
				+ (PowerRanks.plugin_hook_nametagedit ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));

		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		String pluginNames = "";
		for (Plugin plugin : plugins) {
			pluginNames += plugin.getName() + "(" + plugin.getDescription().getVersion() + "), ";
		}
		pluginNames = pluginNames.substring(0, pluginNames.length() - 2);

		sender.sendMessage(ChatColor.GREEN + "Plugins (" + plugins.length + "): " + ChatColor.DARK_GREEN + pluginNames);
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messagePluginhookStats(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
		sender.sendMessage(ChatColor.GREEN + "- Vault Economy (vault_economy): "
				+ (PowerRanks.vaultEconomyEnabled ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions (vault_permissions): "
				+ (PowerRanks.vaultPermissionsEnabled ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI (placeholderapi): "
				+ (PowerRanks.getPlaceholderapiExpansion() != null ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags (deluxetags): "
				+ (PowerRanks.plugin_hook_deluxetags ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- NametagEdit (nametagedit): "
				+ (PowerRanks.plugin_hook_nametagedit ? ChatColor.DARK_GREEN + "enabled"
						: ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandFactoryReset(CommandSender sender) {
		PowerRanks.factoryresetid = (100 + Math.round(Math.random() * 900)) + "-"
				+ (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.DARK_RED + "WARNING!");
		sender.sendMessage(ChatColor.RED + "This action is irreversible if you continue");
		sender.sendMessage(ChatColor.RED + "Factory reset ID: " + ChatColor.GOLD + PowerRanks.factoryresetid);
		sender.sendMessage(
				ChatColor.RED + "To continue do: " + ChatColor.GOLD + "/pr factoryreset " + PowerRanks.factoryresetid);
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandFactoryResetDone(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Factory reset complete!");
		sender.sendMessage(ChatColor.GREEN + "It is recommended to restart your server.");
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandBuyrank(CommandSender sender, Users users, String rankname) {
		if (rankname == null) {
			String tellraw_command = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"Buy\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\" %rank% \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"|\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\" Cost: \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"%cost%\",\"color\":\"%cost_color%\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}}]";
			double player_balance = VaultHook.getVaultEconomy() != null
					? VaultHook.getVaultEconomy().getBalance((Player) sender)
					: 0;

			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
					+ ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_GREEN + "Your balance: " + ChatColor.GREEN + player_balance);
			sender.sendMessage(ChatColor.DARK_GREEN + "Ranks available to buy (click to buy):");
			List<String> ranks = users.getBuyableRanks(users.getPrimaryRank((Player) sender));
			for (String rank : ranks) {
				float cost = CacheManager.getRank(rank).getBuyCost();
				String cost_color = player_balance >= cost ? "green" : "red";
				// sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GREEN + "Buy" +
				// ChatColor.BLACK + "] " + ChatColor.RESET + rank + " | Cost: " +
				// String.valueOf(cost)); // TODO: Make [Buy] clickable
				if (Messages.powerRanks != null)
					Messages.powerRanks.getServer().dispatchCommand(
							(CommandSender) Messages.powerRanks.getServer().getConsoleSender(),
							tellraw_command.replaceAll("%rank%", rank).replaceAll("%cost%", String.valueOf(cost))
									.replaceAll("%cost_color%", cost_color).replaceAll("%player%", sender.getName()));
			}
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		} else {
			String tellraw_command = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}},{\"text\":\"Confirm\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}}]";
			double player_balance = VaultHook.getVaultEconomy() != null
					? VaultHook.getVaultEconomy().getBalance((Player) sender)
					: 0;

			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
					+ ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_GREEN + "Your balance: " + ChatColor.GREEN + player_balance);
			sender.sendMessage(ChatColor.DARK_GREEN + "Click 'confirm' to purchase " + rankname);
			float cost = CacheManager.getRank(rankname).getBuyCost();
			sender.sendMessage(
					"Cost: " + (player_balance >= cost ? ChatColor.GREEN : ChatColor.RED) + String.valueOf(cost));
			if (Messages.powerRanks != null)
				Messages.powerRanks.getServer().dispatchCommand(
						(CommandSender) Messages.powerRanks.getServer().getConsoleSender(),
						tellraw_command.replaceAll("%rank%", rankname).replaceAll("%player%", sender.getName()));
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		}
	}

	public static void messageRankInfo(CommandSender sender, PRRank rank, int page) {

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

		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ Messages.powerRanks.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
				+ "===");
		sender.sendMessage(ChatColor.GREEN + "Name: " + ChatColor.DARK_GREEN + rank.getName());
		sender.sendMessage(ChatColor.GREEN + "Weight: " + ChatColor.DARK_GREEN + rank.getWeight());
		sender.sendMessage(
				ChatColor.GREEN + "Prefix: " + ChatColor.RESET + PowerRanks.chatColor(rank.getPrefix(), true));
		sender.sendMessage(
				ChatColor.GREEN + "Suffix: " + ChatColor.RESET + PowerRanks.chatColor(rank.getSuffix(), true));
		sender.sendMessage(ChatColor.GREEN + "Chat format: " + ChatColor.RESET
				+ getSampleChatFormat(null, sender.getName(), "world", rank));
		sender.sendMessage(ChatColor.YELLOW + "Buyable ranks: " + ChatColor.GOLD + formatted_buyableranks);
		sender.sendMessage(ChatColor.YELLOW + "Buy cost: " + ChatColor.GOLD + rank.getBuyCost());
		sender.sendMessage(ChatColor.YELLOW + "Buy description: " + ChatColor.GOLD + rank.getBuyDescription());
		sender.sendMessage(ChatColor.YELLOW + "Buy command: " + ChatColor.GOLD + rank.getBuyCommand());
		sender.sendMessage(ChatColor.GREEN + "Inheritance(s): " + ChatColor.DARK_GREEN + formatted_inheritances);
		sender.sendMessage(ChatColor.GREEN + "Effective Permissions: ");

		ArrayList<PRPermission> playerPermissions = rank.getPermissions();
		int lines_per_page = sender instanceof Player ? 5 : 10;
		int last_page = playerPermissions.size() / lines_per_page;

		if (!(sender instanceof Player)) {
			page -= 1;
		}

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		if (sender instanceof Player) {
			String page_selector_tellraw = "tellraw " + sender.getName()
					+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
					+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
					+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
					+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%previous_page%"
					+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%next_page%"
					+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%next_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " rankinfo %rankname% " + "%next_page%" + "\"}}]";

			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%",
					String.valueOf(last_page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%rankname%", rank.getName());
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%commandlabel%", "pr");

			Messages.powerRanks.getServer().dispatchCommand(
					(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);
		} else {
			sender.sendMessage(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/"
					+ ChatColor.BLUE + (last_page + 1));
			sender.sendMessage(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/pr" + " rankinfo "
					+ rank.getName() + " " + ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
		}

		int line_index = 0;
		for (PRPermission permission : playerPermissions) {
			if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
				sender.sendMessage(ChatColor.DARK_GREEN + "#" + (line_index + 1) + ". "
						+ (!permission.getValue() ? ChatColor.RED : ChatColor.GREEN) + permission.getName());
			}
			line_index += 1;
		}

		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
				+ ChatColor.BLUE + "===");
	}

	public static void messagePlayerInfo(final CommandSender sender, final PRPlayer prPlayer, int page) {
		Player player = Bukkit.getPlayer(prPlayer.getUUID());

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		long playerPlaytime = CacheManager.getPlayer(prPlayer.getUUID().toString()).getPlaytime();

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
		for (PRPlayerRank playerRank : CacheManager.getPlayer(prPlayer.getUUID().toString()).getRanks()) {
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

		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ Messages.powerRanks.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
				+ "===");
		if (player == null) {
			sender.sendMessage(ChatColor.GREEN + "Player not online, showing limited information.");
		}
		sender.sendMessage(ChatColor.GREEN + "UUID: " + ChatColor.DARK_GREEN + prPlayer.getUUID());
		if (player != null) {
			sender.sendMessage(ChatColor.GREEN + "Player name: " + ChatColor.DARK_GREEN + player.getDisplayName()
					+ (!player.getDisplayName().equals(player.getName())
							? (ChatColor.DARK_GREEN + " aka " + player.getName())
							: ""));
			sender.sendMessage(ChatColor.GREEN + "First joined (UTC): " + ChatColor.DARK_GREEN
					+ format.format(player.getFirstPlayed()));
			sender.sendMessage(
					ChatColor.GREEN + "Last joined (UTC): " + ChatColor.DARK_GREEN
							+ format.format(player.getLastPlayed()));
		} else {
			sender.sendMessage(ChatColor.GREEN + "Player name: " + ChatColor.DARK_GREEN + prPlayer.getName());
		}
		sender.sendMessage(ChatColor.GREEN + "Playtime: " + ChatColor.DARK_GREEN + playerPlaytimeFormatted);
		if (player != null) {
			sender.sendMessage(ChatColor.GREEN + "Chat format: " + ChatColor.RESET + getSampleChatFormat(player));
		}
		sender.sendMessage(ChatColor.GREEN + "Rank(s): " + ChatColor.DARK_GREEN + formatted_ranks);
		if (player != null) {
			sender.sendMessage(ChatColor.GREEN + "Effective Permissions: ");

			List<PRPermission> playerPermissions = powerRanks.getEffectivePlayerPermissions(player);
			int lines_per_page = sender instanceof Player ? 5 : 10;
			int last_page = playerPermissions.size() / lines_per_page;

			if (!(sender instanceof Player)) {
				page -= 1;
			}

			page = page < 0 ? 0 : page;
			page = page > last_page ? last_page : page;

			if (sender instanceof Player) {
				String page_selector_tellraw = "tellraw " + sender.getName()
						+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
						+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
						+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
						+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%previous_page%"
						+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%previous_page%"
						+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%previous_page%"
						+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%next_page%"
						+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%next_page%"
						+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
						+ "%commandlabel%" + " playerinfo %playername% " + "%next_page%" + "\"}}]";

				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%",
						String.valueOf(page - 1));
				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%",
						String.valueOf(last_page + 1));
				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%playername%", player.getName());
				page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%commandlabel%", "pr");

				Messages.powerRanks.getServer().dispatchCommand(
						(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);
			} else {
				sender.sendMessage(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/"
						+ ChatColor.BLUE + (last_page + 1));
				sender.sendMessage(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/pr" + " playerinfo "
						+ player.getName() + " " + ChatColor.BLUE
						+ (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
			}

			int line_index = 0;
			for (PRPermission permission : playerPermissions) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					sender.sendMessage(ChatColor.DARK_GREEN + "#" + (line_index + 1) + ". "
							+ (!permission.getValue() ? ChatColor.RED : ChatColor.GREEN) + permission.getName());
				}
				line_index += 1;
			}
		}

		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
				+ ChatColor.BLUE + "===");
	}

	private static String getSampleChatFormat(Player player) {
		String playersChatMessage = "message";

		String format = PowerRanks.getConfigManager().getString("chat.format", "");

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
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

		PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		Map<?, ?> availableUsertags = PowerRanks.getUsertagManager().getMap("usertags", new HashMap<String, String>());
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
		String player_formatted_name = (nameColor.length() == 0 ? "&r" : "")
				+ PowerRanks.applyMultiColorFlow(nameColor, player.getDisplayName());
		String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : "")
				+ PowerRanks.applyMultiColorFlow(chatColor, playersChatMessage);

		format = PRUtil.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
				.put("suffix", formatted_suffix)
				.put("usertag",
						!PowerRanks.plugin_hook_deluxetags ? usertag
								: PowerRanks.getInstance().getDeluxeTagsHook().getPlayerDisplayTag(player))
				.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
				.put("world", player.getWorld().getName()).build(), '[', ']');

		if (PowerRanks.placeholderapiExpansion != null) {
			format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR,
					"" + PowerColor.UNFORMATTED_COLOR_CHAR);
		}

		for (Entry<File, PowerRanksAddon> prAddon : PowerRanks.getInstance().addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(PowerRanks.getInstance(), player);
			format = prAddon.getValue().onPlayerChat(prPlayer, format, playersChatMessage);
		}

		format = PowerRanks.chatColor(format, true);

		return format;
	}

	private static String getSampleChatFormat(CommandSender sender, String name, String world, PRRank rank) {
		String playersChatMessage = "message";

		String format = PowerRanks.getConfigManager().getString("chat.format", "");

		String formatted_prefix = "";
		String formatted_suffix = "";
		String chatColor = rank.getChatcolor();
		String nameColor = rank.getNamecolor();
		String usertag = "";

		formatted_prefix += rank.getPrefix();
		formatted_suffix += rank.getSuffix();

		String player_formatted_name = (nameColor.length() == 0 ? "&r" : "")
				+ PowerRanks.applyMultiColorFlow(nameColor, name);
		String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : "")
				+ PowerRanks.applyMultiColorFlow(chatColor, playersChatMessage);

		format = PRUtil.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
				.put("suffix", formatted_suffix)
				.put("usertag",
						!PowerRanks.plugin_hook_deluxetags ? usertag
								: (sender != null
										? PowerRanks.getInstance().getDeluxeTagsHook()
												.getPlayerDisplayTag((Player) sender)
										: ""))
				.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
				.put("world", world).build(), '[', ']');

		if (PowerRanks.placeholderapiExpansion != null && sender != null) {
			format = PlaceholderAPI.setPlaceholders((Player) sender, format).replaceAll("" + ChatColor.COLOR_CHAR,
					"" + PowerColor.UNFORMATTED_COLOR_CHAR);
		}

		if (sender != null) {
			for (Entry<File, PowerRanksAddon> prAddon : PowerRanks.getInstance().addonsManager.addonClasses
					.entrySet()) {
				PowerRanksPlayer prPlayer = new PowerRanksPlayer(PowerRanks.getInstance(), (Player) sender);
				format = prAddon.getValue().onPlayerChat(prPlayer, format, playersChatMessage);
			}
		}

		format = PowerRanks.chatColor(format, true);

		return format;
	}

	public static void addonManagerListAddons(CommandSender sender, int page) {
		boolean hasAcceptedTerms = PowerRanks.getConfigManager().getBool("addon_manager.accepted_terms", false);

		if (sender instanceof Player) {

			int lines_per_page = 5;
			int last_page = PowerRanks.getInstance().addonsManager.getAddonDownloader() == null ? 0
					: PowerRanks.getInstance().addonsManager.getAddonDownloader().getDownloadableAddons().size()
							/ lines_per_page;

			page = page < 0 ? 0 : page;
			page = page > last_page ? last_page : page;

			int pageStartIndex = lines_per_page * page;
			int pageEndIndex = lines_per_page * page + lines_per_page;

			int previousPage = page - 1;
			int currentPage = page;
			int nextPage = page + 1;

			String tellrawCommand = "tellraw %player% ";

			if (hasAcceptedTerms) {
				if (PowerRanks.getInstance().addonsManager.getAddonDownloader() == null) {
					PowerRanks.getInstance().addonsManager.setupAddonDownloader();
				}

				tellrawCommand += "[\"\"";
				tellrawCommand += ",{\"text\":\"===-----\",\"color\":\"dark_aqua\"},{\"text\":\"PowerRanks AddonManager\",\"color\":\"aqua\"},{\"text\":\"-----===\",\"color\":\"dark_aqua\"}";
				tellrawCommand += ",{\"text\":\"\\n\"},";
				tellrawCommand += "{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"%currentpage%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %previouspage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Previous page\"}},{\"text\":\"<\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %previouspage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Previous page\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %previouspage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Previous page\"}},{\"text\":\" \"},{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %nextpage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Next page\"}},{\"text\":\">\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %nextpage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Next page\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager page %nextpage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Next page\"}}"
						.replaceAll("%previouspage%", String.valueOf(previousPage))
						.replaceAll("%currentpage%", String.valueOf(currentPage + 1))
						.replaceAll("%nextpage%", String.valueOf(nextPage));
				tellrawCommand += ",{\"text\":\"\\n\"},";

				int addonIndex = 0;

				for (DownloadableAddon addon : PowerRanks.getInstance().addonsManager.getAddonDownloader()
						.getDownloadableAddons()) {
					if (addonIndex >= pageStartIndex && addonIndex < pageEndIndex) {
						String availabilitycolor = (addon.isDownloadable() ? (addon.isCompatible() ? "green" : "red")
								: "yellow");
						tellrawCommand += "{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager info %addonname%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for more info\"}},{\"text\":\"info\",\"color\":\"%availabilitycolor%\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager info %addonname%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for more info\"}},{\"text\":\"] \",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager info %addonname%\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for more info\"}}"
								.replaceAll("%addonname%", addon.getName())
								.replaceAll("%availabilitycolor%", availabilitycolor);
						tellrawCommand += ",{\"text\":\"%addonname%\",\"color\":\"white\"}".replaceAll("%addonname%",
								addon.getName());
						tellrawCommand += ",{\"text\":\"\\n\"},";
					}
					addonIndex++;
				}
				tellrawCommand += "{\"text\":\"===--------------------------------===\",\"color\":\"dark_aqua\"}";
				tellrawCommand += "]";
			} else {
				tellrawCommand += "[\"\",{\"text\":\"===-----\",\"color\":\"dark_aqua\"},";
				tellrawCommand += "{\"text\":\"PowerRanks AddonManager\",\"color\":\"aqua\"},";
				tellrawCommand += "{\"text\":\"-----===\",\"color\":\"dark_aqua\"},";
				tellrawCommand += "{\"text\":\"\\n                     \"},";
				tellrawCommand += "{\"text\":\"!!! WAIT !!!\",\"color\":\"red\"},";
				tellrawCommand += "{\"text\":\"\\n\"},";
				tellrawCommand += "{\"text\":\"In order to use the add-on manager, you must accept the terms.\",\"color\":\"white\"},";
				tellrawCommand += "{\"text\":\"\\nAdd-ons for PowerRanks are external pieces of code intended to\\nadd or change existing behavior in PowerRanks.\\nAdd-ons downloaded using the addon manager are either official or tested and are safe to use.\\nBut, do note, add-ons can be dangerous and/or malicious.\\nThe author of PowerRanks is in no way liable for damage caused by add-ons.\\nAfter accepting these terms, the add-on manager will make contact with\\nthe PowerRanks website to check available add-ons for download.\\nIf you do not want PowerRanks to access a external site, decline these terms.\\n                \"},";

				tellrawCommand += "{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager acceptterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"accept\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager acceptterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager acceptterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"  \",\"color\":\"black\"},";
				tellrawCommand += "{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager declineterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"decline\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager declineterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr addonmanager declineterms\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},";
				tellrawCommand += "{\"text\":\"\\n\"},";
				tellrawCommand += "{\"text\":\"===---------------------------------===\",\"color\":\"dark_aqua\"}]";
			}

			tellrawCommand = tellrawCommand.replaceAll("%player%", sender.getName());

			if (Messages.powerRanks != null)
				Messages.powerRanks.getServer().dispatchCommand(
						(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), tellrawCommand);
		} else {
			if (hasAcceptedTerms) {
				if (PowerRanks.getInstance().addonsManager.getAddonDownloader() == null) {
					PowerRanks.getInstance().addonsManager.setupAddonDownloader();
				}

				ArrayList<String> lines = new ArrayList<String>();

				lines.add(ChatColor.DARK_AQUA + "===-----" + ChatColor.AQUA + "PowerRanks AddonManager"
						+ ChatColor.DARK_AQUA + "-----===");
				lines.add("Run command between () for info.");
				for (DownloadableAddon addon : PowerRanks.getInstance().addonsManager.getAddonDownloader()
						.getDownloadableAddons()) {
					ChatColor availabilitycolor = (addon.isDownloadable()
							? (addon.isCompatible() ? ChatColor.GREEN : ChatColor.RED)
							: ChatColor.YELLOW);
					lines.add(ChatColor.BLACK + "[" + availabilitycolor + "■" + ChatColor.BLACK + "] " + ChatColor.RESET
							+ addon.getName() + " (" + availabilitycolor + "pr addonmanager info " + addon.getName()
							+ ChatColor.RESET + ")");
					// pr addonmanager info ggg
				}
				lines.add(ChatColor.DARK_AQUA + "===---------------------------------===");

				for (String line : lines) {
					sender.sendMessage(line);
				}
			} else {
				ArrayList<String> lines = new ArrayList<String>();

				lines.add(ChatColor.DARK_AQUA + "===-----" + ChatColor.AQUA + "PowerRanks AddonManager"
						+ ChatColor.DARK_AQUA + "-----===");
				lines.add(ChatColor.RED + "             !!! WAIT !!!");
				lines.add("In order to use the add-on manager, you must accept the terms.");
				lines.add("");
				lines.add("Add-ons for PowerRanks are external pieces of code intended to");
				lines.add("add or change existing behavior in PowerRanks.");
				lines.add(
						"Add-ons downloaded using the addon manager are either official or tested and are safe to use.");
				lines.add("But, do note, add-ons can be dangerous and/or malicious.");
				lines.add("The author of PowerRanks is in no way liable for damage caused by add-ons.");
				lines.add("After accepting these terms, the add-on manager will make contact with");
				lines.add("the PowerRanks website to check available add-ons for download.");
				lines.add("If you do not want PowerRanks to access a external site, decline these terms.");
				lines.add("");
				lines.add(ChatColor.GREEN + "To accept the terms, run the command: " + ChatColor.RESET
						+ "pr addonmanager acceptterms");
				lines.add(ChatColor.RED + "To decline the terms, run the command: " + ChatColor.RESET
						+ "pr addonmanager declineterms");
				lines.add(ChatColor.DARK_AQUA + "===---------------------------------===");

				for (String line : lines) {
					sender.sendMessage(line);
				}
			}
		}
	}

	public static void addonManagerInfoAddon(CommandSender sender, String addonname) {
		boolean hasAcceptedTerms = PowerRanks.getConfigManager().getBool("addon_manager.accepted_terms", false);
		if (!hasAcceptedTerms) {
			addonManagerListAddons(sender, 0);
			return;
		}

		DownloadableAddon addon = null;
		for (DownloadableAddon dlAddon : PowerRanks.getInstance().addonsManager.getAddonDownloader()
				.getDownloadableAddons()) {
			if (dlAddon.getName().equalsIgnoreCase(addonname)) {
				addon = dlAddon;
				break;
			}
		}

		if (sender instanceof Player) {
			String tellrawCommand = "tellraw %player% ";

			String formattedDescription = "";
			if (addon == null) {
				// TODO
				// sender.sendMessage(PowerRanks.getInstance().plp + ChatColor.DARK_RED + "Add-on not found.");
				return;
			}
			if (!addon.isInstalled()) {
				formattedDescription += ",{\"text\":\"Description\",\"color\":\"green\"}";
				formattedDescription += ",{\"text\":\":\\\\n\"}";
				for (String line : addon.getDescription()) {
					formattedDescription += ",{\"text\":\"" + line + "\"},{\"text\":\"\\\\n\"}";
				}
			} else {
				PowerRanksAddon prAddon = null;
				for (Entry<File, PowerRanksAddon> a : PowerRanks.getInstance().addonsManager.addonClasses.entrySet()) {
					if (a.getValue().getIdentifier().equalsIgnoreCase(addon.getName())) {
						prAddon = a.getValue();
						break;
					}
				}

				if (prAddon == null) {
					// TODO
					// sender.sendMessage(PowerRanks.getInstance().plp + ChatColor.DARK_RED + "Add-on not found.");
					return;
				}

				formattedDescription += ",{\"text\":\"Registered Commands\",\"color\":\"green\"},{\"text\":\":\\\\n\"}";
				for (String command : prAddon.getRegisteredCommands()) {
					formattedDescription += ",{\"text\":\"" + "- /pr " + command + "\"},{\"text\":\"\\\\n\"}";
				}
				formattedDescription += ",{\"text\":\"Registered Permissions\",\"color\":\"green\"},{\"text\":\":\\\\n\"}";
				for (String permission : prAddon.getRegisteredPermissions()) {
					formattedDescription += ",{\"text\":\"" + "- " + permission + "\"},{\"text\":\"\\\\n\"}";
				}
			}

			formattedDescription = formattedDescription.replaceAll("\\[[a-zA-Z]{1,16}\\]", "");

			String downloadClickAction = addon.isInstalled()
					? "{\"action\":\"run_command\",\"value\":\"/pr addonmanager uninstall %addonname%\"}"
					: (!addon.getURL().toLowerCase().endsWith(".jar")
							? "{\"action\":\"open_url\",\"value\":\"" + addon.getURL() + "\"}"
							: "{\"action\":\"run_command\",\"value\":\"/pr addonmanager download %addonname%\"}");
			String downloadButton = ",{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":%downloadclickaction%,\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},{\"text\":\"%addondownloadbuttontext%\",\"color\":\"%addondownloadbuttoncolor%\",\"clickEvent\":%downloadclickaction%,\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":%downloadclickaction%,\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click\"}}";
			String downloadButtonText = addon.isInstalled() ? "Uninstall"
					: (addon.isDownloadable() ? (addon.isCompatible() ? "download" : "not available") : "more info");
			String downloadButtonColor = addon.isInstalled() ? "red"
					: (addon.isDownloadable() ? (addon.isCompatible() ? "green" : "red") : "yellow");

			downloadButton = downloadButton.replaceAll("%downloadclickaction%", downloadClickAction)
					.replaceAll("%addondownloadbuttontext%", downloadButtonText)
					.replaceAll("%addondownloadbuttoncolor%", downloadButtonColor);

			tellrawCommand += "[\"\"";
			tellrawCommand += ",{\"text\":\"===-----\",\"color\":\"dark_aqua\"},{\"text\":\"PowerRanks AddonManager\",\"color\":\"aqua\"},{\"text\":\"-----===\",\"color\":\"dark_aqua\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"Installed\",\"color\":\"green\"},{\"text\":\": %addonisinstalled%\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"Add-on\",\"color\":\"green\"},{\"text\":\": %addonname%\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"Author\",\"color\":\"green\"},{\"text\":\": %addonauthor%\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"Version\",\"color\":\"green\"},{\"text\":\": %addonversion%\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"Min. PowerRanks version\",\"color\":\"green\"},{\"text\":\": %addonprversion%\"}";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += "%addonddescription%";
			tellrawCommand += "%addondownloadbutton%";
			tellrawCommand += ",{\"text\":\"\\n\"}";
			tellrawCommand += ",{\"text\":\"===---------------------------------===\",\"color\":\"dark_aqua\"}";
			tellrawCommand += "]";

			tellrawCommand = tellrawCommand.replaceAll("%addondownloadbutton%", downloadButton);

			tellrawCommand = tellrawCommand.replaceAll("%player%", sender.getName())
					.replaceAll("%addonisinstalled%", addon.isInstalled() ? "yes" : "no")
					.replaceAll("%addonname%", addon.getName()).replaceAll("%addonauthor%", addon.getAuthor())
					.replaceAll("%addonversion%", addon.getVersion())
					.replaceAll("%addonprversion%", addon.getMinPowerRanksVersion())
					.replaceAll("%addonddescription%", formattedDescription);

			if (Messages.powerRanks != null)
				Messages.powerRanks.getServer().dispatchCommand(
						(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), tellrawCommand);

		} else {
			if (addon == null) {
				// TODO
				// sender.sendMessage(PowerRanks.getInstance().plp + ChatColor.DARK_RED + "Add-on not found.");
				return;
			}
			
			ArrayList<String> lines = new ArrayList<String>();

			lines.add(ChatColor.DARK_AQUA + "===-----" + ChatColor.AQUA + "PowerRanks AddonManager"
					+ ChatColor.DARK_AQUA + "-----===");
			lines.add(ChatColor.GREEN + "Installed" + ChatColor.RESET + ": " + (addon.isInstalled() ? "yes" : "no"));
			lines.add(ChatColor.GREEN + "Add-on" + ChatColor.RESET + ": " + addon.getName());
			lines.add(ChatColor.GREEN + "Author" + ChatColor.RESET + ": " + addon.getAuthor());
			lines.add(ChatColor.GREEN + "Version" + ChatColor.RESET + ": " + addon.getVersion());
			lines.add(ChatColor.GREEN + "Min. PowerRanks version" + ChatColor.RESET + ": "
					+ addon.getMinPowerRanksVersion());
			if (!addon.isInstalled()) {
				lines.add(ChatColor.GREEN + "Description" + ChatColor.RESET + ": ");
				for (String line : addon.getDescription()) {
					lines.add(line.replaceAll("\\[[a-zA-Z]{1,16}\\]", ""));
				}
			} else {
				PowerRanksAddon prAddon = null;
				for (Entry<File, PowerRanksAddon> a : PowerRanks.getInstance().addonsManager.addonClasses.entrySet()) {
					if (a.getValue().getIdentifier().equalsIgnoreCase(addon.getName())) {
						prAddon = a.getValue();
						break;
					}
				}

				lines.add(ChatColor.GREEN + "Registered commands" + ChatColor.RESET + ": ");
				
				if (prAddon != null) {
					for (String command : prAddon.getRegisteredCommands()) {
						lines.add("- /pr " + command);
					}

					lines.add(ChatColor.GREEN + "Registered permissions" + ChatColor.RESET + ": ");
					for (String permission : prAddon.getRegisteredPermissions()) {
						lines.add("- " + permission);
					}
				}
			}

			String downloadText = "";
			if (!addon.isInstalled()) {
				if (addon.isCompatible()) {
					if (addon.isDownloadable()) {
						downloadText = ChatColor.GREEN + "Run the following comand to download this add-on"
								+ ChatColor.RESET + ": pr addonmanager download " + addon.getName();
					} else {
						downloadText = ChatColor.YELLOW + "Click the following URL for more information"
								+ ChatColor.RESET + ": " + addon.getURL();
					}
				} else {
					downloadText = ChatColor.RED + "This add-on is not compatible with your version of PowerRanks";
				}
			} else {
				downloadText = ChatColor.RED + "Run the following comand to uninstall this add-on" + ChatColor.RESET
						+ ": pr addonmanager uninstall " + addon.getName();
			}
			lines.add(downloadText);
			lines.add(ChatColor.DARK_AQUA + "===---------------------------------===");

			for (String line : lines) {
				sender.sendMessage(line);
			}
		}
	}

	public static void listRankPermissions(CommandSender sender, Users users, String rank_name, int page) {
		List<PRPermission> lines = users.getPermissions(rank_name);
		int lines_per_page = 10;

		if (page < 0)
			page = 0;

		if (page > lines.size() / lines_per_page)
			page = lines.size() / lines_per_page;

		String page_selector_tellraw = "tellraw " + sender.getName()
				+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + (page + 1)
				+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page + 1)
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page + 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions "
				+ rank_name + " " + (page + 1) + "\"}}]";

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + "Permissions of " + ChatColor.BLUE
				+ rank_name + ChatColor.DARK_AQUA + "--------");
		if (Messages.powerRanks != null)
			Messages.powerRanks.getServer().dispatchCommand(
					(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);

		for (int i = 0; i < lines_per_page; i++) {
			if (lines_per_page * page + i < lines.size()) {
				PRPermission permission = lines.get(lines_per_page * page + i);
				if (permission.getName().length() > 0)
					sender.sendMessage(
							(permission.getValue() ? ChatColor.GREEN : ChatColor.RED) + permission.getName());
			}
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void listPlayerPermissions(CommandSender sender, Users users, String target_player, int page) {
		Set<PRPermission> lines = users.getPlayerPermissions(target_player);
		int lines_per_page = 10;

		if (page < 0)
			page = 0;

		if (page > lines.size() / lines_per_page)
			page = lines.size() / lines_per_page;

		String page_selector_tellraw = "tellraw " + sender.getName()
				+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + (page + 1)
				+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page - 1)
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page - 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page - 1)
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page + 1)
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page + 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions "
				+ target_player + " " + (page + 1) + "\"}}]";

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + "Permissions of " + ChatColor.BLUE
				+ target_player + ChatColor.DARK_AQUA + "--------");
		if (Messages.powerRanks != null)
			Messages.powerRanks.getServer().dispatchCommand(
					(CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);

		Iterator<PRPermission> permissionIterator = lines.iterator();
		for (int i = 0; i < lines_per_page; i++) {
			if (lines_per_page * page + i < lines.size()) {
				if (!permissionIterator.hasNext()) {
					break;
				}
				PRPermission permission = permissionIterator.next();
				if (permission.getName().length() > 0)
					sender.sendMessage(
							(permission.getValue() ? ChatColor.GREEN : ChatColor.RED) + permission.getName());
			}
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void checkVerbose(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
				+ ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(
				ChatColor.DARK_GREEN + "Verbose: " + ChatColor.GREEN + (!PowerRanksVerbose.USE_VERBOSE ? "Disabled"
						: "Enabled" + (PowerRanksVerbose.USE_VERBOSE_LIVE ? " (Live)" : "")));
		sender.sendMessage(ChatColor.DARK_GREEN + "Log size: " + ChatColor.GREEN + PowerRanksVerbose.logSize()
				+ ChatColor.DARK_GREEN + " lines");
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}
}