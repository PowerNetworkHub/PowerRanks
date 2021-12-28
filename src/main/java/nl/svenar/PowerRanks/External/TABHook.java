package nl.svenar.PowerRanks.External;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

public class TABHook {

    private boolean isReady = false;

    public void setup() {
        new BukkitRunnable() {
			@Override
			public void run() {
				if (PowerRanks.getInstance().getServer().getPluginManager().isPluginEnabled("TAB")) {
                    load();
					this.cancel();
				}
			}
		}.runTaskTimer(PowerRanks.getInstance(), 20, 5);
        
    }
    
    private void load() {
        if (isReady) {
            return;
        }

        isReady = true;
        updateRanks();
        TabAPI.getInstance().debug("Hooked into PowerRanks");
    }

    public void updateRanks() {
        List<PRRank> sortedRanks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(CacheManager.getRanks()));
        List<String> sortingList = new ArrayList<String>();
        for (PRRank rank : sortedRanks) {
            sortingList.add(rank.getName());
        }

        TabAPI.getInstance().getConfig().set("primary-group-finding-list", sortingList);

        List<String> sortingTypes = TabAPI.getInstance().getConfig().getStringList("scoreboard-teams.sorting-types", new ArrayList<String>());
        List<String> newSortingTypes = new ArrayList<String>();
        for (String type : sortingTypes) {
            if (!type.split(":")[0].equals("GROUPS")) {
                newSortingTypes.add(type);
            } else {
                newSortingTypes.add("GROUPS:" + String.join(",", sortingList));
            }
        }
        TabAPI.getInstance().getConfig().set("scoreboard-teams.sorting-types", newSortingTypes);

        TabAPI.getInstance().getConfig().save();

        // for (Player player : Bukkit.getOnlinePlayers()) {
        //     TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());
        //     PowerRanks.getInstance().getLogger().warning("#" + tabPlayer.getName() + ": " + tabPlayer.getGroup() + " (" + tabPlayer.getTeamName() + ")");
        // }

        TabAPI.getInstance().getGroups().remove("Owner");
        TabAPI.getInstance().getGroups().remove("Admin");
        TabAPI.getInstance().getGroups().remove("Player");
        TabAPI.getInstance().getGroups().setProperty("_DEFAULT_.tabprefix", "", "", "", "a");
    }

    public void updatePlayer(Player player) {
        // String prefix = "";
        // String suffix = "";

        // List<PRRank> playerRanks = new ArrayList<PRRank>();
        // for (String rankname : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
        //     if (CacheManager.getRank(rankname) != null) {
        //         playerRanks.add(CacheManager.getRank(rankname));
        //     }
        // }
        // playerRanks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(playerRanks));

        // for (PRRank rank : playerRanks) {
        //     prefix += rank.getPrefix() + " ";
        //     prefix += rank.getSuffix() + " ";
        // }

        // if (prefix.endsWith(" ")) {
        //     prefix = prefix.substring(0, prefix.length() - 1);
        // }

        // if (suffix.endsWith(" ")) {
        //     suffix = suffix.substring(0, suffix.length() - 1);
        // }

        // if (prefix.replaceAll(" ", "").length() == 0) {
        //     prefix = "";
        // }

        // if (suffix.replaceAll(" ", "").length() == 0) {
        //     suffix = "";
        // }
        
        // TabAPI.getInstance().getTablistFormatManager().setPrefix(TabAPI.getInstance().getPlayer(player.getUniqueId()), prefix);
        // TabAPI.getInstance().getTablistFormatManager().setSuffix(TabAPI.getInstance().getPlayer(player.getUniqueId()), suffix);
    }
}
