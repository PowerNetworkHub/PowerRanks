package nl.svenar.powerranks.core.manager;

import java.util.ArrayList;
import java.util.UUID;

import nl.svenar.powerranks.core.data.PowerRanksPlayer;

public class PlayerManager {
	
	private RankManager rank_manager = null;
	private ArrayList<PowerRanksPlayer> players = new ArrayList<PowerRanksPlayer>();
	
	/**
	 * constructor
	 * 
	 * @param rank_manager
	 */
	public PlayerManager(RankManager rank_manager) {
		this.rank_manager = rank_manager;
	}
	
	/**
	 * @return the players
	 */
	public ArrayList<PowerRanksPlayer> get_players() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void set_players(ArrayList<PowerRanksPlayer> players) {
		this.players = players;
	}
	
	/**
	 * Add a player to the player list
	 * 
	 * @param player
	 */
	public void add_player(PowerRanksPlayer player) {
		this.players.add(player);
	}
	
	/**
	 * Remove a player from the player list
	 * 
	 * @param player
	 */
	public void remove_player(PowerRanksPlayer player) {
		this.players.remove(player);
	}
	
	/**
	 * Create a new player with the default rank
	 * 
	 * @param uuid
	 */
	public void create_player(UUID uuid) {
		PowerRanksPlayer player = new PowerRanksPlayer(uuid);
		player.add_rank(rank_manager.get_default_rank());
		add_player(player);
	}
}
