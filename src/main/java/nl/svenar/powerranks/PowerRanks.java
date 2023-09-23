package nl.svenar.powerranks;

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

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import nl.svenar.common.PowerLogger;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.provided.YAMLConfigManager;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;
import nl.svenar.common.utils.PowerColor;
import nl.svenar.powerranks.addons.AddonsManager;
import nl.svenar.powerranks.api.PowerRanksAPI;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.cache.LanguageManager;
import nl.svenar.powerranks.commands.PowerCommandHandler;
import nl.svenar.powerranks.data.BungeecordManager;
import nl.svenar.powerranks.data.Messages;
import nl.svenar.powerranks.data.PowerPermissibleBase;
import nl.svenar.powerranks.data.PowerRanksVerbose;
import nl.svenar.powerranks.data.TablistManager;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.events.ChatTabExecutor;
import nl.svenar.powerranks.events.OnBlockChange;
import nl.svenar.powerranks.events.OnChat;
import nl.svenar.powerranks.events.OnInteract;
import nl.svenar.powerranks.events.OnInventory;
import nl.svenar.powerranks.events.OnJoin;
import nl.svenar.powerranks.events.OnMove;
import nl.svenar.powerranks.events.OnPreCommand;
import nl.svenar.powerranks.events.OnSignChanged;
import nl.svenar.powerranks.events.OnWorldChange;
import nl.svenar.powerranks.external.DeluxeTagsHook;
import nl.svenar.powerranks.external.PowerRanksExpansion;
import nl.svenar.powerranks.external.TABHook;
import nl.svenar.powerranks.external.VaultHook;
import nl.svenar.powerranks.gui.GUI;
import nl.svenar.powerranks.metrics.Metrics;
import nl.svenar.powerranks.update.ConfigFilesUpdater;
import nl.svenar.powerranks.update.Updater;
import nl.svenar.powerranks.update.Updater.UpdateResult;
import nl.svenar.powerranks.update.Updater.UpdateType;
import nl.svenar.powerranks.util.BukkitPowerColor;
import nl.svenar.powerranks.util.Util;

import com.google.common.collect.ImmutableMap;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;

import me.clip.placeholderapi.PlaceholderAPI;

public class PowerRanks extends JavaPlugin implements Listener {
	public String bukkit_dev_url_powerranks = "https://dev.bukkit.org/projects/powerranks";
	public ArrayList<String> donation_urls = new ArrayList<String>(Arrays.asList("https://ko-fi.com/svenar"));

	public int TASK_TPS = 20;
	private static PowerRanks instance;
	public static PluginDescriptionFile pdf;
	public AddonsManager addonsManager;
	private TablistManager tablistManager;
	private static BukkitPowerColor powerColor;
	public String plp;
	public static Logger log;
	public static String fileLoc;
	public static String factoryresetid = null;
	public static Instant powerranks_start_time = Instant.now();
	private String update_available = "";

	private static PowerConfigManager configManager;
	private static LanguageManager languageManager;
	private static PowerConfigManager usertagManager;
	private static PowerConfigManager tablistConfigManager;

	private BungeecordManager bungeecordManager;

	// Soft Dependencies
	private VaultHook vaultHook;
	private DeluxeTagsHook deluxeTagsHook;

	public static boolean vaultEconomyEnabled = false;
	public static boolean vaultPermissionsEnabled = false;
	public static PowerRanksExpansion placeholderapiExpansion;
	public static TABHook plugin_hook_tab;
	public static boolean plugin_hook_deluxetags = false;
	public static boolean plugin_hook_nametagedit = false;
	// Soft Dependencies

	public String updatemsg;
	public Map<UUID, PermissionAttachment> playerPermissionAttachment = new HashMap<UUID, PermissionAttachment>();
	public Map<UUID, String> playerTablistNameBackup = new HashMap<UUID, String>();
	public Map<UUID, Long> playerPlayTimeCache = new HashMap<UUID, Long>();
	// public Map<UUID, String> playerNameCache = new HashMap<UUID, String>();

	public PowerRanks() {
		PowerRanks.pdf = this.getDescription();
		this.plp = ChatColor.BLACK + "[" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.BLACK + "]"
				+ ChatColor.RESET + " ";
		PowerRanks.fileLoc = this.getDataFolder() + File.separator;
		this.updatemsg = "";
	}

	public void onEnable() {
		instance = this;

		new PowerLogger(getLogger());

		Instant startTime = Instant.now();

		PowerRanks.log = this.getLogger();
		PowerRanksAPI.plugin = this;

		ConfigFilesUpdater.updateOldDataFiles();

		if (new File(PowerRanks.fileLoc, "config.yml").exists() && new File(PowerRanks.fileLoc, "lang.yml").exists()) {
			ConfigFilesUpdater.updateConfigFiles();
		}

		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInteract(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnSignChanged(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInventory(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnMove(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnWorldChange(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnBlockChange(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnPreCommand(this), (Plugin) this);

		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new PowerCommandHandler(this));

		Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter(new ChatTabExecutor(this));

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ------- LOADING CONFIGURATION ------ ===");
		PowerRanks.powerColor = new BukkitPowerColor();
		new Messages(this);
		new PowerRanksVerbose(this);

		this.createDir(PowerRanks.fileLoc);

		PowerRanks.log.info("Loading config file");
		configManager = new YAMLConfigManager(PowerRanks.fileLoc, "config.yml", "config.yml");
		PowerRanks.log.info("Loading language file");
		languageManager = new LanguageManager();
		languageManager.setLanguage(configManager.getString("general.language", "en"));
		PowerRanks.log.info("Loading usertags file");
		usertagManager = new YAMLConfigManager(PowerRanks.fileLoc, "usertags.yml");
		PowerRanks.log.info("Loading tablist file");
		tablistConfigManager = new YAMLConfigManager(PowerRanks.fileLoc, "tablist.yml", "tablist.yml");

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ---------- LOADING ADDONS ---------- ===");
		addonsManager = new AddonsManager(this);
		addonsManager.setup();

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ----------- LOADING DATA ----------- ===");
		PowerRanks.log.info("Loading player & rank data");
		CacheManager.load(PowerRanks.fileLoc);
		loadDefaultRanks();

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

		PowerRanks.log.info("");
		PowerRanks.log.info("=== ------- LOADING PLUGIN HOOKS ------- ===");
		setupSoftDependencies();

		GUI.setPlugin(this);

		this.bungeecordManager = new BungeecordManager(this);
		this.bungeecordManager.start();

		this.tablistManager = new TablistManager();
		this.tablistManager.start();

		this.setupCheckRankTagsTask();

		PowerRanks.log.info("");
		PowerRanks.log
				.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  PowerRanks v" + pdf.getVersion());
		PowerRanks.log.info(ChatColor.AQUA + "  ██   ██ ██   ██" + ChatColor.GREEN + "  Running on "
				+ Util.getServerType(getServer()) + " v" + Util.getServerVersion(getServer()));
		PowerRanks.log.info(ChatColor.AQUA + "  ██████  ██████ " + ChatColor.GREEN + "  Startup time: "
				+ Duration.between(startTime, Instant.now()).toMillis() + "ms");
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.GREEN + "  Loaded "
				+ CacheManager.getRanks().size() + " ranks and " + CacheManager.getPlayers().size() + " players ("
				+ getConfigManager().getString("storage.type", "yaml").toUpperCase() + ") " + update_available);
		PowerRanks.log.info(ChatColor.AQUA + "  ██      ██   ██" + ChatColor.RED + "  "
				+ (System.getProperty("POWERRANKSRUNNING", "").equals("TRUE")
						? "Reload detected, why do you hate yourself :C"
						: ""));
		PowerRanks.log.info("");

		System.setProperty("POWERRANKSRUNNING", "TRUE");

		PowerRanks.log.info("If you'd like to donate, please visit " + donation_urls.get(0));

		if (handle_update_checking()) {
			return;
		}

		setupMetrics();

		setupTasks();

		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			OnJoin.handleJoin(this, player);
		}
	}

	public void onDisable() {
		this.tablistManager.stop();
		this.bungeecordManager.stop();

		Bukkit.getServer().getScheduler().cancelTasks(this);

		CacheManager.save();

		saveConfigurationFiles();

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

	private void saveConfigurationFiles() {
		boolean hasErrorInSaving = false;
		if (Objects.nonNull(getConfigManager())) {
			getConfigManager().save();
		} else {
			getLogger().warning("Failed to save configuration file!");
			hasErrorInSaving = true;
		}

		if (Objects.nonNull(getLanguageManager())) {
			getLanguageManager().save();
		} else {
			getLogger().warning("Failed to save language file!");
			hasErrorInSaving = true;
		}

		if (Objects.nonNull(getUsertagManager())) {
			getUsertagManager().save();
		} else {
			getLogger().warning("Failed to save usertags file!");
			hasErrorInSaving = true;
		}

		if (Objects.nonNull(getTablistConfigManager())) {
			getTablistConfigManager().save();
		} else {
			getLogger().warning("Failed to save tablist config file!");
			hasErrorInSaving = true;
		}

		if (hasErrorInSaving) {
			getLogger().warning("This could occur when PowerRanks restarted on a reload in your server.");
		}
	}

	private void loadDefaultRanks() {
		if (CacheManager.getRanks().size() > 0) {
			return;
		}

		PRRank rankMember = new PRRank();
		rankMember.setName("Member");
		rankMember.setDefault(true);
		rankMember.setWeight(0);
		rankMember.setPrefix("[gradient=#127e00,#3eaf18]MEMBER[/gradient]");

		PRRank rankModerator = new PRRank();
		rankModerator.setName("Moderator");
		rankModerator.setDefault(false);
		rankModerator.setWeight(50);
		rankMember.setPrefix("[gradient=#9d1dff,#e22581]MODERATOR[/gradient]");

		PRRank rankAdmin = new PRRank();
		rankAdmin.setName("Admin");
		rankAdmin.setDefault(false);
		rankAdmin.setWeight(75);
		rankMember.setPrefix("[gradient=#ffff00,#ef3300]ADMIN[/gradient]");

		PRRank rankOwner = new PRRank();
		rankOwner.setName("Owner");
		rankOwner.setDefault(false);
		rankOwner.setWeight(100);
		rankMember.setPrefix("[gradient=#ff00ff,#33ccff]OWNER[/gradient]");

		CacheManager.addRank(rankMember);
		CacheManager.addRank(rankModerator);
		CacheManager.addRank(rankAdmin);
		CacheManager.addRank(rankOwner);

		CacheManager.save();
	}

	private void setupTasks() {
		int playtime_interval = 60;
		int autosave_interval = 600;

		try {
			playtime_interval = configManager.getInt("general.playtime-update-interval",
					((int) configManager.getFloat("general.playtime-update-interval", playtime_interval)));
		} catch (Exception e) {
		}

		try {
			playtime_interval = configManager.getInt("general.autosave-files-interval",
					((int) configManager.getFloat("general.autosave-files-interval", autosave_interval)));
		} catch (Exception e) {
		}

		if (playtime_interval > 0) {
			new BukkitRunnable() {
				@Override
				public void run() {
					PowerRanksVerbose.log("task", "Running task update player playtime");
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {

						long current_time = new Date().getTime();
						long last_time = current_time;
						try {
							last_time = playerPlayTimeCache.get(player.getUniqueId()) - 1000;
						} catch (Exception e1) {
						}

						updatePlaytime(player, last_time, current_time, true);

						long time = new Date().getTime();
						playerPlayTimeCache.put(player.getUniqueId(), time);

					}

				}
			}.runTaskTimer(this, 0, playtime_interval * TASK_TPS);
		}

		if (autosave_interval > 0) {
			new BukkitRunnable() {

				@Override
				public void run() {
					PowerRanksVerbose.log("task", "Running task auto-save files");

					CacheManager.save();
				}
			}.runTaskTimer(this, autosave_interval * TASK_TPS, autosave_interval * TASK_TPS);
		}

		new BukkitRunnable() {
			int retryCount = 0;

			@Override
			public void run() {
				if (Objects.nonNull(vaultHook)) {
					if (Objects.isNull(VaultHook.getVaultEconomy())) {
						boolean has_vault_economy = PowerRanks.getInstance().getServer().getPluginManager()
								.getPlugin("Vault") != null && getConfigBool("plugin_hook.vault_economy", false);

						vaultHook.hook(PowerRanks.getInstance(), false, false, has_vault_economy);
					} else {
						this.cancel();
					}
				} else {
					this.cancel();
				}

				retryCount++;
				if (retryCount >= 30) {
					PowerRanks.log.warning("No Vault compatible economy plugin found! Giving up.");
					this.cancel();
				}
			}
		}.runTaskTimer(this, 0, 10 * TASK_TPS);

		int update_check_interval = 12 * 60 * 60 * TASK_TPS;
		new BukkitRunnable() {
			@Override
			public void run() {
				handle_update_checking();
			}
		}.runTaskTimer(this, update_check_interval, update_check_interval);

		// new BukkitRunnable() {
		// @Override
		// public void run() {
		// PowerRanksVerbose.log("task", "Running task check player name change");

		// for (Player player : Bukkit.getServer().getOnlinePlayers()) {
		// if (!playerNameCache.containsKey(player.getUniqueId())) {
		// playerNameCache.put(player.getUniqueId(), player.getName());
		// }

		// if (!playerNameCache.get(player.getUniqueId()).equals(player.getName())) {
		// log.info("Player name changed from '" +
		// playerNameCache.get(player.getUniqueId()) + "' to '" + player.getName() +
		// "'");
		// playerNameCache.put(player.getUniqueId(), player.getName());
		// CacheManager.getPlayer(player.getUniqueId().toString()).setName(player.getName());
		// }
		// }
		// }
		// }.runTaskTimer(this, TASK_TPS, TASK_TPS);
	}

	private Player getPlayerFromUUID(UUID uuid) {
		// PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "=== ----------Checking
		// UUID---------- ===");
		Player player = null;
		for (Player online_player : Bukkit.getServer().getOnlinePlayers()) {
			if (uuid.equals(online_player.getUniqueId())) {
				player = online_player;
				break;
			}
		}
		// PowerRanksVerbose.log("getPlayerFromUUID(UUID)", "===
		// --------------------------------- ===");
		return player;
	}

	private void setupSoftDependencies() {
		boolean has_vault_economy = this.getServer().getPluginManager().getPlugin("Vault") != null
				&& getConfigBool("plugin_hook.vault_economy", false);
		boolean has_vault_permissions = this.getServer().getPluginManager().getPlugin("Vault") != null
				&& getConfigBool("plugin_hook.vault_permissions", false);
		boolean has_vault_experimental_permissions = this.getServer().getPluginManager().getPlugin("Vault") != null
				&& getConfigBool("plugin_hook.vault_permissions_experimental", false);
		boolean has_placeholderapi = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null
				&& getConfigBool("plugin_hook.placeholderapi", false);
		// boolean has_tab = this.getServer().getPluginManager().getPlugin("TAB") !=
		// null
		// && getConfigBool("plugin_hook.tab");
		boolean has_deluxetags = this.getServer().getPluginManager().getPlugin("DeluxeTags") != null
				&& getConfigBool("plugin_hook.deluxetags", false);
		boolean has_nametagedit = this.getServer().getPluginManager().getPlugin("NametagEdit") != null
				&& getConfigBool("plugin_hook.nametagedit", false);

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
				if (has_vault_experimental_permissions) {
					PowerRanks.log.info("Enabling Vault Permission integration (experimental).");
				}

				this.vaultHook = new VaultHook();
				vaultHook.hook(this, has_vault_permissions, has_vault_experimental_permissions, has_vault_economy);
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

		// if (has_tab) {
		// PowerRanks.log.info("TAB found!");
		// PowerRanks.log.warning("TAB INTEGRATION IS EXPERIMENTAL, USE AT YOUR OWN
		// RISK!");
		// plugin_hook_tab = new TABHook();
		// plugin_hook_tab.setup();
		// }

		if (has_deluxetags) {
			PowerRanks.log.info("DeluxeTags found!");
			plugin_hook_deluxetags = true;
			this.deluxeTagsHook = new DeluxeTagsHook();
		}

		if (has_nametagedit) {
			PowerRanks.log.info("NametagEdit found!");
			plugin_hook_nametagedit = true;
			setup_nte();
		}

		if (!has_vault_economy && !has_vault_permissions && !has_placeholderapi && !has_deluxetags
				&& !has_nametagedit)
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
		}.runTaskTimer(this, Util.TASK_TPS, Util.TASK_TPS);
	}

	private void setupCheckRankTagsTask() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (PRPlayer prPlayer : CacheManager.getPlayers()) {

					List<PRPlayerRank> ranksToRemove = new ArrayList<PRPlayerRank>();
					List<PRPlayerRank> ranksToAdd = new ArrayList<PRPlayerRank>();
					for (PRPlayerRank rank : prPlayer.getRanks()) {
						for (Entry<String, Object> entry : rank.getTags().entrySet()) {
							if (entry.getKey().equalsIgnoreCase("expires")) {
								long currentTimeMillis = System.currentTimeMillis();
								long expires = Util.convertToLong(entry.getValue());
								if (currentTimeMillis >= expires) {
									ranksToRemove.add(rank);
								}
							}

							if (entry.getKey().equalsIgnoreCase("expiry-return-ranks")) {
								try {
									@SuppressWarnings("unchecked")
									List<String> expiryReturnRanks = (List<String>) entry.getValue();

									if (expiryReturnRanks.size() > 0) {
										ranksToRemove = new ArrayList<PRPlayerRank>();
										for (PRPlayerRank prPlayerRank : prPlayer.getRanks()) {
											ranksToRemove.add(prPlayerRank);
										}
									}

									for (String rankObject : expiryReturnRanks) {
										String rankName = rankObject.contains(";") ? rankObject.split(";")[0]
												: rankObject;
										List<String> rankTags = rankObject.contains(";")
												? new ArrayList<>(Arrays.asList(rankObject.split(";")))
												: new ArrayList<String>();
										if (!rankTags.isEmpty()) {
											rankTags.remove(0);
										}

										PRRank prRank = CacheManager.getRank(rankName);
										if (prRank != null) {
											PRPlayerRank prPlayerRank = new PRPlayerRank(prRank.getName());
											for (String rankTag : rankTags) {
												prPlayerRank.addTagRaw(rankTag.split(":")[0],
														Util.formatStringToType(rankTag.split(":")[1]));
											}
											ranksToAdd.add(prPlayerRank);
										}
									}
								} catch (Exception e) {
									PowerRanks.log.warning("Failed to parse expiry-return-ranks for rank "
											+ rank.getName() + " for player " + prPlayer.getName());
								}
							}
						}
					}

					for (PRPlayerRank rank : ranksToRemove) {
						prPlayer.removeRank(rank);
						String prPlayername = prPlayer.getName();
						if (prPlayername.length() == 0) {
							prPlayername = Util.getNameFromAPI(prPlayer.getUUID().toString());
						}

						getServer().getConsoleSender().sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
										.getFormattedMessage("general.player-rank-expired-console"),
								ImmutableMap.<String, String>builder()
										.put("player", prPlayername)
										.put("rank", rank.getName())
										.build(),
								'[', ']'));

						Player player = getPlayerFromUUID(prPlayer.getUUID());
						if (player != null) {
							PowerRanks.getInstance().updateTablistName(player);
							PowerRanks.getInstance().getTablistManager().updateSorting(player);

							player.sendMessage(Util.powerFormatter(
									PowerRanks.getLanguageManager()
											.getFormattedMessage("general.player-rank-has-expired"),
									ImmutableMap.<String, String>builder()
											.put("player", player.getName())
											.put("rank", rank.getName())
											.build(),
									'[', ']'));
						}
					}

					for (PRPlayerRank rank : ranksToAdd) {
						if (!prPlayer.hasRank(rank.getName())) {
							prPlayer.addRank(rank);

							Player player = getPlayerFromUUID(prPlayer.getUUID());
							if (player != null) {
								PowerRanks.getInstance().updateTablistName(player);
								PowerRanks.getInstance().getTablistManager().updateSorting(player);
							}
						}
					}
				}
			}

		}.runTaskTimer(this, Util.TASK_TPS, Util.TASK_TPS * 10);
	}

	public TABHook getTABHook() {
		return plugin_hook_tab;
	}

	public DeluxeTagsHook getDeluxeTagsHook() {
		return this.deluxeTagsHook;
	}

	private boolean handle_update_checking() {
		if (getConfigBool("updates.enable_update_checking", false)) {
			PowerRanks.log.info("Checking for updates...");
			Updater updater = new Updater(this, 79251, this.getFile(),
					getConfigBool("updates.automatic_download_updates", false) ? UpdateType.DEFAULT
							: UpdateType.NO_DOWNLOAD,
					true);
			if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				if (!getConfigBool("updates.automatic_download_updates", false)) {
					update_available = "Update available! (v" + updater.getLatestName().replaceAll("[a-zA-Z\" ]", "")
							+ ")";
				} else {
					PowerRanks.log.info("Plugin will now be updated!");
					update_available = "Update complete! Please restart your server";
				}
			} else {
				update_available = "No update available";
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
				return String.valueOf(generateNumber(CacheManager.getPlayers().size()));
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

	private int generateNumber(int input) {
		int rounded = Math.round(input / 50.0f) * 50; // Round the input to the nearest multiple of 50
		return rounded;
	}

	public boolean getConfigBool(String path, boolean defaultValue) {
		if (configManager != null) {
			return configManager.getBool(path, defaultValue);
		}

		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		return configYaml.getBoolean(path, defaultValue);
	}

	public String getConfigString(String path, String defaultValue) {
		if (configManager != null) {
			return configManager.getString(path, defaultValue);
		}

		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		return configYaml.getString(path, defaultValue);

	}

	public boolean configContainsKey(String path) {
		if (configManager != null) {
			return configManager.hasKey(path);
		}

		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		try {
			configYaml.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		return configYaml.isSet(path);
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
		languageManager = new LanguageManager();
		languageManager.setLanguage(configManager.getString("general.language", "en"));

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

	private void updateNametagEditData(Player player, String prefix, String suffix,
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

					prefix_format = prefix.length() == 0 ? prefix_format.replaceAll("\\[prefix\\]( )?", "")
							: prefix_format;
					prefix_format = Util.powerFormatter(prefix_format,
							ImmutableMap.<String, String>builder()
									.put("prefix", prefix)
									.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag
											: getDeluxeTagsHook().getPlayerDisplayTag(player))
									.build(),
							'[', ']');

					suffix_format = suffix.length() == 0 ? suffix_format.replaceAll("( )?\\[suffix\\]", "")
							: suffix_format;
					suffix_format = Util.powerFormatter(suffix_format,
							ImmutableMap.<String, String>builder()
									.put("suffix", suffix)
									.put("usertag",
											!PowerRanks.plugin_hook_deluxetags ? usertag
													: getDeluxeTagsHook().getPlayerDisplayTag(player))
									.build(),
							'[', ']');

					prefix_format += nameColor;

					prefix_format = getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, prefix_format, true,
							false, true);
					suffix_format = getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, suffix_format, true,
							false, true);

					INametagApi nteAPI = NametagEdit.getApi();
					if (nteAPI != null) {
						nteAPI.setNametag(player, prefix_format + (prefix_format.length() > 0 ? " " : ""),
								(suffix_format.length() > 0 ? " " : "") + suffix_format);
						updateTablistName(player, prefix, suffix, usertag, nameColor, false);
					}
				}
			}, 20L);
		}
	}

	public void updateTablistName(Player player) {
		try {
			player.updateCommands(); // TODO find a better place for this
		} catch (NoSuchMethodError e) {
		}

		PRPlayer prPlayer = CacheManager.getPlayer(player);

		List<PRPlayerRank> playerRanks = prPlayer.getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (PRPlayerRank playerRank : playerRanks) {
			if (!playerRank.isDisabled()) {
				PRRank rank = CacheManager.getRank(playerRank.getName());
				if (rank != null) {
					ranks.add(rank);
				}
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

		String formatted_prefix = "";
		String formatted_suffix = "";
		String nameColor = ranks.size() > 0 ? ranks.get(0).getNamecolor() : "&f";

		String usertag = "";

		try {
			for (PRRank rank : ranks) {
				formatted_prefix += rank.getPrefix() + " ";
				formatted_suffix += rank.getSuffix() + " ";
			}

			if (formatted_prefix.endsWith(" ")) {
				formatted_prefix = formatted_prefix.substring(0, formatted_prefix.length() - 1);
			}

			if (formatted_suffix.endsWith(" ")) {
				formatted_suffix = formatted_suffix.substring(0, formatted_suffix.length() - 1);
			}

			if (formatted_prefix.replaceAll(" ", "").length() == 0) {
				formatted_prefix = "";
			}

			if (formatted_suffix.replaceAll(" ", "").length() == 0) {
				formatted_suffix = "";
			}

			Map<?, ?> availableUsertags = getUsertagManager().getMap("usertags", new HashMap<String, String>());
			ArrayList<String> playerUsertags = prPlayer.getUsertags();

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

			updateTablistName(player, formatted_prefix, formatted_suffix, usertag, nameColor, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTablistName(Player player, String prefix, String suffix,
			String usertag, String nameColor, boolean updateNTE) {
		PowerRanksVerbose.log("updateTablistName", "Updating " + player.getName() + "'s tablist format");

		String player_formatted_name = (nameColor.length() == 0 ? "&r" : "")
				+ applyMultiColorFlow(nameColor, player.getDisplayName());

		try {
			if (updateNTE) {
				updateNametagEditData(player, prefix, suffix, usertag, nameColor);
			}

			if (!configManager.getBool("tablist_modification.enabled", true))
				return;

			player.setPlayerListName(playerTablistNameBackup.get(player.getUniqueId()));

			playerTablistNameBackup.put(player.getUniqueId(), player.getPlayerListName());

			String format = configManager.getString("tablist_modification.format", "");

			if (format.contains("[name]")) {
				String tmp_format = configManager.getString("tablist_modification.format", "");
				tmp_format = tmp_format.replace("[name]", "[player]");
				configManager.setString("tablist_modification.format", tmp_format);
				format = tmp_format;
			}

			format = Util.powerFormatter(format,
					ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix)
							.put("usertag", usertag)
							.put("player", player_formatted_name).put("world", player.getWorld().getName()).build(),
					'[', ']');

			while (format.endsWith(" ")) {
				format = format.substring(0, format.length() - 1);
			}

			if (PowerRanks.placeholderapiExpansion != null) {
				format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR,
						"" + PowerColor.UNFORMATTED_COLOR_CHAR);
			}
			format = PowerRanks.chatColor(format, true);

			player.setPlayerListName(format);
			PowerRanksVerbose.log("updateTablistName",
					"Updated " + player.getName() + "'s tablist format to: " + player.getPlayerListName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String chatColor(String textToTranslate, boolean custom_colors) {
		return getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, textToTranslate, custom_colors, false, true);
	}

	public static String chatColorAlt(final String textToTranslate, final boolean custom_colors) {
		return getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, textToTranslate, custom_colors, false, false);
	}

	public static String applyMultiColorFlow(String rawColors, String text) {
		String regexColors = "(&[a-fA-F0-9])|(&?#[a-fA-F0-9]{6})";
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

	public PowerRanksAPI loadAPI() {
		return new PowerRanksAPI();
	}

	public void updatePlaytime(Player player, long join_time, long leave_time, boolean write_to_file) {
		long current_playtime = CacheManager.getPlayer(player.getUniqueId().toString()).getPlaytime();

		CacheManager.getPlayer(player.getUniqueId().toString())
				.setPlaytime(current_playtime + (leave_time - join_time) / 1000);
	}

	public void updatePlayersWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			updateTablistName(p);
			// p.updateCommands();
		}
	}

	public void updatePlayersTABlistWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (users.getPrimaryRank(p).equalsIgnoreCase(rank)) {
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

	public List<PRPermission> getEffectivePlayerPermissions(Player player) {
		PRPlayer prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());

		if (prPlayer == null) {
			return new ArrayList<PRPermission>();
		}

		return prPlayer.getEffectivePermissions();
	}

	public TablistManager getTablistManager() {
		return this.tablistManager;
	}

	public static PowerConfigManager getConfigManager() {
		return configManager;
	}

	public static LanguageManager getLanguageManager() {
		return languageManager;
	}

	public static PowerConfigManager getUsertagManager() {
		return usertagManager;
	}

	public static PowerConfigManager getTablistConfigManager() {
		return tablistConfigManager;
	}

	public BungeecordManager getBungeecordManager() {
		return this.bungeecordManager;
	}

	public static BukkitPowerColor getPowerColor() {
		return powerColor;
	}

	public static PowerRanks getInstance() {
		return instance;
	}

	public static String getVersion() {
		return instance.getDescription().getVersion();
	}
}