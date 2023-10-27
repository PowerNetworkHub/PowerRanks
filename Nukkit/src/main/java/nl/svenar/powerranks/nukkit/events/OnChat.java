package nl.svenar.powerranks.nukkit.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class OnChat implements Listener {

    private PowerRanks plugin;

    public OnChat(PowerRanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        try {
            if (this.plugin.getConfigManager().getBool("chat.enabled", true)) {

                String format = this.plugin.getConfigManager().getString("chat.format", "");

                List<String> ranknames = new ArrayList<>();
                for (PRPlayerRank rank : PRCache.getPlayer(player.getUniqueId().toString()).getRanks()) {
                    ranknames.add(rank.getName());
                }

                List<PRRank> ranks = new ArrayList<PRRank>();
                for (String rankname : ranknames) {
                    PRRank rank = PRCache.getRank(rankname);
                    if (rank != null) {
                        ranks.add(rank);
                    }
                }

                PRUtil.sortRanksByWeight(ranks);
                PRUtil.reverseRanks(ranks);

                String formatted_prefix = "";
                String formatted_suffix = "";
                String chatColor = ranks.size() > 0 ? ranks.get(0).getChatcolor() : "&f";
                String nameColor = ranks.size() > 0 ? ranks.get(0).getNamecolor() : "&f";
                String usertag = "";

                for (PRRank rank : ranks) {
                    formatted_prefix += rank.getPrefix() + " ";
                    formatted_suffix += rank.getSuffix() + " ";
                }

                if (formatted_prefix.endsWith(" ")) {
                    formatted_prefix = formatted_prefix.substring(0, formatted_prefix.length() - 1);
                }

                if (formatted_suffix.endsWith(" ")) {
                    formatted_suffix = formatted_suffix.substring(0, formatted_suffix.length() - 1);
                }

                if (formatted_prefix.replaceAll(" ", "").length() == 0) {
                    formatted_prefix = "";
                }

                if (formatted_suffix.replaceAll(" ", "").length() == 0) {
                    formatted_suffix = "";
                }

                PRPlayer targetPlayer = PRCache.getPlayer(player.getUniqueId().toString());
                Map<?, ?> availableUsertags = this.plugin.getUsertagStorage().getMap("usertags",
                        new HashMap<String, String>());
                Set<String> playerUsertags = targetPlayer.getUsertags();

                for (String playerUsertag : playerUsertags) {
                    String value = "";
                    for (Entry<?, ?> entry : availableUsertags.entrySet()) {
                        if (entry.getKey().toString().equalsIgnoreCase(playerUsertag)) {
                            value = entry.getValue().toString();
                        }
                    }

                    if (value.length() > 0) {
                        usertag += (usertag.length() > 0 ? " " : "") + value;
                    }
                }

                String playersChatMessage = event.getMessage();
                if (!player.hasPermission("powerranks.chat.chatcolor")) {
                    playersChatMessage = playersChatMessage.replaceAll("(&[0-9a-fA-FiIjJrRlLmMnNoO])|(#[0-9a-fA-F]{6})",
                            "");
                }

                String player_formatted_name = (nameColor.length() == 0 ? "&r" : nameColor) + player.getDisplayName();
                String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : chatColor) + playersChatMessage;

                format = PRUtil.powerFormatter(
                        format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
                                .put("suffix", formatted_suffix)
                                .put("usertag", usertag)
                                .put("player", player_formatted_name)
                                .put("msg",
                                        this.plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR,
                                                player_formatted_chat_msg, true, false, false))
                                .put("format", event.getFormat())
                                .put("world", player.level.getName())
                                .build(),
                        '[', ']');

                format = this.plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, format, true, false,
                        false);
                format = format.replaceAll("%", "%%");

                event.setFormat(format);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            event.setFormat("%1$s: %2$s");
        }
    }
}
