package nl.svenar.powerranks.core;

import nl.svenar.powerranks.core.manager.PlayerManager;
import nl.svenar.powerranks.core.manager.RankManager;

public class PowerRanksCore {

	private String plugin_name = "PowerRanks";
	private String plugin_server_type = "";
	private RankManager rank_manager = null;
	private PlayerManager player_manager = null;
	private PowerRanksCoreLogger log = null;
	
	/**
	 * constructor
	 * 
	 * @param plugin_server_type
	 */
	public PowerRanksCore(String plugin_server_type) {
		this.plugin_server_type = plugin_server_type;
	}
	
	public void set_logger(PowerRanksCoreLogger log) {
		this.log = log;
	}
	
	/**
	 * 
	 */
	public void setup() {
		log.info("");
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

	/**
	 * 
	 * @return plugin_server_type
	 */
	public String get_server_type() {
		return this.plugin_server_type;
	}
	
	/**
	 * 
	 * @return plugin_name
	 */
	public String get_name() {
		return this.plugin_name;
	}
}