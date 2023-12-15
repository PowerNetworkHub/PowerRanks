package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.textcomponents.PageNavigationManager;

public class cmd_listplayerpermissions extends PowerCommand {

	public cmd_listplayerpermissions(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		String playername = "";
		int page = 1;

		if (args.length > 0) {
			playername = args[0];
		}
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Invalid page number.");
				return false;
			}
		}

		PageNavigationManager pageNavigationManager = new PageNavigationManager();
		pageNavigationManager.setItemsPerPage(sender instanceof Player ? 5 : 10);
		pageNavigationManager.setMonospace(sender instanceof ConsoleCommandSender);
		pageNavigationManager.setFancyPageControls(sender instanceof Player);
		pageNavigationManager.setBaseCommand("pr listplayerpermissions " + playername);
		pageNavigationManager.setItems(
				formatList(PRCache.getPlayer(playername).getPermissions(), sender instanceof ConsoleCommandSender));

		for (Object line : pageNavigationManager.getPage(page).generate()) {
			if (line instanceof String) {
				sender.sendMessage((String) line);
			} else if (line instanceof TextComponent) {
				sender.spigot().sendMessage((TextComponent) line);
			} else {
				sender.spigot().sendMessage((BaseComponent[]) line);
			}
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRPlayer prPlayer : CacheManager.getPlayers()) {
				tabcomplete.add(prPlayer.getName());
			}
		}

		return tabcomplete;
	}

	private List<String> formatList(Set<PRPermission> permissions, boolean b) {
		List<String> list = new ArrayList<String>();

		for (PRPermission prpermission : permissions) {
			String line = prepareMessage("list.list-permission-item", ImmutableMap.of( //
					"permission", (prpermission.getValue() ? ChatColor.GREEN : ChatColor.RED) + prpermission.getName() //
			), false);
			while (line.endsWith(" ") || line.toLowerCase().endsWith(PowerColor.COLOR_CHAR + "")
					|| line.toLowerCase().endsWith(PowerColor.COLOR_CHAR + "r")) {
				line = line.substring(0, line.length() - 1);
			}
			list.add(line);
		}

		return list;
	}
}
