package nl.svenar.PowerRanks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
import nl.svenar.PowerRanks.Data.PowerRanksGUI;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Events.OnBuild;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnInteract;
import nl.svenar.PowerRanks.Events.OnInventory;
import nl.svenar.PowerRanks.Events.OnJoin;
import nl.svenar.PowerRanks.Events.OnSignChanged;
import nl.svenar.PowerRanks.Events.ChatTabExecutor;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import nl.svenar.PowerRanks.api.PowerRanksAPI;
import nl.svenar.PowerRanks.metrics.Metrics;
import nl.svenar.PowerRanks.update.UpdateChecker;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class PowerRanks extends JavaPlugin implements Listener {
	public static PluginDescriptionFile pdf;
	public static String colorChar;
	public String plp;
	public static Logger log;
	public static String configFileLoc;
	public static String fileLoc;
	public static String langFileLoc;
	
	// Soft Depencencies
	private static Economy vaultEconomy;
	private static Permission vaultPermissions;
	private static PowerRanksExpansion placeholderapiExpansion;
	// Soft Depencencies
	
	File configFile;
	File ranksFile;
	File playersFile;
	File langFile;
	FileConfiguration config;
	FileConfiguration ranks;
	FileConfiguration players;
	FileConfiguration lang;
	protected UpdateChecker updatechecker;
	public String updatemsg;
	public Map<String, PermissionAttachment> playerPermissionAttachment = new HashMap<String, PermissionAttachment>();
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
		new UpdateChecker(this);

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
		this.setupScoreboardTeams();
		
		PowerRanksGUI.setPlugin(this);
		PowerRanksGUI.setupGUI();

		PowerRanks.log.info("Enabled " + PowerRanks.pdf.getName() + " v" + PowerRanks.pdf.getVersion());

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

		if (!has_vault && !has_placeholderapi)
			PowerRanks.log.info("No other plugins found! Working stand-alone.");
	}

	private boolean setupVaultEconomy() {
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		PowerRanks.vaultEconomy = rsp.getProvider();
        return PowerRanks.vaultEconomy != null;
	}
	
	@SuppressWarnings("unused")
	private boolean setupVaultPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		PowerRanks.vaultPermissions = rsp.getProvider();
        return PowerRanks.vaultPermissions != null;
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

	public void createDir(final String path) {
		final File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

//	public void verifyConfig() {
//		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
//		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
//		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
//		final File langFile = new File(PowerRanks.langFileLoc);
//		final YamlConfiguration rankYaml = new YamlConfiguration();
//		final YamlConfiguration playerYaml = new YamlConfiguration();
//		final YamlConfiguration configYaml = new YamlConfiguration();
//		final YamlConfiguration langYaml = new YamlConfiguration();
//		try {
//			rankYaml.load(rankFile);
//			playerYaml.load(playerFile);
//			configYaml.load(configFile);
//			langYaml.load(langFile);
//
//			if (rankYaml.getString("version") == null) {
//				rankYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
//			} else {
//				if (!rankYaml.getString("version").equalsIgnoreCase(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
//					printVersionError("Ranks.yml");
//				}
//			}
//
//			if (playerYaml.getString("version") == null) {
//				playerYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
//			} else {
//				if (!playerYaml.getString("version").equalsIgnoreCase(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
//					printVersionError("Players.yml");
//				}
//			}
//
//			if (configYaml.getString("version") == null) {
//				configYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
//			} else {
//				if (!configYaml.getString("version").equalsIgnoreCase(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
//					printVersionError("config.yml");
//				}
//			}
//
//			if (langYaml.getString("version") == null) {
//				langYaml.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
//			} else {
//				if (!langYaml.getString("version").equalsIgnoreCase(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
//					printVersionError("lang.yml");
//				}
//			}
//
//			rankYaml.save(rankFile);
//			playerYaml.save(playerFile);
//			configYaml.save(configFile);
//			langYaml.save(langFile);
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}
//	}

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

	public void printVersionError(String fileName) {
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

	private void copy(final InputStream in, final File file) {
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
		final PermissionAttachment attachment = playerPermissionAttachment.get(player.getName());

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

			if (GroupPermissions != null) {
				for (int i = 0; i < GroupPermissions.size(); i++) {

					boolean enabled = !GroupPermissions.get(i).startsWith("-");
					if (enabled) {
						attachment.setPermission((String) GroupPermissions.get(i), true);
					} else {
						attachment.setPermission((String) GroupPermissions.get(i).replaceFirst("-", ""), false);
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
							} else {
								attachment.setPermission((String) Permissions.get(j).replaceFirst("-", ""), false);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removePermissions(Player player) {
		final PermissionAttachment attachment = playerPermissionAttachment.get(player.getName());

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

			if (GroupPermissions != null) {
				for (int i = 0; i < GroupPermissions.size(); ++i) {
					attachment.unsetPermission((String) GroupPermissions.get(i));
				}
			}

			if (Inheritances != null) {
				for (int i = 0; i < Inheritances.size(); ++i) {
					final List<String> Permissions = (List<String>) rankYaml.getStringList("Groups." + Inheritances.get(i) + ".permissions");
					if (Permissions != null) {
						for (int j = 0; j < Permissions.size(); ++j) {
							attachment.unsetPermission((String) Permissions.get(j));
							// attachment.unsetPermission((String) GroupPermissions.get(j));
						}
					}
				}
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

	private void setupScoreboardTeams() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Users s = new Users(this);
		Set<String> ranks = s.getGroups();
		for (String rank : ranks) {
			Team team = board.registerNewTeam(rank);
			team.setPrefix(chatColor(colorChar.charAt(0), s.getRanksConfigFieldString(rank, "chat.prefix"), true));
			team.setSuffix(chatColor(colorChar.charAt(0), s.getRanksConfigFieldString(rank, "chat.suffix"), true));

			for (Player player : Bukkit.getOnlinePlayers()) {
				if (s.getGroup(player).equalsIgnoreCase(rank)) {
					team.addEntry(player.getName());
				}
			}
		}
	}

	public void updateTablistName(Player player) {
		try {
			player.setPlayerListName(playerTablistNameBackup.get(player));

			playerTablistNameBackup.put(player, player.getPlayerListName());

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

				format = Util.replaceAll(format, "[name]", chatColor(PowerRanks.colorChar.charAt(0), namecolor, false) + player.getPlayerListName());
				format = Util.replaceAll(format, "[prefix]", chatColor(PowerRanks.colorChar.charAt(0), prefix, true));
				format = Util.replaceAll(format, "[suffix]", chatColor(PowerRanks.colorChar.charAt(0), suffix, true));
//				format = Util.replaceAll(format, "&", "§");
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