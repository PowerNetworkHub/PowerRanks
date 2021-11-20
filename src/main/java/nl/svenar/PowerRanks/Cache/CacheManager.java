package nl.svenar.PowerRanks.Cache;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.PowerSQLConfiguration;
import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.storage.provided.JSONStorageManager;
import nl.svenar.common.storage.provided.MySQLStorageManager;
import nl.svenar.common.storage.provided.PSMStorageManager;
import nl.svenar.common.storage.provided.SQLiteStorageManager;
import nl.svenar.common.storage.provided.YAMLStorageManager;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;

public class CacheManager {

    private static PowerStorageManager storageManager;

    private static ArrayList<PRRank> registeredRanks = new ArrayList<PRRank>();
    private static ArrayList<PRPlayer> registeredPlayers = new ArrayList<PRPlayer>();

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

    public static PRPlayer getPlayer(String name) {
        if (Objects.isNull(registeredPlayers)) {
            registeredPlayers = new ArrayList<PRPlayer>();
        }

        for (PRPlayer player : registeredPlayers) {
            if (player.getName().equals(name) || player.getUUID().toString().equals(name)) {
                return player;
            }
        }

        Player player = null;

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getName().equals(name) || onlinePlayer.getUniqueId().toString().equals(name)) {
                player = onlinePlayer;
                break;
            }
        }

        if (Objects.isNull(player)) {
            return null;
        }

        return createPlayer(player);
    }

    public static PRPlayer createPlayer(Player player) {
        PRPlayer prPlayer = new PRPlayer();
        prPlayer.setUUID(player.getUniqueId());
        prPlayer.setName(player.getName());
        prPlayer.setRank(CacheManager.getDefaultRank());
        CacheManager.addPlayer(prPlayer);
        return prPlayer;
    }

    public static String getDefaultRank() {
        return PowerRanks.getConfigManager().getString("general.defaultrank", "Member");
    }

    public static void setDefaultRank(String rankname) {
        PowerRanks.getConfigManager().setString("general.defaultrank", rankname);
    }

    public static void load(String dataDirectory) {
        String storageType = PowerRanks.getConfigManager().getString("storage.type", "yaml").toUpperCase();
        if (storageManager == null) {
            if (storageType.equals("JSON")) {
                storageManager = new JSONStorageManager(dataDirectory, "ranks.json", "players.json");
            } else if (storageType.equals("PSM")) {
                storageManager = new PSMStorageManager(dataDirectory, "ranks.psm", "players.psm");
            } else if (storageType.equals("SQLITE")) {
                storageManager = new SQLiteStorageManager(dataDirectory, "ranks.db", "players.db");
            } else if (storageType.equals("MYSQL")) {
                PowerConfigManager pcm = PowerRanks.getConfigManager();
                PowerSQLConfiguration configuration = new PowerSQLConfiguration(
                        pcm.getString("storage.mysql.host", "127.0.0.1"), pcm.getInt("storage.mysql.port", 3306),
                        pcm.getString("storage.mysql.database", "powerranks"),
                        pcm.getString("storage.mysql.username", "username"),
                        pcm.getString("storage.mysql.password", "password"), "ranks", "players");
                storageManager = new MySQLStorageManager(configuration, pcm.getBool("storage.mysql.verbose", false));
                // } else if (storageType.equals("MONGO") || storageType.equals("MONGODB")) {
                // PowerConfigManager pcm = PowerRanks.getConfigManager();
                // PowerSQLConfiguration configuration = new PowerSQLConfiguration(
                // pcm.getString("storage.mongodb.host", "127.0.0.1"),
                // pcm.getInt("storage.mongodb.port", 3306),
                // pcm.getString("storage.mongodb.database", "powerranks"),
                // "",
                // "", "", "");
                // storageManager = new MongoDBStorageManager(configuration,
                // pcm.getBool("storage.mongodb.verbose", false));
            } else { // Default to yaml
                storageManager = new YAMLStorageManager(dataDirectory, "ranks.yml", "players.yml");
            }
        }

        storageManager.loadAll();

        registeredRanks = (ArrayList<PRRank>) storageManager.getRanks();
        registeredPlayers = (ArrayList<PRPlayer>) storageManager.getPlayers();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            OnJoin.handleJoin(PowerRanks.getInstance(), player);
        }
    }

    public static void save() {
        storageManager.setRanks(registeredRanks);
        storageManager.setPlayers(registeredPlayers);

        storageManager.saveAll();
    }
}
