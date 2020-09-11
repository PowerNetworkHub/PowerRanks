package me.svenar.powerranks.core.data;

import java.util.ArrayList;
import java.util.UUID;

import me.svenar.powerranks.core.util.RankWeightSorter;

public class PowerRanksPlayer {

	private UUID uuid = new UUID(0, 0);
	private ArrayList<PowerRanksRank> ranks = new ArrayList<PowerRanksRank>();
	private boolean is_online = false;

	/**
	 * constructor
	 * 
	 * @param uuid
	 */
	public PowerRanksPlayer(UUID uuid) {
		set_uuid(uuid);
	}

	/**
	 * @return the player's UUID
	 */
	public UUID get_uuid() {
		return uuid;
	}

	/**
	 * @param uuid set the player's UUID
	 */
	public void set_uuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the ranks
	 */
	public ArrayList<PowerRanksRank> get_ranks() {
		return ranks;
	}

	/**
	 * @param ranks the ranks to set
	 */
	public void set_ranks(ArrayList<PowerRanksRank> ranks) {
		this.ranks = ranks;
		ranks.sort(new RankWeightSorter());
	}

	/**
	 * @param rank, the rank to add
	 */
	public void add_rank(PowerRanksRank rank) {
		this.ranks.add(rank);
		ranks.sort(new RankWeightSorter());
	}

	/**
	 * @param rank, the rank to remove
	 */
	public void remove_rank(PowerRanksRank rank) {
		this.ranks.remove(rank);
		ranks.sort(new RankWeightSorter());
	}

	/**
	 * @param permission
	 * @return true if the the permission is includes in one of the player's ranks
	 */
	public boolean has_permission(String permission) {
		for (PowerRanksRank rank : ranks) {
			if (rank.has_permission(permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return is online
	 */
	public boolean is_online() {
		return is_online;
	}

	/**
	 * @param is_online, the player's online status
	 */
	public void set_online(boolean is_online) {
		this.is_online = is_online;
	}
}
