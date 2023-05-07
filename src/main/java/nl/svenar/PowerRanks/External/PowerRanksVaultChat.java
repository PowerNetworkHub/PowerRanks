package nl.svenar.PowerRanks.External;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.api.PowerRanksAPI;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class PowerRanksVaultChat extends Chat {
	final PowerRanks plugin;
	final Users users;
	final PowerRanksAPI prapi;

	public PowerRanksVaultChat(PowerRanks plugin, Permission perms) {
		super(perms);
		this.plugin = plugin;
		this.users = new Users(plugin);
		this.prapi = new PowerRanksAPI(plugin);
	}

	@Override
	public String getName() {
		return this.plugin.getName();
	}

	@Override
	public boolean isEnabled() {
		return this.plugin.isEnabled();
	}

	@Override
	public boolean getGroupInfoBoolean(String worldName, String rankName, String permissionNode, boolean defaultValue) {
		// TODO: add support
		return false;
	}

	@Override
	public double getGroupInfoDouble(String worldName, String rankName, String permissionNode, double defaultValue) {
		// TODO: add support
		return 0;
	}

	@Override
	public int getGroupInfoInteger(String worldName, String rankName, String permissionNode, int defaultValue) {
		// TODO: add support
		return 0;
	}

	@Override
	public String getGroupInfoString(String worldName, String rankName, String permissionNode, String defaultValue) {
		// TODO: add support
		return null;
	}

	@Override
	public String getGroupPrefix(String worldName, String rankName) {
		return PowerRanks.chatColor(CacheManager.getRank(rankName).getPrefix(), true);
	}

	@Override
	public String getGroupSuffix(String worldName, String rankName) {
		return PowerRanks.chatColor(CacheManager.getRank(rankName).getSuffix(), true);
	}

	@Override
	public boolean getPlayerInfoBoolean(String worldName, String playerName, String permissionNode,
			boolean defaultValue) {
		// TODO: add support
		return false;
	}

	@Override
	public double getPlayerInfoDouble(String worldName, String playerName, String permissionNode, double defaultValue) {
		// TODO: add support
		return 0;
	}

	@Override
	public int getPlayerInfoInteger(String worldName, String playerName, String permissionNode, int defaultValue) {
		// TODO: add support
		return 0;
	}

	@Override
	public String getPlayerInfoString(String worldName, String playerName, String permissionNode, String defaultValue) {
		// TODO: add support
		return null;
	}

	@Override
	public String getPlayerPrefix(String worldName, String playerName) {
		List<PRRank> ranks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : CacheManager.getPlayer(playerName).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		return ranks.size() > 0 ? PowerRanks.chatColor(ranks.get(0).getPrefix(), true) : "";

		// return
		// PowerRanks.chatColor(CacheManager.getRank(CacheManager.getPlayer(playerName).getRank()).getPrefix(),
		// true);
	}

	@Override
	public String getPlayerSuffix(String worldName, String playerName) {
		List<PRRank> ranks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : CacheManager.getPlayer(playerName).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		return ranks.size() > 0 ? PowerRanks.chatColor(ranks.get(0).getSuffix(), true) : "";
		// return
		// PowerRanks.chatColor(CacheManager.getRank(CacheManager.getPlayer(playerName).getRank()).getSuffix(),
		// true);
	}

	@Override
	public void setGroupInfoBoolean(String worldName, String rankName, String permissionNode, boolean value) {
		// TODO: add support

	}

	@Override
	public void setGroupInfoDouble(String worldName, String rankName, String permissionNode, double value) {
		// TODO: add support

	}

	@Override
	public void setGroupInfoInteger(String worldName, String rankName, String permissionNode, int value) {
		// TODO: add support

	}

	@Override
	public void setGroupInfoString(String worldName, String rankName, String permissionNode, String value) {
		// TODO: add support

	}

	@Override
	public void setGroupPrefix(String worldName, String rankName, String value) {
		CacheManager.getRank(rankName).setPrefix(value);
	}

	@Override
	public void setGroupSuffix(String worldName, String rankName, String value) {
		CacheManager.getRank(rankName).setSuffix(value);
	}

	@Override
	public void setPlayerInfoBoolean(String worldName, String playerName, String permissionNode, boolean value) {
		// TODO: add support

	}

	@Override
	public void setPlayerInfoDouble(String worldName, String playerName, String permissionNode, double value) {
		// TODO: add support

	}

	@Override
	public void setPlayerInfoInteger(String worldName, String playerName, String permissionNode, int value) {
		// TODO: add support

	}

	@Override
	public void setPlayerInfoString(String worldName, String playerName, String permissionNode, String value) {
		// TODO: add support

	}

	@Override
	public void setPlayerPrefix(String worldName, String playerName, String value) {
		List<PRRank> ranks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : CacheManager.getPlayer(playerName).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		if (ranks.size() > 0) {
			ranks.get(0).setPrefix(value);
		}
		// CacheManager.getRank(CacheManager.getPlayer(playerName).getRank()).setPrefix(value);
	}

	@Override
	public void setPlayerSuffix(String worldName, String playerName, String value) {
		List<PRRank> ranks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : CacheManager.getPlayer(playerName).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		if (ranks.size() > 0) {
			ranks.get(0).setSuffix(value);
		}
		// CacheManager.getRank(CacheManager.getPlayer(playerName).getRank()).setSuffix(value);
	}
}