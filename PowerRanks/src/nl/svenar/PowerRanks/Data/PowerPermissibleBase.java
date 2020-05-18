package nl.svenar.PowerRanks.Data;

import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class PowerPermissibleBase extends PermissibleBase {
	
	private Player p;
    private Map<String, PermissionAttachmentInfo> permissions;
    private Permissible oldPermissible = new PermissibleBase(null);

    public PowerPermissibleBase(Player p) {
        super(p);
        this.p = p;
        permissions = new LinkedHashMap<String, PermissionAttachmentInfo>() {
        	private static final long serialVersionUID = 1L;
        	
			@Override
            public PermissionAttachmentInfo put(String k, PermissionAttachmentInfo v) {
                PermissionAttachmentInfo existing = this.get(k);
                if (existing != null) {
                    return existing;
                }

                return super.put(k, v);
            }
        };

//        Permissions.getInstance().setField(PermissibleBase.class, this, permissions, "permissions");
    }

    public Permissible getOldPermissible() {
        return oldPermissible;
    }

    public void setOldPermissible(Permissible oldPermissible) {
        this.oldPermissible = oldPermissible;
    }

    public boolean hasSuperPerm(String perm) {
        if (oldPermissible == null) {
            return super.hasPermission(perm);
        }

        return oldPermissible.hasPermission(perm);
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permission == null) {
            throw new NullPointerException("permission");
        }

//        boolean res = Permissions.getInstance().getPermissionsManager().has(p, permission);
        boolean res = true;

        return res;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return hasPermission(permission.getName());
    }

    @Override
    public void recalculatePermissions() {
        if (oldPermissible == null) {
            super.recalculatePermissions();
            return;
        }

        oldPermissible.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        if (oldPermissible == null) {
            return super.getEffectivePermissions();
        }

        return new LinkedHashSet<>(permissions.values());
    }

    @Override
    public boolean isOp() {
        if (oldPermissible == null) {
            return super.isOp();
        }

        return oldPermissible.isOp();
    }

    @Override
    public void setOp(boolean value) {
        if (oldPermissible == null) {
            super.setOp(value);
            return;
        }

        oldPermissible.setOp(value);
    }

    @Override
    public boolean isPermissionSet(String permission) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        if (oldPermissible == null) {
            return super.addAttachment(plugin);
        }

        return oldPermissible.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        if (oldPermissible == null) {
            return super.addAttachment(plugin, ticks);
        }

        return oldPermissible.addAttachment(plugin, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        if (oldPermissible == null) {
            return super.addAttachment(plugin, name, value);
        }

        return oldPermissible.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        if (oldPermissible == null) {
            return super.addAttachment(plugin, name, value, ticks);
        }

        return oldPermissible.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        if (oldPermissible == null) {
            super.removeAttachment(attachment);
            return;
        }

        oldPermissible.removeAttachment(attachment);
    }

    @Override
    public synchronized void clearPermissions() {
        if (oldPermissible == null) {
            super.clearPermissions();
            return;
        }

        if (oldPermissible instanceof PermissibleBase) {
            PermissibleBase base = (PermissibleBase) oldPermissible;
            base.clearPermissions();
        }
    }


}