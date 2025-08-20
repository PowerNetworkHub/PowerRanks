package nl.svenar.powerranks.common.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.permissions.PermissionResolver;

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
    private Set<PRPermission> playerPermissions = new HashSet<>(); // direct overrides
    private Map<String, PRPermission> effectivePermissionsMap = new HashMap<>(); // Resolved effective permissions
    private Set<String> usertags = new HashSet<>();
    private long playtime = 0L;

    private transient PermissionResolver permissionResolver; // Transient to avoid serialization issues
    private boolean caseSensitivePermissions = false;

    public PRPlayer() {
        this.permissionResolver = new PermissionResolver(caseSensitivePermissions);
    }

    public void setCaseSensitivePermissions(boolean caseSensitive) {
        this.caseSensitivePermissions = caseSensitive;
        this.permissionResolver = new PermissionResolver(caseSensitivePermissions);
        // Rebuild with new normalization rules
        recalculateEffectivePermissions();
    }

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
        recalculateEffectivePermissions();
    }

    public void setRank(PRPlayerRank rank) {
        ranks.clear();
        ranks.add(rank);
        recalculateEffectivePermissions();
    }

    public void addRank(PRPlayerRank rank) {
        ranks.add(rank);
        recalculateEffectivePermissions();
    }

    public void addRank(PRRank rank) {
        addRank(new PRPlayerRank(rank.getName()));
    }

    public void removeRank(PRPlayerRank rank) {
        ranks.remove(rank);
        recalculateEffectivePermissions();
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

    public Set<PRPermission> getPlayerPermissions() {
        return playerPermissions;
    }

    public void setPlayerPermissions(Set<PRPermission> playerPermissions) {
        this.playerPermissions = playerPermissions;
        recalculateEffectivePermissions();
    }

    public void addPlayerPermission(PRPermission permission) {
        playerPermissions.add(permission);
        recalculateEffectivePermissions();
    }

    public void removePlayerPermission(PRPermission permission) {
        playerPermissions.remove(permission);
        recalculateEffectivePermissions();
    }

    /**
     * Recalculates the effective permissions for the player using the PermissionResolver.
     */
    public void recalculateEffectivePermissions() {
        if (this.permissionResolver == null) {
            this.permissionResolver = new PermissionResolver(caseSensitivePermissions);
        }
        this.effectivePermissionsMap = this.permissionResolver.resolveEffectivePermissions(this.ranks, this.playerPermissions);
    }

    public boolean hasPermission(String node, boolean wildcard) {
        PRPermission p = getPermission(node, wildcard);
        return p != null && p.getValue();
    }

    public boolean isPermissionAllowed(String node, boolean wildcard) {
        PRPermission p = getPermission(node, wildcard);
        return p != null && p.getValue();
    }

    public PRPermission getPermission(String node) {
        return getPermission(node, false);
    }

    public PRPermission getPermission(String node, boolean wildcard) {
        if (this.effectivePermissionsMap.isEmpty()) {
            recalculateEffectivePermissions();
        }
        return this.permissionResolver.getPermission(this.effectivePermissionsMap, node, wildcard);
    }

    public Map<String, PRPermission> getEffectivePermissions() {
        if (this.effectivePermissionsMap.isEmpty()) {
            recalculateEffectivePermissions();
        }
        return Collections.unmodifiableMap(this.effectivePermissionsMap);
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
                Object worldsObj = rank.getTags().get("worlds");
                if (worldsObj instanceof Collection<?>) {
                    boolean inWorld = ((Collection<?>) worldsObj)
                            .stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .anyMatch(w -> w.equalsIgnoreCase(worldName));
                    rank.setDisabled(!inWorld);
                }
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