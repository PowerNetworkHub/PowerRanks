package nl.svenar.powerranks.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.ErrorManager;

public class YAMLDataHandler extends BaseDataHandler {

    private File ranksFile, playersFile;
    private YamlConfiguration ranksConfiguration, playersConfiguration;

    @Override
    public void setup(PowerRanks plugin) {
        super.setup(plugin);

        this.ranksFile = new File(plugin.getDataFolder(), "ranks.yml");
        this.playersFile = new File(plugin.getDataFolder(), "players.yml");

        if (!this.ranksFile.exists()) {
            createAndWriteFile(this.ranksFile, "ranks: {}");
        }

        if (!this.playersFile.exists()) {
            createAndWriteFile(this.playersFile, "players: {}");
        }

        this.ranksConfiguration = new YamlConfiguration();
        this.playersConfiguration = new YamlConfiguration();
        try {
            this.ranksConfiguration.load(this.ranksFile);
            this.playersConfiguration.load(this.playersFile);
        } catch (IOException | InvalidConfigurationException e) {
            ErrorManager.logError("Failed to load YAML file. Check the error below for more information.");
            e.printStackTrace();
        }

        // if (!this.ranksConfiguration.contains("ranks.default")) {
        //     PRRank defaultRank = new PRRank("default");
        //     saveRank(defaultRank, true);
        // }
    }

    private void createAndWriteFile(File file, String line) {
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(line);
            fileWriter.close();
        } catch (IOException e) {
            ErrorManager.logError(
                    "Failed to create file '" + file.getName() + "'. Check the error below for more information.");
            e.printStackTrace();
        }
    }

    @Override
    public Collection<PRRank> loadRanks() {
        ArrayList<PRRank> ranks = new ArrayList<PRRank>();

        if (this.ranksConfiguration.contains("ranks")) {
            for (String key : this.ranksConfiguration.getConfigurationSection("ranks").getKeys(false)) {
                PRRank rank = new PRRank(key);

                if (this.ranksConfiguration.contains("ranks." + key + ".default")) {
                    if (this.ranksConfiguration.isBoolean("ranks." + key + ".default")) {
                        rank.setDefault(this.ranksConfiguration.getBoolean("ranks." + key + ".default"));
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid default value and will not be used.");
                    }
                }

                if (this.ranksConfiguration.contains("ranks." + key + ".weight")) {
                    if (this.ranksConfiguration.isInt("ranks." + key + ".weight")) {
                        rank.setWeight(this.ranksConfiguration.getInt("ranks." + key + ".weight"));
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid weight and will not be used.");
                    }
                }

                if (this.ranksConfiguration.contains("ranks." + key + ".prefix")) {
                    if (this.ranksConfiguration.isString("ranks." + key + ".prefix")) {
                        rank.setPrefix(this.ranksConfiguration.getString("ranks." + key + ".prefix"));
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid prefix and will not be used.");
                    }
                }

                if (this.ranksConfiguration.contains("ranks." + key + ".suffix")) {
                    if (this.ranksConfiguration.isString("ranks." + key + ".suffix")) {
                        rank.setSuffix(this.ranksConfiguration.getString("ranks." + key + ".suffix"));
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid suffix and will not be used.");
                    }
                }

                if (this.ranksConfiguration.contains("ranks." + key + ".inheritances")) {
                    if (this.ranksConfiguration.isList("ranks." + key + ".inheritances")) {
                        for (String inheritance : this.ranksConfiguration
                                .getStringList("ranks." + key + ".inheritances")) {
                            rank.addInheritedRank(inheritance);
                        }
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid inheritance list and will not be used.");
                    }
                }

                if (this.ranksConfiguration.contains("ranks." + key + ".permissions")) {
                    if (this.ranksConfiguration.isList("ranks." + key + ".permissions")) {
                        for (Object node : this.ranksConfiguration.getList("ranks." + key + ".permissions")) {
                            PRPermission permission = YAMLtoPRPermission(node);
                            if (permission != null) {
                                rank.addPermission(permission);
                            }
                        }
                    } else {
                        ErrorManager.logWarning(
                                "Rank '" + rank.getName() + "' has an invalid permissions list and will not be used.");
                    }
                }

                ranks.add(rank);
            }
        } else {
            ErrorManager.logError(this.ranksFile.getName() + " is invalid!");
        }

        return ranks;
    }

    @Override
    public Collection<PRPlayer> loadPlayers() {
        ArrayList<PRPlayer> players = new ArrayList<PRPlayer>();

        if (this.playersConfiguration.contains("players")) {
            for (String uuid : this.playersConfiguration.getConfigurationSection("players").getKeys(false)) {
                PRPlayer player = new PRPlayer();
                player.setUuid(PRPlayer.stringToUUID(uuid));

                if (this.playersConfiguration.contains("players." + uuid + ".name")) {
                    if (this.playersConfiguration.isString("players." + uuid + ".name")) {
                        player.setName(this.playersConfiguration.getString("players." + uuid + ".name"));
                    } else {
                        ErrorManager.logWarning("Player '" + player.getName() + "'(" + player.getUuid().toString()
                                + ") has an invalid name format.");
                    }
                }

                if (this.playersConfiguration.contains("players." + uuid + ".ranks")) {
                    if (this.playersConfiguration.isList("players." + uuid + ".ranks")) {
                        for (String rankName : this.playersConfiguration.getStringList("players." + uuid + ".ranks")) {
                            PRRank rank = BaseDataHandler.getRank(rankName);
                            if (rank != null) {
                                player.addRank(rank);
                            } else {
                                ErrorManager.logWarning("Player '" + player.getName() + "' ("
                                        + player.getUuid().toString() + ") has a non-existing rank: " + rankName);
                            }
                        }
                    } else {
                        ErrorManager.logWarning("Player '" + player.getName() + "'(" + player.getUuid().toString()
                                + ") has an invalid ranks list and will not be used.");
                    }
                }

                if (this.playersConfiguration.contains("players." + uuid + ".permissions")) {
                    if (this.playersConfiguration.isList("players." + uuid + ".permissions")) {
                        for (Object node : this.playersConfiguration.getList("players." + uuid + ".permissions")) {
                            PRPermission permission = YAMLtoPRPermission(node);
                            if (permission != null) {
                                player.addPermission(permission);
                            }
                        }
                    } else {
                        ErrorManager.logWarning("Player '" + player.getName() + "'(" + player.getUuid().toString()
                                + ") has an invalid permission list and will not be used.");
                    }
                }

                players.add(player);
            }
        } else {
            ErrorManager.logError(this.playersFile.getName() + " is invalid!");
        }

        return players;
    }

    @Override
    public void saveRanks(Collection<PRRank> ranks) {

        for (String key : this.ranksConfiguration.getConfigurationSection("ranks").getKeys(false)) {
            this.ranksConfiguration.set("ranks." + key, null);
        }

        for (PRRank rank : ranks) {
            saveRank(rank, false);
        }

        try {
            this.ranksConfiguration.save(this.ranksFile);
        } catch (IOException e) {
            ErrorManager.logError("Failed to write config: " + this.ranksFile.getName());
        }
    }

    @Override
    public void savePlayers(Collection<PRPlayer> players) {
        for (PRPlayer player : players) {
            savePlayer(player, false);
        }

        try {
            this.playersConfiguration.save(this.playersFile);
        } catch (IOException e) {
            ErrorManager.logError("Failed to write config: " + this.playersFile.getName());
        }
    }

    @Override
    public void saveRank(PRRank rank) {
        saveRank(rank, true);
    }

    @Override
    public void savePlayer(PRPlayer player) {
        savePlayer(player, true);
    }

    public void saveRank(PRRank rank, boolean writeToFile) {

        Object rankToWrite = prRankToYAML(rank);

        this.ranksConfiguration.set("ranks." + rank.getName(), rankToWrite);

        if (writeToFile) {
            try {
                this.ranksConfiguration.save(this.ranksFile);
            } catch (IOException e) {
                ErrorManager.logError("Failed to write config: " + this.ranksFile.getName());
            }
        }
    }

    public void savePlayer(PRPlayer player, boolean writeToFile) {
        Object playerToWrite = prPlayerToYAML(player);

        this.playersConfiguration.set("players." + player.getUuid(), playerToWrite);

        if (writeToFile) {
            try {
                this.playersConfiguration.save(this.playersFile);
            } catch (IOException e) {
                ErrorManager.logError("Failed to write config: " + this.playersFile.getName());
            }
        }
    }

    private Object prRankToYAML(PRRank rank) {
        Object output = null;

        ConfigurationSection rankSection = this.ranksConfiguration.createSection("ranks." + rank.getName());

        ArrayList<Object> permissions = new ArrayList<Object>();
        for (PRPermission permission : rank.getPermissions()) {
            permissions.add(prPermissionToYAML(permission));
        }

        ArrayList<String> inheritances = new ArrayList<String>();
        for (String inheritance : rank.getInheritedRanks()) {
            inheritances.add(inheritance);
        }

        if (rank.getDefault()) {
            rankSection.set("default", rank.getDefault());
        }
        if (rank.getWeight() > 0) {
            rankSection.set("weight", rank.getWeight());
        }
        if (rank.getPrefix().length() > 0) {
            rankSection.set("prefix", rank.getPrefix());
        }
        if (rank.getSuffix().length() > 0) {
            rankSection.set("suffix", rank.getSuffix());
        }
        if (permissions.size() > 0) {
            rankSection.set("permissions", permissions);
        }
        if (inheritances.size() > 0) {
            rankSection.set("inheritances", inheritances);
        }

        output = rankSection;

        return output;
    }

    private Object prPlayerToYAML(PRPlayer player) {
        Object output = null;

        ConfigurationSection playerSection = this.playersConfiguration.createSection("players." + player.getUuid());

        ArrayList<String> ranks = new ArrayList<String>();
        for (PRRank rank : player.getRanks()) {
            ranks.add(rank.getName());
        }

        ArrayList<Object> permissions = new ArrayList<Object>();
        for (PRPermission permission : player.getPermissions()) {
            permissions.add(prPermissionToYAML(permission));
        }

        if (player.getName().length() > 0) {
            playerSection.set("name", player.getName());
        }

        if (ranks.size() > 0) {
            playerSection.set("ranks", ranks);
        }

        if (permissions.size() > 0) {
            playerSection.set("permissions", permissions);
        }

        output = playerSection;

        return output;
    }

    private Object prPermissionToYAML(PRPermission permission) {
        Object output = null;

        if (permission.isAllowed(null) && permission.getWorlds().size() == 0) {
            output = permission.getName();
        } else {
            Map<String, Object> permissionMap = new LinkedHashMap<String, Object>();
            Map<String, Object> options = new LinkedHashMap<String, Object>();
            ArrayList<String> worlds = new ArrayList<String>();
            for (String world : permission.getWorlds()) {
                worlds.add(world);
            }
            if (!permission.isAllowed(null)) {
                options.put("allowed", permission.isAllowed(null));
            }
            if (worlds.size() > 0) {
                options.put("worlds", worlds);
            }
            permissionMap.put(permission.getName(), options);
            output = permissionMap;
        }
        return output;
    }

    private PRPermission YAMLtoPRPermission(Object node) {
        PRPermission permission = null;
        if (node instanceof String) {
            permission = new PRPermission((String) node);
            permission.setAllowed(true);

        } else if (node instanceof LinkedHashMap) {
            Map<?, ?> nodeData = (LinkedHashMap<?, ?>) node;
            for (Entry<?, ?> entry : nodeData.entrySet()) {
                if (entry.getKey() instanceof String) {
                    permission = new PRPermission((String) entry.getKey());

                    if (entry.getValue() instanceof LinkedHashMap) {
                        Map<?, ?> permData = (LinkedHashMap<?, ?>) entry.getValue();
                        for (Entry<?, ?> entry1 : permData.entrySet()) {

                            if (entry1.getKey() instanceof String) {
                                if (((String) entry1.getKey()).equals("allowed")
                                        && entry1.getValue() instanceof Boolean) {
                                    permission.setAllowed((Boolean) entry1.getValue());
                                } else if (((String) entry1.getKey()).equals("worlds")
                                        && entry1.getValue() instanceof ArrayList) {
                                    for (Object worldName : (ArrayList<?>) entry1.getValue()) {
                                        if (worldName instanceof String) {
                                            permission.addWorld((String) worldName);
                                        } else {
                                            ErrorManager.logWarning("Permission '" + node + "' has an invalid world '"
                                                    + worldName + "' and will not be used.");
                                        }
                                    }
                                } else {
                                    ErrorManager.logWarning("Unknown permission option found '" + entry1.getKey()
                                            + "' and will be ignored.");
                                }
                            } else {
                                ErrorManager.logWarning("Invalid permission option found '" + entry1.getKey()
                                        + "' and will not be used.");
                            }
                        }
                    }
                } else {
                    ErrorManager.logWarning("Invalid permission found '" + entry.getKey() + "' and will not be used.");
                }
            }
        } else {
            ErrorManager.logWarning("Permission '" + node + "' has an invalid format and will not be used.");
        }

        return permission;
    }
}
