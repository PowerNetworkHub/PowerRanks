package nl.svenar.powerranks.updater;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.BackupManager;

public class PRV1Converter {

    private PowerRanks plugin;

    public PRV1Converter(PowerRanks plugin) {
        this.plugin = plugin;
    }

    public boolean check() {
        return new File(plugin.getDataFolder() + File.separator + "Ranks", "Ranks.yml").exists();
    }

    public void convert() {
        Logger log = this.plugin.getLogger();

        log.info("===-----------------------------===");
        log.info("PowerRanks v1 data found!");
        log.info("Attempting to convert...");

        File ranksFile = new File(plugin.getDataFolder() + File.separator + "Ranks", "Ranks.yml");
        File playersFile = new File(plugin.getDataFolder() + File.separator + "Ranks", "Players.yml");

        log.info(ranksFile.getName() + " " + (ranksFile.exists() ? "found" : "missing! Ignoring"));
        log.info(playersFile.getName() + " " + (playersFile.exists() ? "found" : "missing! Ignoring"));

        if (ranksFile.exists()) {
            this.convertRanks(ranksFile, log);
            this.plugin.getDataHandler().saveRanks(BaseDataHandler.getRanks());
        }

        if (playersFile.exists()) {
            this.convertPlayers(playersFile, log);
            this.plugin.getDataHandler().savePlayers(BaseDataHandler.getPlayers());
        }

        log.info("Backing up PowerRanks v1 data...");
        new BackupManager().backupFile("v1", ranksFile);
        new BackupManager().backupFile("v1", playersFile);
        File v1DataDir = new File(this.plugin.getDataFolder() + File.separator + "Ranks");
        deleteDirectory(v1DataDir);
        log.info("Done!");
        log.info("===-----------------------------===");
        log.info("");
    }

    private void convertRanks(File ranksFile, Logger log) {
        YamlConfiguration ranksYAML = YamlConfiguration.loadConfiguration(ranksFile);

        List<PRRank> ranksToRemove = new ArrayList<PRRank>();
        for (Iterator<PRRank> iterator = BaseDataHandler.getRanks().iterator(); iterator.hasNext();) {
            PRRank rank = iterator.next();
            // if (!rank.getName().equalsIgnoreCase("default")) {
            ranksToRemove.add(rank);
            // } else {
            // rank.setPermissions(new ArrayList<PRPermission>());
            // }
        }

        for (PRRank rank : ranksToRemove) {
            BaseDataHandler.removeRank(rank);
        }

        log.info("Converting ranks...");
        String defaultRankname = ranksYAML.getString("Default");

        for (String rankname : ranksYAML.getConfigurationSection("Groups").getKeys(false)) {
            if (rankname.equalsIgnoreCase("null")) {
                continue;
            }

            try {
                if (ranksYAML.getStringList("Groups." + rankname + ".permissions") == null
                        || ranksYAML.getString("Groups." + rankname + ".chat.prefix") == null
                        || ranksYAML.getString("Groups." + rankname + ".chat.suffix") == null) {
                    throw new NullPointerException();
                }

                PRRank newRank = null;
                // if (!rankname.equals(defaultRankname)) {
                newRank = new PRRank(rankname);
                newRank.setWeight(1);
                newRank.setDefault(rankname.equals(defaultRankname));
                BaseDataHandler.addRank(newRank);
                // } else {
                // newRank = BaseDataHandler.getRank("default");
                // }

                for (String permission : ranksYAML.getStringList("Groups." + rankname + ".permissions")) {
                    boolean allowed = permission.startsWith("-") ? false : true;
                    String permissionNode = permission.startsWith("-") ? permission.replaceFirst("-", "") : permission;

                    PRPermission newPermission = new PRPermission(permissionNode);
                    newPermission.setAllowed(allowed);
                    newRank.addPermission(newPermission);
                }

                newRank.setPrefix(ranksYAML.getString("Groups." + rankname + ".chat.prefix"));
                newRank.setSuffix(ranksYAML.getString("Groups." + rankname + ".chat.suffix"));
            } catch (Exception e) {
                log.info("Ignoring invalid rank '" + rankname + "'");
            }
        }

        for (String rankname : ranksYAML.getConfigurationSection("Groups").getKeys(false)) {
            PRRank rank = BaseDataHandler.getRank(rankname);
            if (rank != null) {
                if (ranksYAML.getStringList("Groups." + rankname + ".inheritances") == null) {
                    for (String inheritancename : ranksYAML.getStringList("Groups." + rankname + ".inheritances")) {
                        PRRank inheritancerank = BaseDataHandler.getRank(inheritancename);
                        if (inheritancerank != null) {
                            rank.addInheritedRank(inheritancename);
                        }
                    }
                }
            }
        }
        log.info("Done converting " + ranksYAML.getConfigurationSection("Groups").getKeys(false).size() + " ranks!");
    }

    private void convertPlayers(File playersFile, Logger log) {
        YamlConfiguration playersYAML = YamlConfiguration.loadConfiguration(playersFile);

        List<PRPlayer> playersToRemove = new ArrayList<PRPlayer>();
        for (Iterator<PRPlayer> iterator = BaseDataHandler.getPlayers().iterator(); iterator.hasNext();) {
            PRPlayer player = iterator.next();
            playersToRemove.add(player);
        }

        for (PRPlayer player : playersToRemove) {
            BaseDataHandler.removePlayer(player);
        }

        log.info("Converting player...");
        for (String playeruuid : playersYAML.getConfigurationSection("players").getKeys(false)) {
            try {
                if (playersYAML.getStringList("players." + playeruuid + ".permissions") == null
                        || playersYAML.getString("players." + playeruuid + ".name") == null
                        || playersYAML.getString("players." + playeruuid + ".rank") == null
                        || playersYAML.getStringList("players." + playeruuid + ".subranks") == null) {
                    throw new NullPointerException();
                }
                String playername = playersYAML.getString("players." + playeruuid + ".name");
                PRPlayer newPlayer = new PRPlayer();
                newPlayer.setUuid(UUID.fromString(playeruuid));
                newPlayer.setName(playername);
                newPlayer.addRank(BaseDataHandler.getRank(playersYAML.getString("players." + playeruuid + ".rank")));
                for (String subrankname : playersYAML.getStringList("players." + playeruuid + ".subranks")) {
                    PRRank subrank = BaseDataHandler.getRank(subrankname);
                    if (subrank != null) {
                        newPlayer.addRank(subrank);
                    }
                }

                BaseDataHandler.addPlayer(newPlayer);

            } catch (Exception e) {
                log.info("Ignoring invalid player '" + playeruuid + "'");
            }
        }
        log.info("Done converting " + playersYAML.getConfigurationSection("players").getKeys(false).size()
                + " players!");
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
