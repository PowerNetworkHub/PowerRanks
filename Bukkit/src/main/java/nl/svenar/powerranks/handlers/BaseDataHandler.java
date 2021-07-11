package nl.svenar.powerranks.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;

public abstract class BaseDataHandler {
    
    protected PowerRanks plugin;

    private static Collection<PRRank> ranks = new ArrayList<PRRank>();
    private static Collection<PRPlayer> players = new ArrayList<PRPlayer>();

    public void setup(PowerRanks plugin) {
        this.plugin = plugin;
    }

    public abstract Collection<PRRank> loadRanks();
    public abstract Collection<PRPlayer> loadPlayers();

    public abstract void saveRanks(Collection<PRRank> ranks);
    public abstract void savePlayers(Collection<PRPlayer> players);

    public abstract void saveRank(PRRank rank);
    public abstract void savePlayer(PRPlayer player);


    public static Collection<PRRank> getRanks() {
        return ranks;
    }

    public static PRRank getRank(String rankName) {
        PRRank rank = null;
        for (PRRank obj : ranks) {
            if (obj.getName().equalsIgnoreCase(rankName)) {
                rank = obj;
                break;
            }
        }
        return rank;
    }

    public static void addRank(PRRank rank) {
        ranks.add(rank);
    }

    public static void removeRank(PRRank rank) {
        ranks.remove(rank);
    }

    public static Collection<PRPlayer> getPlayers() {
        return players;
    }

    public static PRPlayer getPlayer(String playerName) {
        PRPlayer player = null;
        for (PRPlayer obj : players) {
            if (obj.getName().equalsIgnoreCase(playerName)) {
                player = obj;
                break;
            }
        }
        return player;
    }

    public static PRPlayer getPlayer(UUID playerUUID) {
        PRPlayer player = null;
        for (PRPlayer obj : players) {
            if (obj.getUuid().toString().equals(playerUUID.toString())) {
                player = obj;
                break;
            }
        }
        return player;
    }

    public static void addPlayer(PRPlayer player) {
        players.add(player);
    }

    public static void removePlayer(PRPlayer player) {
        players.remove(player);
    }
}