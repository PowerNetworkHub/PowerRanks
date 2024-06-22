package nl.svenar.powerranks.bukkit.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.storage.PowerConfigManager;
import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.storage.provided.MySQLStorageManager;
import nl.svenar.powerranks.common.storage.provided.PSMStorageManager;
import nl.svenar.powerranks.common.storage.provided.SQLiteStorageManager;
import nl.svenar.powerranks.common.storage.provided.YAMLStorageManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.events.OnJoin;

public class CacheManager {

    private static PowerStorageManager storageManager;

    private static String tmpConvertDefaultRank = "";

    public static List<PRRank> getRanks() {
        return PRCache.getRanks();
    }

    public static void setRanks(ArrayList<PRRank> ranks) {
        PRCache.setRanks(ranks);
    }

    public static void addRank(PRRank rank) {
        PRCache.addRank(rank);
    }

    public static void removeRank(PRRank rank) {
        PRCache.removeRank(rank);
    }

    public static PRRank getRank(String name) {
        return PRCache.getRank(name);
    }

    public static List<PRPlayer> getPlayers() {
        return PRCache.getPlayers();
    }

    public static void setPlayers(ArrayList<PRPlayer> players) {
        PRCache.setPlayers(players);
    }

    public static void addPlayer(PRPlayer player) {
        PRCache.addPlayer(player);
    }

    public static PRPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId().toString());
    }

    public static PRPlayer getPlayer(String identifier) {
        return PRCache.getPlayer(identifier);
    }

    public static PRPlayer createPlayer(Player player) {
        return PRCache.createPlayer(player.getName(), player.getUniqueId());
    }

    public static Set<PRRank> getDefaultRanks() {
        return PRCache.getDefaultRanks();
    }

    public static void load(String dataDirectory) {
        String storageType = PowerRanks.getConfigManager().getString("storage.type", "yaml").toUpperCase();
        if (storageManager == null) {
            if (storageType.equals("YML") || storageType.equals("YAML")) {
                storageManager = new YAMLStorageManager(dataDirectory, "ranks.yml", "players.yml");
            } else if (storageType.equals("JSON")) {
                storageManager = new JSONStorageManager(dataDirectory, "ranks.json", "players.json");
            } else if (storageType.equals("PSM")) {
                storageManager = new PSMStorageManager(dataDirectory, "ranks.psm", "players.psm");
            } else if (storageType.equals("SQLITE")) {
                storageManager = new SQLiteStorageManager(dataDirectory, "ranks.db", "players.db");
            } else if (storageType.equals("MYSQL")) {
                PowerConfigManager pcm = PowerRanks.getConfigManager();
                PowerSQLConfiguration configuration = new PowerSQLConfiguration();

                configuration.setHost(pcm.getString("storage.mysql.host", "127.0.0.1"));
                configuration.setPort(pcm.getInt("storage.mysql.port", 3306));
                configuration.setDatabase(pcm.getString("storage.mysql.database", "powerranks"));
                configuration.setUsername(pcm.getString("storage.mysql.username", "username"));
                configuration.setPassword(pcm.getString("storage.mysql.password", "password"));
                configuration.setUseSSL(pcm.getBool("storage.mysql.ssl", false));
                configuration.setTableRanks("ranks");
                configuration.setTablePlayers("players");
                configuration.setTableMessages("messages");
                configuration.setSilentErrors(pcm.getBool("storage.mysql.verbose", false));
                storageManager = new MySQLStorageManager(configuration);
            } else { // Default to yaml

                PowerRanksAddon usedStorageManagerAddon = null;
                try {
                    PowerRanks pluginInstance = PowerRanks.getInstance();
                    for (Entry<File, PowerRanksAddon> prAddon : pluginInstance.addonsManager.addonClasses.entrySet()) {
                        List<String> addonStorageManager = prAddon.getValue().getStorageManagerNames();
                        if (Objects.nonNull(addonStorageManager)) {
                            for (String storageName : addonStorageManager) {
                                if (storageType.equalsIgnoreCase(storageName)) {
                                    usedStorageManagerAddon = prAddon.getValue();
                                    usedStorageManagerAddon.setupStorageManager(storageName);
                                    storageManager = usedStorageManagerAddon.getStorageManager(storageName);
                                    break;
                                }
                            }
                        }

                        if (Objects.nonNull(storageManager)) {
                            break;
                        }

                    }
                } catch (Exception ex) {
                }

                if (Objects.isNull(storageManager)) {
                    PowerRanks.getInstance().getLogger().warning("Unknown storage method configured! Falling back to YAML");
                    storageManager = new YAMLStorageManager(dataDirectory, "ranks.yml", "players.yml");
                } else {
                    if (usedStorageManagerAddon != null) {
                        PowerRanks.getInstance().getLogger()
                                .info("Using storage engine from add-on: " + usedStorageManagerAddon.getIdentifier());
                    }
                }
            }
        }

        storageManager.loadAll();

        PRCache.setRanks((ArrayList<PRRank>) storageManager.getRanks());
        PRCache.setPlayers((ArrayList<PRPlayer>) storageManager.getPlayers());

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            OnJoin.handleJoin(PowerRanks.getInstance(), player);
        }

        if (tmpConvertDefaultRank.length() > 0) {
            getRank(tmpConvertDefaultRank).setDefault(true);
        }
    }

    public static void save() {
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());

        storageManager.saveAll();
    }

    public static PowerStorageManager getStorageManager() {
        return storageManager;
    }

    public static void configConverterSetDefaultRank(String rankname) {
        tmpConvertDefaultRank = rankname;
    }

    // Bungeecord methods

    public static void postDBMessage(UUID uuid, String key, String message) {
        if (!(getStorageManager() instanceof MySQLStorageManager)) {
            return;
        }
        MySQLStorageManager localStorageManager = (MySQLStorageManager) getStorageManager();
        localStorageManager.SQLInsert(localStorageManager.getConfig().getDatabase(), localStorageManager.getConfig().getTableMessages(),
                uuid.toString() + "." + key, message);
    }

    public static Map<String, String> getDBBroadcastMessages() {
        return getDBMessages(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public static Map<String, String> getDBMessages(UUID uuid) {
        if (!(getStorageManager() instanceof MySQLStorageManager)) {
            return null;
        }
        MySQLStorageManager localStorageManager = (MySQLStorageManager) getStorageManager();

        Map<String, String> messages = localStorageManager.selectSimiliarInTable(localStorageManager.getConfig().getDatabase(),
                localStorageManager.getConfig().getTableMessages(), uuid.toString());
        return messages;
    }

    public static void removeDBMessage(UUID uuid, String key) {
        if (!(getStorageManager() instanceof MySQLStorageManager)) {
            return;
        }
        MySQLStorageManager localStorageManager = (MySQLStorageManager) getStorageManager();
        localStorageManager.deleteKeyInTable(localStorageManager.getConfig().getDatabase(),
                localStorageManager.getConfig().getTableMessages(), uuid.toString() + "." + key);
    }
}
