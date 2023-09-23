package nl.svenar.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;

public class PRCache {

    private static Map<String, PRRank> registeredRanks = new HashMap<String, PRRank>();
    private static Map<String, PRPlayer> registeredPlayersByName = new HashMap<String, PRPlayer>();
    private static Map<UUID, PRPlayer> registeredPlayersByUUID = new HashMap<UUID, PRPlayer>();

    public static List<PRRank> getRanks() {
        return new ArrayList<>(registeredRanks.values());
    }

    public static void setRanks(List<PRRank> ranks) {
        if (ranks == null) {
            throw new NullPointerException("ranks is null");
        }

        registeredRanks.clear();
        for (PRRank rank : ranks) {
            registeredRanks.put(rank.getName(), rank);
        }
    }

    public static void addRank(PRRank rank) {
        if (rank == null) {
            throw new NullPointerException("rank is null");
        }

        registeredRanks.put(rank.getName(), rank);
    }

    public static void removeRank(PRRank rank) {
        if (rank == null) {
            throw new NullPointerException("rank is null");
        }

        registeredRanks.remove(rank.getName());
    }

    public static PRRank getRank(String name) {
        return registeredRanks.get(name);
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

    public static List<PRPlayer> getPlayers() {
        return new ArrayList<>(registeredPlayersByName.values());
    }

    public static void setPlayers(List<PRPlayer> players) {
        if (players == null) {
            throw new NullPointerException("playes is null");
        }

        registeredPlayersByName.clear();
        registeredPlayersByUUID.clear();
        for (PRPlayer player : players) {
            registeredPlayersByName.put(player.getName(), player);
            registeredPlayersByUUID.put(player.getUUID(), player);
        }
    }

    public static void addPlayer(PRPlayer player) {
        if (player == null) {
            throw new NullPointerException("player is null");
        }

        registeredPlayersByName.put(player.getName(), player);
        registeredPlayersByUUID.put(player.getUUID(), player);
    }

    public static void removePlayer(PRPlayer player) {
        if (player == null) {
            throw new NullPointerException("player is null");
        }

        for (PRPlayer prPlayer : registeredPlayersByName.values()) {
            if (prPlayer.getName().equals(player.getName()) && prPlayer.getUUID().equals(player.getUUID())) {
                registeredPlayersByName.remove(prPlayer.getName());
                registeredPlayersByUUID.remove(prPlayer.getUUID());
                break;
            }
        }
    }

    public static PRPlayer getPlayer(String identifier) {
        PRPlayer prPlayer = registeredPlayersByName.get(identifier);
        if (prPlayer != null) {
            return prPlayer;
        }
        try {
            prPlayer = registeredPlayersByUUID.get(UUID.fromString(identifier));
        } catch (IllegalArgumentException e) {
        }

        return prPlayer;
    }

    public static PRPlayer createPlayer(String name, UUID uniqueID) {
        PRPlayer prPlayer = new PRPlayer();
        prPlayer.setUUID(uniqueID);
        prPlayer.setName(name);
        for (PRRank rank : getDefaultRanks()) {
            PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
            prPlayer.addRank(playerRank);
        }
        addPlayer(prPlayer);
        return prPlayer;
    }

}
