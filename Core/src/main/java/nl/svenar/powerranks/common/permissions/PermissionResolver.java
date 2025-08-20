package nl.svenar.powerranks.common.permissions;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionResolver {
    private final boolean caseSensitive;

    public PermissionResolver(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Resolves the effective permissions for a player based on their ranks and
     * direct permissions.
     * This method handles inheritance, weight-based priority, and explicit
     * overrides.
     *
     * @param playerRanks       The set of ranks assigned to the player.
     * @param playerPermissions The set of direct permissions assigned to the
     *                          player.
     * @return A map of effective permissions, where the key is the permission node
     *         and the value is the PRPermission object.
     */
    public Map<String, PRPermission> resolveEffectivePermissions(
            Set<nl.svenar.powerranks.common.structure.PRPlayerRank> playerRanks,
            Set<PRPermission> playerPermissions) {
        Map<String, PRPermission> effectivePermissions = new HashMap<>();

        // 1. Collect all assigned ranks and expand inheritance
        LinkedHashSet<PRRank> allRanks = new LinkedHashSet<>();
        Map<String, Integer> rankDepth = new HashMap<>();

        List<PRRank> assignedActiveRanks = playerRanks.stream()
                .filter(pr -> !pr.isDisabled())
                .map(pr -> PRCache.getRank(pr.getName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // stable deterministic root ordering: higher weight first, then name
        assignedActiveRanks.sort((a, b) -> {
            int w = Integer.compare(b.getWeight(), a.getWeight());
            if (w != 0)
                return w;
            return a.getName().compareToIgnoreCase(b.getName());
        });

        for (PRRank rootRank : assignedActiveRanks) {
            collectInheritedRanksWithDepth(rootRank, 0, allRanks, new HashSet<>(), rankDepth);
        }

        // 2. Order ranks: deeper first, then by weight, then by name (stable)
        TreeMap<Integer, List<PRRank>> ranksByDepth = new TreeMap<>(Collections.reverseOrder());
        for (PRRank rank : allRanks) {
            int depth = rankDepth.getOrDefault(rank.getName(), 0);
            ranksByDepth.computeIfAbsent(depth, k -> new ArrayList<>()).add(rank);
        }

        List<PRRank> orderedRanks = new ArrayList<>();
        for (List<PRRank> bucket : ranksByDepth.values()) {
            bucket.sort((a, b) -> {
                int weightCmp = Integer.compare(b.getWeight(), a.getWeight()); // higher weight first
                if (weightCmp != 0)
                    return weightCmp;
                return a.getName().compareToIgnoreCase(b.getName()); // stable tie-break
            });
            orderedRanks.addAll(bucket);
        }

        // 3. Merge permissions from ordered ranks
        for (PRRank rank : orderedRanks) {
            for (PRPermission perm : rank.getPermissions()) {
                String permName = normalizePermissionName(perm.getName());
                PRPermission existing = effectivePermissions.get(permName);
                if (existing == null) {
                    effectivePermissions.put(permName, perm);
                } else {
                    if (!existing.getValue() && perm.getValue()) {
                        // If existing is deny and new is allow, override with the new allow
                        effectivePermissions.put(permName, perm);
                    }
                }
            }
        }

        // 4. Apply player-specific overrides (always take precedence)
        for (PRPermission playerPerm : playerPermissions) {
            String permName = normalizePermissionName(playerPerm.getName());
            effectivePermissions.put(permName, playerPerm);
        }

        return effectivePermissions;
    }

    /**
     * Recursively collects all inherited ranks and their depths.
     *
     * @param rank      The current rank being processed.
     * @param depth     The current depth in the inheritance hierarchy.
     * @param collector A set to collect all unique ranks.
     * @param visited   A set to keep track of visited rank names to prevent
     *                  infinite loops.
     * @param depthOut  A map to store the maximum depth at which each rank was
     *                  encountered.
     */
    private void collectInheritedRanksWithDepth(
            PRRank rank, int depth, Set<PRRank> collector,
            Set<String> visited, Map<String, Integer> depthOut) {
        if (rank == null || !visited.add(rank.getName())) {
            return;
        }

        depthOut.merge(rank.getName(), depth, Math::max);

        for (String parentName : rank.getInheritances()) {
            PRRank parentRank = PRCache.getRank(parentName);
            collectInheritedRanksWithDepth(parentRank, depth + 1, collector, visited, depthOut);
        }

        collector.add(rank);
    }

    /**
     * Checks if a player has a specific permission, considering wildcards.
     *
     * @param effectivePermissions The pre-resolved effective permissions for the
     *                             player.
     * @param permissionNode       The permission node to check.
     * @return The PRPermission object if found, null otherwise.
     */
    public PRPermission getPermission(
            Map<String, PRPermission> effectivePermissions,
            String permissionNode) {
        return getPermission(effectivePermissions, permissionNode, true);
    }

    public PRPermission getPermission(
            Map<String, PRPermission> effectivePermissions,
            String permissionNode,
            Boolean wildcard) {
        if (permissionNode == null || permissionNode.isEmpty()) {
            return null;
        }

        String normalizedNode = normalizePermissionName(permissionNode);

        // 1. Check for exact match
        PRPermission exactMatch = effectivePermissions.get(normalizedNode);
        if (exactMatch != null) {
            return exactMatch;
        }

        // 2. Check for wildcard matches
        if (wildcard) {
            List<String> wildcardList = PRUtil.generateWildcardList(permissionNode);
            for (String wildcardNode : wildcardList) {
                String normalizedWildcard = normalizePermissionName(wildcardNode);
                PRPermission wildcardMatch = effectivePermissions.get(normalizedWildcard);
                if (wildcardMatch != null) {
                    return wildcardMatch;
                }
            }
        }

        return null;
    }

    /**
     * Normalizes a permission name based on case sensitivity setting.
     *
     * @param name The permission name to normalize.
     * @return The normalized permission name.
     */
    private String normalizePermissionName(String name) {
        return caseSensitive ? name : name.toLowerCase(Locale.ROOT).trim();
    }
}