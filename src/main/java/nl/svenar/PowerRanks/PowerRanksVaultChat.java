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
	public boolean getGroupInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public double getGroupInfoDouble(String arg0, String arg1, String arg2, double arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getGroupInfoInteger(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getGroupInfoString(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getGroupPrefix(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getGroupSuffix(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean getPlayerInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public double getPlayerInfoDouble(String arg0, String arg1, String arg2, double arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getPlayerInfoInteger(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String getPlayerInfoString(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPlayerPrefix(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPlayerSuffix(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setGroupInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoDouble(String arg0, String arg1, String arg2, double arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoInteger(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupInfoString(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupPrefix(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGroupSuffix(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoBoolean(String arg0, String arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoDouble(String arg0, String arg1, String arg2, double arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoInteger(String arg0, String arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerInfoString(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerPrefix(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPlayerSuffix(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}
}