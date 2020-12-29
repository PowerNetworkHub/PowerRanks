package nl.svenar.PowerRanks;

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
import nl.svenar.PowerRanks.Cache.PowerConfigurationSection;
import nl.svenar.PowerRanks.Commands.Cmd;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerPermissibleBase;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Database.PowerDatabase;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnInteract;
import nl.svenar.PowerRanks.Events.OnInventory;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.Events.OnMove;
import nl.svenar.PowerRanks.Events.OnSignChanged;
import nl.svenar.PowerRanks.Events.OnWorldChange;
import nl.svenar.PowerRanks.addons.AddonsManager;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import nl.svenar.PowerRanks.api.PowerRanksAPI;
import nl.svenar.PowerRanks.bungee.BungeeMessageHandler;
import nl.svenar.PowerRanks.bungee.PowerBungeeEvents;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.metrics.Metrics;
import nl.svenar.PowerRanks.update.ConfigFilesUpdater;
import nl.svenar.PowerRanks.update.Updater;
import nl.svenar.PowerRanks.update.Updater.UpdateResult;
import nl.svenar.PowerRanks.update.Updater.UpdateType;

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

	public static PluginDescriptionFile pdf;
	public AddonsManager addonsManager;
	public String plp;
	public static Logger log;
	public static String configFileLoc;
	public static String fileLoc;
	public static String langFileLoc;
	public static String factoryresetid = null;
	public static Instant powerranks_start_time = Instant.now();
	public boolean powerranks_enabled = false;
	public BungeeMessageHandler bungee_message_handler;
	public PowerBungeeEvents power_bungee_events;

	// Soft Dependencies
	public static boolean vaultEconomyEnabled = false;
	public static boolean vaultPermissionsEnabled = false;
	public static PowerRanksExpansion placeholderapiExpansion;
	public static boolean plugin_hook_deluxetags = false;
	public static boolean plugin_hook_nametagedit = false;
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
//	private Map<UUID, PowerPermissibleBase> playerPowerPermissableBase = new HashMap<UUID, PowerPermissibleBase>();
//	public Map<UUID, ArrayList<String>> playerDisallowedPermissions = new HashMap<UUID, ArrayList<String>>();
//	public Map<UUID, ArrayList<String>> playerAllowedPermissions = new HashMap<UUID, ArrayList<String>>();
	public Map<UUID, String> playerTablistNameBackup = new HashMap<UUID, String>();
	public Map<UUID, Long> playerLoginTime = new HashMap<UUID, Long>();
	public Map<UUID, Boolean> playerSetupPermissionsQueue = new HashMap<UUID, Boolean>();

	private PowerDatabase prdb;

	public static enum StorageType {
		INIT, YAML, MySQL, SQLite
	}

	private static StorageType currentStorageType = StorageType.INIT;

	public PowerRanks() {
		PowerRanks.pdf = this.getDescription();
		this.plp = ChatColor.BLACK + "[" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
		PowerRanks.configFileLoc = this.getDataFolder() + File.separator;
		PowerRanks.fileLoc = this.getDataFolder() + File.separator + "Ranks" + File.separator;
		PowerRanks.langFileLoc = PowerRanks.configFileLoc + "lang.yml";
		this.updatemsg = "";
	}

	public void onEnable() {
		powerranks_enabled = true;
		PowerRanks.log = this.getLogger();
		PowerRanksAPI.plugin = this;

		new PowerRanksExceptionsHandler(getDataFolder());

//		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);
//		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnBuild(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInteract(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnSignChanged(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInventory(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnMove(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnWorldChange(this), (Plugin) this);

		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new Cmd(this));
		Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new Cmd(this));

//		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new PowerCommandHandler(this));
//		Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new PowerCommandHandler(this));

		Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter(new ChatTabExecutor(this));
		Bukkit.getServer().getPluginCommand("pr").setTabCompleter(new ChatTabExecutor(this));

		new PowerRanksChatColor();
		new Messages(this);
		new PowerRanksVerbose(this);

//		this.createDir(PowerRanks.fileLoc);
//		this.configFile = new File(this.getDataFolder(), "config.yml");
//		this.ranksFile = new File(PowerRanks.fileLoc, "Ranks.yml");
//		this.playersFile = new File(PowerRanks.fileLoc, "Players.yml");
//		this.langFile = new File(this.getDataFolder(), "lang.yml");
//		this.config = (FileConfiguration) new YamlConfiguration();
//		this.ranks = (FileConfiguration) new YamlConfiguration();
//		this.players = (FileConfiguration) new YamlConfiguration();
//		this.lang = (FileConfiguration) new YamlConfiguration();
//		try {
//			this.copyFiles();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		this.loadAllFiles();

		this.createDir(PowerRanks.fileLoc);

		try {
			this.copyFiles(currentStorageType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.loadAllFiles(currentStorageType);

		ConfigFilesUpdater.updateConfigFiles(this, currentStorageType);

		new CachedConfig(this);

		switch (CachedConfig.getString("storage.type").toLowerCase()) {
		case "yaml":
			currentStorageType = StorageType.YAML;
			PowerRanks.log.info("Using storage type: YAML");
			break;

		case "mysql":
			currentStorageType = StorageType.MySQL;
			PowerRanks.log.info("Using storage type: MySQL");
			PowerRanks.log.warning("===----------WARNING----------===");
			PowerRanks.log.warning("");
			PowerRanks.log.warning("Using MySQL as storage is not yet ready");
			PowerRanks.log.warning("Crashes or data loss may occur");
			PowerRanks.log.warning("Bug reports regarding MySQL will be ignored!");
			PowerRanks.log.warning("");
			PowerRanks.log.warning("===---------------------------===");
			break;

		case "sqlite":
			currentStorageType = StorageType.SQLite;
			PowerRanks.log.info("Using storage type: SQLite");
			PowerRanks.log.warning("===----------WARNING----------===");
			PowerRanks.log.warning("");
			PowerRanks.log.warning("Using SQLite as storage is not yet ready");
			PowerRanks.log.warning("Crashes or data loss may occur");
			PowerRanks.log.warning("Bug reports regarding SQLite will be ignored!");
			PowerRanks.log.warning("");
			PowerRanks.log.warning("===---------------------------===");
			break;

		default:
			PowerRanks.log.warning("===--------------------===");
			PowerRanks.log.warning("");
			PowerRanks.log.warning("WARNING PowerRanks is disabled!");
			PowerRanks.log.warning("Unknown storage type configured: " + CachedConfig.getString("storage.type"));
			PowerRanks.log.warning("");
			PowerRanks.log.warning("===--------------------===");
			PluginManager plg = Bukkit.getPluginManager();
			plg.disablePlugin(plg.getPlugin(PowerRanks.pdf.getName()));
			break;
		}

		this.createDir(PowerRanks.fileLoc);

		try {
			this.copyFiles(currentStorageType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.loadAllFiles(currentStorageType);

		ConfigFilesUpdater.updateDataFiles(this, currentStorageType);

		// Database
		prdb = new PowerDatabase(this, currentStorageType, CachedConfig.getString("storage.database.host"), CachedConfig.getInt("storage.database.port"), CachedConfig.getString("storage.database.username"),
				CachedConfig.getString("storage.database.password"), CachedConfig.getString("storage.database.database"));

		if (CachedConfig.getString("storage.type").equalsIgnoreCase("mysql")) {
			prdb.connectMYSQL();
		} else if (CachedConfig.getString("storage.type").equalsIgnoreCase("sqlite")) {
			prdb.connectSQLITE();
		}
		// Database

		new CachedPlayers(this, this.prdb);
		new CachedRanks(this, this.prdb);

		if (handle_update_checking()) {
			return;
		}

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

		setupSoftDependencies();

//		this.setupPermissions();

		GUI.setPlugin(this);

		PowerRanks.log.info("----------------------");
		PowerRanks.log.info("Loading add-ons");
		addonsManager = new AddonsManager(this);
		addonsManager.setup();

		if (CachedConfig.getBoolean("bungeecord.enabled")) {
			PowerRanks.log.info("");
			PowerRanks.log.info("Loading BungeeCord");
			bungee_message_handler = new BungeeMessageHandler(this);
			PowerRanks.log.info("Loaded BungeeCord");
//			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "powerranks:onjoin");
//			this.getServer().getMessenger().registerIncomingPluginChannel(this, "powerranks:onjoin", (PluginMessageListener) bungee_message_listener);
		}
		power_bungee_events = new PowerBungeeEvents(this);

		PowerRanks.log.info("----------------------");

		PowerRanks.log.info("Enabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
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
	}

	public void onDisable() {
		powerranks_enabled = false;
		Bukkit.getServer().getScheduler().cancelTasks(this);

//		for (Player player : this.getServer().getOnlinePlayers()) {
//			this.playerUninjectPermissible(player);
//		}

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

	private Player getPlayerFromUUID(UUID uuid) {
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "===----------Checking UUID----------===");
		Player player = null;
		for (Player online_player : Bukkit.getServer().getOnlinePlayers()) {
			PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "Matching '" + online_player.getName() + "' " + (uuid == online_player.getUniqueId() ? "MATCH!" : "No match") + " (" + uuid + ", " + online_player.getUniqueId() + ")");
			if (uuid == online_player.getUniqueId()) {
				player = online_player;
				break;
			}
		}
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "===---------------------------------===");
		return player;
	}

	private void setupSoftDependencies() {
		boolean has_vault_economy = this.getServer().getPluginManager().getPlugin("Vault") != null && getConfigBool("plugin_hook.vault_economy");
		boolean has_vault_permissions = this.getServer().getPluginManager().getPlugin("Vault") != null && getConfigBool("plugin_hook.vault_permissions");
		boolean has_placeholderapi = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && getConfigBool("plugin_hook.placeholderapi");
		boolean has_deluxetags = this.getServer().getPluginManager().getPlugin("DeluxeTags") != null && getConfigBool("plugin_hook.deluxetags");
		boolean has_nametagedit = this.getServer().getPluginManager().getPlugin("NametagEdit") != null && getConfigBool("plugin_hook.nametagedit");

		PowerRanks.log.info("Checking for plugins to hook in to:");
		if (has_vault_economy || has_vault_permissions) {
			PowerRanks.log.info("Vault found!");
			if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
				if (has_vault_economy) {
					PowerRanks.log.info("Enabling Vault Economy integration.");
				}
				if (has_vault_permissions) {
					PowerRanks.log.info("Enabling Vault Permission integration (experimental).");
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

		if (!has_vault_economy && !has_vault_permissions && !has_placeholderapi && !has_deluxetags && !has_nametagedit)
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
				PowerRanks.log.info("------------------------------------");
				PowerRanks.log.info("A new " + PowerRanks.pdf.getName() + " version is available!");
				PowerRanks.log.info("Current version: " + PowerRanks.pdf.getVersion());
				PowerRanks.log.info("New version: " + updater.getLatestName().replaceAll("[a-zA-Z\" ]", ""));
				if (!getConfigBool("updates.automatic_download_updates"))
					PowerRanks.log.info("Download the new version from: " + bukkit_dev_url_powerranks);
				else
					PowerRanks.log.info("Plugin will now be updated!");
				PowerRanks.log.info("------------------------------------");
			} else {
				log.info("No new version available");
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
			this.copyFiles(currentStorageType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.loadAllFiles(currentStorageType);

		new CachedConfig(this);
		new CachedPlayers(this, this.prdb);
		new CachedRanks(this, this.prdb);

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

//		this.setupPermissions();

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
			PowerRanks.log.warning("To forcefuly get rid of this message with all its consequences use the following command:");
			PowerRanks.log.warning("/pr forceupdateconfigversion");
			PowerRanks.log.warning("Visit " + PowerRanks.pdf.getWebsite() + " for more info.");
			PowerRanks.log.warning("===------------------------------===");
		} else {
			PowerRanks.log.info("===------------------------------===");
			PowerRanks.log.info("Version mismatch detected in: " + fileName);
			PowerRanks.log.info("Automatically updating " + fileName);
			PowerRanks.log.info("===------------------------------===");
		}
	}

	private void copyFiles(StorageType storageType) throws Exception {
		this.configFile = new File(this.getDataFolder(), "config.yml");
		this.config = (FileConfiguration) new YamlConfiguration();
		this.langFile = new File(this.getDataFolder(), "lang.yml");
		this.lang = (FileConfiguration) new YamlConfiguration();

		if (storageType == StorageType.YAML) {
			this.ranksFile = new File(PowerRanks.fileLoc, "Ranks.yml");
			this.ranks = (FileConfiguration) new YamlConfiguration();
			this.playersFile = new File(PowerRanks.fileLoc, "Players.yml");
			this.players = (FileConfiguration) new YamlConfiguration();
		}

		if (!this.configFile.exists()) {
			this.configFile.getParentFile().mkdirs();
			this.copy(this.getResource("config.yml"), this.configFile);
		}
		if (!this.langFile.exists()) {
			this.langFile.getParentFile().mkdirs();
			this.copy(this.getResource("lang.yml"), this.langFile);
		}
		if (storageType == StorageType.YAML) {
			if (!this.ranksFile.exists()) {
				this.ranksFile.getParentFile().mkdirs();
				this.copy(this.getResource("Ranks.yml"), this.ranksFile);
			}
			if (!this.playersFile.exists()) {
				this.playersFile.getParentFile().mkdirs();
				this.copy(this.getResource("Players.yml"), this.playersFile);
			}
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

	public void loadAllFiles(StorageType storageType) {
		try {
			this.config.load(this.configFile);
			this.lang.load(this.langFile);
			if (storageType == StorageType.YAML) {
				this.ranks.load(this.ranksFile);
				this.players.load(this.playersFile);
			}

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

		String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
		String prefix = CachedRanks.getString("Groups." + rank + ".chat.prefix");
		String suffix = CachedRanks.getString("Groups." + rank + ".chat.suffix");
		String nameColor = CachedRanks.getString("Groups." + rank + ".chat.nameColor");

		String subprefix = "";
		String subsuffix = "";
		String usertag = "";

		try {
			if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") != null) {
				PowerConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");
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
					PowerConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
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

		try {
			if (updateNTE) {
				updateNametagEditData(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor);
			}

			if (!CachedConfig.getBoolean("tablist_modification.enabled"))
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

			format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix).put("usertag", usertag)
					.put("player", nameColor + player.getPlayerListName()).put("world", player.getWorld().getName()).build(), '[', ']');

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
//		current_playtime = (int) CachedPlayers.get("players." + player.getUniqueId() + ".playtime");
		try {
			current_playtime = CachedPlayers.getInt("players." + player.getUniqueId() + ".playtime");
//			PowerRanks.log.info("INTEGER");
//			PowerRanks.log.info("" + current_playtime);
		} catch (Exception e) {
			try {
				current_playtime = CachedPlayers.getDouble("players." + player.getUniqueId() + ".playtime").intValue();
//				PowerRanks.log.info("DOUBLE");
//				PowerRanks.log.info("" + current_playtime);
			} catch (Exception e1) {
				current_playtime = CachedPlayers.getLong("players." + player.getUniqueId() + ".playtime").intValue();
//				PowerRanks.log.info("LONG");
//				PowerRanks.log.info("" + current_playtime);
			}
		}
		CachedPlayers.set("players." + player.getUniqueId() + ".playtime", current_playtime + (leave_time - join_time) / 1000, false);
	}

	public void updatePlayersWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			updateTablistName(p);
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

	public static StorageType getStorageType() {
		return currentStorageType;
	}

	public ArrayList<String> getEffectivePlayerPermissions(Player player) {
		ArrayList<String> permissions = new ArrayList<String>();

		String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");

		try {
			if (CachedRanks.getStringList("Groups." + rank + ".permissions") != null) {
				for (String permission : CachedRanks.getStringList("Groups." + rank + ".permissions"))
					permissions.add(permission);
			} else {
				CachedRanks.set("Groups." + rank + ".permissions", new ArrayList<String>());
			}
		} catch (Exception e) {
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
			PowerRanks.log.warning("Exception catched in PowerRanks.getEffectivePlayerPermissions");
			PowerRanks.log.warning("Ranks.yml 'Groups." + rank + ".permissions' was not found (Player's main rank)");
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
		}

		try {
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
		} catch (Exception e) {
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
			PowerRanks.log.warning("Exception catched in PowerRanks.getEffectivePlayerPermissions");
			PowerRanks.log.warning("Ranks.yml 'Groups." + rank + ".permissions' was not found (Player's main rank inheritances)");
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
		}

		try {
			if (CachedPlayers.getStringList("players." + player.getUniqueId() + ".permissions") != null) {
				for (String permission : CachedPlayers.getStringList("players." + player.getUniqueId() + ".permissions"))
					permissions.add(permission);
			} else {
				CachedPlayers.set("players." + player.getUniqueId() + ".permissions", new ArrayList<String>(), false);
			}
		} catch (Exception e) {
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
			PowerRanks.log.warning("Exception catched in PowerRanks.getEffectivePlayerPermissions");
			PowerRanks.log.warning("Players.yml 'players." + player.getUniqueId() + ".permissions' was not found (Player's permissions)");
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
		}

		try {
			ArrayList<String> useable_subranks = new ArrayList<String>();
			PowerConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks");
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
		} catch (Exception e) {
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
			PowerRanks.log.warning("Exception catched in PowerRanks.getEffectivePlayerPermissions");
			PowerRanks.log.warning("Players.yml An error occured in 'players." + player.getUniqueId() + ".subranks' (Player's subranks)");
			PowerRanks.log.warning("===== ---------- An error occured ---------- =====");
		}

		return permissions;
	}
}