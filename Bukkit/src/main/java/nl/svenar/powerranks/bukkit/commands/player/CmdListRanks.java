package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
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
            // handleServerListRanks(sender);

            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "[gradient=#01b0fb,#f50be5]┌────────────┐┌───────────┐[/gradient]", true,
            // false, false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "#01b0fb│ [gradient=#ffff00,#ef3300]PowerRanks[/gradient] #7E63DE││
            // [gradient=#70D92C,#227200]◀ 01/10 ▶[/gradient] #f50be5│",
            // true, false, false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "[gradient=#01b0fb,#f50be5]├────────────┴┴───────────┤[/gradient]", true,
            // false, false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "#01b0fb│&7 0 &rMember [gradient=#127e00,#3eaf18]MEMBER[/gradient] #f50be5│",
            // true, false,
            // false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "#01b0fb│&7 50 &rModerator [gradient=#9d1dff,#e22581]MODERATOR[/gradient]
            // #f50be5│", true, false,
            // false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "#01b0fb│&7 75 &rAdmin [gradient=#ffff00,#ef3300]ADMIN[/gradient] #f50be5│",
            // true, false,
            // false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "#01b0fb│&7 100 &rOwner [gradient=#ff00ff,#33ccff]OWNER[/gradient] #f50be5│",
            // true, false,
            // false));
            // sender.sendMessage(PowerRanks.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
            // "[gradient=#01b0fb,#f50be5]└─────────────────────────┘[/gradient]", true,
            // false, false));

            PageNavigationManager pageNavigationManager = new PageNavigationManager();
            // pageNavigationManager.setItemsPerPage(2);
            pageNavigationManager.setMonospace(true);
            pageNavigationManager.setItems(formatRankList(PRCache.getRanks()));
            for (String line : pageNavigationManager.getPage(page).generate()) {
                sender.sendMessage(line);

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
            // pageNavigationManager.setItemsPerPage(2);
            pageNavigationManager.setMonospace(false);
            pageNavigationManager.setItems(formatPlayerRankList(prplayer.getRanks()));
            for (String line : pageNavigationManager.getPage(page).generate()) {
                sender.sendMessage(line);

            }

            // handlePlayerListRanks(sender, prplayer);
        }

    }

    // private void handleServerListRanks(CommandSender sender) {
    // sendMessage(sender, "list-header", false);
    // sendMessage(sender, "list-server-ranks", ImmutableMap.of( //
    // "amount", String.valueOf(PRCache.getRanks().size()) //
    // ), false);
    // for (String line : formatRankList(PRCache.getRanks())) {
    // sender.sendMessage(line);

    // }
    // sendMessage(sender, "list-footer", false);
    // }

    // private void handlePlayerListRanks(CommandSender sender, PRPlayer prplayer) {
    // sendMessage(sender, "list-header", false);
    // sendMessage(sender, "list-player-ranks", ImmutableMap.of( //
    // "player", prplayer.getName(), //
    // "amount", String.valueOf(prplayer.getRanks().size()) //
    // ), false);
    // for (String line : formatPlayerRankList(prplayer.getRanks())) {
    // sender.sendMessage(line);

    // }
    // sendMessage(sender, "list-footer", false);
    // }

    private List<String> formatPlayerRankList(Set<PRPlayerRank> playerRanksList) {
        List<PRPlayerRank> playerRanks = playerRanksList.stream().collect(Collectors.toList());
        List<PRRank> ranks = PRUtil.playerRanksToRanks(playerRanks);
        return formatRankList(ranks);
    }

    // │ ┤ ┐ └ ┴ ┬ ├ ─ ┼ ┘ ┌

    private List<String> formatRankList(List<PRRank> ranks) {
        List<String> list = new ArrayList<String>();
        PRUtil.sortRanksByWeight(ranks);

        Map<String, String> nameBuffer = new HashMap<>();
        Map<String, String> weightBuffer = new HashMap<>();
        Map<String, String> prefixBuffer = new HashMap<>();
        Map<String, String> suffixBuffer = new HashMap<>();

        for (PRRank prrank : ranks) {
            nameBuffer.put(prrank.getName(), prrank.getName());
            weightBuffer.put(prrank.getName(), String.valueOf(prrank.getWeight()));
            prefixBuffer.put(prrank.getName(), PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR, prrank.getPrefix()));
            suffixBuffer.put(prrank.getName(), PowerRanks.getPowerColor().removeFormat(PowerColor.UNFORMATTED_COLOR_CHAR, prrank.getSuffix()));
        }

        int longestName = nameBuffer.values().stream().mapToInt(name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max().orElse(0);
        int longestWeight = weightBuffer.values().stream().mapToInt(name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max().orElse(0);
        int longestPrefix  = prefixBuffer.values().stream().mapToInt(name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max().orElse(0);
        int longestSuffix = suffixBuffer.values().stream().mapToInt(name -> name.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum()).max().orElse(0);

        for (Entry<String, String> entry : nameBuffer.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            while (currentLength < longestName) {
                value += " ";
                currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            }
            nameBuffer.put(key, value);
        }

        for (Entry<String, String> entry : weightBuffer.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            while (currentLength < longestWeight) {
                value = " " + value;
                currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            }
            weightBuffer.put(key, value);
        }

        for (Entry<String, String> entry : prefixBuffer.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            while (currentLength < longestPrefix) {
                value += " ";
                currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            }
            prefixBuffer.put(key, value);
        }

        for (Entry<String, String> entry : suffixBuffer.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            while (currentLength < longestSuffix) {
                value += " ";
                currentLength = value.chars().map(c -> DefaultFontInfo.getDefaultFontInfo((char) c).getLength()).sum();
            }
            suffixBuffer.put(key, value);
        }
        
        for (PRRank prrank : ranks) {
            String line = prepareMessage("list-rank-item", ImmutableMap.of( //
                    "rank", nameBuffer.get(prrank.getName()), //
                    "prefix", prefixBuffer.get(prrank.getName()), //
                    "suffix", suffixBuffer.get(prrank.getName()), //
                    "weight", weightBuffer.get(prrank.getName()) //
            ), false);
            list.add(line);
        }
        return list;
    }

}
