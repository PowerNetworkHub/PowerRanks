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

package nl.svenar.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common methods to get the server software version and type
 * 
 * @author svenar
 */
public class ServerInfo {

    /**
     * Get the Minecraft version for this server
     * 
     * @param rawVersion
     * @return String with the Minecraft version that the server works with
     */
    public static String getServerVersion(String rawVersion) {
        try {
            Matcher matcher = Pattern.compile("\\d+\\.\\d+\\.?\\d?").matcher(rawVersion);

            List<String> results = new ArrayList<String>();
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    results.add(matcher.group(1));
                } else {
                    results.add(matcher.group());
                }
            }

            return results.get(0);
        } catch (Exception e) {
            return rawVersion;
        }
    }

    /**
     * Get the server software type (Eg. CraftBukkit, Spigot, Paper, Bungeecord,
     * Waterfall, etc...)
     * 
     * @param rawVersion
     * @return String with the name of the server software
     */
    public static String getServerType(String rawVersion) {
        try {
            Matcher matcher = Pattern.compile("[-:]\\w+-").matcher(rawVersion);

            List<String> results = new ArrayList<String>();
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    results.add(matcher.group(1));
                } else {
                    results.add(matcher.group());
                }
            }

            return results.get(0).replaceAll("-", "").replaceAll(":", "");
        } catch (Exception e) {
            return rawVersion;
        }
    }
}