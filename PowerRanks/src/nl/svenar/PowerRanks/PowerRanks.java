package nl.svenar.PowerRanks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.bukkit.permissions.PermissionAttachment;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Commands.PowerCommandHandler;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerPermissibleBase;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnInteract;
import nl.svenar.PowerRanks.Events.OnInventory;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.Events.OnMove;
import nl.svenar.PowerRanks.Events.OnPreCommand;
import nl.svenar.PowerRanks.Events.OnSignChanged;
import nl.svenar.PowerRanks.Events.OnWorldChange;
import nl.svenar.PowerRanks.addons.AddonsManager;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;
import nl.svenar.PowerRanks.Events.OnBlockChange;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import nl.svenar.PowerRanks.api.PowerRanksAPI;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.metrics.Metrics;
import nl.svenar.PowerRanks.update.ConfigFilesUpdater;
import nl.svenar.PowerRanks.update.Updater;
import nl.svenar.PowerRanks.update.Updater.UpdateResult;
import nl.svenar.PowerRanks.update.Updater.UpdateType;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.ImmutableMap;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;

import me.clip.deluxetags.DeluxeTag;
import me.clip.placeholderapi.PlaceholderAPI;

public class PowerRanks extends JavaPlugin implements Listener {
	public String bukkit_dev_url_powerranks = "https://dev.bukkit.org/projects/powerranks";
	public ArrayList<String> donation_urls = new ArrayList<String>(Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar"));

	private static PowerRanks instance;
	public static PluginDescriptionFile pdf;
	public AddonsManager addonsManager;
	public String plp;
	public static Logger log;
	public static String configFileLoc;
	public static String fileLoc;
	public static String langFileLoc;
	public static String factoryresetid = null;
	public static Instant powerranks_start_time = Instant.now();
	private String update_available = "";

	// Soft Dependencies
	public static boolean vaultEconomyEnabled = false;
	public static boolean vaultPermissionsEnabled = false;
	public static PowerRanksExpansion placeholderapiExpansion;
	public static boolean plugin_hook_deluxetags = false;
	public static boolean plugin_hook_nametagedit = false;
	public static boolean plugin_hook_tab = false;
	// Soft Dependencies

	File configFile;
	File ranksFile;
	File playersFile;
	File langFile;
	FileConfiguration config;
	FileConfiguration ranks;
	FileConfiguration players;
	FileConfiguration lang;
	public String updatemsg;
	public Map<UUID, PermissionAttachment> playerPermissionAttachment = new HashMap<UUID, PermissionAttachment>();
	// public Map<UUID, ArrayList<String>> playerDisallowedPermissions = new HashMap<UUID, ArrayList<String>>();
	// public Map<UUID, ArrayList<String>> playerAllowedPermissions = new HashMap<UUID, ArrayList<String>>();
	public Map<UUID, String> playerTablistNameBackup = new HashMap<UUID, String>();
	public Map<UUID, Long> playerLoginTime = new HashMap<UUID, Long>();
	// private Map<UUID, Boolean> playerSetupPermissionsQueue = new HashMap<UUID, Boolean>();

	public PowerRanks() {
		PowerRanks.pdf = this.getDescription();
		this.plp = ChatColor.BLACK + "[" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
		PowerRanks.configFileLoc = this.getDataFolder() + File.separator;
		PowerRanks.fileLoc = this.getDataFolder() + File.separator + "Ranks" + File.separator;
		PowerRanks.langFileLoc = PowerRanks.configFileLoc + "lang.yml";
		this.updatemsg = "";
	}

	public void onEnable() {
		instance = this;

		Instant startTime = Instant.now();

		PowerRanks.log = this.getLogger();
		PowerRanksAPI.plugin = this;

		// PowerRanks.log.info("=== ---------- LOADING EVENTS ---------- ===");
//		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInteract(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnSignChanged(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInventory(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnMove(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnWorldChange(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnBlockChange(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnPreCommand(this), (Plugin) this);

		// PowerRanks.log.info("");
		// PowerRanks.log.info("=== --------- LOADING COMMANDS --------- ===");
		// Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new Cmd(this));
		// Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new Cmd(this));
		
		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new PowerCommandHandler(this));
		// Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new PowerCommandHandler(this));
		
		Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter(new ChatTabExecutor(this));
		// Bukkit.getServer().getPluginCommand("pr").setTabCompleter(new ChatTabExecutor(this));

		try {
			if (handle_update_checking()) {
				return;
			}
		} catch (Exception e) {}

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ----------- LOADING DATA ----------- ===");
		new PowerRanksChatColor();
		new Messages(this);
		new PowerRanksVerbose(this);

		this.createDir(PowerRanks.fileLoc);
		this.configFile = new File(this.getDataFolder(), "config.yml");
		this.ranksFile = new File(PowerRanks.fileLoc, "Ranks.yml");
		this.playersFile = new File(PowerRanks.fileLoc, "Players.yml");
		this.langFile = new File(this.getDataFolder(), "lang.yml");
		this.config = (FileConfiguration) new YamlConfiguration();
		this.ranks = (FileConfiguration) new YamlConfiguration();
		this.players = (FileConfiguration) new YamlConfiguration();
		this.lang = (FileConfiguration) new YamlConfiguration();
		try {
			this.copyFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.loadAllFiles();

		PowerRanks.log.info("Loading config");
		new CachedConfig(this);
		PowerRanks.log.info("Loading player data");
		new CachedPlayers(this);
		PowerRanks.log.info("Loading rank data");
		new CachedRanks(this);

		ConfigFilesUpdater.updateConfigFiles(this);


		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

		HashMap<String, Object> new_user_data = new HashMap<String, Object>();
		for (final Player player : this.getServer().getOnlinePlayers()) {
			new_user_data.put("players." + player.getUniqueId() + ".name", player.getName());

			if (!CachedPlayers.contains("players." + player.getUniqueId())) {
				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".rank"))
					new_user_data.put("players." + player.getUniqueId() + ".rank", CachedRanks.get("Default"));

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".permissions"))
					new_user_data.put("players." + player.getUniqueId() + ".permissions", new ArrayList<>());

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".subranks"))
					new_user_data.put("players." + player.getUniqueId() + ".subranks", "");

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".usertag"))
					new_user_data.put("players." + player.getUniqueId() + ".usertag", "");

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".playtime"))
					new_user_data.put("players." + player.getUniqueId() + ".playtime", 0);
			}

		}
		CachedPlayers.set(new_user_data, true);

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ------- LOADING PLUGIN HOOKS ------- ===");
		setupSoftDependencies();

		GUI.setPlugin(this);

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ---------- LOADING ADDONS ---------- ===");
		// PowerRanks.log.info("Loading add-ons");
		addonsManager = new AddonsManager(this);
		addonsManager.setup();

		PowerRanks.log.info("");
		PowerRanks.log.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  PowerRanks v" + pdf.getVersion());
		PowerRanks.log.info(ChatColor.AQUA + "  ██   ██ ██   ██" + ChatColor.GREEN + "  Running on " + Util.getServerType(getServer()) + " v" + Util.getServerVersion(getServer()));
		PowerRanks.log.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  Startup time: " + Duration.between(startTime, Instant.now()).toMillis() + "ms");
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.GREEN + "  " + update_available);
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.RED   + "  " + (System.getProperty("POWERRANKSRUNNING", "").equals("TRUE") ? "Reload detected, why do you hate yourself :C" : ""));
		PowerRanks.log.info("");

    	System.setProperty("POWERRANKSRUNNING", "TRUE");

		// PowerRanks.log.info("Enabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		PowerRanks.log.info("If you'd like to donate, please visit " + donation_urls.get(0) + " or " + donation_urls.get(1));

		int pluginId = 7565;
		Metrics metrics = new Metrics(this, pluginId);

		metrics.addCustomChart(new Metrics.SimplePie("number_of_installed_addons", new Callable<String>() {
			@Override
			public String call() throws Exception {
				int addonCount = 0;
				for (Entry<File, Boolean> prAddon : AddonsManager.loadedAddons.entrySet()) {
					if (prAddon.getValue() == true)
						addonCount++;
				}
				return String.valueOf(addonCount);
			}
		}));

		metrics.addCustomChart(new Metrics.SimplePie("accepted_addon_manager_terms", new Callable<String>() {
			@Override
			public String call() throws Exception {
				boolean accepterAddonManagerTerms = CachedConfig.getBoolean("addon_manager.accepted_terms");
				return String.valueOf(accepterAddonManagerTerms ? "true" : "false");
			}
		}));

		setupTasks();
	}

	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTasks(this);

		// for (Player player : this.getServer().getOnlinePlayers()) {
		// 	this.playerUninjectPermissible(player);
		// }

		this.addonsManager.disable();

		for (Entry<UUID, PermissionAttachment> pa : playerPermissionAttachment.entrySet()) {
			pa.getValue().remove();
		}
		playerPermissionAttachment.clear();

		for (Entry<UUID, String> pa : playerTablistNameBackup.entrySet()) {
			Player player = getPlayerFromUUID(pa.getKey());
			if (player != null) {
				player.setPlayerListName(pa.getValue());
			}
		}
		playerTablistNameBackup.clear();

		CachedPlayers.save();

		if (PowerRanks.log != null && PowerRanks.pdf != null) {
			PowerRanks.log.info("Disabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		}
	}

	private void setupTasks() {
		if (plugin_hook_tab) {
			new BukkitRunnable() {
				@Override
				public void run() {
					PowerRanksVerbose.log("task(1s)", "Task setup TAB");
					if (getServer().getPluginManager().isPluginEnabled("TAB")) {
						me.neznamy.tab.shared.permission.PermissionPlugin powerranksTabPermissionPlugin = new PowerRanksTABHook(getInstance());
						me.neznamy.tab.api.TABAPI.registerPermissionPlugin(powerranksTabPermissionPlugin);

						// me.neznamy.tab.api.TABAPI.registerPlayerPlaceholder(new me.neznamy.tab.shared.placeholders.PlayerPlaceholder("%powerranks-prefix%", 500) {
						// 	public String get(me.neznamy.tab.api.TabPlayer tabPlayer) {
						// 		return powerranksTabPermissionPlugin.getPrefix(tabPlayer);
						// 	}
						// });

						// me.neznamy.tab.api.TABAPI.registerPlayerPlaceholder(new me.neznamy.tab.shared.placeholders.PlayerPlaceholder("%powerranks-suffix%", 500) {
						// 	public String get(me.neznamy.tab.api.TabPlayer tabPlayer) {
						// 		return powerranksTabPermissionPlugin.getSuffix(tabPlayer);
						// 	}
						// });
						
						this.cancel();
					}
				}
			}.runTaskTimer(this, 0, 20L);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				PowerRanksVerbose.log("task(60s)", "Running task");
				updateAllPlayersTABlist();
			}
		}.runTaskTimer(this, 0, 1200L);
	}

	private Player getPlayerFromUUID(UUID uuid) {
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "=== ----------Checking UUID---------- ===");
		Player player = null;
		for (Player online_player : Bukkit.getServer().getOnlinePlayers()) {
			PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "Matching '" + online_player.getName() + "' " + (uuid == online_player.getUniqueId() ? "MATCH!" : "No match") + " (" + uuid + ", " + online_player.getUniqueId() + ")");
			if (uuid == online_player.getUniqueId()) {
				player = online_player;
				break;
			}
		}
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "=== --------------------------------- ===");
		return player;
	}

	private void setupSoftDependencies() {
		boolean has_vault_economy = this.getServer().getPluginManager().getPlugin("Vault") != null && getConfigBool("plugin_hook.vault_economy");
		boolean has_vault_permissions = this.getServer().getPluginManager().getPlugin("Vault") != null && getConfigBool("plugin_hook.vault_permissions");
		boolean has_placeholderapi = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && getConfigBool("plugin_hook.placeholderapi");
		boolean has_deluxetags = this.getServer().getPluginManager().getPlugin("DeluxeTags") != null && getConfigBool("plugin_hook.deluxetags");
		boolean has_nametagedit = this.getServer().getPluginManager().getPlugin("NametagEdit") != null && getConfigBool("plugin_hook.nametagedit");
		boolean has_tab = this.getServer().getPluginManager().getPlugin("TAB") != null && getConfigBool("plugin_hook.tab");

		PowerRanks.log.info("Checking for plugins to hook in to:");
		if (has_vault_economy || has_vault_permissions) {
			PowerRanks.log.info("Vault found!");
			if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
				if (has_vault_economy) {
					PowerRanks.log.info("Enabling Vault Economy integration.");
				}
				if (has_vault_permissions) {
					PowerRanks.log.info("Enabling Vault Permission integration.");
				}
				VaultHook vaultHook = new VaultHook();
				vaultHook.hook(this, has_vault_permissions, has_vault_economy);
				vaultEconomyEnabled = has_vault_economy;
				vaultPermissionsEnabled = has_vault_permissions;
			}
		}

		if (has_placeholderapi) {
			PowerRanks.log.info("PlaceholderAPI found!");
			PowerRanks.placeholderapiExpansion = new PowerRanksExpansion(this);
			PowerRanks.placeholderapiExpansion.register();
		} else {
			PowerRanks.placeholderapiExpansion = null;
		}

		if (has_deluxetags) {
			PowerRanks.log.info("DeluxeTags found!");
			plugin_hook_deluxetags = true;
		}

		if (has_nametagedit) {
			PowerRanks.log.info("NametagEdit found!");
			plugin_hook_nametagedit = true;
			setup_nte();
		}

		if (has_tab) {
			PowerRanks.log.info("TAB found!");
			// PowerRanks.log.info("Enabling TAB integration.");
			plugin_hook_tab = true;
			// setup_nte();
		}

		if (!has_vault_economy && !has_vault_permissions && !has_placeholderapi && !has_deluxetags && !has_nametagedit && !has_tab)
			PowerRanks.log.info("No other plugins found! Working stand-alone.");
	}

	private void setup_nte() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (getServer().getPluginManager().isPluginEnabled("NametagEdit")) {
					this.cancel();
					for (Player player : Bukkit.getOnlinePlayers()) {
						updateTablistName(player);
					}
				}
			}
		}.runTaskTimer(this, 20, 20);
	}

	private boolean handle_update_checking() {
		if (getConfigBool("updates.enable_update_checking")) {
			PowerRanks.log.info("Checking for updates...");
			Updater updater = new Updater(this, 79251, this.getFile(), getConfigBool("updates.automatic_download_updates") ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				// PowerRanks.log.info("------------------------------------");
				// PowerRanks.log.info("A new " + PowerRanks.pdf.getName() + " version is available!");
				// PowerRanks.log.info("Current version: " + PowerRanks.pdf.getVersion());
				// PowerRanks.log.info("New version: " + updater.getLatestName().replaceAll("[a-zA-Z\" ]", ""));
				if (!getConfigBool("updates.automatic_download_updates")) {
					update_available = "Update available! (v" + updater.getLatestName().replaceAll("[a-zA-Z\" ]", "") + ")";
					// PowerRanks.log.info("Download the new version from: " + bukkit_dev_url_powerranks);
				} else {
					PowerRanks.log.info("Plugin will now be updated!");
					update_available = "Update complete! Please restart your server";
				}
				// PowerRanks.log.info("------------------------------------");
			} else {
				update_available = "No update available";
				// log.info("No new version available");
			}

			if (updater.getResult() == UpdateResult.SUCCESS) {
				PowerRanks.log.info("------------------------------------");
				PowerRanks.log.info(PowerRanks.pdf.getName() + " updated successfully!");
				PowerRanks.log.warning(PowerRanks.pdf.getName() + " will be disabled until the next server load!");
				PowerRanks.log.info("------------------------------------");

				final PluginManager plg = Bukkit.getPluginManager();
				final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
				plg.disablePlugin(plgname);
				return true;
//				plg.enablePlugin(plgname);
			}

			if (updater.getResult() == UpdateResult.FAIL_DOWNLOAD) {
				PowerRanks.log.info("Update found but failed to download!");
			}
		}

		return false;
	}

	public boolean getConfigBool(String path) {
		boolean val = false;
		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		val = configYaml.getBoolean(path);
		return val;
	}

	public boolean configContainsKey(String path) {
		boolean val = false;
		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		val = configYaml.isSet(path);
		return val;
	}

	public void createDir(final String path) {
		final File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void factoryReset(CommandSender sender) {
		ArrayList<String> files = new ArrayList<String>();
		files.add(configFileLoc + File.separator + "config.yml");
		files.add(configFileLoc + File.separator + "lang.yml");
		files.add(fileLoc + File.separator + "Players.yml");
		files.add(fileLoc + File.separator + "Ranks.yml");

		for (String filePath : files) {
			File tmp_file = new File(filePath);
			if (tmp_file.exists())
				tmp_file.delete();
		}

		this.createDir(PowerRanks.fileLoc);

		this.configFile = new File(this.getDataFolder(), "config.yml");
		this.ranksFile = new File(PowerRanks.fileLoc, "Ranks.yml");
		this.playersFile = new File(PowerRanks.fileLoc, "Players.yml");
		this.langFile = new File(this.getDataFolder(), "lang.yml");
		this.config = (FileConfiguration) new YamlConfiguration();
		this.ranks = (FileConfiguration) new YamlConfiguration();
		this.players = (FileConfiguration) new YamlConfiguration();
		this.lang = (FileConfiguration) new YamlConfiguration();
		try {
			this.copyFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.loadAllFiles();

		new CachedConfig(this);
		new CachedPlayers(this);
		new CachedRanks(this);

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

		HashMap<String, Object> new_user_data = new HashMap<String, Object>();
		for (final Player player : this.getServer().getOnlinePlayers()) {
			new_user_data.put("players." + player.getUniqueId() + ".name", player.getName());

			if (!CachedPlayers.contains("players." + player.getUniqueId())) {
				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".rank"))
					new_user_data.put("players." + player.getUniqueId() + ".rank", CachedRanks.get("Default"));

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".permissions"))
					new_user_data.put("players." + player.getUniqueId() + ".permissions", new ArrayList<>());

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".subranks"))
					new_user_data.put("players." + player.getUniqueId() + ".subranks", "");

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".usertag"))
					new_user_data.put("players." + player.getUniqueId() + ".usertag", "");

				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".playtime"))
					new_user_data.put("players." + player.getUniqueId() + ".playtime", 0);
			}

		}
		CachedPlayers.set(new_user_data, true);

		//this.setupPermissions();

		Messages.messageCommandFactoryResetDone(sender);
	}

	public void printVersionError(String fileName, boolean auto_update) {
		if (!auto_update) {
			PowerRanks.log.warning("===------------------------------===");
			PowerRanks.log.warning("              WARNING!");
			PowerRanks.log.warning("Version mismatch detected in:");
			PowerRanks.log.warning(fileName);
			PowerRanks.log.warning(PowerRanks.pdf.getName() + " may not work with this config.");
			PowerRanks.log.warning("Manual verification is required.");
			PowerRanks.log.warning("Visit " + PowerRanks.pdf.getWebsite() + " for more info.");
			PowerRanks.log.warning("===------------------------------===");
		} else {
			// PowerRanks.log.info("Version mismatch detected in: " + fileName);
			// PowerRanks.log.info("Automatically updating " + fileName);
			 PowerRanks.log.info("Updating " + fileName);
		}
	}

	private void copyFiles() throws Exception {
		if (!this.configFile.exists()) {
			this.configFile.getParentFile().mkdirs();
			this.copy(this.getResource("config.yml"), this.configFile);
		}
		if (!this.ranksFile.exists()) {
			this.ranksFile.getParentFile().mkdirs();
			this.copy(this.getResource("Ranks.yml"), this.ranksFile);
		}
		if (!this.playersFile.exists()) {
			this.playersFile.getParentFile().mkdirs();
			this.copy(this.getResource("Players.yml"), this.playersFile);
		}
		if (!this.langFile.exists()) {
			this.langFile.getParentFile().mkdirs();
			this.copy(this.getResource("lang.yml"), this.langFile);
		}
	}

	public void copy(final InputStream in, final File file) {
		try {
			final OutputStream out = new FileOutputStream(file);
			final byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveAllFiles() {
		try {
			this.config.save(this.configFile);
			this.ranks.save(this.ranksFile);
			this.players.save(this.playersFile);
			this.lang.save(this.langFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAllFiles() {
		try {
			this.config.load(this.configFile);
			this.ranks.load(this.ranksFile);
			this.players.load(this.playersFile);
			this.lang.load(this.langFile);
		} catch (Exception e) {
			System.out.println("-----------------------------");
			PowerRanks.log.warning("Failed to load the config files (If this is the first time PowerRanks starts you could ignore this message)");
			PowerRanks.log.warning("Try reloading the server. If this message continues to display report this to the plugin page on bukkit.");
			System.out.println("-----------------------------");
		}
	}

	public void playerInjectPermissible(Player player) {
		try {
			Field f = Util.obcClass("entity.CraftHumanEntity").getDeclaredField("perm");
			f.setAccessible(true);
			f.set(player, new PowerPermissibleBase(player, this));
			f.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateNametagEditData(Player player, String prefix, String suffix, String subprefix, String subsuffix, String usertag, String nameColor) {
		if (plugin_hook_nametagedit) {
			PowerRanksVerbose.log("updateNametagEditData", "Updating " + player.getName() + "'s nametag format");

			BukkitScheduler scheduler = getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					String prefix_format = CachedConfig.getString("nametagedit.prefix");
					String suffix_format = CachedConfig.getString("nametagedit.suffix");
					if (prefix_format == null || suffix_format == null) {
						if (prefix_format == null) {
							PowerRanksVerbose.log("updateNametagEditData", "prefix_format is NULL");
						}

						if (suffix_format == null) {
							PowerRanksVerbose.log("updateNametagEditData", "suffix_format is NULL");
						}
						return;
					}

					prefix_format = Util.powerFormatter(prefix_format, ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix)
							.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player)).build(), '[', ']');

					suffix_format = Util.powerFormatter(suffix_format, ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix)
							.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player)).build(), '[', ']');

					prefix_format += nameColor;

					prefix_format = PowerRanksChatColor.colorize(prefix_format, true);
					suffix_format = PowerRanksChatColor.colorize(suffix_format, true);

					INametagApi nteAPI = NametagEdit.getApi();
					if (nteAPI != null) {
						nteAPI.setNametag(player, prefix_format + (prefix_format.length() > 0 ? " " : ""), (suffix_format.length() > 0 ? " " : "") + suffix_format);
						updateTablistName(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor, false);
					}
				}
			}, 20L);
		}
	}

	public void updateTablistName(Player player) {
//		PowerRanksVerbose.log("updateTablistName", "Updating " + player.getName() + "'s tablist format");
		String uuid = player.getUniqueId().toString();
		try {
			player.updateCommands(); // TODO find a better place for this
		} catch (NoSuchMethodError e) {}

		String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
		String prefix = CachedRanks.getString("Groups." + rank + ".chat.prefix");
		String suffix = CachedRanks.getString("Groups." + rank + ".chat.suffix");
		String nameColor = CachedRanks.getString("Groups." + rank + ".chat.nameColor");

		String subprefix = "";
		String subsuffix = "";
		String usertag = "";

		try {
			if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") != null) {
				ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");
				for (String r : subranks.getKeys(false)) {
					boolean in_world = false;
					if (!CachedPlayers.contains("players." + uuid + ".subranks." + r + ".worlds")) {
						in_world = true;

						ArrayList<String> default_worlds = new ArrayList<String>();
						default_worlds.add("All");
						CachedPlayers.set("players." + uuid + ".subranks." + r + ".worlds", default_worlds, false);
					}

					String player_current_world = player.getWorld().getName();
					List<String> worlds = CachedPlayers.getStringList("players." + uuid + ".subranks." + r + ".worlds");
					for (String world : worlds) {
						if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
							in_world = true;
						}
					}

					if (in_world) {
						if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_prefix")) {
							subprefix += (CachedRanks.getString("Groups." + r + ".chat.prefix") != null && CachedRanks.getString("Groups." + r + ".chat.prefix").length() > 0
									? ChatColor.RESET + CachedRanks.getString("Groups." + r + ".chat.prefix") + " "
									: "");
						}

						if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_suffix")) {
							subsuffix += (CachedRanks.getString("Groups." + r + ".chat.suffix") != null && CachedRanks.getString("Groups." + r + ".chat.suffix").length() > 0
									? ChatColor.RESET + CachedRanks.getString("Groups." + r + ".chat.suffix") + " "
									: "");

						}
					}
				}
			}

			subprefix = subprefix.trim();
			subsuffix = subsuffix.trim();

			if (subsuffix.endsWith(" ")) {
				subsuffix = subsuffix.substring(0, subsuffix.length() - 1);
			}

			if (subsuffix.replaceAll(" ", "").length() == 0) {
				subsuffix = "";
			}

			if (CachedPlayers.contains("players." + uuid + ".usertag") && CachedPlayers.getString("players." + uuid + ".usertag").length() > 0) {
				String tmp_usertag = CachedPlayers.getString("players." + uuid + ".usertag");

				if (CachedRanks.getConfigurationSection("Usertags") != null) {
					ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
					for (String key : tags.getKeys(false)) {
						if (key.equalsIgnoreCase(tmp_usertag)) {
							usertag = CachedRanks.getString("Usertags." + key) + ChatColor.RESET;
							break;
						}
					}
				}
			}

			updateTablistName(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTablistName(Player player, String prefix, String suffix, String subprefix, String subsuffix, String usertag, String nameColor, boolean updateNTE) {
		PowerRanksVerbose.log("updateTablistName", "Updating " + player.getName() + "'s tablist format");

		String player_formatted_name = (nameColor.length() == 0 ? "&r" : "") + applyMultiColorFlow(nameColor, player.getDisplayName());

		try {
			if (updateNTE) {
				updateNametagEditData(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor);
			}

			if (plugin_hook_tab) {
				new BukkitRunnable() {
					@Override
					public void run() {
						me.neznamy.tab.api.TabPlayer tabPlayer = me.neznamy.tab.api.TABAPI.getPlayer(player.getUniqueId());
	
						// PowerRanks.log.info("Tab player null: " + (tabPlayer == null));
						// PowerRanks.log.info("Tab player loaded: " + tabPlayer.isLoaded());
	
						if (tabPlayer.isLoaded()) {
							// PowerRanks.log.info("Tab player old prefix: " + tabPlayer.getTemporaryValue(me.neznamy.tab.api.EnumProperty.TABPREFIX));
	
							tabPlayer.setValueTemporarily(me.neznamy.tab.api.EnumProperty.TABPREFIX, prefix + " " + ChatColor.RESET);
							tabPlayer.setValueTemporarily(me.neznamy.tab.api.EnumProperty.TABSUFFIX, ChatColor.RESET + " " + suffix);
							// tabPlayer.setValueTemporarily(me.neznamy.tab.api.EnumProperty.TAGPREFIX, prefix + " " + ChatColor.RESET);
	
							// PowerRanks.log.info("Tab player new prefix: " + tabPlayer.getTemporaryValue(me.neznamy.tab.api.EnumProperty.TABPREFIX));
							this.cancel();
						}
					}
				}.runTaskTimer(this, 0, 20L);
			}

			if (!CachedConfig.getBoolean("tablist_modification.enabled") || plugin_hook_tab)
				return;

			player.setPlayerListName(playerTablistNameBackup.get(player.getUniqueId()));

			playerTablistNameBackup.put(player.getUniqueId(), player.getPlayerListName());

			String format = CachedConfig.getString("tablist_modification.format");

			if (format.contains("[name]")) {
				String tmp_format = CachedConfig.getString("tablist_modification.format");
				tmp_format = tmp_format.replace("[name]", "[player]");
				CachedConfig.set("tablist_modification.format", tmp_format);
				format = tmp_format;
			}

			format = Util.powerFormatter(format,
			ImmutableMap.<String, String>builder()
			.put("prefix", prefix)
			.put("suffix", suffix)
			.put("subprefix", subprefix)
			.put("subsuffix", subsuffix)
			.put("usertag", usertag)
			.put("player", player_formatted_name)
			.put("world", player.getWorld().getName())
			.build(), '[', ']');

			while (format.endsWith(" ")) {
				format = format.substring(0, format.length() - 1);
			}

			if (PowerRanks.placeholderapiExpansion != null) {
				format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR, "" + PowerRanksChatColor.unformatted_default_char);
			}
			format = PowerRanks.chatColor(format, true);

			player.setPlayerListName(format);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String chatColor(String textToTranslate, boolean custom_colors) {
		return PowerRanksChatColor.colorize(textToTranslate, custom_colors);
	}

	public static String chatColorAlt(final String textToTranslate, final boolean custom_colors) {
        return PowerRanksChatColor.colorizeRaw(textToTranslate, custom_colors, false);
    }

	public static String applyMultiColorFlow(String rawColors, String text) {
		String regexColors = "(&[a-fA-F0-9])|(#[a-fA-F0-9]{6})";
		String output = "";

		Pattern p = Pattern.compile(regexColors);
		Matcher m = p.matcher(rawColors);
		ArrayList<String> colors = new ArrayList<String>();
		while (m.find()) {
			String color = m.group(0);
			colors.add(color);
		}

		String[] textSplit = text.split("");

		if (colors.size() > 1) {
			int index = 0;
			for (String character : textSplit) {
				output += colors.get(index % colors.size()) + character;
				index++;
			}
		} else {
			output = rawColors + text;
		}

		return output;
	}

	public static YamlConfiguration loadLangFile() {
		File langFile = new File(PowerRanks.langFileLoc);
		YamlConfiguration langYaml = new YamlConfiguration();
		try {
			langYaml.load(langFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return langYaml;
	}

	public PowerRanksAPI loadAPI() {
		return new PowerRanksAPI();
	}

	public void updatePlaytime(Player player, long join_time, long leave_time) {
		int current_playtime = 0;
		try {
			current_playtime = CachedPlayers.getInt("players." + player.getUniqueId() + ".playtime");
		} catch (Exception e) {
			try {
				current_playtime = CachedPlayers.getDouble("players." + player.getUniqueId() + ".playtime").intValue();
			} catch (Exception e1) {
				current_playtime = CachedPlayers.getLong("players." + player.getUniqueId() + ".playtime").intValue();
			}
		}
		CachedPlayers.set("players." + player.getUniqueId() + ".playtime", current_playtime + (leave_time - join_time) / 1000, false);
	}

	public void updatePlayersWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			updateTablistName(p);
			// p.updateCommands();
		}
	}

	public void updatePlayersTABlistWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (users.getGroup(p).equalsIgnoreCase(rank)) {
				updateTablistName(p);
			}
		}
	}

	public void updateAllPlayersTABlist() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			updateTablistName(p);
		}
	}

	public static PowerRanksExpansion getPlaceholderapiExpansion() {
		return placeholderapiExpansion;
	}

	public ArrayList<String> getEffectivePlayerPermissions(Player player) {
		ArrayList<String> permissions = new ArrayList<String>();

		// if (!playerSetupPermissionsQueue.containsKey(player.getUniqueId()) || !playerSetupPermissionsQueue.get(player.getUniqueId())) {
		// 	log.warning("(+) Recalculating permissions for " + player.getName());
		// 	playerSetupPermissionsQueue.put(player.getUniqueId(), true);
		// 	BukkitScheduler scheduler = getServer().getScheduler();
		// 	scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		// 		@Override
		// 		public void run() {
		// 			playerSetupPermissionsQueue.put(player.getUniqueId(), false);
		// 			// if (player != null) {
		// 			// player.recalculatePermissions();
		// 			// player.updateCommands();
		// 			// }
		// 			log.warning("(=) Recalculating permissions for " + player.getName());
		// 			PowerRanksVerbose.log("recalculatePermissions", "Permissions recalculated");
		// 		}
		// 	}, 20L);
		// } else {
		// 	// log.warning("(-) Recalculating permissions for " + player.getName());
		// }

		// player.recalculatePermissions();
		// log.warning("Recalculating permissions for " + player.getName());
		//player.updateCommands();

		String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");

		// for (String permission : CachedRanks.getStringList("Groups." + rank + ".permissions"))
		// 	permissions.add(permission);
		if (CachedRanks.getStringList("Groups." + rank + ".permissions") != null) {
			for (String permission : CachedRanks.getStringList("Groups." + rank + ".permissions"))
				permissions.add(permission);
		} else {
			CachedRanks.set("Groups." + rank + ".permissions", new ArrayList<String>());
		}

		// for (String inheritance : CachedRanks.getStringList("Groups." + rank + ".inheritance"))
		// 	for (String permission : CachedRanks.getStringList("Groups." + inheritance + ".permissions"))
		// 		permissions.add(permission);
		if (CachedRanks.getStringList("Groups." + rank + ".inheritance") != null) {
			for (String inheritance : CachedRanks.getStringList("Groups." + rank + ".inheritance")) {
				if (CachedRanks.getStringList("Groups." + inheritance + ".permissions") != null) {
					for (String permission : CachedRanks.getStringList("Groups." + inheritance + ".permissions")) {
						permissions.add(permission);
					}
				} else {
					CachedRanks.set("Groups." + inheritance + ".permissions", new ArrayList<String>());
				}
			}
		} else {
			CachedRanks.set("Groups." + rank + ".inheritance", new ArrayList<String>());
		}

		// if (player != null && CachedPlayers.contains("players." + player.getUniqueId() + ".permissions")) {
		// 	for (String permission : CachedPlayers.getStringList("players." + player.getUniqueId() + ".permissions"))
		// 		permissions.add(permission);
		// }
		if (CachedPlayers.getStringList("players." + player.getUniqueId() + ".permissions") != null) {
			for (String permission : CachedPlayers.getStringList("players." + player.getUniqueId() + ".permissions"))
				permissions.add(permission);
		} else {
			CachedPlayers.set("players." + player.getUniqueId() + ".permissions", new ArrayList<String>(), false);
		}

		ArrayList<String> useable_subranks = new ArrayList<String>();
		ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks");
		if (subranks != null) {
			for (String r : subranks.getKeys(false)) {
				boolean in_world = false;
				if (!CachedPlayers.contains("players." + player.getUniqueId() + ".subranks." + r + ".worlds")) {
					in_world = true;

					ArrayList<String> default_worlds = new ArrayList<String>();
					default_worlds.add("All");
					CachedPlayers.set("players." + player.getUniqueId() + ".subranks." + r + ".worlds", default_worlds, true);
				}

				String player_current_world = player.getWorld().getName();
				List<String> worlds = CachedPlayers.getStringList("players." + player.getUniqueId() + ".subranks." + r + ".worlds");
				for (String world : worlds) {
					if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
						in_world = true;
					}
				}

				if (in_world) {
					if (CachedPlayers.getBoolean("players." + player.getUniqueId() + ".subranks." + r + ".use_permissions")) {
						useable_subranks.add(r);
					}
				}
			}
		}

		for (String inheritance : useable_subranks)
			for (String permission : CachedRanks.getStringList("Groups." + inheritance + ".permissions"))
				permissions.add(permission);

		return permissions;
	}

	public static PowerRanks getInstance() {
		return instance;
	}
}