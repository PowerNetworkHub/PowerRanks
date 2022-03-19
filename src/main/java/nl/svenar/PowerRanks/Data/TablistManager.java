package nl.svenar.PowerRanks.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {

    private Scoreboard scoreboard;
    private HashMap <UUID, Integer> playerHighestWeight = new HashMap <UUID, Integer>();
    private int numRanks = -1;
    private int totalWeight = -1;
    private BukkitRunnable runnable;

    public TablistManager() {}

    public void start() {
        PowerRanksVerbose.log("TablistSort", "Initializing");
        if (!PowerRanks.getConfigManager().getBool("tablist_modification.sorting.enabled", true)) {
            return;
        }

        runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    Instant startTime = Instant.now();

                    if (scoreboard == null) {
                        try {
                            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                            for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                                onPlayerJoin(player);
                            }
                        } catch (Exception e) {}
                    }

                    if (scoreboard == null) {
                        return;
                    }

                    boolean doUpdateRanks = false;

                    List <PRRank> sortedRanks = PRUtil.sortRanksByWeight(
                        CacheManager.getRanks()
                    );
                    if (!PowerRanks.getConfigManager().getBool("tablist_modification.sorting.reverse", false)) {
                        Collections.reverse(sortedRanks);
                    }

                    if (numRanks != CacheManager.getRanks().size()) {
                        numRanks = CacheManager.getRanks().size();
                        doUpdateRanks = true;
                    }

                    int newTotalRankWeight = 0;
                    for (PRRank rank: sortedRanks) {
                        newTotalRankWeight += rank.getWeight();
                    }
                    if (totalWeight != newTotalRankWeight) {
                        totalWeight = newTotalRankWeight;
                        doUpdateRanks = true;
                    }

                    if (doUpdateRanks) {
                        for (Team team : scoreboard.getTeams()) {
                            for (String entry: team.getEntries()) {
                                team.removeEntry(entry);
                            }
                            team.unregister();
                        }
                        for (int i = 0; i < sortedRanks.size(); i++) {
                            scoreboard.registerNewTeam(i + "-" + sortedRanks.get(i).getName());
                            PowerRanksVerbose.log("TablistSort", "Creating new scoreboard team: " + i + "-" + sortedRanks.get(i).getName());
                        }
                    }

                    for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                        updateSorting(player);
                    }

                    PowerRanksVerbose.log("task", "Running task sort tablist in " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                }
            };
            runnable.runTaskTimer(
                PowerRanks.getInstance(),
                0,
                PowerRanks.getInstance().TASK_TPS * PowerRanks.getConfigManager().getInt("tablist_modification.sorting.update-interval", 5)
            );
            
    }

    public void stop() {
        // if (!PowerRanks.getConfigManager().getBool("tablist_modification.sorting.enabled", false)) {
        //     return;
        // }

        for (Entry<UUID,Integer> entry : playerHighestWeight.entrySet()) {
            entry.setValue(Integer.MIN_VALUE);
        }
        numRanks = -1;
        totalWeight = -1;

        PowerRanksVerbose.log("TablistSort", "Stopping");

        if (runnable != null) {
            runnable.cancel();
            runnable = null;
        }

        if (scoreboard != null) {
            for (Team team : scoreboard.getTeams()) {
                for (String entry : team.getEntries()) {
                    team.removeEntry(entry);
                }
                team.unregister();
            }

            scoreboard = null;
        }
    }

    public void updateSorting(Player player) {
        if (!playerHighestWeight.containsKey(player.getUniqueId())) {
            playerHighestWeight.put(player.getUniqueId(), Integer.MIN_VALUE);
        }

        PRPlayer targetPlayer = CacheManager.getPlayer(
            player.getUniqueId().toString()
        );
        int playerHighestRankWeight = Integer.MIN_VALUE;
        PRRank playerHighestRank = null;
        for (String rankname: targetPlayer.getRanks()) {
            PRRank rank = CacheManager.getRank(rankname);
            if (rank.getWeight() > playerHighestRankWeight) {
                playerHighestRankWeight = rank.getWeight();
                playerHighestRank = rank;
            }
        }
        if (playerHighestWeight.get(player.getUniqueId()) != playerHighestRankWeight) {
            playerHighestWeight.put(player.getUniqueId(), playerHighestRankWeight);
            if (playerHighestRank != null) {
                for (Team team : scoreboard.getTeams()) {
                    if (team.getName().contains("-")) {
                        if (
                            team.getName().split("-")[1].equals(playerHighestRank.getName())
                        ) {
                            team.addEntry(player.getName());
                            PowerRanksVerbose.log("TablistSort", "Setting " + player.getName() + " to scoreboard team: " + team.getName());
                        } else {
                            team.removeEntry(player.getName());
                        }
                    }
                }

                PowerRanks.getInstance().updateTablistName(player);
            }
        }
    }

    public void onPlayerJoin(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    public void onPlayerLeave(Player player) {
        // Nothing to do here... for now
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
}