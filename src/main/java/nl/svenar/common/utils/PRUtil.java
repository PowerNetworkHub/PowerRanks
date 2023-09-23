package nl.svenar.common.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import nl.svenar.common.structure.PRRank;

public class PRUtil {

    public static void sortRanksByWeight(List<PRRank> ranks) {
        ranks.removeIf(Objects::isNull);

        ranks.sort(Comparator.comparingInt(PRRank::getWeight));
    }

    public static List<PRRank> reverseRanks(List<PRRank> ranks) {
        Collections.reverse(ranks);
        return ranks;
    }

    public static boolean containsIgnoreCase(List<String> objects, String object) {
        for (String obj : objects) {
            if (obj.equalsIgnoreCase(object)) {
                return true;
            }
        }
        return false;
    }
}
