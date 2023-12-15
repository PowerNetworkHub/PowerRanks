package nl.svenar.powerranks.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;

public class PRCache {

    private static Map<String, PRRank> registeredRanks = new TreeMap<String, PRRank>();
    private static Map<String, PRPlayer> registeredPlayersByName = new TreeMap<String, PRPlayer>();
    private static Map<UUID, PRPlayer> registeredPlayersByUUID = new TreeMap<UUID, PRPlayer>();
    private static Set<PRRank> defaultRanks = new HashSet<PRRank>();

    public static void reset() {
        registeredRanks.clear();
        registeredPlayersByName.clear();
        registeredPlayersByUUID.clear();
        defaultRanks.clear();
    }

    public static List<PRRank> getRanks() {
        return new ArrayList<>(registeredRanks.values());
    }

    public static void setRanks(List<PRRank> ranks) {
        if (ranks == null) {
            throw new NullPointerException("ranks is null");
        }

        registeredRanks.clear();
        defaultRanks.clear();
        for (PRRank rank : ranks) {
            registeredRanks.put(rank.getName(), rank);
            if (rank.isDefault()) {
                defaultRanks.add(rank);
            }
        }
    }

    public static void addRank(PRRank rank) {
        if (rank == null) {
            throw new NullPointerException("rank is null");
        }

        registeredRanks.put(rank.getName(), rank);
        if (rank.isDefault()) {
            defaultRanks.add(rank);
        }

    }

    public static void removeRank(PRRank rank) {
        if (rank == null) {
            throw new NullPointerException("rank is null");
        }

        String rankName = rank.getName();

        registeredRanks.remove(rankName);
        defaultRanks.remove(rank);

        for (PRPlayer prPlayer : registeredPlayersByName.values()) {
            if (prPlayer.hasRank(rankName)) {
                prPlayer.getRanks()
                        .removeIf(prPlayerRank -> prPlayerRank.getName().equalsIgnoreCase(rankName));

            }
        }
    }

    public static PRRank getRank(String name) {
        return registeredRanks.get(name);
    }

    public static PRRank getRankIgnoreCase(String name) {
        PRRank rank = getRank(name);
        if (rank != null) {
            return rank;
        }
        for (PRRank prRank : registeredRanks.values()) {
            if (prRank.getName().equalsIgnoreCase(name)) {
                return prRank;
            }
        }
        return null;
    }

    public static Set<PRRank> getDefaultRanks() {
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
            if (prPlayer.equals(player)) {
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

        for (PRPlayer player : registeredPlayersByName.values()) {
            if (player.getName().equalsIgnoreCase(identifier)) {
                return player;
            }
        }

        return prPlayer;
    }

    public static PRPlayer createPlayer(String name, UUID uniqueID) {
        Optional<PRPlayer> existingPlayer = getPlayers().stream()
                .filter(player -> player.getUUID().equals(uniqueID))
                .findFirst();
    
        if (existingPlayer.isPresent()) {
            return existingPlayer.get(); // Return existing player if found
        }
    
        PRPlayer prPlayer = new PRPlayer();
        prPlayer.setUUID(uniqueID);
        prPlayer.setName(name);
    
        Set<PRPlayerRank> playerRanks = getDefaultRanks().stream()
                .map(PRPlayerRank::new)
                .collect(Collectors.toSet());
    
        prPlayer.setRanks(playerRanks);
    
        addPlayer(prPlayer);
        return prPlayer;
    }

    public static PRRank createRank(String name) {
        return createRank(name, "&r[&7" + name + "&r]", "", 0, false);
    }

    public static PRRank createRank(String name, int weight) {
        return createRank(name, "&r[&7" + name + "&r]", "", weight, false);
    }

    public static PRRank createRank(String name, String prefix, String suffix, int weight) {
        return createRank(name, prefix, suffix, weight, false);
    }

    public static PRRank createRank(String name, String prefix, String suffix, int weight, boolean isDefault) {
        for (PRRank prRank : getRanks()) {
            if (prRank.getName().equalsIgnoreCase(name)) {
                return null;
            }
        }

        PRRank rank = new PRRank();
        rank.setName(name);
        rank.setPrefix(prefix);
        rank.setSuffix(suffix);
        rank.setWeight(weight);
        rank.setDefault(isDefault);
        addRank(rank);
        return rank;
    }

}
