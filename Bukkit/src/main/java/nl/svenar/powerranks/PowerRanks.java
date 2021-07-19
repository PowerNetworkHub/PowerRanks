package nl.svenar.powerranks;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import nl.svenar.powerranks.api.PowerRanksAPI;
import nl.svenar.powerranks.commands.CommandHelp;
import nl.svenar.powerranks.commands.CommandStats;
import nl.svenar.powerranks.commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.powerranks.commands.player.CommandPlayer;
import nl.svenar.powerranks.commands.rank.CommandRank;
import nl.svenar.powerranks.configuration.CoreConfig;
import nl.svenar.powerranks.configuration.LangConfig;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.events.JoinEvent;
import nl.svenar.powerranks.events.LeaveEvent;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.handlers.PowerCommandHandler;
import nl.svenar.powerranks.hooks.PowerRanksPlaceholderExpansion;
import nl.svenar.powerranks.hooks.VaultHook;
import nl.svenar.powerranks.metrics.Metrics;
import nl.svenar.powerranks.storage.MYSQLDataHandler;
import nl.svenar.powerranks.storage.SQLITEDataHandler;
import nl.svenar.powerranks.storage.YAMLDataHandler;
import nl.svenar.powerranks.updater.PRV1Converter;
import nl.svenar.powerranks.updater.Updater;
import nl.svenar.powerranks.updater.Updater.UpdateResult;
import nl.svenar.powerranks.updater.Updater.UpdateType;
import nl.svenar.powerranks.utils.ErrorManager;
import nl.svenar.powerranks.utils.PowerFormatter;
import nl.svenar.powerranks.utils.ServerInfo;
import nl.svenar.powerranks.utils.VersionUtils;

public class PowerRanks extends JavaPlugin {

    private static PowerRanks instance;
    private static PowerRanksAPI apiInstance;

    private CoreConfig coreConfig;
    private LangConfig langConfig;

    private BaseDataHandler currentStorageHandler;
    private Instant startup_time;

    public static PowerRanksPlaceholderExpansion placeholderapiExpansion;

    @Override
    public void onEnable() {
        PowerRanks.instance = this;
        PowerRanks.apiInstance = new PowerRanksAPI();

        if (System.getProperty("POWERRANKSENABLED", "").equals("TRUE")) {
            getLogger().info("Error enabling " + getDescription().getName() + " v" + getDescription().getVersion()
                    + ": plugin already enabled");
        }

        startup_time = Instant.now();

        this.coreConfig = new CoreConfig(this, "config");
        this.langConfig = new LangConfig(this, getCoreConfig().getLanguage());

        Bukkit.getServer().getPluginManager().registerEvents((Listener) new JoinEvent(this), (Plugin) this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener) new LeaveEvent(this), (Plugin) this);

        Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new PowerCommandHandler(this));
        Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter((TabCompleter) new PowerCommandHandler(this));

        // Load all base commands //
        PowerCommandHandler.addPowerCommand("help", new CommandHelp(this, COMMAND_EXECUTOR.ALL, true));
        PowerCommandHandler.addPowerCommand("?", new CommandHelp(this, COMMAND_EXECUTOR.ALL, true));
        PowerCommandHandler.addPowerCommand("rank", new CommandRank(this, COMMAND_EXECUTOR.ALL, true));
        PowerCommandHandler.addPowerCommand("r", new CommandRank(this, COMMAND_EXECUTOR.ALL, false));
        PowerCommandHandler.addPowerCommand("player", new CommandPlayer(this, COMMAND_EXECUTOR.ALL, true));
        PowerCommandHandler.addPowerCommand("p", new CommandPlayer(this, COMMAND_EXECUTOR.ALL, false));
        PowerCommandHandler.addPowerCommand("stats", new CommandStats(this, COMMAND_EXECUTOR.ALL, false));
        // Load all base commands //

        if (!loadPRData()) {
            return;
        }

        PRV1Converter powerRanksV1Converter = new PRV1Converter(this);
        if (powerRanksV1Converter.check()) {
            powerRanksV1Converter.convert();
        }

        checkForUpdates();
        setupMetrics();
        setupPluginHooks();

        List<String> logoLines = new ArrayList<String>();
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██   ██ ██   ██");
        logoLines.add("  ██████  ██████ ");
        logoLines.add("  ██      ██   ██");
        logoLines.add("  ██      ██   ██");
        Iterator<String> logoLinesIterator = logoLines.iterator();

        String preload_message = "Loaded " + BaseDataHandler.getRanks().size() + " ranks and "
                + BaseDataHandler.getPlayers().size() + " players";

        String storageType = "Unknown";
        storageType = this.coreConfig.isUsingYAML() ? "YAML" : storageType;
        storageType = this.coreConfig.isUsingSQLite() ? "SQLite" : storageType;
        storageType = this.coreConfig.isUsingMySQL() ? "MySQL" : storageType;

        getLogger().info("");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  " + getDescription().getName()
                + " v" + getDescription().getVersion());
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  Running on "
                + ServerInfo.getServerType(getServer()) + " v" + ServerInfo.getServerVersion(getServer()) + " (Java: "
                + VersionUtils.getJavaVersion() + ")");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  Startup time: "
                + Duration.between(startup_time, Instant.now()).toMillis() + "ms");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.GREEN + "  " + preload_message + " ("
                + storageType + ")");
        getLogger().info(ChatColor.AQUA + logoLinesIterator.next() + ChatColor.RED + "  "
                + (System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
                        ? getLangConfig().getNode("plugin.reload-detected")
                        : ""));
        getLogger().info("");

        System.setProperty("POWERRANKSRUNNING", "TRUE");
        System.setProperty("POWERRANKSENABLED", "TRUE");

        for (Player player : getServer().getOnlinePlayers()) {
            JoinEvent.onJoin(player);
        }
    }

    @Override
    public void onDisable() {
        if (!System.getProperty("POWERRANKSENABLED", "").equals("TRUE")) {
            if (getLogger() != null) {
                getLogger().info("Error disabling " + getDescription().getName() + " v" + getDescription().getVersion()
                        + ": plugin already disabled");
            } else {
                System.out.println("Error disabling " + this.getClass().getName() + ": plugin already disabled");
            }
            return;
        }

        Bukkit.getServer().getScheduler().cancelTasks(this);

        boolean dataSaveResult = savePRData();

        if (getLogger() != null && getDescription() != null) {
            if (dataSaveResult) {
                getLogger().info("Saved ranks and player data to storage.");
            } else {
                ErrorManager.logError("An error occured while saving ranks and players data to storage!");
            }
            getLogger().info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
        }

        System.setProperty("POWERRANKSENABLED", "FALSE");
    }

    /**
     * Load ranks and players from storage
     */
    private boolean loadPRData() {
        if (this.coreConfig.isUsingYAML()) {
            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loading"),
                    ImmutableMap.<String, String>builder().put("type", "YAML").build(), '[', ']'));

            this.currentStorageHandler = new YAMLDataHandler();

            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loaded"),
                    ImmutableMap.<String, String>builder().put("type", "YAML").build(), '[', ']'));
        } else if (this.coreConfig.isUsingSQLite()) {
            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loading"),
                    ImmutableMap.<String, String>builder().put("type", "SQLite").build(), '[', ']'));

            this.currentStorageHandler = new SQLITEDataHandler();

            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loaded"),
                    ImmutableMap.<String, String>builder().put("type", "SQLite").build(), '[', ']'));
        } else if (this.coreConfig.isUsingMySQL()) {
            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loading"),
                    ImmutableMap.<String, String>builder().put("type", "MySQL").build(), '[', ']'));

            this.currentStorageHandler = new MYSQLDataHandler();

            getLogger().info(PowerFormatter.format(getLangConfig().getNode("plugin.storage.loaded"),
                    ImmutableMap.<String, String>builder().put("type", "MySQL").build(), '[', ']'));
        } else {
            getLogger().severe("");
            getLogger().severe("===---------------===");
            getLogger().severe(getLangConfig().getNode("error.unknown-storage-method"));
            getLogger().severe("===---------------===");
            getLogger().severe("");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        this.currentStorageHandler.setup(this);

        for (PRRank rank : this.currentStorageHandler.loadRanks()) {
            BaseDataHandler.getRanks().add(rank);

        }

        for (PRPlayer player : this.currentStorageHandler.loadPlayers()) {
            BaseDataHandler.getPlayers().add(player);
        }

        return true;
    }

    /**
     * Save ranks and players to storage
     */
    private boolean savePRData() {
        this.currentStorageHandler.saveRanks(BaseDataHandler.getRanks());
        this.currentStorageHandler.savePlayers(BaseDataHandler.getPlayers());

        return true;
    }

    /**
     * Handle update checking in a non-blocking way using a delayed task
     */
    private void checkForUpdates() {
        if (!getCoreConfig().doUpdateCheck()) {

        }
        PowerRanks self = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                getLogger().info(getLangConfig().getNode("plugin.update.checking"));
                Updater updater = new Updater(self, 79251, self.getFile(), UpdateType.NO_DOWNLOAD, true);
                getLogger()
                        .info(updater.getResult() == UpdateResult.UPDATE_AVAILABLE
                                ? ChatColor.GREEN
                                        + PowerFormatter.format(getLangConfig().getNode("plugin.update.available"),
                                                ImmutableMap.<String, String>builder()
                                                        .put("version",
                                                                "v" + updater.getLatestName()
                                                                        .replaceAll("^(\\w){0,64}[ ]{0,1}[v]{0,1}", ""))
                                                        .build(),
                                                '[', ']')
                                : getLangConfig().getNode("plugin.update.not-available"));
            }
        }.runTaskLaterAsynchronously(this, 20 * 1);
    }

    /**
     * Send some anonymous data about this PowerRanks installation
     */
    private void setupMetrics() {
        int metricsID = 11264;
        Metrics metrics = new Metrics(this, metricsID);

        metrics.addCustomChart(new Metrics.SimplePie("number_of_ranks", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(BaseDataHandler.getRanks().size());
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("number_of_players", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(BaseDataHandler.getPlayers().size());
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("storage_type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                String storageType = "UNKNOWN";
                storageType = coreConfig.isUsingYAML() ? "YAML" : storageType;
                storageType = coreConfig.isUsingMySQL() ? "MYSQL" : storageType;
                storageType = coreConfig.isUsingSQLite() ? "SQLITE" : storageType;
                return storageType;
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("plugin_language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return coreConfig.getLanguage().toUpperCase();
            }
        }));
    }

    /**
     * Set-up any third-party plugins
     */
    private void setupPluginHooks() {
        boolean hasVaultPermissions = this.getServer().getPluginManager().getPlugin("Vault") != null
                && getCoreConfig().pluginhookEnabled("vault");

        boolean hasPlaceholderAPI = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null
                && getCoreConfig().pluginhookEnabled("placeholderapi");

        getLogger().info("Checking for plugins to hook in to:");

        if (hasVaultPermissions) {
            if (hasVaultPermissions) {
                if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                    getLogger().info("Vault found!");
                    getLogger().info("Enabling Vault Permission integration.");

                    VaultHook vaultHook = new VaultHook();
                    vaultHook.hook(this, hasVaultPermissions);
                } else {
                    getLogger().info("Vault is disabled!");
                }
            }

            if (hasPlaceholderAPI) {
                getLogger().info("PlaceholderAPI found!");
                getLogger().info("Enabling PlaceholderAPI integration.");
                PowerRanks.placeholderapiExpansion = new PowerRanksPlaceholderExpansion(this);
                PowerRanks.placeholderapiExpansion.register();
            } else {
                PowerRanks.placeholderapiExpansion = null;
            }
        } else {
            getLogger().info("No other plugins found! Working stand-alone.");
        }
    }

    /**
     * Get the main plugin configuration
     * 
     * @return CoreConfig
     */
    public CoreConfig getCoreConfig() {
        return coreConfig;
    }

    /**
     * Get the language configuration
     * 
     * @return LangConfig
     */
    public LangConfig getLangConfig() {
        return langConfig;
    }

    // =================================================================
    // Formatters
    //
    // Default formatters on how PowerRanks looks in the chat
    // in commands and responses.
    // Defined here to keep them uniform for all commands/responses.
    //
    // [PR] {{message}}
    //
    // ===----------PowerRanks----------===
    // {{message}}
    // ===------------------------------===
    //
    // ===-----PowerRanks|{{title}}-----===
    // {{message}}
    // ===------------------------------===
    //
    // =================================================================

    public String pluginChatPrefix() {
        return ChatColor.BLACK + "[" + ChatColor.AQUA + "PR" + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
    }

    /**
     * Get the max length of a single line
     * 
     * @return max format line length
     */
    public int getChatMaxLineLength() {
        return 30;
    }

    /**
     * Get the default header format for multi-line PowerRanks messages
     * 
     * @return formatted header
     */
    public String getCommandHeader() {
        return getCommandHeader("");
    }

    /**
     * Get the default header format with title for multi-line PowerRanks messages
     * 
     * @param title
     * @return formatted header
     */
    public String getCommandHeader(String title) {
        int maxLength = getChatMaxLineLength();
        String text = getDescription().getName() + (title.length() > 0 ? " | " + title : "");

        if (text.length() > maxLength) {
            text = title;
        }

        if (text.length() > maxLength) {
            text = text.substring(0, maxLength - 5) + "...";
        }

        String divider = "";
        for (int i = 0; i < maxLength - text.length(); i++) {
            if (i == (maxLength - text.length()) / 2) {
                divider += text;
            }
            divider += "-";
        }
        return ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + divider + ChatColor.BLUE + "===";
    }

    /**
     * Get the default footer format for multi-line PowerRanks messages
     * 
     * @return formatted footer
     */
    public String getCommandFooter() {
        int maxLength = getChatMaxLineLength();
        String divider = "";
        for (int i = 0; i < maxLength; i++) {
            divider += "-";
        }
        return ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + divider + ChatColor.BLUE + "===";
    }

    /**
     * Get the current running PowerRanks instance
     * 
     * @return the current PowerRank instance
     */
    public static PowerRanks getInstance() {
        return instance;
    }

    /**
     * Get the PowerRanks API
     * 
     * @return a PowerRanksAPI instance
     */
    public static PowerRanksAPI getAPI() {
        return apiInstance;
    }

    public Instant getStartupTime() {
        return this.startup_time;
    }
}
