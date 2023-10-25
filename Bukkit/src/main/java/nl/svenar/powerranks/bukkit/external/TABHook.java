package nl.svenar.powerranks.bukkit.external;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import me.neznamy.tab.api.TabAPI;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.util.PluginReloader;

public class TABHook {

    private boolean isReady = false;
    private List<PRRank> sortedRanks = new ArrayList<PRRank>();
    private PRTABFeature prTabFeature = null;

    public void setup() {
        prTabFeature = new PRTABFeature("powerranks", "");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (PowerRanks.getInstance().getServer().getPluginManager().isPluginEnabled("TAB")) {
                    TabAPI.getInstance().getFeatureManager().registerFeature("powerranks",
                            new PRTABFeature("powerranks", ""));
                    load();
                    this.cancel();
                }
            }
        }.runTaskTimer(PowerRanks.getInstance(), 5, 1);

        new BukkitRunnable() {
            List<PRRank> ranks = new ArrayList<PRRank>(CacheManager.getRanks());

            @Override
            public void run() {
                if (isReady) {
                    // PowerRanks.getInstance().getLogger().warning(CacheManager.getRanks().size() +
                    // " - " + ranks.size() + " | " +
                    // TabAPI.getInstance().getFeatureManager().isFeatureEnabled("powerranks"));

                    if (CacheManager.getRanks().size() != ranks.size()) {
                        ranks = new ArrayList<PRRank>(CacheManager.getRanks());
                        // updateRanks();
                        isReady = false;
                        load();
                        // this.cancel();
                        // setup();
                    }

                    // if (!TabAPI.getInstance().getFeatureManager().isFeatureEnabled("powerranks"))
                    // {
                    // TabAPI.getInstance().getFeatureManager().registerFeature("powerranks", new
                    // PRTABFeature("powerranks", ""));
                    // }
                }
            }
        }.runTaskTimer(PowerRanks.getInstance(), 20 * 5, 20 * 5);
    }

    private void load() {
        if (isReady) {
            return;
        }

        if (!updateRanks()) {
            isReady = true;
            // try {
            // TabAPI.getInstance().getFeatureManager().registerFeature("powerranks", new
            // PRTABFeature("powerranks", ""));
            // } catch (Exception e) {}
            TabAPI.getInstance().debug("Hooked into PowerRanks");
        }
    }

    public boolean updateRanks() {
        sortedRanks = getSortedRanks();

        boolean doRestart = false;

        List<String> sortingList = new ArrayList<String>();
        for (PRRank rank : sortedRanks) {
            sortingList.add(rank.getName());
        }

        List<String> sortingTypes = TabAPI.getInstance().getConfig().getStringList("scoreboard-teams.sorting-types",
                new ArrayList<String>());
        List<String> newSortingTypes = new ArrayList<String>();

        newSortingTypes.add("GROUPS:" + String.join(",", sortingList));

        for (String type : sortingTypes) {
            if (!type.split(":")[0].equals("GROUPS")) {
                newSortingTypes.add(type);
            } else {

                // PowerRanks.getInstance().getLogger().warning(type.split(":")[1] + " - " +
                // String.join(",", sortingList));

                if (String.join(",", sortingList).length() != type.split(":")[1].length()) {
                    // doRestart = true; // Restart the plugin
                }
            }
        }
        TabAPI.getInstance().getConfig().set("scoreboard-teams.sorting-types", newSortingTypes);

        TabAPI.getInstance().getConfig().save();

        if (doRestart) {
            TabAPI.getInstance().getFeatureManager().unregisterFeature("powerranks");
            PluginReloader pluginReloader = new PluginReloader();
            TabAPI.getInstance().debug("[PR] Restarting TAB...");
            pluginReloader.reload("TAB");
            TabAPI.getInstance().debug("[PR] Restarted TAB.");
            TabAPI.getInstance().getFeatureManager().registerFeature("powerranks", prTabFeature);
            load();
        }

        return doRestart;

    }

    private List<PRRank> getSortedRanks() {
        List<PRRank> ranks = CacheManager.getRanks();
        PRUtil.sortRanksByWeight(ranks);
        PRUtil.reverseRanks(ranks);
        return ranks;
    }

    static List<PRRank> getSortedRanks(PRPlayer prPlayer) {
        List<PRRank> playerRanks = new ArrayList<PRRank>();
        for (PRPlayerRank rank : prPlayer.getRanks()) {
            if (CacheManager.getRank(rank.getName()) != null) {
                playerRanks.add(CacheManager.getRank(rank.getName()));
            }
        }

        PRUtil.sortRanksByWeight(playerRanks);
        PRUtil.reverseRanks(playerRanks);
        return playerRanks;
    }
}
