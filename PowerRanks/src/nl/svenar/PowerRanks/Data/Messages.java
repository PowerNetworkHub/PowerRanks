package nl.svenar.PowerRanks.Data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.PowerRanks.StorageType;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.VaultHook;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.addons.AddonsManager;

public class Messages {
	private static PowerRanks powerRanks = null;

	public Messages(PowerRanks powerRanks) {
		Messages.powerRanks = powerRanks;
	}

	public static String getGeneralMessage(YamlConfiguration langYaml, String lang_config_line) {
		String msg = "";

		String line = langYaml.getString(lang_config_line);
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				line = Util.replaceAll(line, "%base_cmd%", "/pr");
				msg = PowerRanks.chatColor(line, true);
			}
		}

		return msg;
	}

	public static void messageNoArgs(final CommandSender console) {
		console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
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

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Server version: " + ChatColor.DARK_GREEN + Bukkit.getVersion() + " | " + Bukkit.getServer().getBukkitVersion());
		sender.sendMessage(ChatColor.GREEN + "Java version: " + ChatColor.DARK_GREEN + System.getProperty("java.version"));
		sender.sendMessage(ChatColor.GREEN + "Uptime: " + ChatColor.DARK_GREEN + format.format(Duration.between(PowerRanks.powerranks_start_time, current_time).toMillis()));
		sender.sendMessage(ChatColor.GREEN + "PowerRanks Version: " + ChatColor.DARK_GREEN + PowerRanks.pdf.getVersion());
		sender.sendMessage(ChatColor.GREEN + "PowerRanks Storage Type: " + ChatColor.DARK_GREEN
				+ (PowerRanks.getStorageType() == StorageType.YAML ? "YAML" : (PowerRanks.getStorageType() == StorageType.MySQL ? "MySQL" : (PowerRanks.getStorageType() == StorageType.SQLite ? "SQLite" : "Unknown"))));
		sender.sendMessage(ChatColor.GREEN + "Registered ranks: " + ChatColor.DARK_GREEN + users.getGroups().size());
		sender.sendMessage(ChatColor.GREEN + "Registered players: " + ChatColor.DARK_GREEN + users.getCachedPlayers().size());
		sender.sendMessage(ChatColor.GREEN + "Registered addons: " + ChatColor.DARK_GREEN + addonCount);
		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
		sender.sendMessage(ChatColor.GREEN + "- Vault Economy: " + (PowerRanks.vaultEconomyEnabled ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions: " + (PowerRanks.vaultPermissionsEnabled ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI: " + (PowerRanks.getPlaceholderapiExpansion() != null ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags: " + (PowerRanks.plugin_hook_deluxetags ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- NametagEdit: " + (PowerRanks.plugin_hook_nametagedit ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messagePluginhookStats(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
		sender.sendMessage(ChatColor.GREEN + "- Vault Economy (vault_economy): " + (PowerRanks.vaultEconomyEnabled ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions (vault_permissions): " + (PowerRanks.vaultPermissionsEnabled ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI (placeholderapi): " + (PowerRanks.getPlaceholderapiExpansion() != null ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags (deluxetags): " + (PowerRanks.plugin_hook_deluxetags ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- NametagEdit (nametagedit): " + (PowerRanks.plugin_hook_nametagedit ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled"));
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandFactoryReset(CommandSender sender) {
		PowerRanks.factoryresetid = (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.DARK_RED + "WARNING!");
		sender.sendMessage(ChatColor.RED + "This action is irreversible if you continue");
		sender.sendMessage(ChatColor.RED + "Factory reset ID: " + ChatColor.GOLD + PowerRanks.factoryresetid);
		sender.sendMessage(ChatColor.RED + "To continue do: " + ChatColor.GOLD + "/pr factoryreset " + PowerRanks.factoryresetid);
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandFactoryResetDone(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Factory reset complete!");
		sender.sendMessage(ChatColor.GREEN + "It is recommended to restart your server.");
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageCommandBuyrank(CommandSender sender, Users users, String rankname) {
		if (rankname == null) {
			String tellraw_command = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"Buy\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\" %rank% \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"|\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\" Cost: \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}},{\"text\":\"%cost%\",\"color\":\"%cost_color%\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Buy this rank\"}}]";
			double player_balance = VaultHook.getVaultEconomy() != null ? VaultHook.getVaultEconomy().getBalance((Player) sender) : 0;

			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_GREEN + "Your balance: " + ChatColor.GREEN + player_balance);
			sender.sendMessage(ChatColor.DARK_GREEN + "Ranks available to buy (click to buy):");
			List<String> ranks = users.getBuyableRanks(users.getGroup((Player) sender));
			for (String rank : ranks) {
				int cost = users.getRanksConfigFieldInt(rank, "economy.cost");
				String cost_color = player_balance >= cost ? "green" : "red";
//			sender.sendMessage(ChatColor.BLACK + "[" + ChatColor.GREEN + "Buy" + ChatColor.BLACK + "] " + ChatColor.RESET + rank + " | Cost: " + String.valueOf(cost)); // TODO: Make [Buy] clickable
				if (Messages.powerRanks != null)
					Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(),
							tellraw_command.replaceAll("%rank%", rank).replaceAll("%cost%", String.valueOf(cost)).replaceAll("%cost_color%", cost_color).replaceAll("%player%", sender.getName()));
			}
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		} else {
			String tellraw_command = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}},{\"text\":\"Confirm\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}},{\"text\":\"]\",\"color\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr buyrank %rank% confirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to buy\"}}]";
			double player_balance = VaultHook.getVaultEconomy() != null ? VaultHook.getVaultEconomy().getBalance((Player) sender) : 0;

			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_GREEN + "Your balance: " + ChatColor.GREEN + player_balance);
			sender.sendMessage(ChatColor.DARK_GREEN + "Click 'confirm' to purchase " + rankname);
			int cost = users.getRanksConfigFieldInt(rankname, "economy.cost");
			sender.sendMessage("Cost: " + (player_balance >= cost ? ChatColor.GREEN : ChatColor.RED) + String.valueOf(cost));
			if (Messages.powerRanks != null)
				Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), tellraw_command.replaceAll("%rank%", rankname).replaceAll("%player%", sender.getName()));
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		}
	}
	
	public static void messagePlayerInfo(final CommandSender sender, final Player player) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "UUID: " + ChatColor.DARK_GREEN + player.getUniqueId());
		sender.sendMessage(ChatColor.GREEN + "Player name: " + ChatColor.DARK_GREEN + player.getDisplayName() + (!player.getDisplayName().equals(player.getName()) ? (ChatColor.DARK_GREEN  + " aka " + player.getName()) : ""));
		sender.sendMessage(ChatColor.GREEN + "First joined (UTC): " + ChatColor.DARK_GREEN + format.format(player.getFirstPlayed()));
		sender.sendMessage(ChatColor.GREEN + "Last joined (UTC): " + ChatColor.DARK_GREEN + format.format(player.getLastPlayed()));
		sender.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.DARK_GREEN + CachedPlayers.getString("players." + player.getUniqueId() + ".rank"));
		sender.sendMessage(ChatColor.GREEN + "Subrank(s): " + ChatColor.DARK_GREEN + (CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks") != null ? (CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks").getKeys(false).size() > 0 ? String.join(", ", CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks").getKeys(false)) : "None") : "None"));
		sender.sendMessage(ChatColor.GREEN + "Effective Permissions: ");
		for (String permission : powerRanks.getEffectivePlayerPermissions(player)) {
			sender.sendMessage((permission.startsWith("-") ? ChatColor.DARK_RED : ChatColor.DARK_GREEN) + "- " + permission);
		}
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
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

	public static void helpMenu(final Player sender, int page) {
		String tellrawbase = "tellraw %player% [\"\",{\"text\":\"[\",\"color\":\"black\"},{\"text\":\"/%cmd% %arg%\",\"color\":\"%color_command_allowed%\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/%cmd% %arg%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/%cmd% %arg%\"}},{\"text\":\"]\",\"color\":\"black\"},{\"text\":\" %help%\",\"color\":\"dark_green\"}]";
		String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%" + "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
				+ ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}" + ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%previous_page%"
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%next_page%"
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + "pr" + " help " + "%next_page%" + "\"}}]";

		ArrayList<String> help_messages = new ArrayList<String>();

		help_messages.add(
				"tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"%plugin%\",\"color\":\"aqua\"},{\"text\":\"----------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]"
						.replaceAll("%plugin%", PowerRanks.pdf.getName()).replaceAll("%player%", sender.getName()));

		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		ConfigurationSection lines = langYaml.getConfigurationSection("commands.help");

		int lines_per_page = 5;
		int last_page = lines.getKeys(false).size() / lines_per_page;

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
			for (String section : lines.getKeys(false)) {
				if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
					String help_command = langYaml.getString("commands.help." + section + ".command");
					String help_description = langYaml.getString("commands.help." + section + ".description");
					help_messages.add(tellrawbase.replaceAll("%arg%", help_command).replaceAll("%help%", help_description).replaceAll("%player%", sender.getName()).replaceAll("%cmd%", "pr").replaceAll("%color_command_allowed%",
							sender.hasPermission("powerranks.cmd." + section) ? "green" : "red"));
				}
				line_index += 1;
			}
		}

		help_messages.add("tellraw %player% [\"\",{\"text\":\"===\",\"color\":\"blue\"},{\"text\":\"------------------------------\",\"color\":\"dark_aqua\"},{\"text\":\"===\",\"color\":\"blue\"}]".replaceAll("%player%", sender.getName()));

		if (Messages.powerRanks != null)
			for (String msg : help_messages)
				Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), msg);
	}

	public static void helpMenu(final ConsoleCommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		ConfigurationSection lines = langYaml.getConfigurationSection("commands.help");
		if (lines != null) {
			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_AQUA + "[Optional] <Required>");
			String prefix = langYaml.getString("general.prefix");
			for (String section : lines.getKeys(false)) {
				String line = "&a/pr " + langYaml.getString("commands.help." + section + ".command") + "&2 - " + langYaml.getString("commands.help." + section + ".description");
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

	public static void listRankPermissions(CommandSender sender, Users users, String rank_name, int page) {
		List<String> lines = (List<String>) users.getPermissions(rank_name);
		int lines_per_page = 10;

		if (page < 0)
			page = 0;

		if (page > lines.size() / lines_per_page)
			page = lines.size() / lines_per_page;

		String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + (page + 1)
				+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page - 1)
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page + 1)
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page + 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listpermissions " + rank_name + " " + (page + 1) + "\"}}]";

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + "Permissions of " + ChatColor.BLUE + rank_name + ChatColor.DARK_AQUA + "--------");
		if (Messages.powerRanks != null)
			Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);

		for (int i = 0; i < lines_per_page; i++) {
			if (lines_per_page * page + i < lines.size()) {
				String permission = lines.get(lines_per_page * page + i);
				if (permission.length() > 0)
					sender.sendMessage((permission.charAt(0) == '-' ? ChatColor.RED : ChatColor.GREEN) + permission);
			}
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void listPlayerPermissions(CommandSender sender, Users users, String target_player, int page) {
		List<String> lines = (List<String>) users.getPlayerPermissions(target_player);
		int lines_per_page = 10;

		if (page < 0)
			page = 0;

		if (page > lines.size() / lines_per_page)
			page = lines.size() / lines_per_page;

		String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + (page + 1)
				+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page - 1)
				+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page - 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page - 1)
				+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page + 1)
				+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page + 1)
				+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr listplayerpermissions " + target_player + " " + (page + 1) + "\"}}]";

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + "Permissions of " + ChatColor.BLUE + target_player + ChatColor.DARK_AQUA + "--------");
		if (Messages.powerRanks != null)
			Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);

		for (int i = 0; i < lines_per_page; i++) {
			if (lines_per_page * page + i < lines.size()) {
				String permission = lines.get(lines_per_page * page + i);
				if (permission.length() > 0)
					sender.sendMessage((permission.charAt(0) == '-' ? ChatColor.RED : ChatColor.GREEN) + permission);
			}
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void checkVerbose(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.DARK_GREEN + "Verbose: " + ChatColor.GREEN + (!PowerRanksVerbose.USE_VERBOSE ? "Disabled" : "Enabled" + (PowerRanksVerbose.USE_VERBOSE_LIVE ? " (Live)" : "")));
		sender.sendMessage(ChatColor.DARK_GREEN + "Log size: " + ChatColor.GREEN + PowerRanksVerbose.logSize() + ChatColor.DARK_GREEN + " lines");
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void noPermission(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.no_permission");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageSetRankSuccessSender(CommandSender console, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.rank_set_sender");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
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
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.rank_set_target");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
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
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageGroupNotFound(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.group_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messagePlayerCheckRank(CommandSender console, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_check_rank");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageCommandUsageReload(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_reload");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadConfig(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadConfigDone(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadPlugin(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadPluginDone(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSet(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetown(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setownrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageCheck(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_check");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddperm(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDelperm(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAdded(CommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionRemoved(CommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddInheritance(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveInheritance(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetPrefix(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_prefix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetSuffix(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_suffix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetChatColor(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_chat_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetNameColor(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_name_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageCreateRank(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDeleteRank(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_delete_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageEnableBuild(CommandSender console) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_enable_build");
//		if (msg.length() > 0)
//			console.sendMessage(msg);
//	}
//
//	public static void messageCommandUsageDisableBuild(CommandSender console) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_disable_build");
//		if (msg.length() > 0)
//			console.sendMessage(msg);
//	}

	public static void messageCommandUsagePromote(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_promote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDemote(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_demote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceAdded(CommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceRemoved(CommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetPrefix(CommandSender console, String prefix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetSuffix(CommandSender console, String suffix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetChatColor(CommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetNameColor(CommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankCharacterWarning(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created_warning_characters");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}
	
	public static void messageCommandCreateRankColorCharacterWarning(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created_warning_characters_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteSuccess(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteError(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteSuccess(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteError(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandBuildEnabled(CommandSender console, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			console.sendMessage(msg);
//	}
//
//	public static void messageCommandBuildDisabled(CommandSender console, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			console.sendMessage(msg);
//	}

	public static void messageCommandRenameRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandRenameRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.default_rank_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_changing_default_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageSignUnknownCommand(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_sign_unknown_rank");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageSignCreated(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.sign_created");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageListPermissions(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listpermissions");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageErrorAddingPermission(CommandSender console, String rank, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_permission");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAddedToAllRanks(CommandSender console, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added_to_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionRemovedFromAllRanks(CommandSender console, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed_from_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageBuyRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buy_success");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageBuyRankError(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buy_not_enough_money");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageBuyRankNotAvailable(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buy_not_available");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void unknownCommand(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.unknown_command");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandAddbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandAddbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddbuyablerank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandDelbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandDelbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelbuyablerank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_del_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetcostSuccess(CommandSender sender, String rankname, String cost) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buy_cost_set");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetcostError(CommandSender sender, String rankname, String cost) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_setting_buy_cost");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetcost(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_cost");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandPlayerPermissionAdded(CommandSender sender, String permission, String target_player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_permission_added");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorAddingPlayerPermission(CommandSender sender, String target_player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddplayerperm(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandPlayerPermissionRemoved(CommandSender sender, String permission, String target_player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_permission_removed");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorRemovingPlayerPermission(CommandSender sender, String target_player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelplayerperm(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessAddsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorAddsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessDelsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorDelsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddsubrank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelsubrank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageListSubranks(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listsubranks");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessChangesubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);

	}

	public static void messageErrorChangesubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubrankprefix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubrankprefix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubranksuffix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubranksuffix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEnablesubrankpermissions(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDisablesubrankpermissions(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageCreateusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageEditusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_edit_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageClearusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageListusertags(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_list_usertags");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCreateusertagSuccess(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCreateusertagError(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandEditusertagSuccess(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandEditusertagError(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandRemoveusertagSuccess(CommandSender sender, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandRemoveusertagError(CommandSender sender, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetusertagSuccess(CommandSender sender, String playername, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetusertagError(CommandSender sender, String playername, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearusertagSuccess(CommandSender sender, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearusertagError(CommandSender sender, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetpromoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSetdemoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageClearpromoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageCleardemoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetpromoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetpromoterankError(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetdemoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandSetdemoterankError(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearpromoterankSuccess(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandClearpromoterankError(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCleardemoterankSuccess(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandCleardemoterankError(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageUsertagsDisabled(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String external_plugin_name = "None";
		if (PowerRanks.plugin_hook_deluxetags)
			external_plugin_name = "DeluxeTags";
		String msg = getGeneralMessage(langYaml, "messages.error_usertags_disable_use_external_plugin");
		msg = Util.replaceAll(msg, "%argument_external_plugin%", external_plugin_name); // external_plugin
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddoninfo(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_addoninfo");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandErrorAddonNotFound(CommandSender sender, String addon_name) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_addon_not_found");
		msg = Util.replaceAll(msg, "%argument_addon%", addon_name);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageFactoryReset(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_factoryreset");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageSeticon(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_seticon");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageErrorMustHoldItem(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_must_hold_item");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageSuccessSetIcon(CommandSender sender, String materialName, String rankName) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_icon");
		msg = Util.replaceAll(msg, "%argument_material%", materialName);
		msg = Util.replaceAll(msg, "%argument_rank%", rankName);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageVerbose(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_verbose");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseAlreadyRunning(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_verbose_already_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseNotRunning(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_verbose_not_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseMustStopBeforeSaving(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_verbose_must_stop_before_save");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}
	
	public static void messageCommandVerboseCleared(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_verbose_cleared");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseStopped(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_verbose_stopped");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseStarted(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_verbose_started");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandVerboseSaved(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_verbose_saved");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandErrorSavingVerbose(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_saving_verbose");
		if (msg.length() > 0)
			sender.sendMessage(msg);

	}

	public static void messageCommandUsageListPlayerPermissions(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listplayerpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageAddsubrankworld(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_addsubrankworld");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageDelsubrankworld(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_delsubrankworld");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageBuyrank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_buyrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsagePluginhook(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_pluginhook");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsageConfig(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_config");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void pluginhookStateChanged(CommandSender sender, String plugin_name, String new_state) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg1 = getGeneralMessage(langYaml, "messages.pluginhook_state_changed");
		String msg2 = getGeneralMessage(langYaml, "messages.suggest_restart");
		msg1 = Util.replaceAll(msg1, "%argument_plugin_name%", plugin_name);
		msg1 = Util.replaceAll(msg1, "%argument_new_state%", new_state);
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void pluginhookUnknownPlugin(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.pluginhook_unknown_plugin");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void pluginhookUnknownState(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.pluginhook_unknown_state");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void configWorldTagRemoved(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg1 = getGeneralMessage(langYaml, "messages.config_worldtag_removed");
		String msg2 = getGeneralMessage(langYaml, "messages.suggest_restart");
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void configStateChanged(CommandSender sender, String target, String new_state) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg1 = getGeneralMessage(langYaml, "messages.config_state_changed");
		String msg2 = getGeneralMessage(langYaml, "messages.suggest_restart");
		msg1 = Util.replaceAll(msg1, "%argument_config_target%", target);
		msg1 = Util.replaceAll(msg1, "%argument_new_state%", new_state);
		if (msg1.length() > 0)
			sender.sendMessage(msg1);
		if (msg2.length() > 0)
			sender.sendMessage(msg2);
	}

	public static void messageUsertagNotFound(CommandSender sender, String usertag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String msg = getGeneralMessage(langYaml, "messages.usertag_not_found");
		msg = Util.replaceAll(msg, "%argument_usertag%", usertag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

	public static void messageCommandUsagePlayerinfo(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_playerinfo");
		if (msg.length() > 0)
			sender.sendMessage(msg);		
	}
}