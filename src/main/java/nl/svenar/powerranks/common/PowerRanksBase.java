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

package nl.svenar.powerranks.common;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import nl.svenar.powerranks.common.storage.PowerConfigManager;
import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.MongoDBStorageManager;
import nl.svenar.powerranks.common.storage.provided.YAMLConfigManager;
import nl.svenar.powerranks.common.storage.provided.YAMLStorageManager;
import nl.svenar.powerranks.common.utils.ServerInfo;
import nl.svenar.powerranks.common.utils.VersionUtils;

public class PowerRanksBase {

    private Instant startTime;
    private String serverVersion, pluginName, pluginVersion, pluginType;
    private PowerConfigManager mainConfig, langConfig;
    private PowerStorageManager storageManager;

    public PowerRanksBase(Logger logger, String serverVersion, String pluginName, String pluginVersion,
            String pluginType) {
        this.serverVersion = serverVersion;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.pluginType = pluginType;
        this.startTime = Instant.now();
        new PowerLogger(logger);
    }

    public void onEnable() {
        this.mainConfig = new YAMLConfigManager("dir", "config.yml");
        this.langConfig = new YAMLConfigManager("dir", "lang.yml");

        String host = "prmysql";
        int port = 27017;
        String database = "powerranks";
        String username = "";
        String password = "";

        PowerSQLConfiguration sqlConfig = new PowerSQLConfiguration(host, port, database, username, password, "ranks",
                "players");
        this.storageManager = new MongoDBStorageManager(sqlConfig, false);

        // this.storageManager = new YAMLStorageManager("dir", "ranks.yml",
        // "players.yml"); // TODO
        this.storageManager.loadAll();

        showStartupInfo();
    }

    public void onDisable() {
        this.mainConfig.save();
        this.langConfig.save();

        if (Objects.nonNull(this.storageManager)) {
            this.storageManager.saveAll();
        } else {
            PowerLogger.severe("Error saving ranks and players to the filesystem!");
            PowerLogger.severe("Data might be lost.");
        }
    }

    private void showStartupInfo() {
        String preload_message = "Loaded " + this.storageManager.getRanks().size() + " ranks and "
                + this.storageManager.getPlayers().size() + " players";

        String storageType = this.storageManager.getType();

        List<String> logoLines = new ArrayList<String>();
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██   ██ ██   ██");
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██      ██   ██");
        logoLines.add("  ██      ██   ██");
        Iterator<String> logoLinesIterator = logoLines.iterator();

        List<String> messageLines = new ArrayList<String>();
        messageLines.add(this.pluginName + " (" + pluginType + ") v" + this.pluginVersion);
        messageLines.add("Running on " + ServerInfo.getServerType(this.serverVersion) + " v"
                + ServerInfo.getServerVersion(this.serverVersion) + " (Java: " + VersionUtils.getJavaVersion() + ")");
        messageLines.add("Startup time: " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
        messageLines.add(preload_message + " (" + storageType + ")");
        messageLines.add(System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
                ? "Reload detected, why do you hate yourself? :C"
                : "");
        Iterator<String> messageLinesIterator = messageLines.iterator();

        PowerLogger.info("");
        while (logoLinesIterator.hasNext()) {
            try {
                PowerLogger.info(org.bukkit.ChatColor.AQUA + logoLinesIterator.next() + "  "
                        + org.bukkit.ChatColor.GREEN + messageLinesIterator.next());
            } catch (NoClassDefFoundError ex) {
                PowerLogger.info(net.md_5.bungee.api.ChatColor.AQUA + logoLinesIterator.next() + "  "
                        + net.md_5.bungee.api.ChatColor.GREEN + messageLinesIterator.next());
            }
        }
        PowerLogger.info("");
    }
}
