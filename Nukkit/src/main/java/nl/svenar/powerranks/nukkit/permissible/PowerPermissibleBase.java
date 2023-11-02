package nl.svenar.powerranks.nukkit.permissible;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import nl.svenar.powerranks.common.storage.PermissionRegistry;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;

public class PowerPermissibleBase extends PermissibleBase {

    private PowerRanks plugin;
	
    // private Player player;

	private PermissionRegistry permissionRegistry;

	private PRPlayer prPlayer;

	public static Map<String, Integer> permissionCallCount = new HashMap<String, Integer>();

    public PowerPermissibleBase(Player player, PowerRanks plugin) {
        super(player);
        this.plugin = plugin;
        // this.player = player;
		this.permissionRegistry = plugin.getPermissionRegistry();
		this.prPlayer = PRCache.getPlayer(player.getUniqueId().toString());
		if (prPlayer == null) {
			PRCache.createPlayer(player.getName(), player.getUniqueId());
			this.prPlayer = PRCache.getPlayer(player.getUniqueId().toString());
		}
    }

    @Override
	public boolean isOp() {
		return super.isOp();
	}

	@Override
	public void setOp(boolean value) {
		super.setOp(value);
	}

    @Override
	public boolean isPermissionSet(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return isPermissionSet(perm.getName());
	}

	@Override
	public boolean isPermissionSet(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		permissionRegistry.queuePermission(name);

		PRPermission prPermission = getPRPermission(name);
		if (prPermission == null) {
			for (String wildCardPermissionName : PRUtil.generateWildcardList(name)) {
				prPermission = getPRPermission(wildCardPermissionName);
				if (prPermission != null) {
					break;
				}
			}
		}

		if (prPermission != null) {
			return prPermission.getValue();
		}

		return super.isPermissionSet(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Permission cannot be null");
		}

		return hasPermission(perm.getName());
	}

	@Override
	public boolean hasPermission(String inName) {
		if (inName == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		permissionRegistry.queuePermission(inName);

		if (permissionCallCount.get(inName) == null) {
			permissionCallCount.put(inName, 0);
		} else {
			permissionCallCount.put(inName, permissionCallCount.get(inName) + 1);
		}

		PRPermission prPermission = getPRPermission(inName);
		if (prPermission == null) {
			for (String wildCardPermissionName : PRUtil.generateWildcardList(inName)) {
				prPermission = getPRPermission(wildCardPermissionName);
				if (prPermission != null) {
					break;
				}
			}
		}

		boolean defaultHasPermission = false;
		try {
			defaultHasPermission = super.hasPermission(inName);
		} catch (NullPointerException e) {
			super.recalculatePermissions();
		}

		if (prPermission != null) {
			return prPermission.getValue();
		}
		return defaultHasPermission;
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
		return super.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return super.addAttachment(plugin);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		try {
			super.removeAttachment(attachment);
		} catch (Exception e) {
		}
	}

	@Override
	public void recalculatePermissions() {
		super.recalculatePermissions();
	}

	public synchronized void clearPermissions() {
		super.clearPermissions();
	}

	@Override
	public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
		return super.getEffectivePermissions();
	}

	private PRPermission getPRPermission(String name) {
		PRPermission prPermission = null;

		boolean caseSensitive = plugin.getConfigManager().getBool("general.case-sensitive-permissions", false);

		for (PRPermission permission : this.prPlayer.getEffectivePermissions()) {
			if ((caseSensitive && permission.getName().equals(name))
					|| (!caseSensitive && permission.getName().equalsIgnoreCase(name))) {
				prPermission = permission;
				break;
			}
		}

		return prPermission;
	}
    
}
