package nl.svenar.PowerRanks.Data;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class Messages {
	
	public static String getGeneralMessage(YamlConfiguration langYaml, String lang_config_line) {
		String msg = "";

		String line = langYaml.getString(lang_config_line);
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			}
		}

		return msg;
	}
	
	public static void messageNoArgs(final Player player) {
		player.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		player.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
		player.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		player.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors());
		player.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
		player.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
		player.sendMessage(ChatColor.DARK_GREEN + "Support me on: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
		player.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void messageNoArgs(final ConsoleCommandSender console) {
		console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		console.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
		console.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		console.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors());
		console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
		console.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
		console.sendMessage(ChatColor.DARK_GREEN + "Support me on: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
		console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public static void helpMenu(final Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		if (lines != null) {
			String prefix = langYaml.getString("general.prefix");
			for (String line : lines) {
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
				if (msg.length() > 0)
					player.sendMessage(msg);
			}
		}
	}

	public static void helpMenu(final ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		if (lines != null) {
			String prefix = langYaml.getString("general.prefix");
			for (String line : lines) {
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
				String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
				if (msg.length() > 0)
					console.sendMessage(msg);
			}
		}
	}

	public static void noPermission(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.no_permission");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageSetRankSuccessSender(Player player, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.rank_set_sender");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				player.sendMessage(msg);
		}
	}

	public static void messageSetRankSuccessSender(ConsoleCommandSender console, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.rank_set_sender");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
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
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				target.sendMessage(msg);
		}

	}

	public static void messagePlayerNotFound(Player player, String target) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				player.sendMessage(msg);
		}
	}

	public static void messagePlayerNotFound(ConsoleCommandSender console, String target) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageGroupNotFound(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.group_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				player.sendMessage(msg);
		}
	}

	public static void messageGroupNotFound(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.group_not_found");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messagePlayerCheckRank(Player player, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_check_rank");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				player.sendMessage(msg);
		}
	}

	public static void messagePlayerCheckRank(ConsoleCommandSender console, String target, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();

		String line = langYaml.getString("messages.player_check_rank");
		if (line != null) {
			String prefix = langYaml.getString("general.prefix");
			line = Util.replaceAll(line, "%plugin_prefix%", prefix);
			line = Util.replaceAll(line, "%plugin_name%", PowerRanks.pdf.getName());
			line = Util.replaceAll(line, "%argument_target%", target);
			line = Util.replaceAll(line, "%argument_rank%", rank);
			String msg = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), line, true);
			if (msg.length() > 0)
				console.sendMessage(msg);
		}
	}

	public static void messageCommandUsageReload(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_reload");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageReload(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_reload");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadConfig(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandReloadConfig(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadConfigDone(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config_done");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandReloadConfigDone(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_config_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadPlugin(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandReloadPlugin(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandReloadPluginDone(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin_done");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandReloadPluginDone(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.reload_plugin_done");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSet(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSet(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetown(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setown");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSetown(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_setown");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageCheck(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_check");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageCheck(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_check");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddperm(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_permission");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageAddperm(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDelperm(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_permission");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageDelperm(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_permission");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAdded(Player player, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandPermissionAdded(ConsoleCommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionRemoved(Player player, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandPermissionRemoved(ConsoleCommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageConfigVersionUpdated(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.config_version_updated");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageConfigVersionUpdated(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.config_version_updated");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageAddInheritance(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_inheritance");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageAddInheritance(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_add_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveInheritance(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageRemoveInheritance(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetPrefix(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_prefix");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSetPrefix(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_prefix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetSuffix(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_suffix");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSetSuffix(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_suffix");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetChatColor(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_chat_color");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSetChatColor(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_chat_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageSetNameColor(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_name_color");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageSetNameColor(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_set_name_color");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageCreateRank(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_rank");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageCreateRank(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_create_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDeleteRank(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_delete_rank");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageDeleteRank(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_delete_rank");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageEnableBuild(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enable_build");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageEnableBuild(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_enable_build");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDisableBuild(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disable_build");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageDisableBuild(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_disable_build");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsagePromote(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_promote");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsagePromote(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_promote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandUsageDemote(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_demote");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandUsageDemote(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_demote");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceAdded(Player player, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandInheritanceAdded(ConsoleCommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandInheritanceRemoved(Player player, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandInheritanceRemoved(ConsoleCommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetPrefix(Player player, String prefix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetPrefix(ConsoleCommandSender console, String prefix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetSuffix(Player player, String suffix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetSuffix(ConsoleCommandSender console, String suffix, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetChatColor(Player player, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetChatColor(ConsoleCommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetNameColor(Player player, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetNameColor(ConsoleCommandSender console, String color, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandCreateRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandCreateRankError(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandCreateRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandDeleteRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDeleteRankError(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandDeleteRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteSuccess(Player player, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandPromoteSuccess(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPromoteError(Player player, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandPromoteError(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteSuccess(Player player, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandDemoteSuccess(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandDemoteError(Player player, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandDemoteError(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandBuildEnabled(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandBuildEnabled(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandBuildDisabled(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandBuildDisabled(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandRenameRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandRenameRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandRenameRankError(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandRenameRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.default_rank_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.default_rank_changed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankError(Player player, String rank) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_changing_default_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}

	public static void messageCommandSetDefaultRankError(ConsoleCommandSender console, String rank) {
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

	public static void messageCommandUsageListPermissions(Player player) {
		 YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listpermissions");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}
	
	public static void messageCommandUsageListPermissions(ConsoleCommandSender console) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.usage_command_listpermissions");
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageErrorAddingPermission(Player player, String rank, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_permission");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}
	
	public static void messageErrorAddingPermission(ConsoleCommandSender console, String rank, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_adding_permission");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}

	public static void messageCommandPermissionAddedToAllRanks(Player player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added_to_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}
	
	public static void messageCommandPermissionAddedToAllRanks(ConsoleCommandSender console, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added_to_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			console.sendMessage(msg);
	}
	
	public static void messageCommandPermissionRemovedFromAllRanks(Player player, String permission) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed_from_all_ranks");
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		if (msg.length() > 0)
			player.sendMessage(msg);
	}
	
	public static void messageCommandPermissionRemovedFromAllRanks(ConsoleCommandSender console, String permission) {
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

	public static void unknownCommand(Player player) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		String msg = getGeneralMessage(langYaml, "commands.unknown_command");
		if (msg.length() > 0)
			player.sendMessage(msg);
	}
}
