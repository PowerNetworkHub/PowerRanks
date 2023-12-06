package nl.svenar.powerranks.bukkit.commands.rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.textcomponents.DefaultFontInfo;
import nl.svenar.powerranks.bukkit.textcomponents.PageNavigationManager;

public class cmd_listdefaultranks extends PowerCommand {

	public cmd_listdefaultranks(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
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

		PageNavigationManager pageNavigationManager = new PageNavigationManager();
		pageNavigationManager.setItemsPerPage(sender instanceof Player ? 5 : 10);
		pageNavigationManager.setMonospace(sender instanceof ConsoleCommandSender);
		pageNavigationManager.setFancyPageControls(sender instanceof Player);
		pageNavigationManager.setBaseCommand("pr listranks");
		pageNavigationManager.setItems(formatRankList(PRCache.getDefaultRanks(), sender instanceof ConsoleCommandSender));

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

		return tabcomplete;
	}

	private List<String> formatRankList(Set<PRRank> ranksSet, boolean hasMonospaceFont) {
		List<String> list = new ArrayList<String>();
		List<PRRank> ranks = new ArrayList<PRRank>();
		for (PRRank rank : ranksSet) {
			ranks.add(rank);
		}
		PRUtil.sortRanksByWeight(ranks);

		Map<String, String> nameBuffer = new HashMap<>();
		Map<String, String> weightBuffer = new HashMap<>();
		Map<String, String> prefixBuffer = new HashMap<>();
		Map<String, String> unformattedPrefixBuffer = new HashMap<>();
		Map<String, String> suffixBuffer = new HashMap<>();
		Map<String, String> unformattedSuffixBuffer = new HashMap<>();

		for (PRRank prrank : ranks) {
			nameBuffer.put(prrank.getName(), prrank.getName());
			weightBuffer.put(prrank.getName(), String.valueOf(prrank.getWeight()));

			prefixBuffer.put(prrank.getName(), prrank.getPrefix());
			unformattedPrefixBuffer.put(prrank.getName(),
					PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR, prrank.getPrefix()));

			suffixBuffer.put(prrank.getName(), prrank.getSuffix());
			unformattedSuffixBuffer.put(prrank.getName(),
					PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR, prrank.getSuffix()));
		}

		if (!hasMonospaceFont) {
			int longestName = nameBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);
			int longestWeight = weightBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);
			int longestPrefix = unformattedPrefixBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);
			int longestSuffix = unformattedSuffixBuffer.values().stream().mapToInt(
					name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max()
					.orElse(0);

			for (Entry<String, String> entry : nameBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestName) {
					value += " ";
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				nameBuffer.put(key, value);
			}

			for (Entry<String, String> entry : weightBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestWeight) {
					value = " " + value;
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				weightBuffer.put(key, value);
			}

			for (Entry<String, String> entry : unformattedPrefixBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String targetValue = prefixBuffer.get(key);
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestPrefix) {
					value += " ";
					targetValue += " ";
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				prefixBuffer.put(key, targetValue);
			}

			for (Entry<String, String> entry : unformattedSuffixBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String targetValue = suffixBuffer.get(key);
				int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
						.sum();
				while (currentLength < longestSuffix) {
					value += " ";
					targetValue += " ";
					currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength())
							.sum();
				}
				suffixBuffer.put(key, targetValue);
			}
		} else {
			int longestName = nameBuffer.values().stream().mapToInt(name -> name.length()).max().orElse(0);
			int longestWeight = weightBuffer.values().stream().mapToInt(name -> name.length()).max().orElse(0);
			int longestPrefix = unformattedPrefixBuffer.values().stream().mapToInt(name -> name.length()).max()
					.orElse(0);
			int longestSuffix = unformattedSuffixBuffer.values().stream().mapToInt(name -> name.length()).max()
					.orElse(0);

			for (Entry<String, String> entry : nameBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				while (currentLength < longestName) {
					value += " ";
					currentLength = value.length();
				}
				nameBuffer.put(key, value);
			}

			for (Entry<String, String> entry : weightBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				while (currentLength < longestWeight) {
					value = " " + value;
					currentLength = value.length();
				}
				weightBuffer.put(key, value);
			}

			for (Entry<String, String> entry : unformattedPrefixBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				String targetValue = prefixBuffer.get(key);
				while (currentLength < longestPrefix) {
					value += " ";
					targetValue += " ";
					currentLength = value.length();
				}
				prefixBuffer.put(key, targetValue);
			}

			for (Entry<String, String> entry : unformattedSuffixBuffer.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				int currentLength = value.length();
				String targetValue = suffixBuffer.get(key);
				while (currentLength < longestSuffix) {
					value += " ";
					targetValue += " ";
					currentLength = value.length();
				}
				suffixBuffer.put(key, targetValue);
			}
		}

		for (PRRank prrank : ranks) {
			String line = prepareMessage("list.list-rank-item", ImmutableMap.of( //
					"rank", nameBuffer.get(prrank.getName()), //
					"prefix", prefixBuffer.get(prrank.getName()), //
					"suffix", suffixBuffer.get(prrank.getName()), //
					"weight", weightBuffer.get(prrank.getName()) //
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
