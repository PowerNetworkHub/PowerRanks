package nl.svenar.PowerRanks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
// import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import nl.svenar.PowerRanks.Cache.CacheManager;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommandHandler;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerPermissibleBase;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;
import nl.svenar.PowerRanks.Events.OnBlockChange;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnInteract;
import nl.svenar.PowerRanks.Events.OnInventory;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.Events.OnMove;
import nl.svenar.PowerRanks.Events.OnPreCommand;
import nl.svenar.PowerRanks.Events.OnSignChanged;
import nl.svenar.PowerRanks.Events.OnWorldChange;
import nl.svenar.PowerRanks.addons.AddonsManager;
import nl.svenar.PowerRanks.api.PowerRanksAPI;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.metrics.Metrics;
import nl.svenar.PowerRanks.update.ConfigFilesUpdater;
// import nl.svenar.PowerRanks.update.ConfigFilesUpdater;
import nl.svenar.PowerRanks.update.Updater;
import nl.svenar.PowerRanks.update.Updater.UpdateResult;
import nl.svenar.PowerRanks.update.Updater.UpdateType;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.provided.YAMLConfigManager;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.structure.PRSubrank;

import com.google.common.collect.ImmutableMap;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;

import me.clip.deluxetags.DeluxeTag;
import me.clip.placeholderapi.PlaceholderAPI;

public class PowerRanks extends JavaPlugin implements Listener {
	public String bukkit_dev_url_powerranks = "https://dev.bukkit.org/projects/powerranks";
	public ArrayList<String> donation_urls = new ArrayList<String>(
			Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar"));

	private static PowerRanks instance;
	public static PluginDescriptionFile pdf;
	public AddonsManager addonsManager;
	public String plp;
	public static Logger log;
	// public static String configFileLoc;
	public static String fileLoc;
	// public static String langFileLoc;
	public static String factoryresetid = null;
	public static Instant powerranks_start_time = Instant.now();
	private String update_available = "";

	private static PowerConfigManager configManager;
	private static PowerConfigManager languageManager;
	private static PowerConfigManager usertagManager;

	// Soft Dependencies
	public static boolean vaultEconomyEnabled = false;
	public static boolean vaultPermissionsEnabled = false;
	public static PowerRanksExpansion placeholderapiExpansion;
	public static boolean plugin_hook_deluxetags = false;
	public static boolean plugin_hook_nametagedit = false;
	// Soft Dependencies

	// File configFile;
	// File ranksFile;
	// File playersFile;
	// File langFile;
	// FileConfiguration config;
	// FileConfiguration ranks;
	// FileConfiguration players;
	// FileConfiguration lang;
	public String updatemsg;
	public Map<UUID, PermissionAttachment> playerPermissionAttachment = new HashMap<UUID, PermissionAttachment>();
	// public Map<UUID, ArrayList<String>> playerDisallowedPermissions = new
	// HashMap<UUID, ArrayList<String>>();
	// public Map<UUID, ArrayList<String>> playerAllowedPermissions = new
	// HashMap<UUID, ArrayList<String>>();
	public Map<UUID, String> playerTablistNameBackup = new HashMap<UUID, String>();
	// public Map<UUID, Long> playerLoginTime = new HashMap<UUID, Long>();
	public Map<UUID, Long> playerPlayTimeCache = new HashMap<UUID, Long>();
	// private Map<UUID, Boolean> playerSetupPermissionsQueue = new HashMap<UUID,
	// Boolean>();

	public PowerRanks() {
		PowerRanks.pdf = this.getDescription();
		this.plp = ChatColor.BLACK + "[" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.BLACK + "]"
				+ ChatColor.RESET + " ";
		// PowerRanks.configFileLoc = this.getDataFolder() + File.separator;
		PowerRanks.fileLoc = this.getDataFolder() + File.separator;
		// PowerRanks.langFileLoc = PowerRanks.configFileLoc + "lang.yml";
		this.updatemsg = "";
	}

	public void onEnable() {
		instance = this;

		Instant startTime = Instant.now();

		PowerRanks.log = this.getLogger();
		PowerRanksAPI.plugin = this;

		ConfigFilesUpdater.updateConfigFiles();

		// PowerRanks.log.info("=== ---------- LOADING EVENTS ---------- ===");
		// Bukkit.getServer().getPluginManager().registerEvents((Listener) this,
		// (Plugin) this);
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
		// Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor)
		// new Cmd(this));
		// Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new
		// Cmd(this));
		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new PowerCommandHandler(this));

		// Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor)
		// new PowerCommandHandler(this));
		// Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new
		// PowerCommandHandler(this));

		Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter(new ChatTabExecutor(this));
		// Bukkit.getServer().getPluginCommand("pr").setTabCompleter(new
		// ChatTabExecutor(this));

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ----------- LOADING DATA ----------- ===");
		new PowerRanksChatColor();
		new Messages(this);
		new PowerRanksVerbose(this);

		this.createDir(PowerRanks.fileLoc);

		PowerRanks.log.info("Loading config");
		configManager = new YAMLConfigManager(PowerRanks.fileLoc, "config.yml", "config.yml");
		languageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml", "lang.yml");
		usertagManager = new YAMLConfigManager(PowerRanks.fileLoc, "usertags.yml");

		// new CachedConfig(this);
		PowerRanks.log.info("Loading player&rank data");
		CacheManager.load(PowerRanks.fileLoc);
		loadDefaultRanks();

		// ConfigFilesUpdater.updateConfigFiles(this); // TODO: remove

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

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
		PowerRanks.log
				.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  PowerRanks v" + pdf.getVersion());
		PowerRanks.log.info(ChatColor.AQUA + "  ██   ██ ██   ██" + ChatColor.GREEN + "  Running on "
				+ Util.getServerType(getServer()) + " v" + Util.getServerVersion(getServer()));
		PowerRanks.log.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  Startup time: "
				+ Duration.between(startTime, Instant.now()).toMillis() + "ms");
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.GREEN + "  Loaded "
				+ CacheManager.getRanks().size() + " ranks and " + CacheManager.getPlayers().size() + " players ("
				+ getConfigManager().getString("storage.type", "yaml").toUpperCase() + ") " + update_available);
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.RED + "  "
				+ (System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
						? "Reload detected, why do you hate yourself :C"
						: ""));
		PowerRanks.log.info("");

		System.setProperty("POWERRANKSRUNNING", "TRUE");

		// PowerRanks.log.info("Enabled " + PowerRanks.pdf.getName() + " v" +
		// PowerRanks.pdf.getVersion());
		PowerRanks.log
				.info("If you'd like to donate, please visit " + donation_urls.get(0) + " or " + donation_urls.get(1));

		if (handle_update_checking()) {
			return;
		}

		setupMetrics();

		setupTasks();
	}

	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTasks(this);

		// for (Player player : this.getServer().getOnlinePlayers()) {
		// this.playerUninjectPermissible(player);
		// }

		getConfigManager().save();
		getLanguageManager().save();
		getUsertagManager().save();

		if (Objects.nonNull(this.addonsManager)) {
			this.addonsManager.disable();
		}

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

		if (PowerRanks.log != null && PowerRanks.pdf != null) {
			PowerRanks.log.info("Disabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		}
	}

	private void loadDefaultRanks() {
		if (CacheManager.getRanks().size() > 0) {
			return;
		}

		PRRank rankMember = new PRRank();
		rankMember.setName("Member");
		rankMember.setPrefix("#127e00M#1a8704E#239109M#2c9b0eB#35a513E#3eaf18R");

		PRRank rankModerator = new PRRank();
		rankModerator.setName("Moderator");
		rankModerator.setPrefix("#9d1dffM#a51eefO#ae1fdfD#b720d0E#bf21c0R#c822b0A#d123a1T#d92491O#e22581R");

		PRRank rankAdmin = new PRRank();
		rankAdmin.setName("Admin");
		rankAdmin.setPrefix("#ffff00A#fbcc00D#f79900M#f36600I#ef3300N");

		PRRank rankOwner = new PRRank();
		rankOwner.setName("Owner");
		rankOwner.setPrefix("#ff00ffO#cc33ffW#9966ffN#6699ffE#33ccffR");

		CacheManager.addRank(rankMember);
		CacheManager.addRank(rankModerator);
		CacheManager.addRank(rankAdmin);
		CacheManager.addRank(rankOwner);

		CacheManager.setDefaultRank(rankMember.getName());
		CacheManager.save();
	}

	private void setupTasks() {
		int playtime_interval = 60;

		try {
			playtime_interval = configManager.getInt("general.playtime-update-interval", 0);
		} catch (Exception e) {
		}

		if (playtime_interval < 1) {
			playtime_interval = 1;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				PowerRanksVerbose.log("task", "Running task update player playtime");
				// updateAllPlayersTABlist();
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					// playerPlayTimeCache

					long current_time = new Date().getTime();
					long last_time = current_time;
					try {
						last_time = playerPlayTimeCache.get(player.getUniqueId()) - 1000;
					} catch (Exception e1) {
					}

					// player.sendMessage("T: " + current_time + " - " + last_time + " - " +
					// CachedPlayers.getLong("players." + player.getUniqueId() + ".playtime"));
					updatePlaytime(player, last_time, current_time, true);

					long time = new Date().getTime();
					playerPlayTimeCache.put(player.getUniqueId(), time);

					// TimeZone tz = TimeZone.getTimeZone("UTC");
					// SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
					// df.setTimeZone(tz);
					// String time = df.format(new Date((CachedPlayers.getLong("players." +
					// player.getUniqueId() + ".playtime") == null ? CachedPlayers.getInt("players."
					// + player.getUniqueId() + ".playtime") : CachedPlayers.getLong("players." +
					// player.getUniqueId() + ".playtime")) * 1000));

				}

				// CachedPlayers.save();
			}
		}.runTaskTimer(this, 0, playtime_interval * 20);
	}

	private Player getPlayerFromUUID(UUID uuid) {
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "=== ----------Checking UUID---------- ===");
		Player player = null;
		for (Player online_player : Bukkit.getServer().getOnlinePlayers()) {
			PowerRanksVerbose.log("getPlayerFromUUID(UUID)",
					"Matching '" + online_player.getName() + "' "
							+ (uuid == online_player.getUniqueId() ? "MATCH!" : "No match") + " (" + uuid + ", "
							+ online_player.getUniqueId() + ")");
			if (uuid == online_player.getUniqueId()) {
				player = online_player;
				break;
			}
		}
		PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "=== --------------------------------- ===");
		return player;
	}

	private void setupSoftDependencies() {
		boolean has_vault_economy = this.getServer().getPluginManager().getPlugin("Vault") != null
				&& getConfigBool("plugin_hook.vault_economy");
		boolean has_vault_permissions = this.getServer().getPluginManager().getPlugin("Vault") != null
				&& getConfigBool("plugin_hook.vault_permissions");
		boolean has_placeholderapi = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null
				&& getConfigBool("plugin_hook.placeholderapi");
		boolean has_deluxetags = this.getServer().getPluginManager().getPlugin("DeluxeTags") != null
				&& getConfigBool("plugin_hook.deluxetags");
		boolean has_nametagedit = this.getServer().getPluginManager().getPlugin("NametagEdit") != null
				&& getConfigBool("plugin_hook.nametagedit");

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
			Updater updater = new Updater(this, 79251, this.getFile(),
					getConfigBool("updates.automatic_download_updates") ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD,
					true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				// PowerRanks.log.info("------------------------------------");
				// PowerRanks.log.info("A new " + PowerRanks.pdf.getName() + " version is
				// available!");
				// PowerRanks.log.info("Current version: " + PowerRanks.pdf.getVersion());
				// PowerRanks.log.info("New version: " +
				// updater.getLatestName().replaceAll("[a-zA-Z\" ]", ""));
				if (!getConfigBool("updates.automatic_download_updates")) {
					update_available = "Update available! (v" + updater.getLatestName().replaceAll("[a-zA-Z\" ]", "")
							+ ")";
					// PowerRanks.log.info("Download the new version from: " +
					// bukkit_dev_url_powerranks);
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
				// plg.enablePlugin(plgname);
			}

			if (updater.getResult() == UpdateResult.FAIL_DOWNLOAD) {
				PowerRanks.log.info("Update found but failed to download!");
			}
		}

		return false;
	}

	/**
	 * Send some anonymous data about this PowerRanks installation
	 */
	private void setupMetrics() {

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
				boolean accepterAddonManagerTerms = configManager.getBool("addon_manager.accepted_terms", false);
				return String.valueOf(accepterAddonManagerTerms ? "true" : "false");
			}
		}));

		metrics.addCustomChart(new Metrics.SimplePie("number_of_registered_players", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return String.valueOf(CacheManager.getPlayers().size());
			}
		}));

		metrics.addCustomChart(new Metrics.SimplePie("number_of_registered_ranks", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return String.valueOf(CacheManager.getRanks().size());
			}
		}));

		metrics.addCustomChart(new Metrics.SimplePie("storage_method", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return getConfigManager().getString("storage.type", "yaml").toUpperCase();
			}
		}));
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

	private boolean deleteDir(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				this.deleteDir(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public void factoryReset(CommandSender sender) {

		CacheManager.setRanks(new ArrayList<PRRank>());
		CacheManager.setPlayers(new ArrayList<PRPlayer>());

		this.deleteDir(new File(PowerRanks.fileLoc));
		this.createDir(PowerRanks.fileLoc);

		configManager = new YAMLConfigManager(PowerRanks.fileLoc, "config.yml", "config.yml");
		languageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml", "lang.yml");

		PowerConfigManager ranksManager = new YAMLConfigManager(PowerRanks.fileLoc, "ranks.yml");
		PowerConfigManager playersManager = new YAMLConfigManager(PowerRanks.fileLoc, "players.yml");
		ranksManager.save();
		playersManager.save();

		CacheManager.load(PowerRanks.fileLoc);
		this.loadDefaultRanks();

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

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

	private void updateNametagEditData(Player player, String prefix, String suffix, String subprefix, String subsuffix,
			String usertag, String nameColor) {
		if (plugin_hook_nametagedit) {
			PowerRanksVerbose.log("updateNametagEditData", "Updating " + player.getName() + "'s nametag format");

			BukkitScheduler scheduler = getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					String prefix_format = configManager.getString("nametagedit.prefix", "");
					String suffix_format = configManager.getString("nametagedit.suffix", "");
					if (prefix_format == null || suffix_format == null) {
						if (prefix_format == null) {
							PowerRanksVerbose.log("updateNametagEditData", "prefix_format is NULL");
						}

						if (suffix_format == null) {
							PowerRanksVerbose.log("updateNametagEditData", "suffix_format is NULL");
						}
						return;
					}

					prefix_format = Util.powerFormatter(prefix_format,
							ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix)
									.put("subprefix", subprefix).put("subsuffix", subsuffix)
									.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag
											: DeluxeTag.getPlayerDisplayTag(player))
									.build(),
							'[', ']');

					suffix_format = Util.powerFormatter(suffix_format,
							ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix)
									.put("subprefix", subprefix).put("subsuffix", subsuffix)
									.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag
											: DeluxeTag.getPlayerDisplayTag(player))
									.build(),
							'[', ']');

					prefix_format += nameColor;

					prefix_format = PowerRanksChatColor.colorize(prefix_format, true);
					suffix_format = PowerRanksChatColor.colorize(suffix_format, true);

					INametagApi nteAPI = NametagEdit.getApi();
					if (nteAPI != null) {
						nteAPI.setNametag(player, prefix_format + (prefix_format.length() > 0 ? " " : ""),
								(suffix_format.length() > 0 ? " " : "") + suffix_format);
						updateTablistName(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor, false);
					}
				}
			}, 20L);
		}
	}

	public void updateTablistName(Player player) {
		// PowerRanksVerbose.log("updateTablistName", "Updating " + player.getName() +
		// "'s tablist format");
		try {
			player.updateCommands(); // TODO find a better place for this
		} catch (NoSuchMethodError e) {
		}

		String rank = CacheManager.getPlayer(player.getUniqueId().toString()).getRank();// CachedPlayers.getString("players."
																						// + player.getUniqueId() +
																						// ".rank");
		String prefix = CacheManager.getRank(rank).getPrefix();// CachedRanks.getString("Groups." + rank +
																// ".chat.prefix");
		String suffix = CacheManager.getRank(rank).getSuffix();// CachedRanks.getString("Groups." + rank +
																// ".chat.suffix");
		String nameColor = CacheManager.getRank(rank).getNamecolor();// CachedRanks.getString("Groups." + rank +
																		// ".chat.nameColor");

		String subprefix = "";
		String subsuffix = "";
		String usertag = "";

		try {
			ArrayList<PRSubrank> subranks = CacheManager.getPlayer(player.getUniqueId().toString()).getSubRanks();
			for (PRSubrank subrank : subranks) {
				PRRank targetRank = CacheManager.getRank(subrank.getName());
				boolean in_world = false;

				String player_current_world = player.getWorld().getName();
				List<String> worlds = subrank.getWorlds();
				for (String world : worlds) {
					if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
						in_world = true;
					}
				}

				if (Objects.nonNull(targetRank)) {
					if (in_world) {
						if (subrank.getUsingPrefix()) {
							subprefix += ChatColor.RESET + targetRank.getPrefix();
						}

						if (subrank.getUsingSuffix()) {
							subsuffix += ChatColor.RESET + targetRank.getSuffix();

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

			PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
			Map<?, ?> availableUsertags = getUsertagManager().getMap("usertags", new HashMap<String, String>());
			ArrayList<String> playerUsertags = targetPlayer.getUsertags();

			for (String playerUsertag : playerUsertags) {
				String value = "";
				for (Entry<?, ?> entry : availableUsertags.entrySet()) {
					if (entry.getKey().toString().equalsIgnoreCase(playerUsertag)) {
						value = entry.getValue().toString();
					}
				}

				if (value.length() > 0) {
					usertag += (usertag.length() > 0 ? " " : "") + value;
				}
			}

			updateTablistName(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTablistName(Player player, String prefix, String suffix, String subprefix, String subsuffix,
			String usertag, String nameColor, boolean updateNTE) {
		PowerRanksVerbose.log("updateTablistName", "Updating " + player.getName() + "'s tablist format");

		String player_formatted_name = (nameColor.length() == 0 ? "&r" : "")
				+ applyMultiColorFlow(nameColor, player.getDisplayName());

		try {
			if (updateNTE) {
				updateNametagEditData(player, prefix, suffix, subprefix, subsuffix, usertag, nameColor);
			}

			if (!configManager.getBool("tablist_modification.enabled", true))
				return;

			player.setPlayerListName(playerTablistNameBackup.get(player.getUniqueId()));

			playerTablistNameBackup.put(player.getUniqueId(), player.getPlayerListName());

			String format = configManager.getString("tablist_modification.format", "");

			if (format.contains("[name]")) {
				String tmp_format = configManager.getString("tablist_modification.format", ""); // CachedConfig.getString("tablist_modification.format");
				tmp_format = tmp_format.replace("[name]", "[player]");
				configManager.setString("tablist_modification.format", tmp_format);
				// CachedConfig.set("tablist_modification.format", tmp_format);
				format = tmp_format;
			}

			format = Util.powerFormatter(format,
					ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix)
							.put("subprefix", subprefix).put("subsuffix", subsuffix).put("usertag", usertag)
							.put("player", player_formatted_name).put("world", player.getWorld().getName()).build(),
					'[', ']');

			while (format.endsWith(" ")) {
				format = format.substring(0, format.length() - 1);
			}

			if (PowerRanks.placeholderapiExpansion != null) {
				format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR,
						"" + PowerRanksChatColor.unformatted_default_char);
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

	// public static YamlConfiguration loadLangFile() {
	// File langFile = new File(PowerRanks.langFileLoc);
	// YamlConfiguration langYaml = new YamlConfiguration();
	// try {
	// langYaml.load(langFile);
	// } catch (IOException | InvalidConfigurationException e) {
	// e.printStackTrace();
	// }
	// return langYaml;
	// }

	public PowerRanksAPI loadAPI() {
		return new PowerRanksAPI();
	}

	public void updatePlaytime(Player player, long join_time, long leave_time, boolean write_to_file) {
		long current_playtime = CacheManager.getPlayer(player.getUniqueId().toString()).getPlaytime();

		CacheManager.getPlayer(player.getUniqueId().toString())
				.setPlaytime(current_playtime + (leave_time - join_time) / 1000);
		// try {
		// try {
		// current_playtime = CachedPlayers.getInt("players." + player.getUniqueId() +
		// ".playtime");
		// } catch (Exception e) {
		// try {
		// current_playtime = CachedPlayers.getDouble("players." + player.getUniqueId()
		// + ".playtime")
		// .intValue();
		// } catch (Exception e1) {
		// current_playtime = CachedPlayers.getLong("players." + player.getUniqueId() +
		// ".playtime")
		// .intValue();
		// }
		// }
		// CachedPlayers.set("players." + player.getUniqueId() + ".playtime",
		// current_playtime + (leave_time - join_time) / 1000, !write_to_file);
		// } catch (NullPointerException npe) {
		// }
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

	public ArrayList<PRPermission> getEffectivePlayerPermissions(Player player) {
		ArrayList<PRPermission> permissions = new ArrayList<PRPermission>();

		String rank = CacheManager.getPlayer(player.getUniqueId().toString()).getRank();

		for (PRPermission permission : CacheManager.getRank(rank).getPermissions()) {
			permissions.add(permission);
		}

		for (String inheritance : CacheManager.getRank(rank).getInheritances()) {
			for (PRPermission permission : CacheManager.getRank(inheritance).getPermissions()) {
				permissions.add(permission);
			}
		}

		for (PRPermission permission : CacheManager.getPlayer(player.getUniqueId().toString()).getPermissions()) {
			permissions.add(permission);
		}

		ArrayList<PRSubrank> useable_subranks = new ArrayList<PRSubrank>();

		ArrayList<PRSubrank> subranks = CacheManager.getPlayer(player.getUniqueId().toString()).getSubRanks();
		for (PRSubrank subrank : subranks) {
			boolean in_world = false;

			String player_current_world = player.getWorld().getName();
			List<String> worlds = subrank.getWorlds();
			for (String world : worlds) {
				if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
					in_world = true;
				}
			}

			if (in_world) {
				if (subrank.getUsingPermissions()) {
					useable_subranks.add(subrank);
				}
			}
		}

		for (PRSubrank subrank : useable_subranks) {
			if (Objects.nonNull(CacheManager.getRank(subrank.getName()))) {
				for (PRPermission permission : CacheManager.getRank(subrank.getName()).getPermissions()) {
					permissions.add(permission);
				}
			}

		}

		return permissions;
	}

	public static PowerConfigManager getConfigManager() {
		return configManager;
	}

	public static PowerConfigManager getLanguageManager() {
		return languageManager;
	}

	public static PowerConfigManager getUsertagManager() {
		return usertagManager;
	}

	public static PowerRanks getInstance() {
		return instance;
	}
}