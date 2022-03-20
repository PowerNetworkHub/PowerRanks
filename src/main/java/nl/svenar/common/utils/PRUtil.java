package nl.svenar.common.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.svenar.common.structure.PRRank;

public class PRUtil {

    public static List<PRRank> sortRanksByWeight(List<PRRank> ranks) {
        for (PRRank rank : ranks) {
            if (rank == null) {
                return ranks;
            }
        }
        Comparator<PRRank> compareByWeight = (PRRank o1, PRRank o2) -> Integer.compare(o1.getWeight(), o2.getWeight());
        Collections.sort(ranks, compareByWeight);

        return ranks;
    }

    public static List<PRRank> reverseRanks(List<PRRank> ranks) {
        Collections.reverse(ranks);
        return ranks;
    }
}
