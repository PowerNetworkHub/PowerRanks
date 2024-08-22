package nl.svenar.powerranks.bukkit.commands.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.PowerPermissibleBase;
import nl.svenar.powerranks.bukkit.textcomponents.DefaultFontInfo;
import nl.svenar.powerranks.bukkit.textcomponents.PageNavigationManager;
import nl.svenar.powerranks.common.utils.PowerColor;

public class cmd_permissionlookups extends PowerCommand {

	private final int maxResultsConsole = 20;
	private final int maxResultsPlayer = 10;

	public cmd_permissionlookups(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {

		int page = 1;
		if (args.length > 0) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Invalid page number.");
				return false;
			}
		}

		Map<String, Integer> permissionCallCount = PowerPermissibleBase.permissionCallCount;
		// sort from high to low value
		permissionCallCount = sortByValue(permissionCallCount, false);

		List<PRCallCount> callCounts = new ArrayList<>();
		for (Entry<String, Integer> entry : permissionCallCount.entrySet()) {
			callCounts.add(new PRCallCount(entry.getKey(), entry.getValue()));
		}

		PageNavigationManager pageNavigationManager = new PageNavigationManager();
		pageNavigationManager.setItemsPerPage(sender instanceof Player ? maxResultsPlayer : maxResultsConsole);
		pageNavigationManager.setMonospace(sender instanceof ConsoleCommandSender);
		pageNavigationManager.setFancyPageControls(sender instanceof Player);
		pageNavigationManager.setBaseCommand("pr permissionlookups");
		pageNavigationManager.setItems(formatPermissionCountList(callCounts, sender instanceof ConsoleCommandSender));

		for (Object line : pageNavigationManager.getPage(page).generate()) {
			if (line instanceof String) {
				sender.sendMessage((String) line);
			} else if (line instanceof TextComponent) {
				sender.spigot().sendMessage((TextComponent) line);
			} else {
				sender.spigot().sendMessage((BaseComponent[]) line);
			}
		}

		sender.sendMessage(PowerRanks.chatColor("&7callcount &apermission&2.&anode", true));
		sender.sendMessage(PowerRanks.chatColor("&2Total called permissions: &a" + permissionCallCount.size(), true));

		return false;
	}

	private Map<String, Integer> sortByValue(Map<String, Integer> permissionCallCount, boolean asc) {
		// sort from high to low value
		List<Entry<String, Integer>> list = new LinkedList<>(permissionCallCount.entrySet());
		java.util.Collections.sort(list, (Entry<String, Integer> o1, Entry<String, Integer> o2) -> {
			if (asc) {
				return (o1.getValue()).compareTo(o2.getValue());
			} else {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		// return
		Map<String, Integer> result = new LinkedHashMap<>();
		for (Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	@Override
	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<>();

		return tabcomplete;
	}

	private List<String> formatPermissionCountList(List<PRCallCount> prCallCounts, boolean hasMonospaceFont) {
		List<String> list = new ArrayList<>();

		Map<String, String> permissionNameBuffer = new HashMap<>();
		Map<String, String> countBuffer = new HashMap<>();

		for (PRCallCount prcc : prCallCounts) {
			permissionNameBuffer.put(prcc.getName(), prcc.getName());
			countBuffer.put(prcc.getName(), String.valueOf(prcc.getCount()));
		}

		if (!hasMonospaceFont) {
			int longestName = permissionNameBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);
			int longestCount = countBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);

			for (Entry<String, String> entry : permissionNameBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestName) {
					value += " ";
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				permissionNameBuffer.put(key, value);
			}

			for (Entry<String, String> entry : countBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestCount) {
					value = " " + value;
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				countBuffer.put(key, value);
			}

		} else {
			int longestName = permissionNameBuffer.values().stream().mapToInt(name -> name.length()).max().orElse(0);
			int longestCount = countBuffer.values().stream().mapToInt(name -> name.length()).max().orElse(0);

			for (Entry<String, String> entry : permissionNameBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				while (currentLength < longestName) {
					value += " ";
					currentLength = value.length();
				}
				permissionNameBuffer.put(key, value);
			}

			for (Entry<String, String> entry : countBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				while (currentLength < longestCount) {
					value = " " + value;
					currentLength = value.length();
				}
				countBuffer.put(key, value);
			}
		}

		for (Iterator<PRCallCount> it = prCallCounts.iterator(); it.hasNext();) {
			PRCallCount prcc = it.next();
			String line = prepareMessage("list.list-permissioncallcount-item", ImmutableMap.of( //
					"permission", "&a" + permissionNameBuffer.get(prcc.getName()).replaceAll("\\.", "&2.&a"), //
					"callcount", countBuffer.get(prcc.getName()) //
			), false);
			while (line.endsWith(" ") || line.toLowerCase().endsWith(PowerColor.COLOR_CHAR + "")
					|| line.toLowerCase().endsWith(PowerColor.COLOR_CHAR + "r")) {
				line = line.substring(0, line.length() - 1);
			}
			list.add(line);
		}
		return list;
	}

	private class PRCallCount {
		private final String name;
		private final int count;

		public PRCallCount(String name, int count) {
			this.name = name;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public int getCount() {
			return count;
		}
	}
}
