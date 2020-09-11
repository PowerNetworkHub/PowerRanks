package me.svenar.powerranks.core;

import me.svenar.powerranks.core.manager.PlayerManager;
import me.svenar.powerranks.core.manager.RankManager;

public class PowerRanksCore {

	private String plugin_server_type = "";
	private RankManager rank_manager = null;
	private PlayerManager player_manager = null;
	
	/**
	 * constructor
	 * 
	 * @param plugin_server_type
	 */
	public PowerRanksCore(String plugin_server_type) {
		this.plugin_server_type = plugin_server_type;
		
		
	}
	
	/**
	 * 
	 */
	public void setup() {
		this.rank_manager = new RankManager(null);
		this.player_manager = new PlayerManager(this.rank_manager);
	}

	/**
	 * @return the ranks_manager
	 */
	public RankManager get_rank_manager() {
		return rank_manager;
	}

	/**
	 * @return the player_manager
	 */
	public PlayerManager get_player_manager() {
		return player_manager;
	}
}