package nl.svenar.powerranks.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powerranks.common.PowerRanksBase;

public class PowerRanks extends JavaPlugin {

    private PowerRanksBase prBase;

    @Override
    public void onEnable() {
        this.prBase = new PowerRanksBase(getLogger(), getServer().getVersion(), getDescription().getName(), getDescription().getVersion(), "bukkit");
        this.prBase.onEnable();
    }

    @Override
    public void onDisable() {
        this.prBase.onDisable();
    }

    // private PowerStorageManager mainConfig, langConfig;
    // private static PowerRanks instance;
    // private Instant startup_time;

    // @Override
    // public void onEnable() {
    //     instance = this;

    //     startup_time = Instant.now();

    //     Logger log = getLogger();
    //     PowerLogger.setLogger(log);

    //     loadPowerConfig();
    //     // loadDataStorage();

    //     showStartupInfo();

    //     System.setProperty("POWERRANKSENABLED", "TRUE");
    // }

    // @Override
    // public void onDisable() {
    //     Instant saveTime = Instant.now();
    //     PowerLogger.info("Saving to storage...");
    //     // saveDataStorage();
    //     savePowerConfig();
    //     PowerLogger.info("Done saving to storage in " + Duration.between(saveTime, Instant.now()).toMillis() + "ms!");

    //     PowerLogger.info("PowerRanks has been disabled!");
    // }

    // private void showStartupInfo() {
    //     String preload_message = "Loaded TODO ranks and TODO players";

    //     // String preload_message = "Loaded "
    //     // + this.rankStorage.getMap("ranks", new HashMap<String,
    //     // Object>()).keySet().size() + " ranks and "
    //     // + this.playerStorage.getMap("players", new HashMap<String,
    //     // Object>()).keySet().size() + " players";

    //     String storageType = "MYSQL";

    //     List<String> logoLines = new ArrayList<String>();
    //     logoLines.add("  ██████  ██████ ");
    //     logoLines.add("  ██   ██ ██   ██");
    //     logoLines.add("  ██████  ██████ ");
    //     logoLines.add("  ██      ██   ██");
    //     logoLines.add("  ██      ██   ██");
    //     Iterator<String> logoLinesIterator = logoLines.iterator();

    //     List<String> messageLines = new ArrayList<String>();
    //     messageLines.add(getDescription().getName() + " v" + getDescription().getVersion());
    //     messageLines.add("Running on " + ServerInfo.getServerType(getServer().getVersion()) + " v"
    //             + ServerInfo.getServerVersion(getServer().getVersion()) + " (Java: "
    //             + VersionUtils.getJavaVersion() + ")");
    //     messageLines.add("Startup time: " + Duration.between(startup_time, Instant.now()).toMillis() + "ms");
    //     messageLines.add(preload_message + " (" + storageType + ")");
    //     messageLines.add(System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
    //             ? "Reload detected, why do you hate yourself? :C"
    //             : "");
    //     Iterator<String> messageLinesIterator = messageLines.iterator();

    //     PowerLogger.info("");
    //     while (logoLinesIterator.hasNext()) {
    //         PowerLogger.info(
    //                 ChatColor.AQUA + logoLinesIterator.next() + "  " + ChatColor.GREEN + messageLinesIterator.next());
    //     }
    //     PowerLogger.info("");
    // }

    // private void loadPowerConfig() {
    //     try {
    //         String directory = "plugins" + File.separator + Strings.pluginName;
    //         this.mainConfig = new PowerYamlManager(directory, "config.yml", "/static/bungeecord/config.yml");
    //         this.langConfig = new PowerYamlManager(directory, "lang.yml", "/static/bungeecord/lang.yml");
    //     } catch (IOException e) {
    //         PowerLogger.exception(e.getMessage());
    //     }
    // }

    // private void savePowerConfig() {
    //     try {
    //         this.mainConfig.save();
    //         this.langConfig.save();
    //     } catch (IOException e) {
    //         PowerLogger.exception(e.getMessage());
    //     }
    // }

    // // private void loadDataStorage() {
    // // String mysqlHost = mainConfig.getString("storage.mysql.host", "127.0.0.1");
    // // int mysqlPort = mainConfig.getInt("storage.mysql.port", 3306);
    // // String mysqlDatabase = mainConfig.getString("storage.mysql.database",
    // // "powerranks");
    // // String mysqlUsername = mainConfig.getString("storage.mysql.username",
    // // "dbUser");
    // // String mysqlPassword = mainConfig.getString("storage.mysql.password",
    // // "dbPassword");
    // // SQLConnectionData mysqlData = new SQLConnectionData(mysqlHost, mysqlPort,
    // // mysqlDatabase, mysqlUsername,
    // // mysqlPassword);

    // // try {
    // // this.rankStorage = new PowerMySQLManager(mysqlData, "ranks");
    // // this.playerStorage = new PowerMySQLManager(mysqlData, "players");
    // // this.generalStorage = new PowerMySQLManager(mysqlData, "general");
    // // } catch (SQLException e) {
    // // PowerLogger.exception(e.getMessage());
    // // }
    // // }

    // // private void saveDataStorage() {
    // // try {
    // // this.rankStorage.save();
    // // this.playerStorage.save();
    // // this.generalStorage.save();
    // // } catch (IOException e) {
    // // PowerLogger.exception(e.getMessage());
    // // }
    // // }

    // // public PowerStorageManager getRankStorage() {
    // // return this.rankStorage;
    // // }

    // // public PowerStorageManager getPlayerStorage() {
    // // return this.playerStorage;
    // // }

    // // public PowerStorageManager getGeneralStorage() {
    // // return this.generalStorage;
    // // }

    // public static PowerRanks getInstance() {
    //     return PowerRanks.instance;
    // }

    // private PowerStorageManager mainConfig, langConfig, rankStorage, playerStorage, generalStorage;
    // private static PowerRanks instance;
    // private Instant startup_time;

    // @Override
    // public void onEnable() {
    //     instance = this;

    //     startup_time = Instant.now();

    //     Logger log = getLogger();
    //     PowerLogger.setLogger(log);

    //     loadPowerConfig();
    //     loadDataStorage();

    //     showStartupInfo();

    //     PowerLogger.info(rankStorage.getKV("ranks.TestRank.permissions", "").toString() + " --- " + rankStorage.getKV("ranks.TestRank.permissions", "").getClass().getCanonicalName());

    //     // PRRank testRank = new PRRank("TestRank", 69, 42069L);
    //     // PRPermission permission = new PRPermission("powerranks.command.help", true, 123456789L);
    //     // permission.addProperty("test.property", "value");
    //     // testRank.addPermission(permission);
    //     // testRank.addPermission(new PRPermission("powerranks.command.admin", false, 9876543210L));
    //     // testRank.addProperty("powerchat.prefix", "PREFIX");
    //     // testRank.addProperty("powerchat.suffix", "SUFFIX");

    //     // // PowerLogger.warning(new PowerStorageSerializer().convertToMap(testRank).toString());

    //     // try {
    //     //     PowerStorageManager tmpyml = new PowerYamlManager("plugins" + File.separator + Strings.pluginName,
    //     //             "test.yml");
    //     //     PowerStorageManager tmpjson = new PowerJsonManager("plugins" + File.separator + Strings.pluginName,
    //     //             "test.json");

    //     //     tmpyml.setMap("ranks", new PowerStorageSerializer().convertToMap(testRank));
    //     //     tmpyml.save();
    //     //     tmpjson.setMap("ranks", new PowerStorageSerializer().convertToMap(testRank));
    //     //     tmpjson.save();
    //     //     rankStorage.setMap("ranks", new PowerStorageSerializer().convertToMap(testRank));
    //     //     rankStorage.save();
    //     // } catch (Exception e) {
    //     //     e.printStackTrace();
    //     // }

    //     System.setProperty("POWERRANKSENABLED", "TRUE");
    // }

    // @Override
    // public void onDisable() {
    //     Instant saveTime = Instant.now();
    //     PowerLogger.info("Saving to storage...");
    //     saveDataStorage();
    //     savePowerConfig();
    //     PowerLogger.info("Done saving to storage in " + Duration.between(saveTime, Instant.now()).toMillis() + "ms!");

    //     PowerLogger.info("PowerRanks has been disabled!");
    // }

    // private void showStartupInfo() {
    //     String preload_message = "Loaded "
    //             + this.rankStorage.getMap("ranks", new HashMap<String, Object>()).keySet().size() + " ranks and "
    //             + this.playerStorage.getMap("players", new HashMap<String, Object>()).keySet().size() + " players";

    //     String storageType = "Unknown";
    //     String rawStorageType = this.mainConfig.getString("storage.type", "yaml");
    //     storageType = rawStorageType.equalsIgnoreCase("yaml") || rawStorageType.equalsIgnoreCase("yml") ? "YAML"
    //             : storageType;
    //     storageType = rawStorageType.equalsIgnoreCase("json") ? "JSON" : storageType;
    //     storageType = rawStorageType.equalsIgnoreCase("mysql") ? "MYSQL" : storageType;
    //     storageType = rawStorageType.equalsIgnoreCase("sqlite") ? "SQLITE" : storageType;

    //     List<String> logoLines = new ArrayList<String>();
    //     logoLines.add("  ██████  ██████ ");
    //     logoLines.add("  ██   ██ ██   ██");
    //     logoLines.add("  ██████  ██████ ");
    //     logoLines.add("  ██      ██   ██");
    //     logoLines.add("  ██      ██   ██");
    //     Iterator<String> logoLinesIterator = logoLines.iterator();

    //     List<String> messageLines = new ArrayList<String>();
    //     messageLines.add(getDescription().getName() + " v" + getDescription().getVersion());
    //     messageLines.add("Running on " + ServerInfo.getServerType(getServer().getVersion()) + " v"
    //             + ServerInfo.getServerVersion(getServer().getVersion()) + " (Java: " + VersionUtils.getJavaVersion()
    //             + ")");
    //     messageLines.add("Startup time: " + Duration.between(startup_time, Instant.now()).toMillis() + "ms");
    //     messageLines.add(preload_message + " (" + storageType + ")");
    //     messageLines.add(System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
    //             ? "Reload detected, why do you hate yourself? :C"
    //             : "");
    //     Iterator<String> messageLinesIterator = messageLines.iterator();

    //     PowerLogger.info("");
    //     while (logoLinesIterator.hasNext()) {
    //         PowerLogger.info(
    //                 ChatColor.AQUA + logoLinesIterator.next() + "  " + ChatColor.GREEN + messageLinesIterator.next());
    //     }
    //     PowerLogger.info("");
    // }

    // private void loadPowerConfig() {
    //     String directory = "plugins" + File.separator + Strings.pluginName;
    //     try {
    //         this.mainConfig = new PowerYamlManager(directory, "config.yml", "/static/bukkit/config.yml");
    //         this.langConfig = new PowerYamlManager(directory, "lang.yml", "/static/bukkit/lang.yml");
    //     } catch (IOException e) {
    //         PowerLogger.exception(e.getMessage());
    //     }
    // }

    // private void savePowerConfig() {
    //     try {
    //         this.mainConfig.save();
    //         this.langConfig.save();
    //     } catch (IOException e) {
    //         PowerLogger.exception(e.getMessage());
    //     }
    // }

    // private void loadDataStorage() {
    //     String directory = "plugins" + File.separator + Strings.pluginName;

    //     String storageType = mainConfig.getString("storage.type", "yaml").toLowerCase();

    //     switch (storageType) {
    //         case "json":
    //             try {
    //                 this.rankStorage = new PowerJsonManager(directory, "ranks.json");
    //                 this.playerStorage = new PowerJsonManager(directory, "players.json");
    //             } catch (IOException e) {
    //                 PowerLogger.exception(e.getMessage());
    //             }
    //             break;

    //         case "mysql":
    //             String mysqlHost = mainConfig.getString("storage.mysql.host", "127.0.0.1");
    //             int mysqlPort = mainConfig.getInt("storage.mysql.port", 3306);
    //             String mysqlDatabase = mainConfig.getString("storage.mysql.database", "powerranks");
    //             String mysqlUsername = mainConfig.getString("storage.mysql.username", "dbUser");
    //             String mysqlPassword = mainConfig.getString("storage.mysql.password", "dbPassword");
    //             SQLConnectionData mysqlData = new SQLConnectionData(mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername,
    //                     mysqlPassword);

    //             try {
    //                 this.rankStorage = new PowerMySQLManager(mysqlData, "ranks");
    //                 this.playerStorage = new PowerMySQLManager(mysqlData, "players");
    //                 if (this.mainConfig.getBool("network.bungeecord", false)) {
    //                     this.generalStorage = new PowerMySQLManager(mysqlData, "general");
    //                 }
    //             } catch (SQLException e) {
    //                 PowerLogger.exception(e.getMessage());
    //             }
    //             break;

    //         case "sqlite":
    //             try {
    //                 this.rankStorage = new PowerSQLiteManager(directory, "ranks.db", "ranks");
    //                 this.playerStorage = new PowerSQLiteManager(directory, "players.db", "players");
    //             } catch (IOException e) {
    //                 PowerLogger.exception(e.getMessage());
    //             } catch (SQLException e) {
    //                 PowerLogger.exception(e.getMessage());
    //                 e.printStackTrace();
    //             }
    //             break;

    //         case "yml":
    //         case "yaml":
    //         default:
    //             try {
    //                 this.rankStorage = new PowerYamlManager(directory, "ranks.yml");
    //                 this.playerStorage = new PowerYamlManager(directory, "players.yml");
    //             } catch (IOException e) {
    //                 PowerLogger.exception(e.getMessage());
    //             }
    //             break;
    //     }

    // }

    // private void saveDataStorage() {
    //     try {
    //         this.rankStorage.save();
    //         this.playerStorage.save();
    //         if (!Objects.isNull(generalStorage)) {
    //             this.generalStorage.save();
    //         }
    //     } catch (IOException e) {
    //         PowerLogger.exception(e.getMessage());
    //     }
    // }

    // public PowerStorageManager getRankStorage() {
    //     return this.rankStorage;
    // }

    // public PowerStorageManager getPlayerStorage() {
    //     return this.playerStorage;
    // }

    // public PowerStorageManager getGeneralStorage() {
    //     return this.generalStorage;
    // }

    // public static PowerRanks getInstance() {
    //     return PowerRanks.instance;
    // }
}
