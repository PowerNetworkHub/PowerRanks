package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_listplayerpermissions extends PowerCommand {

	public cmd_listplayerpermissions(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
			if (targetPlayer != null) {
				displayList(sender, targetPlayer, commandLabel, 0);
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", args[0])
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
			PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
			if (targetPlayer != null) {
				displayList(sender, targetPlayer, commandLabel, page);
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", args[0])
								.build(),
						'[', ']'));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	private void displayList(CommandSender sender, PRPlayer prPlayer, String commandLabel, int page) {
		ArrayList<String> output_messages = new ArrayList<String>();

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
				+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");

		List<PRPermission> playerPermissions = prPlayer.getPermissions();

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
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
					+ "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
					+ "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%"
					+ "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%"
					+ "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
					+ "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%" + "\"}}]";

			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%next_page%", String.valueOf(page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%previous_page%", String.valueOf(page - 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%last_page%",
					String.valueOf(last_page + 1));
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%playername%", prPlayer.getName());
			page_selector_tellraw = Util.replaceAll(page_selector_tellraw, "%commandlabel%", commandLabel);

			output_messages.add(page_selector_tellraw);

			output_messages.add(ChatColor.AQUA + prPlayer.getName() + "'s permissions:");

			// sender.sendMessage("[A] " + last_page + " " + lines_per_page);
		} else {
			output_messages.add(ChatColor.AQUA + "Page " + ChatColor.BLUE + (page + 1) + ChatColor.AQUA + "/"
					+ ChatColor.BLUE + (last_page + 1));
			output_messages.add(ChatColor.AQUA + "Next page " + ChatColor.BLUE + "/" + commandLabel
					+ " listplayerpermissions " + prPlayer.getName() + " " + ChatColor.BLUE
					+ (page + 2 > last_page + 1 ? last_page + 1 : page + 2));
		}

		int line_index = 0;
		for (PRPermission permission : playerPermissions) {
			if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
				output_messages.add(ChatColor.DARK_GREEN + "#" + (line_index + 1) + ". "
						+ (permission.getValue() ? ChatColor.GREEN : ChatColor.RED) + permission.getName());
			}
			line_index += 1;
		}

		output_messages.add(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
				+ ChatColor.BLUE + "===");

		if (plugin != null) {
			for (String msg : output_messages) {
				if (msg.startsWith("tellraw")) {
					plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);
				} else {
					sender.sendMessage(msg);
				}
			}
		}
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
}
