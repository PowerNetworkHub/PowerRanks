package nl.svenar.powerranks.bukkit.data;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powerranks.common.storage.provided.MySQLStorageManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;

public class BungeecordManager {

    private UUID serverUUID;
    
    private ArrayList<UUID> onlineServers = new ArrayList<UUID>();

    private boolean ready = false;

    private BukkitRunnable mainTask;

    private int ranksHashcode;

    private int playersHashcode;

    private int lastRanksHashcode;

    private int lastPlayersHashcode;

    public BungeecordManager(PowerRanks powerRanks) {
        serverUUID = UUID.fromString(PowerRanks.getConfigManager().getString("bungeecord.uuid", UUID.randomUUID().toString()));
    }

    public void start() {
        if (!PowerRanks.getConfigManager().getBool("bungeecord.enabled", false)) {
            return;
        }

        if (!(CacheManager.getStorageManager() instanceof MySQLStorageManager)) {
            PowerRanks.log.severe("");
            PowerRanks.log.severe("=== ----------------------------- ===");
            PowerRanks.log.severe("               WARNING               ");
            PowerRanks.log.severe("     Bungeecord is enabled, but      ");
            PowerRanks.log.severe("  storage type is not set to MYSQL!  ");
            PowerRanks.log.severe("");
            PowerRanks.log.severe("Bungeecord functionality is disabled!");
            PowerRanks.log.severe("=== ----------------------------- ===");
            PowerRanks.log.severe("");
            return;
        }
        this.ready = true;
        PowerRanks.log.info("Setting up Bungeecord with ID " + this.serverUUID.toString());

        CacheManager.postDBMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"), this.serverUUID.toString(), "online");

        this.updateHashes();
        this.lastRanksHashcode = this.ranksHashcode;
        this.lastPlayersHashcode = this.playersHashcode;

        mainTask = new BukkitRunnable() {
            @Override
            public void run() {
                Instant startTime = Instant.now();

                for (Entry<String, String> e : CacheManager.getDBBroadcastMessages().entrySet()) {
                    UUID targetUUID = UUID.fromString(e.getKey().split("\\.")[1]);
                    if (e.getValue().equalsIgnoreCase("online") && !targetUUID.toString().equals(serverUUID.toString())) {
                        if (!onlineServers.contains(targetUUID)) {
                            onlineServers.add(targetUUID);
                        }
                    } else {
                        if (onlineServers.contains(targetUUID)) {
                            onlineServers.remove(targetUUID);
                        }
                    }
                }

                updateHashes();
                boolean rankHashChanged = lastRanksHashcode != ranksHashcode;
                boolean playerHashChanged = lastPlayersHashcode != playersHashcode;

                if (rankHashChanged || playerHashChanged) {
                    CacheManager.save();
                }

                if (rankHashChanged) {
                    for (UUID remoteServer : onlineServers) {
                        CacheManager.postDBMessage(remoteServer, "update.ranks", String.valueOf(Instant.now().getEpochSecond()));
                    }
                }

                if (playerHashChanged) {
                    for (UUID remoteServer : onlineServers) {
                        CacheManager.postDBMessage(remoteServer, "update.players", String.valueOf(Instant.now().getEpochSecond()));
                    }
                }

                boolean doReloadData = false;
                for (Entry<String, String> newMessage : CacheManager.getDBMessages(serverUUID).entrySet()) {
                    String command = newMessage.getKey().split("\\.")[1];

                    switch(command.toLowerCase()) {
                        case "update":
                            String updateType = newMessage.getKey().split("\\.")[2];

                            switch(updateType.toLowerCase()) {
                                case "ranks":
                                    doReloadData = true;
                                    break;
                                
                                case "players":
                                    doReloadData = true;
                                    break;
                                
                                default:
                                    break;
                            }
                            break;
                        
                        default:
                            break;
                    }

                    CacheManager.removeDBMessage(serverUUID, newMessage.getKey().replaceAll(serverUUID.toString() + ".", ""));
                }

                if (doReloadData) {
                    PowerRanks.log.warning("Reloading data from database!");

                    CacheManager.load(PowerRanks.fileLoc);
                    PowerRanks.getInstance().updateAllPlayersTABlist();

                    updateHashes();
                    lastRanksHashcode = ranksHashcode;
                    lastPlayersHashcode = playersHashcode;
                }

                PowerRanksVerbose.log("task", "Running task bungeecord mainTask in " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
            }
        };
        mainTask.runTaskTimer(
            PowerRanks.getInstance(),
            0,
            20 * 5
        );
    }

    public void stop() {
        if (!PowerRanks.getConfigManager().getBool("bungeecord.enabled", false)) {
            return;
        }

        if (!(CacheManager.getStorageManager() instanceof MySQLStorageManager)) {
            return;
        }
        this.ready = false;

        CacheManager.postDBMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"), serverUUID.toString(), "offline");
    }
    
    public boolean isReady() {
        return this.ready;
    }

    public int getServerCount() {
        return this.onlineServers.size();
    }

    private void updateHashes() {
        this.lastRanksHashcode = this.ranksHashcode;
        this.lastPlayersHashcode = this.playersHashcode;

        this.ranksHashcode = 17;
        List<PRRank> ranks = CacheManager.getRanks();
        PRUtil.sortRanksByWeight(ranks);
        for (PRRank rank : ranks) {
            ranksHashcode = 31 * ranksHashcode + (rank != null ? rank.hashCode() : 0);
        }

        this.playersHashcode = 0;
        for (PRPlayer player : CacheManager.getPlayers()) {
            playersHashcode += player.hashCode();
        }
    }
}
