package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util;

public class cmd_listsubranks extends PowerCommand {

	private Users users;

	public cmd_listsubranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.listsubranks")) {
			if (args.length == 1) {
				Player targetPlayer = Util.getPlayerByName(args[0]);
				if (targetPlayer != null) {
					List<String> subranks = users.getSubranks(targetPlayer.getName());
					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
					// sender.sendMessage(ChatColor.DARK_GREEN + "Subranks from player: " + ChatColor.GREEN + targetPlayer.getName());
					// sender.sendMessage(ChatColor.DARK_GREEN + "Number of subranks: " + ChatColor.GREEN + subranks.size());
					sender.sendMessage(ChatColor.AQUA + targetPlayer.getName() + "'s subranks (" + ChatColor.AQUA + subranks.size() + "):");
					int index = 0;
					for (String subrank : subranks) {
						index++;
						sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + subrank + ChatColor.RESET + " " + PowerRanks.chatColor(users.getPrefix(subrank), true));
					}
					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");
				} else {
					Messages.messagePlayerNotFound(sender, args[0]);
				}
			} else {
				Messages.messageCommandUsageListSubranks(sender);
			}
		} else {
			Messages.noPermission(sender);
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

		return tabcomplete;
	}
}
