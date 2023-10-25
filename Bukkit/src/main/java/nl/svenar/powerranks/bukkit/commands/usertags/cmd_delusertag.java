package nl.svenar.powerranks.bukkit.commands.usertags;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_delusertag extends PowerCommand {

	private Users users;

	public cmd_delusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			if (!PowerRanks.plugin_hook_deluxetags) {
				final String playername = sender.getName();
				final String tag = args[0];
				final boolean result = this.users.delUserTag(playername, tag);
				if (result) {
                    Player targetPlayer = Bukkit.getServer().getPlayer(playername);
                    if (targetPlayer != null) {
                        PowerRanks.getInstance().updateTablistName(targetPlayer);
                    }
                    
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".success"),
							ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", playername)
								.put("usertag", tag)
								.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".failed"),
							ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", playername)
								.put("usertag", tag)
								.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".disabled"));
			}
		} else if (args.length == 2) {
			if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".other")) {
				if (!PowerRanks.plugin_hook_deluxetags) {
					final String playername = args[0];
					final String tag = args[1];
					final boolean result = this.users.delUserTag(playername, tag);
					if (result) {
                        Player targetPlayer = Bukkit.getServer().getPlayer(playername);
                        if (targetPlayer != null) {
                            PowerRanks.getInstance().updateTablistName(targetPlayer);
                        }
                        
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success"),
								ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", playername)
									.put("usertag", tag)
									.build(),
								'[', ']'));
					} else {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed"),
								ImmutableMap.<String, String>builder()
								    .put("player", sender.getName())
									.put("target", playername)
									.put("usertag", tag)
									.build(),
								'[', ']'));
					}
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".disabled"));
				}
			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
            Player target_player = Util.getPlayerByName(args[0]);
			if (target_player != null) {
                for (String tag : CacheManager.getPlayer(target_player.getUniqueId().toString()).getUsertags()) {
                    tabcomplete.add(tag);
                }
			}
		}

		return tabcomplete;
	}
}
