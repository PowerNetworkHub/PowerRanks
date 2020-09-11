package me.svenar.powerranks.core.manager;

import java.util.ArrayList;

import me.svenar.powerranks.core.data.PowerRanksRank;
import me.svenar.powerranks.core.util.RankWeightSorter;

public class RankManager {
	
	private PowerRanksRank default_rank = null;
	private ArrayList<PowerRanksRank> ranks = new ArrayList<PowerRanksRank>();
	
	/**
	 * constructor
	 * 
	 * @param default_rank
	 */
	public RankManager(PowerRanksRank default_rank) {
		set_default_rank(default_rank);
	}

	/**
	 * @return the default rank
	 */
	public PowerRanksRank get_default_rank() {
		return default_rank;
	}

	/**
	 * @param default_rank the default rank to set
	 */
	public void set_default_rank(PowerRanksRank default_rank) {
		this.default_rank = default_rank;
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
}
