package nl.svenar.PowerRanks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.CommandExecutor;
import nl.svenar.PowerRanks.Commands.Cmd;
import nl.svenar.PowerRanks.Data.PermissibleInjector;
import nl.svenar.PowerRanks.Data.PowerPermissibleBase;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Events.OnBuild;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnInteract;
import nl.svenar.PowerRanks.Events.OnInventory;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.Events.OnSignChanged;
import nl.svenar.PowerRanks.addons.AddonsManager;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;

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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PowerRanks extends JavaPlugin implements Listener {
	public String bukkit_dev_url_powerranks = "https://dev.bukkit.org/projects/powerranks";
	public ArrayList<String> donation_urls = new ArrayList<String>(Arrays.asList("https://ko-fi.com/svenar", "https://patreon.com/svenar"));

	public static PluginDescriptionFile pdf;
	public AddonsManager addonsManager;
	public static String colorChar;
	public String plp;
	public static Logger log;
	public static String configFileLoc;
	public static String fileLoc;
	public static String langFileLoc;

	// Soft Dependencies
	private static Economy vaultEconomy;
	private static Permission vaultPermissions;
	private static PowerRanksExpansion placeholderapiExpansion;
	public static boolean plugin_hook_deluxetags = false;
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
	public Map<String, PermissionAttachment> playerPermissionAttachment = new HashMap<String, PermissionAttachment>();
	public Map<Player, ArrayList<String>> playerDisallowedPermissions = new HashMap<Player, ArrayList<String>>();
	public Map<Player, String> playerTablistNameBackup = new HashMap<Player, String>();
	public Map<Player, Long> playerLoginTime = new HashMap<Player, Long>();

	public PowerRanks() {
		PowerRanks.pdf = this.getDescription();
		PowerRanks.colorChar = "&";
		this.plp = ChatColor.BLACK + "[" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.BLACK + "]" + ChatColor.RESET + " ";
		PowerRanks.configFileLoc = this.getDataFolder() + File.separator;
		PowerRanks.fileLoc = this.getDataFolder() + File.separator + "Ranks" + File.separator;
		PowerRanks.langFileLoc = PowerRanks.configFileLoc + "lang.yml";
		this.updatemsg = "";
	}

	public void onEnable() {
		PowerRanks.log = this.getLogger();
		PowerRanksAPI.main = this;

//		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnBuild(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInteract(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnSignChanged(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnInventory(this), (Plugin) this);

		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new Cmd(this));
		Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new Cmd(this));
		Bukkit.getServer().getPluginCommand("powerranks").setTabCompleter(new ChatTabExecutor(this));
		Bukkit.getServer().getPluginCommand("pr").setTabCompleter(new ChatTabExecutor(this));

		this.createDir(PowerRanks.fileLoc);
//		this.log.info("By: " + this.pdf.getAuthors().get(0));
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
//		this.verifyConfig();

		if (handle_update_checking()) {
			return;
		}

		ConfigFilesUpdater.updateConfigFiles(this);

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerInjectPermissible(player);
		}

		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);

			for (final Player player : this.getServer().getOnlinePlayers()) {
				if (playerYaml.getString("players." + player.getUniqueId() + ".rank") == null) {
					playerYaml.set("players." + player.getUniqueId() + ".rank", rankYaml.get("Default"));
				}
			}
			playerYaml.save(playerFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		setupSoftDependencies();

		this.setupPermissions();
//		this.setupScoreboardTeams();

		GUI.setPlugin(this);
//		GUI.setupGUI();
		
		PowerRanks.log.info("----------------------");
		PowerRanks.log.info("Loading add-ons");
		addonsManager = new AddonsManager();
		addonsManager.setup();
		PowerRanks.log.info("----------------------");

		PowerRanks.log.info("Enabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		PowerRanks.log.info("If you'd like to donate, please visit " + donation_urls.get(0) + " or " + donation_urls.get(1));

		int pluginId = 7565;
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, pluginId);
	}

	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTasks(this);

		for (Player player : this.getServer().getOnlinePlayers()) {
			this.playerUninjectPermissible(player);
		}

		for (Entry<String, PermissionAttachment> pa : playerPermissionAttachment.entrySet()) {
			pa.getValue().remove();
		}
		playerPermissionAttachment.clear();

		for (Entry<Player, String> pa : playerTablistNameBackup.entrySet()) {
			pa.getKey().setPlayerListName(pa.getValue());
		}
		playerTablistNameBackup.clear();

		if (PowerRanks.log != null && PowerRanks.pdf != null) {
			PowerRanks.log.info("Disabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());
		}
	}

	private void setupSoftDependencies() {
		boolean has_vault = this.getServer().getPluginManager().getPlugin("Vault") != null && getConfigBool("plugin_hook.vault");
		boolean has_placeholderapi = this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && getConfigBool("plugin_hook.placeholderapi");
		boolean has_deluxetags = this.getServer().getPluginManager().getPlugin("DeluxeTags") != null && getConfigBool("plugin_hook.deluxetags");

		PowerRanks.log.info("Checking for plugins to hook in to:");
		if (has_vault) {
			PowerRanks.log.info("Vault found!");
			setupVaultEconomy();
			// setupVaultPermissions(); // TODO: Implement Vault permissions
		}

		if (has_placeholderapi) {
			PowerRanks.log.info("PlaceholderAPI found!");
			PowerRanks.placeholderapiExpansion = new PowerRanksExpansion(this);
			PowerRanks.placeholderapiExpansion.register();
		}

		if (has_deluxetags) {
			PowerRanks.log.info("DeluxeTags found!");
			plugin_hook_deluxetags = true;
		}

		if (!has_vault && !has_placeholderapi && !has_deluxetags)
			PowerRanks.log.info("No other plugins found! Working stand-alone.");
	}

	private boolean setupVaultEconomy() {
		try {
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			PowerRanks.vaultEconomy = rsp.getProvider();
		} catch (Exception e) {
			PowerRanks.log.warning("Failed to load Vault Economy! Is an economy plugin present?");
		}
		return PowerRanks.vaultEconomy != null;
	}

	@SuppressWarnings("unused")
	private boolean setupVaultPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		PowerRanks.vaultPermissions = rsp.getProvider();
		return PowerRanks.vaultPermissions != null;
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

	public void forceUpdateConfigVersions() {
		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final File langFile = new File(PowerRanks.langFileLoc);
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		final YamlConfiguration configYaml = new YamlConfiguration();
		final YamlConfiguration langYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			configYaml.load(configFile);
			langYaml.load(langFile);

			rankYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			playerYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			configYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			langYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));

			rankYaml.save(rankFile);
			playerYaml.save(playerFile);
			configYaml.save(configFile);
			langYaml.save(langFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
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

	private void setupPermissions() {
		for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
			this.setupPermissions(player);
			this.updateTablistName(player);
		}
	}

	public void setupPermissions(Player player) {
		if (!playerPermissionAttachment.containsKey(player.getName())) {
			this.playerInjectPermissible(player);
		}

//		if (!playerDisallowedPermissions.containsKey(player)) {
		playerDisallowedPermissions.put(player, new ArrayList<String>());
//		}

		final PermissionAttachment attachment = playerPermissionAttachment.get(player.getName());
		final String uuid = player.getUniqueId().toString();

		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
			final List<String> GroupPermissions = (List<String>) rankYaml.getStringList("Groups." + rank + ".permissions");
			final List<String> Inheritances = (List<String>) rankYaml.getStringList("Groups." + rank + ".inheritance");
			final List<String> Subranks = new ArrayList<String>();

			try {
				if (playerYaml.getConfigurationSection("players." + uuid + ".subranks") != null) {
					ConfigurationSection subranks = playerYaml.getConfigurationSection("players." + uuid + ".subranks");
					for (String r : subranks.getKeys(false)) {
						if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_permissions")) {
							Subranks.add(r);
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < Subranks.size(); i++) {
				List<String> permissions = (List<String>) rankYaml.getStringList("Groups." + Subranks.get(i) + ".permissions");
				if (permissions != null) {
					for (int j = 0; j < permissions.size(); j++) {

						boolean enabled = !permissions.get(j).startsWith("-");
						if (enabled) {
							attachment.setPermission((String) permissions.get(j), true);
							if (playerDisallowedPermissions.get(player).contains((String) permissions.get(j)) && !permissions.get(j).equals("*"))
								playerDisallowedPermissions.get(player).remove((String) permissions.get(j));
						} else {
							attachment.setPermission((String) permissions.get(j).replaceFirst("-", ""), false);
							if (!playerDisallowedPermissions.get(player).contains((String) permissions.get(j).replaceFirst("-", "")) && !permissions.get(j).equals("*"))
								playerDisallowedPermissions.get(player).add((String) permissions.get(j).replaceFirst("-", ""));
						}
					}
				}
			}

			if (Inheritances != null) {
				for (int i = 0; i < Inheritances.size(); i++) {
					List<String> Permissions = (List<String>) rankYaml.getStringList("Groups." + Inheritances.get(i) + ".permissions");
					if (Permissions != null) {
						for (int j = 0; j < Permissions.size(); j++) {

							boolean enabled = !Permissions.get(j).startsWith("-");
							if (enabled) {
								attachment.setPermission((String) Permissions.get(j), true);
								if (playerDisallowedPermissions.get(player).contains((String) Permissions.get(j)) && !Permissions.get(j).equals("*"))
									playerDisallowedPermissions.get(player).remove((String) Permissions.get(j));
							} else {
								attachment.setPermission((String) Permissions.get(j).replaceFirst("-", ""), false);
								if (!playerDisallowedPermissions.get(player).contains((String) Permissions.get(j).replaceFirst("-", "")) && !Permissions.get(j).equals("*"))
									playerDisallowedPermissions.get(player).add((String) Permissions.get(j).replaceFirst("-", ""));
							}
						}
					}
				}
			}

			if (GroupPermissions != null) {
				for (int i = 0; i < GroupPermissions.size(); i++) {

					boolean enabled = !GroupPermissions.get(i).startsWith("-");
					if (enabled) {
						attachment.setPermission((String) GroupPermissions.get(i), true);
						if (playerDisallowedPermissions.get(player).contains((String) GroupPermissions.get(i)) && !GroupPermissions.get(i).equals("*"))
							playerDisallowedPermissions.get(player).remove((String) GroupPermissions.get(i));
					} else {
						attachment.setPermission((String) GroupPermissions.get(i).replaceFirst("-", ""), false);
						if (!playerDisallowedPermissions.get(player).contains((String) GroupPermissions.get(i).replaceFirst("-", "")) && !GroupPermissions.get(i).equals("*"))
							playerDisallowedPermissions.get(player).add((String) GroupPermissions.get(i).replaceFirst("-", ""));
					}
				}
			}

			if (playerYaml.getString("players." + player.getUniqueId() + ".permissions") != null) {
				List<String> permissions = (List<String>) playerYaml.getStringList("players." + player.getUniqueId() + ".permissions");
				if (permissions != null) {
					for (int i = 0; i < permissions.size(); i++) {
						boolean enabled = !permissions.get(i).startsWith("-");
						if (enabled) {
							attachment.setPermission((String) permissions.get(i), true);
							if (playerDisallowedPermissions.get(player).contains((String) permissions.get(i)) && !permissions.get(i).equals("*"))
								playerDisallowedPermissions.get(player).remove((String) permissions.get(i));
						} else {
							attachment.setPermission((String) permissions.get(i).replaceFirst("-", ""), false);
							if (!playerDisallowedPermissions.get(player).contains((String) permissions.get(i).replaceFirst("-", "")) && !permissions.get(i).equals("*"))
								playerDisallowedPermissions.get(player).add((String) permissions.get(i).replaceFirst("-", ""));
						}
					}
				}
			} else {
				playerYaml.set("players." + player.getUniqueId() + ".permissions", "[]");
				playerYaml.save(playerFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removePermissions(Player player) {
		if (playerPermissionAttachment.containsKey(player.getName())) {
			this.playerInjectPermissible(player);
		}

//		if (!playerDisallowedPermissions.containsKey(player)) {
		playerDisallowedPermissions.put(player, new ArrayList<String>());
//		}

		final PermissionAttachment attachment = playerPermissionAttachment.get(player.getName());
		final String uuid = player.getUniqueId().toString();

		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
			final List<String> GroupPermissions = (List<String>) rankYaml.getStringList("Groups." + rank + ".permissions");
			final List<String> Inheritances = (List<String>) rankYaml.getStringList("Groups." + rank + ".inheritance");
			final List<String> Subranks = new ArrayList<String>();

			try {
				if (playerYaml.getConfigurationSection("players." + uuid + ".subranks") != null) {
					ConfigurationSection subranks = playerYaml.getConfigurationSection("players." + uuid + ".subranks");
					for (String r : subranks.getKeys(false)) {
						if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_permissions")) {
							Subranks.add(r);
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < Subranks.size(); i++) {
				List<String> permissions = (List<String>) rankYaml.getStringList("Groups." + Subranks.get(i) + ".permissions");
				if (permissions != null) {
					for (int j = 0; j < permissions.size(); j++) {

						boolean enabled = !permissions.get(j).startsWith("-");
						if (enabled) {
							attachment.setPermission((String) permissions.get(j), true);
							if (playerDisallowedPermissions.get(player).contains((String) permissions.get(j)) && !permissions.get(j).equals("*"))
								playerDisallowedPermissions.get(player).remove((String) permissions.get(j));
						} else {
							attachment.setPermission((String) permissions.get(j).replaceFirst("-", ""), false);
							if (!playerDisallowedPermissions.get(player).contains((String) permissions.get(j).replaceFirst("-", "")) && !permissions.get(j).equals("*"))
								playerDisallowedPermissions.get(player).add((String) permissions.get(j).replaceFirst("-", ""));
						}
					}
				}
			}

			if (GroupPermissions != null) {
				for (int i = 0; i < GroupPermissions.size(); ++i) {
					attachment.unsetPermission((String) GroupPermissions.get(i));
//					if (playerDisallowedPermissions.get(player).contains((String) GroupPermissions.get(i)) && !GroupPermissions.get(i).equals("*"))
//						playerDisallowedPermissions.get(player).remove((String) GroupPermissions.get(i));
				}
			}

			if (Inheritances != null) {
				for (int i = 0; i < Inheritances.size(); ++i) {
					final List<String> Permissions = (List<String>) rankYaml.getStringList("Groups." + Inheritances.get(i) + ".permissions");
					if (Permissions != null) {
						for (int j = 0; j < Permissions.size(); ++j) {
							attachment.unsetPermission((String) Permissions.get(j));
//							if (playerDisallowedPermissions.get(player).contains((String) Permissions.get(i)) && !Permissions.get(i).equals("*"))
//								playerDisallowedPermissions.get(player).remove((String) Permissions.get(i));
							// attachment.unsetPermission((String) GroupPermissions.get(j));
						}
					}
				}
			}

			if (playerYaml.getString("players." + player.getUniqueId() + ".permissions") != null) {
				List<String> permissions = (List<String>) rankYaml.getStringList("players." + player.getUniqueId() + ".permissions");
				if (permissions != null) {
					for (int i = 0; i < permissions.size(); i++) {
						attachment.unsetPermission((String) permissions.get(i));
//						if (playerDisallowedPermissions.get(player).contains((String) permissions.get(i)) && !permissions.get(i).equals("*"))
//							playerDisallowedPermissions.get(player).remove((String) permissions.get(i));
					}
				}
			} else {
				playerYaml.set("players." + player.getUniqueId() + ".permissions", "[]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playerInjectPermissible(Player player) {
		Permissible permissible = new PowerPermissibleBase(player, this);
		Permissible oldPermissible = PermissibleInjector.inject(player, permissible);
		((PowerPermissibleBase) permissible).setOldPermissible(oldPermissible);

		if (playerPermissionAttachment.get(player.getName()) == null)
			playerPermissionAttachment.put(player.getName(), player.addAttachment(this));
	}

	public void playerUninjectPermissible(Player player) {
		PermissibleInjector.uninject(player);
	}

//	private void setupScoreboardTeams() {
//		ScoreboardManager manager = Bukkit.getScoreboardManager();
//		Scoreboard board = manager.getNewScoreboard();
//		Users s = new Users(this);
//		Set<String> ranks = s.getGroups();
//		for (String rank : ranks) {
//			Team team = board.registerNewTeam(rank);
//			team.setPrefix(chatColor(colorChar.charAt(0), s.getRanksConfigFieldString(rank, "chat.prefix"), true));
//			team.setSuffix(chatColor(colorChar.charAt(0), s.getRanksConfigFieldString(rank, "chat.suffix"), true));
//
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				if (s.getGroup(player).equalsIgnoreCase(rank)) {
//					team.addEntry(player.getName());
//				}
//			}
//		}
//	}

	public void updateTablistName(Player player) {
		try {
			player.setPlayerListName(playerTablistNameBackup.get(player));

			playerTablistNameBackup.put(player, player.getPlayerListName());
			String uuid = player.getUniqueId().toString();

			File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
			File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
			File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
			YamlConfiguration configYaml = new YamlConfiguration();
			YamlConfiguration rankYaml = new YamlConfiguration();
			YamlConfiguration playerYaml = new YamlConfiguration();
			try {
				configYaml.load(configFile);
				if (!configYaml.getBoolean("tablist_modification.enabled"))
					return;

				rankYaml.load(rankFile);
				playerYaml.load(playerFile);

				String format = configYaml.getString("tablist_modification.format");
				String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
				String prefix = rankYaml.getString("Groups." + rank + ".chat.prefix");
				String suffix = rankYaml.getString("Groups." + rank + ".chat.suffix");
				String namecolor = rankYaml.getString("Groups." + rank + ".chat.nameColor");

				String subprefix = "";
				String subsuffix = "";
				String usertag = "";

				try {
					if (playerYaml.getConfigurationSection("players." + uuid + ".subranks") != null) {
						ConfigurationSection subranks = playerYaml.getConfigurationSection("players." + uuid + ".subranks");
						for (String r : subranks.getKeys(false)) {
							if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_prefix")) {
								subprefix += (rankYaml.getString("Groups." + r + ".chat.prefix") != null && rankYaml.getString("Groups." + r + ".chat.prefix").length() > 0
										? ChatColor.RESET + rankYaml.getString("Groups." + r + ".chat.prefix")
										: "");
							}

							if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_suffix")) {
								subsuffix += (rankYaml.getString("Groups." + r + ".chat.suffix") != null && rankYaml.getString("Groups." + r + ".chat.suffix").length() > 0
										? ChatColor.RESET + rankYaml.getString("Groups." + r + ".chat.suffix")
										: "");

							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				subprefix = subprefix.trim();
				subsuffix = subsuffix.trim();

				if (subsuffix.endsWith(" ")) {
					subsuffix = subsuffix.substring(0, subsuffix.length() - 1);
				}

				if (subsuffix.replaceAll(" ", "").length() == 0) {
					subsuffix = "";
				}

				if (playerYaml.isSet("players." + uuid + ".usertag") && playerYaml.getString("players." + uuid + ".usertag").length() > 0) {
					String tmp_usertag = playerYaml.getString("players." + uuid + ".usertag");

					if (rankYaml.getConfigurationSection("Usertags") != null) {
						ConfigurationSection tags = rankYaml.getConfigurationSection("Usertags");
						for (String key : tags.getKeys(false)) {
							if (key.equalsIgnoreCase(tmp_usertag)) {
								usertag = rankYaml.getString("Usertags." + key) + ChatColor.RESET;
								break;
							}
						}
					}
				}

				if (format.contains("[name]")) {
					String tmp_format = configYaml.getString("tablist_modification.format");
					tmp_format = tmp_format.replace("[name]", "[player]");
					configYaml.set("tablist_modification.format", tmp_format);
					configYaml.save(configFile);
					format = tmp_format;
				}

				format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix)
						.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player)).put("player", namecolor + player.getPlayerListName()).build(), '[', ']');

				format = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), format, true);

				while (format.endsWith(" ")) {
					format = format.substring(0, format.length() - 1);
				}

				player.setPlayerListName(format);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTablistName(Player player, String prefix, String suffix, String subprefix, String subsuffix, String usertag, String nameColor) {
		try {
			player.setPlayerListName(playerTablistNameBackup.get(player));

			playerTablistNameBackup.put(player, player.getPlayerListName());

			File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
			YamlConfiguration configYaml = new YamlConfiguration();
			try {
				configYaml.load(configFile);
				if (!configYaml.getBoolean("tablist_modification.enabled"))
					return;

				String format = configYaml.getString("tablist_modification.format");

				if (format.contains("[name]")) {
					String tmp_format = configYaml.getString("tablist_modification.format");
					tmp_format = tmp_format.replace("[name]", "[player]");
					configYaml.set("tablist_modification.format", tmp_format);
					configYaml.save(configFile);
					format = tmp_format;
				}

				format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix).put("usertag", usertag)
						.put("player", nameColor + player.getPlayerListName()).build(), '[', ']');

				format = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), format, true);

				while (format.endsWith(" ")) {
					format = format.substring(0, format.length() - 1);
				}

				player.setPlayerListName(format);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String chatColor(char altColorChar, String textToTranslate, boolean custom_colors) {

		if (custom_colors) {
			for (int i = 0; i < textToTranslate.length() - 1; i++) {
				if (textToTranslate.charAt(i) == altColorChar && "Ii".indexOf(textToTranslate.charAt(i + 1)) > -1) {
					String rainbow_msg = "";
					for (int j = i + 2; j < textToTranslate.length() - 1; j++) {
						if (textToTranslate.charAt(j) == altColorChar)
							break;
						rainbow_msg += textToTranslate.charAt(j);
					}
					String converted_rainbow_msg = rainbowColor(rainbow_msg);
					textToTranslate = Util.replaceAll(textToTranslate, rainbow_msg, converted_rainbow_msg);
				}
			}

			for (int i = 0; i < textToTranslate.length() - 1; i++) {
				if (textToTranslate.charAt(i) == altColorChar && "Jj".indexOf(textToTranslate.charAt(i + 1)) > -1) {
					String random_msg = "";
					for (int j = i + 2; j < textToTranslate.length() - 1; j++) {
						if (textToTranslate.charAt(j) == altColorChar)
							break;
						random_msg += textToTranslate.charAt(j);
					}
					String converted_random_msg = randomColor(random_msg);
					textToTranslate = Util.replaceAll(textToTranslate, random_msg, converted_random_msg);
				}
			}

			textToTranslate = Util.replaceAll(textToTranslate, altColorChar + "I", "");
			textToTranslate = Util.replaceAll(textToTranslate, altColorChar + "i", "");
			textToTranslate = Util.replaceAll(textToTranslate, altColorChar + "J", "");
			textToTranslate = Util.replaceAll(textToTranslate, altColorChar + "j", "");
		}

		final char[] charArray = textToTranslate.toCharArray();

		for (int i = 0; i < charArray.length - 1; ++i) {
			if (charArray[i] == altColorChar && "0123456789AaBbCcDdEeFfKkNnRrLlMmOo".indexOf(charArray[i + 1]) > -1) {
				charArray[i] = '§';
				charArray[i + 1] = Character.toLowerCase(charArray[i + 1]);
			}
		}
		return new String(charArray);
	}

	private static String randomColor(String random_msg) {
		String msg = "";

		Random rand = new Random();
		int i = rand.nextInt(10) + 1;
		final char[] __tmpChars = random_msg.toCharArray();
		char[] ac;
		for (int i2 = (ac = __tmpChars).length, l = 0; l < i2; ++l) {
			final char __curr = ac[l];
			msg = String.valueOf(msg) + "&" + Integer.toString(i % 15 + 1, 16) + Character.toString(__curr);
			i = i + rand.nextInt(10) + 1;
		}

		return msg;
	}

	private static String rainbowColor(String rainbow_msg) {
		String msg = "";

		final int[] colors = { 3, 5, 13, 1, 2, 4, 12 };
		int i = 0;
		final char[] __tmpChars = rainbow_msg.toCharArray();
		char[] ac;
		for (int i2 = (ac = __tmpChars).length, l = 0; l < i2; ++l) {
			final char __curr = ac[l];
			msg = String.valueOf(msg) + "&" + Integer.toString(colors[i] % 15 + 1, 16) + Character.toString(__curr);
			if (i < colors.length - 1) {
				++i;
			} else {
				i = 0;
			}
		}

		return msg;
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
		File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		YamlConfiguration playerYaml = new YamlConfiguration();
		try {
			playerYaml.load(playerFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		Long current_playtime = playerYaml.getLong("players." + player.getUniqueId() + ".playtime");
		playerYaml.set("players." + player.getUniqueId() + ".playtime", current_playtime + (leave_time - join_time) / 1000);

		try {
			playerYaml.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePlayersWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (users.getGroup(p).equalsIgnoreCase(rank)) {
				setupPermissions(p);
			}
		}
	}

	public void updatePlayersTABlistWithRank(Users users, String rank) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (users.getGroup(p).equalsIgnoreCase(rank)) {
				updateTablistName(p);
			}
		}
	}

	public static Economy getVaultEconomy() {
		return vaultEconomy;
	}

	public static Permission getVaultPermissions() {
		return vaultPermissions;
	}

	public static PowerRanksExpansion getPlaceholderapiExpansion() {
		return placeholderapiExpansion;
	}
}