package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;
import nl.svenar.powerranks.bukkit.textcomponents.DefaultFontInfo;
import nl.svenar.powerranks.bukkit.textcomponents.PageNavigationManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;

@CommandAlias("%powerrankscommand")
@Description("List all ranks on the server or of a player")
public class CmdListRanks extends PowerBaseCommand {

    public CmdListRanks(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("listranks")
    @CommandPermission("powerranks.cmd.listranks")
    @Syntax("[player]")
    @CommandCompletion("@prplayers @range:1-5")
    public void onListranks(CommandSender sender, @Optional String targetName, @Optional Integer page) {
        if (page == null) {
            if (targetName == null) {
                page = 1;
            } else {
                try {
                    page = Integer.parseInt(targetName);
                    targetName = null;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
        }
        if (targetName == null) {
            PageNavigationManager pageNavigationManager = new PageNavigationManager();
            pageNavigationManager.setItemsPerPage(sender instanceof Player ? 5 : 10);
            pageNavigationManager.setMonospace(sender instanceof ConsoleCommandSender);
            pageNavigationManager.setFancyPageControls(sender instanceof Player);
            pageNavigationManager.setBaseCommand("pr listranks");
            pageNavigationManager.setItems(formatRankList(PRCache.getRanks(), sender instanceof ConsoleCommandSender));
            for (Object line : pageNavigationManager.getPage(page).generate()) {
                if (line instanceof String) {
                    sender.sendMessage((String)line);
                } else if (line instanceof TextComponent) {
                    sender.spigot().sendMessage((TextComponent)line);
                } else {
                    sender.spigot().sendMessage((BaseComponent[])line);
                }
            }
        } else {
            PRPlayer prplayer = getPRPlayer(targetName);
            if (prplayer == null) {
                sendMessage(sender, "player-not-found", ImmutableMap.of( //
                        "player", targetName //
                ), true);
                return;
            }

            PageNavigationManager pageNavigationManager = new PageNavigationManager();
            pageNavigationManager.setItemsPerPage(sender instanceof Player ? 5 : 10);
            pageNavigationManager.setMonospace(sender instanceof ConsoleCommandSender);
            pageNavigationManager.setFancyPageControls(sender instanceof Player);
            pageNavigationManager.setBaseCommand("pr listranks");
            pageNavigationManager
                    .setItems(formatPlayerRankList(prplayer.getRanks(), sender instanceof ConsoleCommandSender));
            for (Object line : pageNavigationManager.getPage(page).generate()) {
                if (line instanceof String) {
                    sender.sendMessage((String)line);
                } else if (line instanceof TextComponent) {
                    sender.spigot().sendMessage((TextComponent)line);
                } else {
                    sender.spigot().sendMessage((BaseComponent[])line);
                }
            }
        }

    }

    private List<String> formatPlayerRankList(Set<PRPlayerRank> playerRanksList, boolean hasMonospaceFont) {
        List<PRPlayerRank> playerRanks = playerRanksList.stream().collect(Collectors.toList());
        List<PRRank> ranks = PRUtil.playerRanksToRanks(playerRanks);
        return formatRankList(ranks, hasMonospaceFont);
    }

    // │ ┤ ┐ └ ┴ ┬ ├ ─ ┼ ┘ ┌

    private List<String> formatRankList(List<PRRank> ranks, boolean hasMonospaceFont) {
        List<String> list = new ArrayList<String>();
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
            String line = prepareMessage("list-rank-item", ImmutableMap.of( //
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
