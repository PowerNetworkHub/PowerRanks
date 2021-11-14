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

/**
 * Common methods to get information about the host's Java version and calculate
 * a integer version that is easily available for calculation.
 * 
 * @author svenar
 */
public class VersionUtils {

    /**
     * Get the host's JVM version
     * 
     * @return Integer of the JVM verion
     */
    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

    /**
     * Convert a verson string (Eg. 1.2 or 1.2.3) into a usable number to check if a
     * version is older or newer
     * 
     * @param input
     * @return
     */
    public static int calculateVersionFromString(String input) {
        int output = 0;
        input = input.split("R")[0].replaceAll("[a-zA-Z- ]", "");
        String[] input_split = input.split("\\.");

        String calcString = "1000000";
        for (int i = 0; i < input_split.length; i++) {
            if (input_split[i].length() != 0) {
                int num = Integer.parseInt(input_split[i]) * Integer.parseInt(calcString);
                if (calcString.charAt(calcString.length() - 1) == '0') {
                    calcString = calcString.substring(0, calcString.length() - 1);
                }
                output += num;
            }
        }

        return output;
    }
}