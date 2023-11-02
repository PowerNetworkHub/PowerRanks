package nl.svenar.powerranks.nukkit;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.TextFormat;
import nl.svenar.powerranks.common.storage.PermissionRegistry;
import nl.svenar.powerranks.common.storage.PowerConfigManager;
import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.StorageLoader;
import nl.svenar.powerranks.common.storage.provided.YAMLConfigManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.commands.PowerCommandHandler;
import nl.svenar.powerranks.nukkit.events.OnChat;
import nl.svenar.powerranks.nukkit.events.OnJoin;
import nl.svenar.powerranks.nukkit.events.OnLeave;
import nl.svenar.powerranks.nukkit.manager.LanguageManager;
import nl.svenar.powerranks.nukkit.manager.UsertagManager;
import nl.svenar.powerranks.nukkit.permissible.PermissibleInjector;
import nl.svenar.powerranks.nukkit.util.NukkitPowerColor;
import nl.svenar.powerranks.nukkit.util.Util;

public class PowerRanks extends PluginBase {

    private static PowerRanks instance;

    private StorageLoader storageLoader;
    
    private PowerStorageManager storageManager;

    private PowerConfigManager configManager;

    private LanguageManager languageManager;

    private PowerConfigManager usertagStorage;

    private UsertagManager usertagManager;

    private PermissionRegistry permissionRegistry;

    private NukkitPowerColor powerColor = new NukkitPowerColor();

    private PluginLogger logger;

    private String chatPluginPrefix;

    public Instant pluginStartTime;

    public PowerRanks() {
        instance = this;
    }

    @Override
    public void onLoad() {
        this.logger = this.getLogger();

        pluginStartTime = Instant.now();

        Util.DATA_DIR = this.getDataFolder() + File.separator;
        PRUtil.createDir(Util.DATA_DIR);


        this.chatPluginPrefix = TextFormat.BLACK + "[" + TextFormat.AQUA + this.getDescription().getName() + TextFormat.BLACK + "]"
                + TextFormat.RESET + " ";

        this.permissionRegistry = new PermissionRegistry();

        // ===---------------------------------------------------------=== //
        // ===-------------------- LOAD ALL CONFIGS -------------------=== //
        // ===---------------------------------------------------------=== //
        logger.info("Loading config file");
        configManager = new YAMLConfigManager(Util.DATA_DIR, "config.yml", "config.yml");
        logger.info("Loading language file");
        languageManager = new LanguageManager();
        languageManager.setLanguage(configManager.getString("general.language", "en"));
        logger.info("Loading usertags file");
        usertagStorage = new YAMLConfigManager(Util.DATA_DIR, "usertags.yml");

        // ===---------------------------------------------------------=== //
        // ===--------------- LOAD PRPLAYERS & PRRANKS ----------------=== //
        // ===---------------------------------------------------------=== //
        logger.info("Loading player & rank data");
        this.storageLoader = new StorageLoader();
        PowerSQLConfiguration sqlConfig = new PowerSQLConfiguration();

        sqlConfig.setHost(configManager.getString("storage.mysql.host", "127.0.0.1"));
        sqlConfig.setPort(configManager.getInt("storage.mysql.port", 3306));
        sqlConfig.setDatabase(configManager.getString("storage.mysql.database", "powerranks"));
        sqlConfig.setUsername(configManager.getString("storage.mysql.username", "username"));
        sqlConfig.setPassword(configManager.getString("storage.mysql.password", "password"));
        sqlConfig.setUseSSL(configManager.getBool("storage.mysql.ssl", false));
        sqlConfig.setTableRanks("ranks");
        sqlConfig.setTablePlayers("players");
        sqlConfig.setTableMessages("messages");
        sqlConfig.setSilentErrors(configManager.getBool("storage.mysql.verbose", false));

        this.storageManager = storageLoader.getStorageManager(Util.DATA_DIR,
                configManager.getString("storage.type", "yaml"), sqlConfig);

        this.storageLoader.loadData(storageManager);

        // ===---------------------------------------------------------=== //
        // ===------------------- REGISTER COMMANDS -------------------=== //
        // ===---------------------------------------------------------=== //
        this.getServer().getCommandMap().register("powerranks", new PowerCommandHandler(this, "powerranks"));

    }

    @Override
    public void onEnable() {

        // ===---------------------------------------------------------=== //
        // ===-------------------- REGISTER EVENTS --------------------=== //
        // ===---------------------------------------------------------=== //
        getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new OnLeave(this), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);

        // ===---------------------------------------------------------=== //
        // ===---------------- INJECT EXISTING PLAYERS ----------------=== //
        // ===---------------------------------------------------------=== //
        for (Entry<UUID, Player> playerData : this.getServer().getOnlinePlayers().entrySet()) {
            PermissibleInjector.inject(this, playerData.getValue());
        }

        // ===---------------------------------------------------------=== //
        // ===---------------- ALL DONE & SHOW STARTUP ----------------=== //
        // ===---------------------------------------------------------=== //
        logger.info("");
        logger.info(TextFormat.AQUA + "  ██████  ██████ " + TextFormat.GREEN + "  PowerRanks v" + this.getDescription().getVersion());
        logger.info(TextFormat.AQUA + "  ██   ██ ██   ██" + TextFormat.GREEN + "  Running on " + Util.getServerType(getServer()) + " v" + Util.getServerVersion(getServer()));
        logger.info(TextFormat.AQUA + "  ██████  ██████ " + TextFormat.GREEN + "  Startup time: " + Duration.between(pluginStartTime, Instant.now()).toMillis() + "ms");
        logger.info(TextFormat.AQUA + "  ██      ██   ██" + TextFormat.GREEN + "  Loaded " + PRCache.getRanks().size() + " ranks and " + PRCache.getPlayers().size() + " players (" + getConfigManager().getString("storage.type", "yaml").toUpperCase() + ") "); // TODO : update_available message
        logger.info(TextFormat.AQUA + "  ██      ██   ██" + "  " + TextFormat.RED + (System.getProperty("POWERRANKSRUNNING", "").equals("TRUE") ? "Reload detected, why do you hate yourself :C" : ""));
        logger.info("");

        System.setProperty("POWERRANKSRUNNING", "TRUE");
    }

    @Override
    public void onDisable() {
        logger.info("Saving config file");
        configManager.save();
        logger.info("Saving language file");
        languageManager.save();
        logger.info("Saving usertags file");
        usertagStorage.save();

        this.logger.info("Saving player & rank data");
        if (this.storageLoader != null && this.storageManager != null) {
            this.storageLoader.saveData(this.storageManager);
        }

        if (this.getDescription() != null) {
            this.logger.info("Disabled " + this.getDescription().getName() + " v" + this.getDescription().getVersion());
        } else {
            this.logger.info("Disabled " + this.getClass().getSimpleName());
        }
    }

    public PowerConfigManager getConfigManager() {
        return configManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public PowerConfigManager getUsertagStorage() {
        return usertagStorage;
    }

    public UsertagManager getUsertagManager() {
        return usertagManager;
    }

    public PermissionRegistry getPermissionRegistry() {
        return permissionRegistry;
    }

    public NukkitPowerColor getPowerColor() {
        return powerColor;
    }

    public String getChatPluginPrefix() {
        return chatPluginPrefix;
    }

    public PowerStorageManager getStorageManager() {
        return storageManager;
    }

    public StorageLoader getStorageLoader() {
        return storageLoader;
    }

    public static PowerRanks getInstance() {
        return instance;
    }

    public void factoryReset(CommandSender sender) {
        PRCache.setRanks(new ArrayList<PRRank>());
        PRCache.setPlayers(new ArrayList<PRPlayer>());

        PRUtil.deleteDir(new File(Util.DATA_DIR));
        PRUtil.createDir(Util.DATA_DIR);

        configManager = new YAMLConfigManager(Util.DATA_DIR, "config.yml", "config.yml");
        languageManager = new LanguageManager();
        languageManager.setLanguage(configManager.getString("general.language", "en"));

        PowerConfigManager ranksManager = new YAMLConfigManager(Util.DATA_DIR, "ranks.yml");
        PowerConfigManager playersManager = new YAMLConfigManager(Util.DATA_DIR, "players.yml");
        ranksManager.save();
        playersManager.save();

        getStorageLoader().loadData(getStorageManager());

        for (Player player : this.getServer().getOnlinePlayers().values()) {
            PermissibleInjector.inject(this, player);
        }
    }
}