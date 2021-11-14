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

package nl.svenar.common;

import java.util.Objects;
import java.util.logging.Logger;

public class PowerLogger {
    private static Logger logger;

    /**
     * Create a instance without setting a logger. Calling setLogger(logger) is
     * required in order to log data.
     */
    public PowerLogger() {
    }

    /**
     * Create a instance and setting a logger.
     * 
     * @param logger
     */
    public PowerLogger(Logger logger) {
        setLogger(logger);
    }

    /**
     * Set a logging instance.
     * 
     * @param logger
     */
    public static void setLogger(Logger logger) {
        PowerLogger.logger = logger;
    }

    /**
     * Log a message with level 'info'
     * 
     * @param message
     */
    public static void info(String message) {
        if (Objects.isNull(PowerLogger.logger))
            return;

        PowerLogger.logger.info(message);
    }

    /**
     * Log a message with level 'warning'
     * 
     * @param message
     */
    public static void warning(String message) {
        if (Objects.isNull(PowerLogger.logger))
            return;

        PowerLogger.logger.warning(message);
    }

    /**
     * Log a message with level 'severe'
     * 
     * @param message
     */
    public static void severe(String message) {
        if (Objects.isNull(PowerLogger.logger))
            return;

        PowerLogger.logger.severe(message);
    }

    /**
     * Basic exception message logger
     * 
     * @param message
     */
    public static void exception(String message) {
        PowerLogger.warning("===-------------------------===");
        PowerLogger.warning("An exception occured!");
        PowerLogger.warning("");
        PowerLogger.warning("Exception:");
        PowerLogger.severe(message);
        PowerLogger.warning("===-------------------------===");
    }
}
