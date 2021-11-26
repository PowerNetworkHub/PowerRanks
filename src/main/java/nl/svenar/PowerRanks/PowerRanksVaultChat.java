package nl.svenar.PowerRanks;

import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.api.PowerRanksAPI;

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
	public boolean getGroupInfoBoolean(String worldName, String rankName, String node, boolean defaultValue) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public double getGroupInfoDouble(String worldName, String rankName, String node, double defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getGroupInfoInteger(String worldName, String rankName, String node, int defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getGroupInfoString(String worldName, String rankName, String node, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getGroupPrefix(String worldName, String rankName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getGroupSuffix(String worldName, String rankName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean getPlayerInfoBoolean(String worldName, String playerName, String node, boolean defaultValue) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public double getPlayerInfoDouble(String worldName, String playerName, String node, double defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getPlayerInfoInteger(String worldName, String playerName, String node, int defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getPlayerInfoString(String worldName, String playerName, String node, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPlayerPrefix(String worldName, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPlayerSuffix(String worldName, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setGroupInfoBoolean(String worldName, String rankName, String node, boolean value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoDouble(String worldName, String rankName, String node, double value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoInteger(String worldName, String rankName, String node, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoString(String worldName, String rankName, String node, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupPrefix(String worldName, String rankName, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupSuffix(String worldName, String rankName, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoBoolean(String worldName, String playerName, String node, boolean value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoDouble(String worldName, String playerName, String node, double value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoInteger(String worldName, String playerName, String node, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoString(String worldName, String playerName, String node, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerPrefix(String worldName, String playerName, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerSuffix(String worldName, String playerName, String value) {
		// TODO Auto-generated method stub
		
	}
}