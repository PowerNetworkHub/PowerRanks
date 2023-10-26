package nl.svenar.powerranks.common.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.svenar.powerranks.common.structure.PRRank;

public class PRUtil {

    public static void sortRanksByWeight(List<PRRank> ranks) {
        ranks.removeIf(Objects::isNull);

        ranks.sort(Comparator.comparingInt(PRRank::getWeight));
    }

    public static void reverseRanks(List<PRRank> ranks) {
        Collections.reverse(ranks);
    }

    public static boolean containsIgnoreCase(List<String> objects, String object) {
        for (String obj : objects) {
            if (obj.equalsIgnoreCase(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert a time string to seconds
     * 
     * @param time_input
     * @return
     */
    public static int timeStringToSecondsConverter(String time_input) {
        Matcher regex_int = Pattern.compile("^\\d+[^a-zA-Z]{0,1}$").matcher(time_input);

        Matcher regex_seconds = Pattern.compile("\\d+[sS]").matcher(time_input);
        Matcher regex_minutes = Pattern.compile("\\d+[mM]").matcher(time_input);
        Matcher regex_hours = Pattern.compile("\\d+[hH]").matcher(time_input);
        Matcher regex_days = Pattern.compile("\\d+[dD]").matcher(time_input);
        Matcher regex_weeks = Pattern.compile("\\d+[wW]").matcher(time_input);
        Matcher regex_years = Pattern.compile("\\d+[yY]").matcher(time_input);

        int seconds = 0;

        if (regex_int.find()) {
            seconds = Integer.parseInt(time_input);
        } else {
            if (regex_seconds.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_seconds.start(), regex_seconds.end() - 1));
            }

            if (regex_minutes.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_minutes.start(), regex_minutes.end() - 1)) * 60;
            }

            if (regex_hours.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_hours.start(), regex_hours.end() - 1))
                        * (60 * 60);
            }

            if (regex_days.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_days.start(), regex_days.end() - 1))
                        * (60 * 60 * 24);
            }

            if (regex_weeks.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_weeks.start(), regex_weeks.end() - 1))
                        * (60 * 60 * 24 * 7);
            }

            if (regex_years.find()) {
                seconds += Integer.parseInt(time_input.substring(regex_years.start(), regex_years.end() - 1))
                        * (60 * 60 * 24 * 365);
            }
        }

        return seconds;
    }
}
