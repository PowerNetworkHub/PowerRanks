package me.svenar.powerranks.core.util;

import java.util.Comparator;

import me.svenar.powerranks.core.data.PowerRanksRank;

/**
 * Used for sorting the player's ranks based on the ranks weight
 */
public class RankWeightSorter implements Comparator<PowerRanksRank> {
	@Override
	public int compare(PowerRanksRank rank1, PowerRanksRank rank2) {
		return Integer.valueOf(rank2.get_weight()).compareTo(rank1.get_weight());
	}
}
