package nl.svenar.powerranks.common.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;

import java.util.*;

/**
 * Represents a player in PowerRanks with ranks, permissions, tags, and
 * metadata.
 */
@JsonIgnoreProperties({ "defaultRanks", "effectivePermissions" })
public class PRPlayer {

    private UUID uuid;
    private String name = "";
    private String nickname = "";
    private Set<PRPlayerRank> ranks = new HashSet<>();
    private Set<PRPermission> permissions = new HashSet<>(); // effective permissions
    private Set<PRPermission> playerPermissions = new HashSet<>(); // direct overrides
    private Set<String> usertags = new HashSet<>();
    private long playtime = 0L;

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<PRPlayerRank> getRanks() {
        return ranks;
    }

    public void setRanks(Set<PRPlayerRank> ranks) {
        this.ranks = ranks;
    }

    public void setRank(PRPlayerRank rank) {
        ranks.clear();
        ranks.add(rank);
    }

    public void addRank(PRPlayerRank rank) {
        ranks.add(rank);
    }

    public void addRank(PRRank rank) {
        addRank(new PRPlayerRank(rank.getName()));
    }

    public void removeRank(PRPlayerRank rank) {
        ranks.remove(rank);
    }

    public boolean hasRank(String rankName) {
        return ranks.stream().anyMatch(r -> r.getName().equalsIgnoreCase(rankName));
    }

    public List<PRRank> getDefaultRanks() {
        List<PRRank> result = new ArrayList<>();
        for (PRPlayerRank r : ranks) {
            PRRank rank = PRCache.getRank(r.getName());
            if (rank != null && rank.isDefault()) {
                result.add(rank);
            }
        }
        return result;
    }

    public Set<PRPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PRPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<PRPermission> getPlayerPermissions() {
        return playerPermissions;
    }

    public void setPlayerPermissions(Set<PRPermission> playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    public void addPlayerPermission(PRPermission permission) {
        playerPermissions.add(permission);
    }

    public void removePlayerPermission(PRPermission permission) {
        playerPermissions.remove(permission);
    }

    /**
     * Rebuild effective permissions from ranks, then apply player overrides last.
     */
    public void updatePermissionsFromRanks() {
        Set<PRPermission> merged = new HashSet<>();

        // Collect directly assigned ranks
        List<PRRank> assigned = new ArrayList<>();
        for (PRPlayerRank pr : ranks) {
            if (!pr.isDisabled()) {
                PRRank rank = PRCache.getRank(pr.getName());
                if (rank != null)
                    assigned.add(rank);
            }
        }

        // Expand inheritance and track max depth
        LinkedHashSet<PRRank> all = new LinkedHashSet<>();
        Map<String, Integer> depth = new HashMap<>();
        for (PRRank root : assigned) {
            collectInheritedRanksWithDepth(root, 0, all, new HashSet<>(), depth);
        }

        // Order: deeper first, then by weight
        TreeMap<Integer, List<PRRank>> byDepth = new TreeMap<>(Collections.reverseOrder());
        for (PRRank r : all) {
            int d = depth.getOrDefault(r.getName(), 0);
            byDepth.computeIfAbsent(d, k -> new ArrayList<>()).add(r);
        }

        List<PRRank> ordered = new ArrayList<>();
        for (List<PRRank> bucket : byDepth.values()) {
            PRUtil.sortRanksByWeight(bucket);
            ordered.addAll(bucket);
        }

        // Merge permissions in order
        for (PRRank rank : ordered) {
            for (PRPermission p : rank.getPermissions()) {
                Optional<PRPermission> existing = merged.stream()
                        .filter(e -> e.getName().equals(p.getName()))
                        .findFirst();

                if (existing.isPresent()) {
                    PRPermission current = existing.get();
                    // Deny should override allow when same depth/weight
                    if (!current.getValue() && p.getValue()) {
                        // keep deny, skip allow
                        continue;
                    }
                    // Otherwise replace (allow overriding deny at deeper/stronger level)
                    merged.remove(current);
                }
                merged.add(p);
            }
        }

        // Apply player overrides
        for (PRPermission p : playerPermissions) {
            merged.removeIf(existing -> existing.getName().equals(p.getName()));
            merged.add(p);
        }

        permissions.clear();
        permissions.addAll(merged);
    }

    private void collectInheritedRanksWithDepth(
            PRRank rank, int depth, Set<PRRank> collector,
            Set<String> visited, Map<String, Integer> depthOut) {
        if (rank == null || !visited.add(rank.getName()))
            return;

        depthOut.merge(rank.getName(), depth, Math::max);

        for (String parent : rank.getInheritances()) {
            collectInheritedRanksWithDepth(PRCache.getRank(parent), depth + 1, collector, visited, depthOut);
        }

        collector.add(rank);
    }

    public boolean hasPermission(String node, boolean wildcard) {
        return getPermission(node, wildcard) != null;
    }

    public boolean isPermissionAllowed(String node, boolean wildcard) {
        PRPermission p = getPermission(node, wildcard);
        return p != null && p.getValue();
    }

    public PRPermission getPermission(String node) {
        return getPermission(node, false);
    }

    public PRPermission getPermission(String node, boolean wildcard) {
        // Exact match
        for (PRPermission p : permissions) {
            if (p.getName().equals(node))
                return p;
        }

        if (!wildcard)
            return null;

        // Wildcard search
        for (String candidate : PRUtil.generateWildcardList(node)) {
            for (PRPermission p : permissions) {
                if (p.getName().equals(candidate))
                    return p;
            }
        }
        return null;
    }

    public Set<PRPermission> getEffectivePermissions() {
        updatePermissionsFromRanks();
        return permissions;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public Set<String> getUsertags() {
        return usertags;
    }

    public void setUsertags(Set<String> tags) {
        this.usertags = tags;
    }

    public void setUsertag(String tag) {
        usertags.clear();
        usertags.add(tag);
    }

    public void addUsertag(String tag) {
        usertags.add(tag);
    }

    public void removeUsertag(String tag) {
        usertags.remove(tag);
    }

    public boolean hasUsertag(String tag) {
        return usertags.contains(tag);
    }

    public void updateTags(String worldName) {
        for (PRPlayerRank rank : ranks) {
            if (rank.getTags().containsKey("worlds")) {
                boolean inWorld = ((List<?>) rank.getTags().get("worlds"))
                        .stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .anyMatch(w -> w.equalsIgnoreCase(worldName));
                rank.setDisabled(!inWorld);
            }
        }
    }

    @Override
    public String toString() {
        return "PRPlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", ranks=" + ranks +
                ", permissions=" + permissions +
                ", playerPermissions=" + playerPermissions +
                ", usertags=" + usertags +
                ", playtime=" + playtime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PRPlayer && Objects.equals(uuid, ((PRPlayer) o).uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
