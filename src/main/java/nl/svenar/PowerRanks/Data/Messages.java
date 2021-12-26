package nl.svenar.PowerRanks.Data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.VaultHook;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.addons.AddonsManager;
import nl.svenar.PowerRanks.addons.DownloadableAddon;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import me.clip.placeholderapi.PlaceholderAPI;

public class Messages {
	private static PowerRanks powerRanks = null;

	public Messages(PowerRanks powerRanks) {
		Messages.powerRanks = powerRanks;
	}

	public static String getGeneralMessage(PowerConfigManager languageManager, String lang_config_line) {
		String msg = "";

		String line = languageManager.getString(lang_config_line, "");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = languageManager.getString("general.prefix", "");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				line = Util.replaceAll(line, "%base_cmd%", "/pr");
				msg = PowerRanks.chatColor(line, true);
			}
		}

		return msg;
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
		} catch (Exception e) {
			hex_color_supported = false;
		}
		sender.sendMessage(ChatColor.GREEN + "RGB colors: "
				+ (hex_color_supported ? ChatColor.DARK_GREEN + "" : ChatColor.DARK_RED + "un") + "supported");

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

	public static void messagePlayerInfo(final CommandSender sender, final Player player, int page) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		long playerPlaytime = CacheManager.getPlayer(player.getUniqueId().toString()).getPlaytime();

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
		for (String rankname : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			formatted_ranks += rankname + " ";
		}
		if (formatted_ranks.endsWith(" ")) {
			formatted_ranks = formatted_ranks.substring(0, formatted_ranks.length() - 1);
		}

		sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ Messages.powerRanks.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE
				+ "===");
		sender.sendMessage(ChatColor.GREEN + "UUID: " + ChatColor.DARK_GREEN + player.getUniqueId());
		sender.sendMessage(ChatColor.GREEN + "Player name: " + ChatColor.DARK_GREEN + player.getDisplayName()
				+ (!player.getDisplayName().equals(player.getName())
						? (ChatColor.DARK_GREEN + " aka " + player.getName())
						: ""));
		sender.sendMessage(ChatColor.GREEN + "First joined (UTC): " + ChatColor.DARK_GREEN
				+ format.format(player.getFirstPlayed()));
		sender.sendMessage(
				ChatColor.GREEN + "Last joined (UTC): " + ChatColor.DARK_GREEN + format.format(player.getLastPlayed()));
		sender.sendMessage(ChatColor.GREEN + "Playtime: " + ChatColor.DARK_GREEN + playerPlaytimeFormatted);
		sender.sendMessage(ChatColor.GREEN + "Chat format: " + ChatColor.RESET + getSampleChatFormat(player));
		sender.sendMessage(ChatColor.GREEN + "Rank(s): " + ChatColor.DARK_GREEN + formatted_ranks);
		sender.sendMessage(ChatColor.GREEN + "Effective Permissions: ");

		ArrayList<PRPermission> playerPermissions = powerRanks.getEffectivePlayerPermissions(player);
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
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
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
					+ player.getName() + " " + ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
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

	private static String getSampleChatFormat(Player player) {
		String playersChatMessage = "message";

		String format = PowerRanks.getConfigManager().getString("chat.format", "");

		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		String formatted_prefix = "";
		String formatted_suffix = "";
		String chatColor = ranks.get(ranks.size() - 1).getChatcolor();
		String nameColor = ranks.get(ranks.size() - 1).getNamecolor();
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

		// String rank =
		// CacheManager.getPlayer(player.getUniqueId().toString()).getRank();
		// String prefix = CacheManager.getRank(rank).getPrefix();
		// String suffix = CacheManager.getRank(rank).getSuffix();
		// String chatColor = CacheManager.getRank(rank).getChatcolor();
		// String nameColor = CacheManager.getRank(rank).getNamecolor();
		// String usertag = "";

		PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		Map<?, ?> availableUsertags = PowerRanks.getUsertagManager().getMap("usertags", new HashMap<String, String>());
		ArrayList<String> playerUsertags = targetPlayer.getUsertags();

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

		format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
				.put("suffix", formatted_suffix)
				.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player))
				.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
				.put("world", player.getWorld().getName()).build(), '[', ']');

		if (PowerRanks.placeholderapiExpansion != null) {
			format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR,
					"" + PowerRanksChatColor.unformatted_default_char);
		}

		for (Entry<File, PowerRanksAddon> prAddon : PowerRanks.getInstance().addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(PowerRanks.getInstance(), player);
			format = prAddon.getValue().onPlayerChat(prPlayer, format, playersChatMessage);
		}

		format = PowerRanks.chatColor(format, true);

		return format;
	}

	public static void helpMenu(final Player player) {
		helpMenu(player, 0);
	}

	public static void helpMenu(final CommandSender sender) {
		if (sender instanceof Player) {
			helpMenu((Player) sender, 0);
		} else if (sender instanceof ConsoleCommandSender) {
			helpMenu((ConsoleCommandSender) sender);
		}
	}

	public static void helpMenu(final CommandSender sender, int page) {
		if (sender instanceof Player) {
			helpMenu((Player) sender, page);
		} else if (sender instanceof ConsoleCommandSender) {
			helpMenu((ConsoleCommandSender) sender);
		}
	}

	@SuppressWarnings("unchecked")
	public static void helpMenu(final Player sender, int page) {
		String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"%color_command_allowed%\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
		String page_selector_tellraw = "tellraw " + sender.getName()
				+ " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
				+ "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
				+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
				+ ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr"
				+ " help " + "%previous_page%"
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
				+ "pr" + " help " + "%next_page%" + "\"}}]";

		ArrayList<String> help_messages = new ArrayList<String>();

		help_messages.add(
				"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
						.replaceAll("%plugin%", PowerRanks.pdf.getName()).replaceAll("%player%", sender.getName()));

		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		HashMap<String, String> lines = (HashMap<String, String>) languageManager.getMap("commands.help",
				new HashMap<String, String>());

		int lines_per_page = 5;
		int last_page = lines.size() / lines_per_page;

		page = page < 0 ? 0 : page;
		page = page > last_page ? last_page : page;

		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
		page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%", String.valueOf(last_page + 1));

		if (lines != null) {
			help_messages.add(page_selector_tellraw);
			help_messages.add(
					"tellraw %player% [\"\",{\"text\":\"Arguments: \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"blue\"},{\"text\":\"optional\",\"color\":\"aqua\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Arguments between [] are not required.\"}},{\"text\":\"]\",\"color\":\"blue\"},{\"text\":\" <\",\"color\":\"blue\"},{\"text\":\"required\",\"color\":\"aqua\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Arguments between <> are required.\"}},{\"text\":\">\",\"color\":\"blue\"}]"
							.replaceAll("%player%", sender.getName()));

			int line_index = 0;
			for (String section : lines.keySet()) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					String help_command = languageManager.getString("commands.help." + section + ".command", "");
					String help_description = languageManager.getString("commands.help." + section + ".description",
							"");
					help_messages.add(tellrawbase.replaceAll("%arg%", help_command)
							.replaceAll("%help%", help_description).replaceAll("%player%", sender.getName())
							.replaceAll("%cmd%", "pr").replaceAll("%color_command_allowed%",
									sender.hasPermission("powerranks.cmd." + section) ? "green" : "red"));
				}
				line_index += 1;
			}
		}

		help_messages.add(
				"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
						.replaceAll("%player%", sender.getName()));

		if (Messages.powerRanks != null)
			for (String msg : help_messages)
				Messages.powerRanks.getServer()
						.dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), msg);
	}

	@SuppressWarnings("unchecked")
	public static void helpMenu(final ConsoleCommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();

		HashMap<String, String> lines = (HashMap<String, String>) languageManager.getMap("commands.help",
				new HashMap<String, String>());

		if (lines != null) {
			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName()
					+ ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_AQUA + "[Optional] <Required>");
			String prefix = languageManager.getString("general.prefix", "");
			for (String section : lines.keySet()) {
				String line = "&a/pr " + languageManager.getString("commands.help." + section + ".command", "")
						+ "&2 - " + languageManager.getString("commands.help." + section + ".description", "");
				line = Util.replaceAll(line, "%base_cmd%", "/pr");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				String msg = PowerRanks.chatColor(line, true);
				if (msg.length() > 0)
					sender.sendMessage(msg);
			}
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		}
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
				for (String command : prAddon.getRegisteredCommands()) {
					lines.add("- /pr " + command);
				}

				lines.add(ChatColor.GREEN + "Registered permissions" + ChatColor.RESET + ": ");
				for (String permission : prAddon.getRegisteredPermissions()) {
					lines.add("- " + permission);
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

	public static void addonManagerTermsAccepted(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_terms_accepted");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void addonManagerTermsDeclined(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_terms_declined");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void addonManagerDownloadNotAvailable(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_download_not_available");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void addonManagerDownloadComplete(CommandSender sender, String addonname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_download_complete");
		msg = msg.replaceAll("%addonname%", addonname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void addonManagerDownloadFailed(CommandSender sender, String addonname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_download_failed");
		msg = msg.replaceAll("%addonname%", addonname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void addonManagerUninstallComplete(CommandSender sender, String addonname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.addonmanager_uninstall_complete");
		msg = msg.replaceAll("%addonname%", addonname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
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
		List<PRPermission> lines = users.getPlayerPermissions(target_player);
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

	public static void noPermission(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.no_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSetRankSuccessSender(CommandSender console, String target, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String line = languageManager.getString("messages.rank_set_sender", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageAddRankSuccessSender(CommandSender console, String target, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String line = languageManager.getString("messages.rank_add_sender", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageSetRankSuccessTarget(Player target, String sender, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String line = languageManager.getString("messages.rank_set_target", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_sender%", sender);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				target.sendMessage(msg);
		}
	}

	public static void messageAddRankSuccessTarget(Player target, String sender, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String line = languageManager.getString("messages.rank_add_target", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_sender%", sender);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				target.sendMessage(msg);
		}
	}

	public static void messagePlayerNotFound(CommandSender console, String target) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();

		String line = languageManager.getString("messages.player_not_found", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageGroupNotFound(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();

		String line = languageManager.getString("messages.group_not_found", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messagePlayerCheckRank(CommandSender console, String target, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();

		String line = languageManager.getString("messages.player_check_rank", "");
		if (line != null) {
			String prefix = languageManager.getString("general.prefix", "");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageCommandUsageReload(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_reload");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandReloadWarning(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_warning");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandReloadConfig(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_config");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandReloadConfigDone(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_config_done");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandReloadPlugin(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_plugin");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadPluginDone(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_plugin_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadAddons(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_addons");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadAddonsDone(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.reload_addons_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSet(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_setrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAdd(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_addrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetown(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_setownrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddown(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_addownrank");
		if (msg.length() > 0)
		sender.sendMessage(msg);
	}

	public static void messageCommandUsageCheck(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_check");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddperm(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_add_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDelperm(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_remove_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAdded(CommandSender console, String permission, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionRemoved(CommandSender console, String permission, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddInheritance(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_add_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveInheritance(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_remove_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetPrefix(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_prefix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetSuffix(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_suffix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetChatColor(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_chat_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetNameColor(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_name_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageCreateRank(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_create_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDeleteRank(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_delete_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsagePromote(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_promote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDemote(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_demote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceAdded(CommandSender console, String inheritance, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceRemoved(CommandSender console, String inheritance, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetPrefix(CommandSender console, String prefix, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetSuffix(CommandSender console, String suffix, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetChatColor(CommandSender console, String color, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetNameColor(CommandSender console, String color, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankSuccess(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankError(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankSuccess(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankError(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteSuccess(CommandSender console, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteError(CommandSender console, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteSuccess(CommandSender console, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteError(CommandSender console, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandRenameRankSuccess(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandRenameRankError(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankSuccess(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.default_rank_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankError(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_changing_default_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageSignUnknownCommand(Player player) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_sign_unknown_rank");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageSignCreated(Player player) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.sign_created");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageListPermissions(CommandSender console) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_listpermissions");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageErrorAddingPermission(CommandSender console, String rank, String permission) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_adding_permission");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAddedToAllRanks(CommandSender console, String permission) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.permission_added_to_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionRemovedFromAllRanks(CommandSender console, String permission) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.permission_removed_from_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageBuyRankSuccess(CommandSender sender, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buy_success");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageBuyRankError(CommandSender sender, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buy_not_enough_money");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageBuyRankNotAvailable(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buy_not_available");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void unknownCommand(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.unknown_command");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandAddbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buyable_rank_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandAddbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_adding_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddbuyablerank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_add_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandDelbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buyable_rank_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandDelbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_removing_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelbuyablerank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_del_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetcostSuccess(CommandSender sender, String rankname, String cost) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.buy_cost_set");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetcostError(CommandSender sender, String rankname, String cost) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_setting_buy_cost");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetcost(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_cost");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandPlayerPermissionAdded(CommandSender sender, String permission,
			String target_player) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.player_permission_added");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorAddingPlayerPermission(CommandSender sender, String target_player,
			String permission) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_adding_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddplayerperm(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_add_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandPlayerPermissionRemoved(CommandSender sender, String permission,
			String target_player) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.player_permission_removed");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorRemovingPlayerPermission(CommandSender sender, String target_player,
			String permission) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_removing_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelplayerperm(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_remove_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessAddsubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorAddsubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessDelsubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorDelsubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddsubrank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_add_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelsubrank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_remove_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageListSubranks(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_listsubranks");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessChangesubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);

	}

	public static void messageErrorChangesubrank(CommandSender sender, String subrank, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubrankprefix(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_enablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubrankprefix(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_disablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubranksuffix(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_enablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubranksuffix(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_disablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubrankpermissions(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_enablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubrankpermissions(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_disablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageCreateusertag(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_create_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEditusertag(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_edit_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveusertag(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_remove_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetusertag(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageClearusertag(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_clear_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageListusertags(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_list_usertags");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCreateusertagSuccess(CommandSender sender, String tag, String text) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCreateusertagError(CommandSender sender, String tag, String text) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandEditusertagSuccess(CommandSender sender, String tag, String text) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandEditusertagError(CommandSender sender, String tag, String text) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandRemoveusertagSuccess(CommandSender sender, String tag) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandRemoveusertagError(CommandSender sender, String tag) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetusertagSuccess(CommandSender sender, String playername, String tag) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetusertagError(CommandSender sender, String playername, String tag) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearusertagSuccess(CommandSender sender, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearusertagError(CommandSender sender, String playername) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetpromoterank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetdemoterank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_set_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageClearpromoterank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_clear_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageCleardemoterank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_clear_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetpromoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetpromoterankError(CommandSender sender, String rankname, String promote_rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetdemoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetdemoterankError(CommandSender sender, String rankname, String promote_rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearpromoterankSuccess(CommandSender sender, String rankname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearpromoterankError(CommandSender sender, String rankname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCleardemoterankSuccess(CommandSender sender, String rankname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCleardemoterankError(CommandSender sender, String rankname) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageUsertagsDisabled(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String external_plugin_name = "None";
		if (PowerRanks.plugin_hook_deluxetags)
			external_plugin_name = "DeluxeTags";
		String msg = getGeneralMessage(languageManager, "messages.error_usertags_disable_use_external_plugin");
		msg = Util.replaceAll(msg, "%argument_external_plugin%", external_plugin_name); // external_plugin
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddoninfo(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_addoninfo");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandErrorAddonNotFound(CommandSender sender, String addon_name) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_addon_not_found");
		msg = Util.replaceAll(msg, "%argument_addon%", addon_name);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageFactoryReset(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_factoryreset");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSeticon(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_seticon");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorMustHoldItem(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_must_hold_item");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessSetIcon(CommandSender sender, String materialName, String rankName) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_icon");
		msg = Util.replaceAll(msg, "%argument_material%", materialName);
		msg = Util.replaceAll(msg, "%argument_rank%", rankName);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageVerbose(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_verbose");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseAlreadyRunning(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_verbose_already_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseNotRunning(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_verbose_not_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseMustStopBeforeSaving(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_verbose_must_stop_before_save");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseStopped(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_verbose_stopped");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseStarted(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_verbose_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseSaved(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_verbose_saved");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandErrorSavingVerbose(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_saving_verbose");
		if (msg.length() > 0)
			sender.sendMessage(msg);

	}

	public static void messageCommandUsageListPlayerPermissions(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_listplayerpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddsubrankworld(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_addsubrankworld");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelsubrankworld(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_delsubrankworld");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageBuyrank(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_buyrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsagePluginhook(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_pluginhook");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageConfig(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_config");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void pluginhookStateChanged(CommandSender sender, String plugin_name, String new_state) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg1 = getGeneralMessage(languageManager, "messages.pluginhook_state_changed");
		String msg2 = getGeneralMessage(languageManager, "messages.suggest_restart");
		msg1 = Util.replaceAll(msg1, "%argument_plugin_name%", plugin_name);
		msg1 = Util.replaceAll(msg1, "%argument_new_state%", new_state);
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void pluginhookUnknownPlugin(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.pluginhook_unknown_plugin");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void pluginhookUnknownState(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.pluginhook_unknown_state");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void configWorldTagRemoved(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg1 = getGeneralMessage(languageManager, "messages.config_worldtag_removed");
		String msg2 = getGeneralMessage(languageManager, "messages.suggest_restart");
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void configStateChanged(CommandSender sender, String target, String new_state) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg1 = getGeneralMessage(languageManager, "messages.config_state_changed");
		String msg2 = getGeneralMessage(languageManager, "messages.suggest_restart");
		msg1 = Util.replaceAll(msg1, "%argument_config_target%", target);
		msg1 = Util.replaceAll(msg1, "%argument_new_state%", new_state);
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void messageUsertagNotFound(CommandSender sender, String usertag) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();

		String msg = getGeneralMessage(languageManager, "messages.usertag_not_found");
		msg = Util.replaceAll(msg, "%argument_usertag%", usertag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCreateRankCharacterWarning(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_created_warning_characters");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankColorCharacterWarning(CommandSender console, String rank) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_created_warning_characters_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandVerboseCleared(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_verbose_cleared");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsagePlayerinfo(CommandSender sender) {
		PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_playerinfo");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetbuydescriptionSuccess(final CommandSender sender, final String rankname,
			final String description) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_buydescription");
		msg = Util.replaceAll(msg, "%argument_description%", description);
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandSetbuydescriptionError(final CommandSender sender, final String rankname,
			final String description) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_set_buydescription");
		msg = Util.replaceAll(msg, "%argument_description%", description);
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandUsageSetbuydescription(final CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "commands.usage_command_setbuydescription");
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandSetbuycommandSuccess(final CommandSender sender, final String rankname,
			final String command) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.success_set_buycommand");
		msg = Util.replaceAll(msg, "%argument_command%", command);
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandSetbuycommandError(final CommandSender sender, final String rankname,
			final String command) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.error_set_buycommand");
		msg = Util.replaceAll(msg, "%argument_command%", command);
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandUsageSetbuycommand(final CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "commands.usage_command_setbuycommand");
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandUsageListranks(final CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "commands.usage_command_listaddons");
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageConsoleNotAPlayer(final CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.console_is_not_a_player");
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandCheckrankResponse(final CommandSender sender, final String target_player,
			final String rankname) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.response_command_checkrank");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void numbersOnly(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "commands.only_numbers");
		if (msg.length() > 0) {
			sender.sendMessage(msg);
		}
	}

	public static void messageCommandUsageWebeditor(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "commands.usage_command_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void preparingWebeditor(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.preparing_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void webeditorTimedout(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.timedout_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void downloadingWebeditorData(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.downloading_data_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void downloadedWebeditorData(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.downloaded_data_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void FailedDownloadingWebeditorData(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.failed_downloading_data_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void LoadedRanksPlayersWebeditor(CommandSender sender, int ranks, int players) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.loaded_ranks_players_webeditor");
		msg = Util.replaceAll(msg, "%ranks%", String.valueOf(ranks));
		msg = Util.replaceAll(msg, "%players%", String.valueOf(players));
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void downloadedInvalidWebeditorData(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		final String msg = getGeneralMessage(languageManager, "messages.downloaded_invalid_webeditor");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void incompattiblePowerRanksVersionWebeditor(CommandSender sender, String editorPRVersion,
			String serverPRVersion) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.incompattible_powerranks_version_webeditor");
		msg = Util.replaceAll(msg, "%prversioneditor%", editorPRVersion);
		msg = Util.replaceAll(msg, "%prversionserver%", serverPRVersion);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorNotInt(CommandSender sender, String notInt) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.not_an_integer");
		msg = Util.replaceAll(msg, "%argument_notint%", notInt);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandWeightSet(CommandSender sender, int weight, String rankname) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "messages.rank_weight_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_weight%", String.valueOf(weight));
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetWeight(CommandSender sender) {
		final PowerConfigManager languageManager = PowerRanks.getLanguageManager();
		String msg = getGeneralMessage(languageManager, "commands.usage_command_setweight");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}
}