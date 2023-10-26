/**
 * This file is part of PowerRanks, licensed under the MIT License.
 *
 * Copyright (c) svenar (Sven) <powerranks@svenar.nl>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

package nl.svenar.powerranks.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.svenar.powerranks.common.structure.PRRank;

public class PRUtil {

    /**
     * Sort a list of ranks by weight
     * 
     * @param ranks
     */
    public static void sortRanksByWeight(List<PRRank> ranks) {
        ranks.removeIf(Objects::isNull);

        ranks.sort(Comparator.comparingInt(PRRank::getWeight));
    }

    /**
     * Reverse a list of ranks
     * 
     * @param ranks
     */
    public static void reverseRanks(List<PRRank> ranks) {
        Collections.reverse(ranks);
    }

    /**
     * Check if a list of strings contains a string (case insensitive)
     * 
     * @param objects
     * @param object
     * @return boolean
     */
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

    /**
     * Push an item to the end of an array
     * @param arr
     * @param item
     * @return T[] array
     */
	public static <T> T[] array_push(T[] arr, T item) {
		T[] tmp = Arrays.copyOf(arr, arr.length + 1);
		tmp[tmp.length - 1] = item;
		return tmp;
	}

    /**
     * Remove the last item from an array
     * 
     * @param arr
     * @return T[] array
     */
	public static <T> T[] array_pop(T[] arr) {
		T[] tmp = Arrays.copyOf(arr, arr.length - 1);
		return tmp;
	}

    /**
     * Generate a list of wildcard permissions
     * 
     * @param permission
     * @return ArrayList<String> wildcard permissions
     */
	public static ArrayList<String> generateWildcardList(String permission) {
		ArrayList<String> output = new ArrayList<String>();
		String[] permission_split = permission.split("\\.");

		output.add("*");

		permission_split = array_pop(permission_split);
		for (int i = 0; i < permission_split.length + 1; i++) {
			if (permission_split.length == 0)
				break;
			output.add(String.join(".", permission_split) + ".*");
			permission_split = array_pop(permission_split);
		}

		return output;
	}
}
