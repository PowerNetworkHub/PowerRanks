package nl.svenar.powerranks.common.storage;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PermissionRegistry {

    private Set<String> permissions = new HashSet<String>();
    private Queue<String> queue = new ConcurrentLinkedQueue<>();

    public PermissionRegistry() {
    }

    public void queuePermission(String permissionNode) {
        this.queue.add(permissionNode);
    }

    public void tick() {
        String permissionNode = this.queue.poll();
        if (permissionNode != null) {
            addPermission(permissionNode);
        }
    }

    private void addPermission(String permissionNode) {
        if (permissionNode == null || permissionNode.isEmpty()) {
            return;
        }
        if (permissions.contains(permissionNode)) {
            return;
        }
        permissions.add(permissionNode);
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
