package nl.svenar.PowerRanks.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {

    private Scoreboard scoreboard;
    private HashMap<UUID, Integer> playerHighestWeight = new HashMap<UUID, Integer>();
    private int numRanks = -1;
    private int totalWeight = -1;
    private BukkitRunnable sortingTask, headerFooterTask;
    private ArrayList<String> headerLines = new ArrayList<String>();
    private ArrayList<String> footerLines = new ArrayList<String>();
    private HashMap<String, TablistAnimation> tablistAnimations = new HashMap<String, TablistAnimation>();
    private int verboseLogInterval = 0;

    public TablistManager() {
    }

    public void start() {
        PowerRanksVerbose.log("TablistSort", "Initializing");
        if (PowerRanks.getTablistConfigManager().getBool("sorting.enabled", true)) {
            setupSorting();
        }
        if (PowerRanks.getTablistConfigManager().getBool("header-footer.enabled", true)) {
            setupHeaderFooter();
        }

    }

    private void setupSorting() {
        sortingTask = new BukkitRunnable() {
            @Override
            public void run() {
                Instant startTime = Instant.now();

                if (scoreboard == null) {
                    try {
                        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            onPlayerJoin(player);
                        }
                    } catch (Exception e) {
                    }
                }

                if (scoreboard == null) {
                    return;
                }

                boolean doUpdateRanks = false;

                List<PRRank> sortedRanks = PRUtil.sortRanksByWeight(
                        CacheManager.getRanks());
                if (!PowerRanks.getTablistConfigManager().getBool("sorting.reverse", false)) {
                    Collections.reverse(sortedRanks);
                }

                if (numRanks != CacheManager.getRanks().size()) {
                    numRanks = CacheManager.getRanks().size();
                    doUpdateRanks = true;
                }

                int newTotalRankWeight = 0;
                for (PRRank rank : sortedRanks) {
                    newTotalRankWeight += rank.getWeight();
                }
                if (totalWeight != newTotalRankWeight) {
                    totalWeight = newTotalRankWeight;
                    doUpdateRanks = true;
                }

                if (doUpdateRanks) {
                    for (Team team : scoreboard.getTeams()) {
                        for (String entry : team.getEntries()) {
                            team.removeEntry(entry);
                        }
                        team.unregister();
                    }
                    for (int i = 0; i < sortedRanks.size(); i++) {
                        scoreboard.registerNewTeam(i + "-" + sortedRanks.get(i).getName());
                        PowerRanksVerbose.log("TablistSort",
                                "Creating new scoreboard team: " + i + "-" + sortedRanks.get(i).getName());
                    }
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    updateSorting(player);
                }

                PowerRanksVerbose.log("task",
                        "Running task sort tablist in " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
            }
        };
        sortingTask.runTaskTimer(
                PowerRanks.getInstance(),
                0,
                PowerRanks.getInstance().TASK_TPS
                        * PowerRanks.getTablistConfigManager().getInt("sorting.update-interval", 1));
    }

    @SuppressWarnings("unchecked")
    private void setupHeaderFooter() {
        for (String line : (ArrayList<String>) PowerRanks.getTablistConfigManager().getList("header-footer.header",
                new ArrayList<String>())) {
            headerLines.add(line);
        }

        for (String line : (ArrayList<String>) PowerRanks.getTablistConfigManager().getList("header-footer.footer",
                new ArrayList<String>())) {
            footerLines.add(line);
        }

        for (String animationName : PowerRanks.getTablistConfigManager().getKeys("header-footer.animations")) {
            TablistAnimation newAnimation = new TablistAnimation();
            newAnimation.setDelay(PowerRanks.getTablistConfigManager()
                    .getInt("header-footer.animations." + animationName + ".delay", 1));
            newAnimation.setFrames((ArrayList<String>) PowerRanks.getTablistConfigManager()
                    .getList("header-footer.animations." + animationName + ".frames", new ArrayList<String>()));
            tablistAnimations.put(animationName, newAnimation);
        }

        headerFooterTask = new BukkitRunnable() {
            @Override
            public void run() {
                Instant startTime = Instant.now();

                for (TablistAnimation animation : tablistAnimations.values()) {
                    animation.update();
                }

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    player.setPlayerListHeader(getHeader());
                    player.setPlayerListFooter(getFooter());
                }

                if (verboseLogInterval > 100) {
                    PowerRanksVerbose.log("task", "Running task header/footer tablist in "
                            + Duration.between(startTime, Instant.now()).toMillis() + "ms");
                    verboseLogInterval = 0;
                }
                verboseLogInterval++;
            }
        };
        headerFooterTask.runTaskTimer(
                PowerRanks.getInstance(),
                0,
                PowerRanks.getTablistConfigManager().getInt("header-footer.update-interval", 1));
    }

    private String formatAnimations(String line) {
        Pattern pattern = Pattern.compile("\\{.+:.+\\}");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            String symbol = line.substring(matcher.start(), matcher.end());
            String replacement = "";

            Matcher keyMatcher = Pattern.compile("\\{.+:").matcher(symbol);
            Matcher valueMatcher = Pattern.compile(":.+\\}").matcher(symbol);
            keyMatcher.find();
            valueMatcher.find();
            String key = symbol.substring(keyMatcher.start(), keyMatcher.end());
            String value = symbol.substring(valueMatcher.start(), valueMatcher.end());
            key = key.substring(1, key.length() - 1);
            value = value.substring(1, value.length() - 1);

            switch (key.toLowerCase()) {
                case "animation":
                    TablistAnimation animation = tablistAnimations.get(value);
                    if (animation != null) {
                        replacement = animation.getCurrentFrame();
                    }
                    break;

                default:
                    break;
            }

            line = line.replace(symbol, replacement);
            matcher = pattern.matcher(line);
        }

        return line;
    }

    private String getHeader() {
        String output = "";

        for (String line : headerLines) {
            output += line + "\n";
        }

        output = output.substring(0, output.length() - 1);
        output = formatAnimations(output);
        output = PowerRanks.chatColor(output, true);

        return output;
    }

    private String getFooter() {
        String output = "";

        for (String line : footerLines) {
            output += line + "\n";
        }

        output = output.substring(0, output.length() - 1);
        output = formatAnimations(output);
        output = PowerRanks.chatColor(output, true);

        return output;
    }

    public void stop() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.setPlayerListHeader(null);
            player.setPlayerListFooter(null);
        }

        headerLines = new ArrayList<String>();
        footerLines = new ArrayList<String>();
        tablistAnimations = new HashMap<String, TablistAnimation>();

        for (Entry<UUID, Integer> entry : playerHighestWeight.entrySet()) {
            entry.setValue(Integer.MIN_VALUE);
        }

        numRanks = -1;
        totalWeight = -1;

        PowerRanksVerbose.log("TablistSort", "Stopping");

        if (sortingTask != null) {
            sortingTask.cancel();
            sortingTask = null;
        }

        if (headerFooterTask != null) {
            headerFooterTask.cancel();
            headerFooterTask = null;
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
                player.getUniqueId().toString());
        int playerHighestRankWeight = Integer.MIN_VALUE;
        PRRank playerHighestRank = null;

        List<String> ranknames = new ArrayList<>();
        for (PRPlayerRank playerRank : targetPlayer.getRanks()) {
            ranknames.add(playerRank.getName());
        }

        for (String rankname : ranknames) {
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
                        if (team.getName().split("-")[1].equals(playerHighestRank.getName())) {
                            team.addEntry(player.getName());
                            PowerRanksVerbose.log("TablistSort",
                                    "Setting " + player.getName() + " to scoreboard team: " + team.getName());
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