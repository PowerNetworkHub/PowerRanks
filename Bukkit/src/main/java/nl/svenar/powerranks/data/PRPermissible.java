package nl.svenar.powerranks.data;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.powerranks.utils.PermissionUtils;

public class PRPermissible extends PermissibleBase {

    private PRPlayer prPlayer;

    public PRPermissible(PRPlayer prPlayer) {
        super(prPlayer.getPlayer());
        this.prPlayer = prPlayer;
    }

    @Override
    public boolean hasPermission(String permission) {
        PermissionAttachmentInfo pai = null;
        if (isPermissionSet(permission)) {
            for (PermissionAttachmentInfo effectivePermission : super.getEffectivePermissions()) {
                if (effectivePermission.getPermission().equals(permission)) {
                    pai = effectivePermission;
                }
            }
        }

        // String out = "\n\n";
        // out += "" + permission + "\n";
        // // out += "1a: " + prPlayer.isPermissionAllowed(permission,
        // // this.prPlayer.getPlayer().getWorld()) + "\n";
        // out += "1a: " + PermissionUtils.hasPermission(prPlayer, permission) + "\n";
        // out += "1b: " + PermissionUtils.hasPermission(prPlayer, permission, pai !=
        // null ? pai.getValue() : super.hasPermission(permission)) + "\n";
        // out += "1c: " + super.hasPermission(permission) + "\n";
        // out += "1d: " + (pai != null ? pai.getValue() : "null") + "\n";
        // out += "\n";
        // ErrorManager.logWarning(out);

        return PermissionUtils.hasPermission(prPlayer, permission,
                pai != null ? pai.getValue() : super.hasPermission(permission)) || prPlayer.isOP();
    }

    @Override
    public boolean hasPermission(Permission permission) {
        // String out = "\n\n";
        // out += "" + permission.getName() + "\n";
        // out += "2a: " + prPlayer.isPermissionAllowed(permission,
        // this.prPlayer.getPlayer().getWorld()) + "\n";
        // out += "2b: " + permission.getDefault().getValue(true) + "\n";
        // out += "2c: " + super.hasPermission(permission) + "\n";
        // out += "\n";
        // ErrorManager.logWarning(out);
        return hasPermission(permission.getName()) || permission.getDefault().getValue(true);

        // return prPlayer.isPermissionAllowed(permission,
        // this.prPlayer.getPlayer().getWorld())
        // || permission.getDefault().getValue(true) || prPlayer.isOP();// ||
        // super.hasPermission(permission);
    }
}
