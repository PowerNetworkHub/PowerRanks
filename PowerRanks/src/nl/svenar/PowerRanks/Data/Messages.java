package nl.svenar.PowerRanks.Data;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
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
				msg = PowerRanks.chatColor(line, true);
			}
		}

		return msg;
	}

//	public static void messageNoArgs(final Player player) {
//		player.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
//		player.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
//		player.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
//		player.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors());
//		player.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
//		player.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
//		player.sendMessage(ChatColor.DARK_GREEN + "Support me on: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
//		player.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
//	}

	public static void messageNoArgs(final CommandSender console) {
		console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		console.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
		console.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		console.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors());
		console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
		console.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
		console.sendMessage(ChatColor.DARK_GREEN + "Support me on: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
		console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

//	public static void messageStats(Player sender) {
//		Users users = new Users(null);
//
//		int addonCount = 0;
//		for (Entry<File, Boolean> prAddon : AddonsManager.loadedAddons.entrySet()) {
//			if (prAddon.getValue() == true)
//				addonCount++;
//		}
//
//		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
//		sender.sendMessage(ChatColor.GREEN + "Server version: " + ChatColor.DARK_GREEN + Bukkit.getVersion() + " | " + Bukkit.getServer().getBukkitVersion());
//		sender.sendMessage(ChatColor.GREEN + "Java version: " + ChatColor.DARK_GREEN + System.getProperty("java.version"));
//		sender.sendMessage(ChatColor.GREEN + "PowerRanks Version: " + ChatColor.DARK_GREEN + PowerRanks.pdf.getVersion());
//		sender.sendMessage(ChatColor.GREEN + "Registered ranks: " + ChatColor.DARK_GREEN + users.getGroups().size());
//		sender.sendMessage(ChatColor.GREEN + "Registered players: " + ChatColor.DARK_GREEN + users.getCachedPlayers().size());
//		sender.sendMessage(ChatColor.GREEN + "Registered addons: " + ChatColor.DARK_GREEN + addonCount);
//		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
//		sender.sendMessage(ChatColor.GREEN + "- Vault Economy: " + ChatColor.DARK_GREEN + (PowerRanks.getVaultEconomy() != null ? "enabled" : "disabled"));
//		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions: " + ChatColor.DARK_GREEN + (PowerRanks.getVaultPermissions() != null ? "enabled" : "disabled"));
//		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI: " + ChatColor.DARK_GREEN + (PowerRanks.getPlaceholderapiExpansion() != null ? "enabled" : "disabled"));
//		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags: " + ChatColor.DARK_GREEN + (PowerRanks.plugin_hook_deluxetags ? "enabled" : "disabled"));
//		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
//	}

	public static void messageStats(CommandSender sender) {
		Users users = new Users(null);

		int addonCount = 0;
		for (Entry<File, Boolean> prAddon : AddonsManager.loadedAddons.entrySet()) {
			if (prAddon.getValue() == true)
				addonCount++;
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		sender.sendMessage(ChatColor.GREEN + "Server version: " + ChatColor.DARK_GREEN + Bukkit.getVersion() + " | " + Bukkit.getServer().getBukkitVersion());
		sender.sendMessage(ChatColor.GREEN + "Java version: " + ChatColor.DARK_GREEN + System.getProperty("java.version"));
		sender.sendMessage(ChatColor.GREEN + "PowerRanks Version: " + ChatColor.DARK_GREEN + PowerRanks.pdf.getVersion());
		sender.sendMessage(ChatColor.GREEN + "Registered ranks: " + ChatColor.DARK_GREEN + users.getGroups().size());
		sender.sendMessage(ChatColor.GREEN + "Registered players: " + ChatColor.DARK_GREEN + users.getCachedPlayers().size());
		sender.sendMessage(ChatColor.GREEN + "Registered addons: " + ChatColor.DARK_GREEN + addonCount);
		sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
		sender.sendMessage(ChatColor.GREEN + "- Vault Economy: " + ChatColor.DARK_GREEN + (PowerRanks.getVaultEconomy() != null ? "enabled" : "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- Vault Permissions: " + ChatColor.DARK_GREEN + (PowerRanks.getVaultPermissions() != null ? "enabled" : "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI: " + ChatColor.DARK_GREEN + (PowerRanks.getPlaceholderapiExpansion() != null ? "enabled" : "disabled"));
		sender.sendMessage(ChatColor.GREEN + "- DeluxeTags: " + ChatColor.DARK_GREEN + (PowerRanks.plugin_hook_deluxetags ? "enabled" : "disabled"));
		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}
	
//	public static void messageCommandFactoryReset(Player sender) {
//		PowerRanks.factoryresetid = (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));
//		
//		sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
//		sender.sendMessage(ChatColor.DARK_RED + "WARNING!");
//		sender.sendMessage(ChatColor.RED + "This action is irreversible if you continue");
//		sender.sendMessage(ChatColor.RED + "Factory reset ID: " + ChatColor.GOLD + PowerRanks.factoryresetid);
//		sender.sendMessage(ChatColor.RED + "To continue do: " + ChatColor.GOLD + "/pr factoryreset " + PowerRanks.factoryresetid);
//		sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
//	}
	
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

	public static void helpMenu(final Player player) {
		helpMenu(player, 0);
	}

	public static void helpMenu(final Player sender, int page) {
		if (page < 0)
			page = 0;
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		int lines_per_page = 5;
		if (page > lines.size() / lines_per_page)
			page = lines.size() / lines_per_page;

		if (lines != null) {
			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_AQUA + "[Optional] <Required>");
//			sender.sendMessage(ChatColor.DARK_AQUA + "Page: " + page + "[<] [>]");

			String page_selector_tellraw = "tellraw " + sender.getName() + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + page
					+ "\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page - 1)
					+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page - 1)
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page - 1)
					+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page + 1)
					+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page + 1)
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/pr help " + (page + 1) + "\"}}]";

			if (Messages.powerRanks != null)
				Messages.powerRanks.getServer().dispatchCommand((CommandSender) Messages.powerRanks.getServer().getConsoleSender(), page_selector_tellraw);

			String prefix = langYaml.getString("general.prefix");
			for (int i = 0; i < lines_per_page; i++) {
				if (lines_per_page * page + i < lines.size()) {
					String line = lines.get(lines_per_page * page + i);
					line = Util.replaceAll(line, "%plugin_prefix%", prefix);
					line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
					String msg = PowerRanks.chatColor(line, true);
					if (msg.length() > 0)
						sender.sendMessage(msg);
				}
			}
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		}
	}

	public static void helpMenu(final ConsoleCommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		if (lines != null) {
			sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
			sender.sendMessage(ChatColor.DARK_AQUA + "[Optional] <Required>");
			String prefix = langYaml.getString("general.prefix");
			for (String line : lines) {
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				String msg = PowerRanks.chatColor(line, true);
				if (msg.length() > 0)
					sender.sendMessage(msg);
			}
			sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
		}
	}

	public static void noPermission(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.no_permission");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

//	public static void messageSetRankSuccessSender(Player player, String target, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//
//		String line = langYaml.getString("messages.rank_set_sender");
//		if (line != null) {
//			String prefix = langYaml.getString("general.prefix");
//			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
//			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
//			line = Util.replaceAll(line, "%argument_target%", target);
//			line = Util.replaceAll(line, "%argument_rank%", rank);
//			String msg = PowerRanks.chatColor(line, true);
//			if (msg.length() > 0)
//				player.sendMessage(msg);
//		}
//	}

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

//	public static void messagePlayerNotFound(Player player, String target) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//
//		String line = langYaml.getString("messages.player_not_found");
//		if (line != null) {
//			String prefix = langYaml.getString("general.prefix");
//			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
//			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
//			line = Util.replaceAll(line, "%argument_target%", target);
//			String msg = PowerRanks.chatColor(line, true);
//			if (msg.length() > 0)
//				player.sendMessage(msg);
//		}
//	}

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

//	public static void messageGroupNotFound(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//
//		String line = langYaml.getString("messages.group_not_found");
//		if (line != null) {
//			String prefix = langYaml.getString("general.prefix");
//			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
//			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
//			line = Util.replaceAll(line, "%argument_rank%", rank);
//			String msg = PowerRanks.chatColor(line, true);
//			if (msg.length() > 0)
//				player.sendMessage(msg);
//		}
//	}

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

//	public static void messagePlayerCheckRank(Player player, String target, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//
//		String line = langYaml.getString("messages.player_check_rank");
//		if (line != null) {
//			String prefix = langYaml.getString("general.prefix");
//			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
//			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
//			line = Util.replaceAll(line, "%argument_target%", target);
//			line = Util.replaceAll(line, "%argument_rank%", rank);
//			String msg = PowerRanks.chatColor(line, true);
//			if (msg.length() > 0)
//				player.sendMessage(msg);
//		}
//	}

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

//	public static void messageCommandUsageReload(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_reload");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageReload(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_reload");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandReloadConfig(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.reload_config");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandReloadConfig(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandReloadConfigDone(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.reload_config_done");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandReloadConfigDone(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandReloadPlugin(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.reload_plugin");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandReloadPlugin(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandReloadPluginDone(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.reload_plugin_done");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandReloadPluginDone(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSet(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_setrank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSet(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSetown(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_setownrank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetown(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setownrank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageCheck(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_check");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageCheck(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_check");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageAddperm(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_permission");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageAddperm(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageDelperm(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_permission");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageDelperm(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPermissionAdded(Player player, String permission, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.permission_added");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandPermissionAdded(CommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPermissionRemoved(Player player, String permission, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandPermissionRemoved(CommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageAddInheritance(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_inheritance");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageAddInheritance(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageRemoveInheritance(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageRemoveInheritance(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSetPrefix(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_prefix");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetPrefix(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_prefix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSetSuffix(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_suffix");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetSuffix(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_suffix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSetChatColor(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_chat_color");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetChatColor(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_chat_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageSetNameColor(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_name_color");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetNameColor(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_name_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageCreateRank(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_rank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageCreateRank(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageDeleteRank(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_delete_rank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageDeleteRank(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_delete_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageEnableBuild(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_enable_build");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageEnableBuild(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enable_build");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageDisableBuild(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_disable_build");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageDisableBuild(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disable_build");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsagePromote(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_promote");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsagePromote(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_promote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandUsageDemote(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_demote");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageDemote(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_demote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandInheritanceAdded(Player player, String inheritance, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
//		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandInheritanceAdded(CommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandInheritanceRemoved(Player player, String inheritance, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
//		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandInheritanceRemoved(CommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetPrefix(Player player, String prefix, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
//		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetPrefix(CommandSender console, String prefix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetSuffix(Player player, String suffix, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
//		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetSuffix(CommandSender console, String suffix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetChatColor(Player player, String color, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
//		msg = Util.replaceAll(msg, "%argument_color%", color);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetChatColor(CommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetNameColor(Player player, String color, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
//		msg = Util.replaceAll(msg, "%argument_color%", color);
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetNameColor(CommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandCreateRankSuccess(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_created");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandCreateRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandCreateRankError(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandCreateRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandDeleteRankSuccess(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDeleteRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandDeleteRankError(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDeleteRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPromoteSuccess(Player player, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandPromoteSuccess(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPromoteError(Player player, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandPromoteError(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandDemoteSuccess(Player player, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDemoteSuccess(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandDemoteError(Player player, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDemoteError(CommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandBuildEnabled(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandBuildEnabled(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandBuildDisabled(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandBuildDisabled(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandRenameRankSuccess(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandRenameRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandRenameRankError(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandRenameRankError(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetDefaultRankSuccess(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.default_rank_changed");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetDefaultRankSuccess(CommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.default_rank_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandSetDefaultRankError(Player player, String rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_changing_default_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

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

//	public static void messageCommandUsageListPermissions(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_listpermissions");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageListPermissions(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listpermissions");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageErrorAddingPermission(Player player, String rank, String permission) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_adding_permission");
//		msg = Util.replaceAll(msg, "%argument_rank%", rank);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageErrorAddingPermission(CommandSender console, String rank, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_permission");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPermissionAddedToAllRanks(Player player, String permission) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.permission_added_to_all_ranks");
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandPermissionAddedToAllRanks(CommandSender console, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added_to_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandPermissionRemovedFromAllRanks(Player player, String permission) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.permission_removed_from_all_ranks");
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

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

//	public static void unknownCommand(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.unknown_command");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void unknownCommand(CommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.unknown_command");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

//	public static void messageCommandAddbuyablerankSuccess(Player player, String rankname, String rankname2) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_added");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandAddbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandAddbuyablerankError(Player player, String rankname, String rankname2) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_adding_buyable_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandAddbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageAddbuyablerank(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_buyable_rank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageAddbuyablerank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandDelbuyablerankSuccess(Player player, String rankname, String rankname2) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_removed");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDelbuyablerankSuccess(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buyable_rank_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandDelbuyablerankError(Player player, String rankname, String rankname2) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_removing_buyable_rank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandDelbuyablerankError(CommandSender sender, String rankname, String rankname2) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_buyable_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", rankname2);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDelbuyablerank(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_del_buyable_rank");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageDelbuyablerank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_del_buyable_rank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetcostSuccess(Player player, String rankname, String cost) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.buy_cost_set");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_cost%", cost);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetcostSuccess(CommandSender sender, String rankname, String cost) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.buy_cost_set");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetcostError(Player player, String rankname, String cost) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_setting_buy_cost");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_cost%", cost);
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandSetcostError(CommandSender sender, String rankname, String cost) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_setting_buy_cost");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_cost%", cost);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageSetcost(Player player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_cost");
//		if (msg.length() > 0)
//			player.sendMessage(msg);
//	}

	public static void messageCommandUsageSetcost(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_cost");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandPlayerPermissionAdded(Player sender, String permission, String target_player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.player_permission_added");
//		msg = Util.replaceAll(msg, "%argument_target%", target_player);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandPlayerPermissionAdded(CommandSender sender, String permission, String target_player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_permission_added");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageErrorAddingPlayerPermission(Player sender, String target_player, String permission) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_adding_player_permission");
//		msg = Util.replaceAll(msg, "%argument_target%", target_player);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageErrorAddingPlayerPermission(CommandSender sender, String target_player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageAddplayerperm(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_player_permission");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageAddplayerperm(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandPlayerPermissionRemoved(Player sender, String permission, String target_player) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.player_permission_removed");
//		msg = Util.replaceAll(msg, "%argument_target%", target_player);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandPlayerPermissionRemoved(CommandSender sender, String permission, String target_player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_permission_removed");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageErrorRemovingPlayerPermission(Player sender, String target_player, String permission) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_removing_player_permission");
//		msg = Util.replaceAll(msg, "%argument_target%", target_player);
//		msg = Util.replaceAll(msg, "%argument_permission%", permission);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageErrorRemovingPlayerPermission(CommandSender sender, String target_player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_player_permission");
		msg = Util.replaceAll(msg, "%argument_target%", target_player);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDelplayerperm(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_player_permission");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageDelplayerperm(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_player_permission");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageSuccessAddsubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_adding_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageSuccessAddsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageErrorAddsubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_adding_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageErrorAddsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageSuccessDelsubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_removing_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageSuccessDelsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageErrorDelsubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_removing_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageErrorDelsubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_removing_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageAddsubrank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_subrank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageAddsubrank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDelsubrank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_subrank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageDelsubrank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_subrank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageListSubranks(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_listsubranks");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageListSubranks(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listsubranks");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageSuccessChangesubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_change_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//
//	}

	public static void messageSuccessChangesubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);

	}

//	public static void messageErrorChangesubrank(Player sender, String subrank, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_change_subrank");
//		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
//		msg = Util.replaceAll(msg, "%argument_player%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageErrorChangesubrank(CommandSender sender, String subrank, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_change_subrank");
		msg = Util.replaceAll(msg, "%argument_subrank%", subrank);
		msg = Util.replaceAll(msg, "%argument_player%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageEnablesubrankprefix(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankprefix");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageEnablesubrankprefix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDisablesubrankprefix(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankprefix");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageDisablesubrankprefix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankprefix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageEnablesubranksuffix(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubranksuffix");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageEnablesubranksuffix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDisablesubranksuffix(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubranksuffix");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageDisablesubranksuffix(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubranksuffix");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageEnablesubrankpermissions(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankpermissions");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageEnablesubrankpermissions(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageDisablesubrankpermissions(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankpermissions");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageDisablesubrankpermissions(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disablesubrankpermissions");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageCreateusertag(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_usertag");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageCreateusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageEditusertag(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_edit_usertag");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageEditusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_edit_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageRemoveusertag(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_usertag");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageRemoveusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageSetusertag(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_usertag");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageSetusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageClearusertag(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_usertag");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageClearusertag(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_usertag");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageListusertags(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_list_usertags");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageListusertags(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_list_usertags");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandCreateusertagSuccess(Player sender, String tag, String text) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_create_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandCreateusertagSuccess(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandCreateusertagError(Player sender, String tag, String text) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_create_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandCreateusertagError(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandEditusertagSuccess(Player sender, String tag, String text) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_edit_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandEditusertagSuccess(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandEditusertagError(Player sender, String tag, String text) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_edit_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandEditusertagError(CommandSender sender, String tag, String text) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_edit_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		msg = Util.replaceAll(msg, "%argument_usertag_text%", text);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandRemoveusertagSuccess(Player sender, String tag) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_remove_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandRemoveusertagSuccess(CommandSender sender, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandRemoveusertagError(Player sender, String tag) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_remove_usertag");
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandRemoveusertagError(CommandSender sender, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_remove_usertag");
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetusertagSuccess(Player sender, String playername, String tag) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_set_usertag");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetusertagSuccess(CommandSender sender, String playername, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetusertagError(Player sender, String playername, String tag) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_set_usertag");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetusertagError(CommandSender sender, String playername, String tag) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		msg = Util.replaceAll(msg, "%argument_usertag_tag%", tag);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandClearusertagSuccess(Player sender, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_clear_usertag");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandClearusertagSuccess(CommandSender sender, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandClearusertagError(Player sender, String playername) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_clear_usertag");
//		msg = Util.replaceAll(msg, "%argument_target%", playername);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandClearusertagError(CommandSender sender, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_usertag");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageSetpromoterank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_promoterank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageSetpromoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageSetdemoterank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_demoterank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageSetdemoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageClearpromoterank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_promoterank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageClearpromoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_promoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageCleardemoterank(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_demoterank");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageCleardemoterank(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_clear_demoterank");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetpromoterankSuccess(Player sender, String rankname, String promote_rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_set_promoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetpromoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetpromoterankError(Player sender, String rankname, String promote_rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_set_promoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetpromoterankError(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetdemoterankSuccess(Player sender, String rankname, String promote_rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_set_demoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetdemoterankSuccess(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandSetdemoterankError(Player sender, String rankname, String promote_rank) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_set_demoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandSetdemoterankError(CommandSender sender, String rankname, String promote_rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_set_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		msg = Util.replaceAll(msg, "%argument_target_rank%", promote_rank);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandClearpromoterankSuccess(Player sender, String rankname) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_clear_promoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandClearpromoterankSuccess(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandClearpromoterankError(Player sender, String rankname) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_clear_promoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandClearpromoterankError(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_promoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandCleardemoterankSuccess(Player sender, String rankname) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.success_clear_demoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandCleardemoterankSuccess(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.success_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandCleardemoterankError(Player sender, String rankname) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_clear_demoterank");
//		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandCleardemoterankError(CommandSender sender, String rankname) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_clear_demoterank");
		msg = Util.replaceAll(msg, "%argument_rank%", rankname);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageUsertagsDisabled(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String external_plugin_name = "None";
//		if (PowerRanks.plugin_hook_deluxetags)
//			external_plugin_name = "DeluxeTags";
//		String msg = getGeneralMessage(langYaml, "messages.error_usertags_disable_use_external_plugin");
//		msg = Util.replaceAll(msg, "%argument_external_plugin%", external_plugin_name); // external_plugin
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

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

//	public static void messageCommandUsageAddoninfo(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_addoninfo");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandUsageAddoninfo(CommandSender sender) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_addoninfo");
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandErrorAddonNotFound(Player sender, String addon_name) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "messages.error_addon_not_found");
//		msg = Util.replaceAll(msg, "%argument_addon%", addon_name);
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}

	public static void messageCommandErrorAddonNotFound(CommandSender sender, String addon_name) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_addon_not_found");
		msg = Util.replaceAll(msg, "%argument_addon%", addon_name);
		if (msg.length() > 0)
			sender.sendMessage(msg);
	}

//	public static void messageCommandUsageFactoryReset(Player sender) {
//		YamlConfiguration langYaml = PowerRanks.loadLangFile();
//		String msg = getGeneralMessage(langYaml, "commands.usage_command_factoryreset");
//		if (msg.length() > 0)
//			sender.sendMessage(msg);
//	}
	
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
}