package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.PowerSQLConfiguration;
import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.storage.provided.JSONStorageManager;
import nl.svenar.common.storage.provided.MySQLStorageManager;
import nl.svenar.common.storage.provided.PSMStorageManager;
import nl.svenar.common.storage.provided.SQLiteStorageManager;
import nl.svenar.common.storage.provided.YAMLStorageManager;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;

public class CacheManager {

    private static PowerStorageManager storageManager;

    private static ArrayList<PRRank> registeredRanks = new ArrayList<PRRank>();
    private static ArrayList<PRPlayer> registeredPlayers = new ArrayList<PRPlayer>();

    private static String tmpConvertDefaultRank = "";

    public static ArrayList<PRRank> getRanks() {
        return registeredRanks;
    }

    public static void setRanks(ArrayList<PRRank> ranks) {
        registeredRanks = ranks;
    }

    public static void addRank(PRRank rank) {
        if (Objects.isNull(registeredRanks)) {
            registeredRanks = new ArrayList<PRRank>();
        }

        registeredRanks.add(rank);
    }

    public static void removeRank(PRRank rank) {
        if (Objects.isNull(registeredRanks)) {
            registeredRanks = new ArrayList<PRRank>();
        }

        registeredRanks.remove(rank);
    }

    public static PRRank getRank(String name) {
        if (Objects.isNull(registeredRanks)) {
            registeredRanks = new ArrayList<PRRank>();
        }

        for (PRRank rank : registeredRanks) {
            if (rank.getName().equals(name)) {
                return rank;
            }
        }
        return null;
    }

    public static ArrayList<PRPlayer> getPlayers() {
        return registeredPlayers;
    }

    public static void setPlayers(ArrayList<PRPlayer> players) {
        registeredPlayers = players;
    }

    public static void addPlayer(PRPlayer player) {
        if (Objects.isNull(registeredPlayers)) {
            registeredPlayers = new ArrayList<PRPlayer>();
        }

        registeredPlayers.add(player);
    }

    public static PRPlayer getPlayer(String identifier) {
        try {
            if (Objects.isNull(registeredPlayers)) {
                registeredPlayers = new ArrayList<PRPlayer>();
            }

            for (PRPlayer player : registeredPlayers) {
                if (player.getName().equalsIgnoreCase(identifier) || player.getUUID().toString().replaceAll("-", "").equalsIgnoreCase(identifier.replaceAll("-", ""))) {
                    return player;
                }
            }

            Player player = null;

            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                if (onlinePlayer.getName().equals(identifier)
                        || onlinePlayer.getUniqueId().toString().replaceAll("-", "").equalsIgnoreCase(identifier.replaceAll("-", ""))) {
                    player = onlinePlayer;
                    break;
                }
            }

            if (Objects.isNull(player)) {
                return null;
            }

            return createPlayer(player);
        } catch (ConcurrentModificationException cme) {
            return null;
        }
    }

    public static PRPlayer createPlayer(Player player) {
        PRPlayer prPlayer = new PRPlayer();
        prPlayer.setUUID(player.getUniqueId());
        prPlayer.setName(player.getName());
        for (PRRank rank : CacheManager.getDefaultRanks()) {
            PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
            prPlayer.addRank(playerRank);
        }
        CacheManager.addPlayer(prPlayer);
        return prPlayer;
    }

    public static List<PRRank> getDefaultRanks() {
        List<PRRank> defaultRanks = new ArrayList<PRRank>();
        for (PRRank rank : getRanks()) {
            if (rank.isDefault()) {
                defaultRanks.add(rank);
            }
        }
        return defaultRanks;
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
                PowerSQLConfiguration configuration = new PowerSQLConfiguration(pcm.getString("storage.mysql.host", "127.0.0.1"),
                        pcm.getInt("storage.mysql.port", 3306), pcm.getString("storage.mysql.database", "powerranks"),
                        pcm.getString("storage.mysql.username", "username"), pcm.getString("storage.mysql.password", "password"),
                        pcm.getBool("storage.mysql.ssl", false), "ranks", "players", "messages");
                storageManager = new MySQLStorageManager(configuration, pcm.getBool("storage.mysql.verbose", false));
            } else { // Default to yaml

                PowerRanksAddon usedStorageManagerAddon = null;
                try {
                    PowerRanks pluginInstance = PowerRanks.getInstance();
                    for (Entry<File, PowerRanksAddon> prAddon : pluginInstance.addonsManager.addonClasses.entrySet()) {
                        List<String> addonStorageManager = prAddon.getValue().getStorageManagerNames();
                        if (Objects.nonNull(addonStorageManager)) {
                            for (String storageName : addonStorageManager) {
                                if (storageType.equals(storageName.toUpperCase())) {
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
                    PowerRanks.getInstance().getLogger()
                            .info("Using storage engine from add-on: " + usedStorageManagerAddon.getIdentifier());
                }
            }
        }

        storageManager.loadAll();

        registeredRanks = (ArrayList<PRRank>) storageManager.getRanks();
        registeredPlayers = (ArrayList<PRPlayer>) storageManager.getPlayers();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            OnJoin.handleJoin(PowerRanks.getInstance(), player);
        }

        if (tmpConvertDefaultRank.length() > 0) {
            getRank(tmpConvertDefaultRank).setDefault(true);
        }
    }

    public static void save() {
        storageManager.setRanks(registeredRanks);
        storageManager.setPlayers(registeredPlayers);

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
