package nl.svenar.PowerRanks;

import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import nl.svenar.PowerRanks.Events.OnBuild;
import nl.svenar.PowerRanks.Events.OnChat;
import nl.svenar.PowerRanks.Events.OnJoin;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import nl.svenar.PowerRanks.api.Rank;
import nl.svenar.PowerRanks.metrics.Metrics;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public PluginDescriptionFile pdf;
	public String colorChar;
	public ChatColor black;
	public ChatColor aqua;
	public ChatColor red;
	public ChatColor dark_red;
	public ChatColor blue;
	public ChatColor dark_blue;
	public ChatColor reset;
	public String plp;
	public Logger log;
	public String configFileLoc;
	public String fileLoc;
	public static String langFileLoc;
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

	public Main() {
		this.pdf = this.getDescription();
		this.colorChar = "&";
		this.black = ChatColor.BLACK;
		this.aqua = ChatColor.AQUA;
		this.red = ChatColor.RED;
		this.dark_red = ChatColor.DARK_RED;
		this.blue = ChatColor.BLUE;
		this.dark_blue = ChatColor.DARK_BLUE;
		this.reset = ChatColor.RESET;
		this.plp = this.black + "[" + this.aqua + this.pdf.getName() + this.black + "]" + this.reset + " ";
		this.configFileLoc = this.getDataFolder() + File.separator;
		this.fileLoc = this.getDataFolder() + File.separator + "Ranks" + File.separator;
		Main.langFileLoc = this.configFileLoc + "lang.yml";
		this.updatemsg = "";
	}

	public void onEnable() {
		this.log = this.getLogger();
		Rank.main = this;
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnJoin(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnChat(this), (Plugin) this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnBuild(this), (Plugin) this);
		Bukkit.getServer().getPluginCommand("powerranks").setExecutor((CommandExecutor) new Cmd(this));
		Bukkit.getServer().getPluginCommand("pr").setExecutor((CommandExecutor) new Cmd(this));
		this.createDir(this.fileLoc);
		this.log.info("Enabled " + this.pdf.getName() + " v" + this.pdf.getVersion().replaceAll("[a-zA-Z]", ""));
//		this.log.info("By: " + this.pdf.getAuthors().get(0));
		this.configFile = new File(this.getDataFolder(), "config.yml");
		this.ranksFile = new File(this.fileLoc, "Ranks.yml");
		this.playersFile = new File(this.fileLoc, "Players.yml");
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
		this.verifyConfig();

		for (Player player : this.getServer().getOnlinePlayers()) {
			String playerName = player.getName();
			this.playerInjectPermissible(player);
			if (playerPermissionAttachment.get(playerName) == null)
				playerPermissionAttachment.put(playerName, player.addAttachment(this));
		}

		this.setupPermissions();
		final File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
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

		if (this.log != null && this.pdf != null) {
			this.log.info("Disabled " + this.pdf.getName() + " v" + this.pdf.getVersion().replaceAll("[a-zA-Z]", ""));
		}
	}

	public void createDir(final String path) {
		final File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void verifyConfig() {
		final File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final File langFile = new File(Main.langFileLoc);
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		final YamlConfiguration configYaml = new YamlConfiguration();
		final YamlConfiguration langYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			configYaml.load(configFile);
			langYaml.load(langFile);

			if (rankYaml.getString("version") == null) {
				rankYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} else {
				if (!rankYaml.getString("version").equalsIgnoreCase(this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					printVersionError("Ranks.yml");
				}
			}

			if (playerYaml.getString("version") == null) {
				playerYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} else {
				if (!playerYaml.getString("version").equalsIgnoreCase(this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					printVersionError("Players.yml");
				}
			}

			if (configYaml.getString("version") == null) {
				configYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} else {
				if (!configYaml.getString("version").equalsIgnoreCase(this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					printVersionError("config.yml");
				}
			}

			if (langYaml.getString("version") == null) {
				langYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} else {
				if (!langYaml.getString("version").equalsIgnoreCase(this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					printVersionError("lang.yml");
				}
			}

			rankYaml.save(rankFile);
			playerYaml.save(playerFile);
			configYaml.save(configFile);
			langYaml.save(langFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void forceUpdateConfigVersions() {
		final File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
		final File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		final File langFile = new File(Main.langFileLoc);
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		final YamlConfiguration configYaml = new YamlConfiguration();
		final YamlConfiguration langYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			configYaml.load(configFile);
			langYaml.load(langFile);

			rankYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			playerYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			configYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			langYaml.set("version", this.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));

			rankYaml.save(rankFile);
			playerYaml.save(playerFile);
			configYaml.save(configFile);
			langYaml.save(langFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void printVersionError(String fileName) {
		this.log.warning("===------------------------------===");
		this.log.warning("              WARNING!");
		this.log.warning("Version mismatch detected in:");
		this.log.warning(fileName);
		this.log.warning(this.pdf.getName() + " may not work with this config.");
		this.log.warning("Manual verification is required.");
		this.log.warning("To forcefuly get rid of this message with all its consequences use the following command:");
		this.log.warning("/pr forceupdateconfigversion");
		this.log.warning("Visit " + this.pdf.getWebsite() + " for more info.");
		this.log.warning("===------------------------------===");
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
			this.log.warning("Failed to load the config files (If this is the first time PowerRanks starts you could ignore this message)");
			this.log.warning("Try reloading the server. If this message continues to display report this to the plugin page on bukkit.");
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

		final File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
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

		final File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
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
		Permissible permissible = new PowerPermissibleBase(player);
		Permissible oldPermissible = PermissibleInjector.inject(player, permissible);
		((PowerPermissibleBase) permissible).setOldPermissible(oldPermissible);
	}

	public void playerUninjectPermissible(Player player) {
		PermissibleInjector.uninject(player);
	}

	public void updateTablistName(Player player) {
		playerTablistNameBackup.put(player, player.getPlayerListName());

		File configFile = new File(this.getDataFolder() + File.separator + "config" + ".yml");
		File rankFile = new File(String.valueOf(this.fileLoc) + "Ranks" + ".yml");
		File playerFile = new File(String.valueOf(this.fileLoc) + "Players" + ".yml");
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

			format = Util.replaceAll(format, "[name]", chatColor(this.colorChar.charAt(0), namecolor) + player.getPlayerListName());
			format = Util.replaceAll(format, "[prefix]", prefix);
			format = Util.replaceAll(format, "[suffix]", suffix);
			format = Util.replaceAll(format, "&", "§");
			player.setPlayerListName(format);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String chatColor(final char altColorChar, final String textToTranslate) {
		final char[] charArray = textToTranslate.toCharArray();
		for (int i = 0; i < charArray.length - 1; ++i) {
			if (charArray[i] == altColorChar && "0123456789AaBbCcDdEeFfKkNnRrLlMmOo".indexOf(charArray[i + 1]) > -1) {
				charArray[i] = '§';
				charArray[i + 1] = Character.toLowerCase(charArray[i + 1]);
			}
		}
		return new String(charArray);
	}

//	public void errorMessage(final Player player, final String args) {
//		player.sendMessage(ChatColor.RED + "--------" + ChatColor.DARK_BLUE + this.pdf.getName() + ChatColor.RED + "--------");
//		player.sendMessage("Argument " + args + " not found");
//		player.sendMessage(ChatColor.GREEN + "/pr help");
//		player.sendMessage(ChatColor.RED + "--------------------------");
//	}

	public YamlConfiguration loadLangFile() {
		File langFile = new File(Main.langFileLoc);
		YamlConfiguration langYaml = new YamlConfiguration();
		try {
			langYaml.load(langFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return langYaml;
	}

	public String getGeneralMessage(YamlConfiguration langYaml, String lang_config_line) {
		String msg = "";

		String line = langYaml.getString(lang_config_line);
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				msg = chatColor(this.colorChar.charAt(0), line);
			}
		}

		return msg;
	}

	public void messageNoArgs(final Player player) {
		player.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + this.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		player.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
		player.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		player.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + this.pdf.getAuthors());
		player.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + this.pdf.getVersion());
		player.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + this.pdf.getWebsite());
		player.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public void messageNoArgs(final ConsoleCommandSender console) {
		console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + this.pdf.getName() + ChatColor.DARK_AQUA + "--------");
		console.sendMessage(ChatColor.GREEN + "/pr help" + ChatColor.DARK_GREEN + " - For the command list.");
		console.sendMessage(new StringBuilder().append(ChatColor.GREEN).toString());
		console.sendMessage(ChatColor.DARK_GREEN + "Authors: " + ChatColor.GREEN + this.pdf.getAuthors());
		console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + this.pdf.getVersion());
		console.sendMessage(ChatColor.DARK_GREEN + "Bukkit DEV: " + ChatColor.GREEN + this.pdf.getWebsite());
		console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
	}

	public void helpMenu(final Player player) {
		YamlConfiguration langYaml = loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		if (lines != null) {
			String prefix = langYaml.getString("general.prefix");
			for (String line : lines) {
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				player.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void helpMenu(final ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();

		List<String> lines = (List<String>) langYaml.getStringList("commands.help");
		if (lines != null) {
			String prefix = langYaml.getString("general.prefix");
			for (String line : lines) {
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				console.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void noPermission(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "messages.no_permission"));
	}

	public void messageSetRankSuccessSender(Player player, String target, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.rank_set_sender");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_target%", target);
				line = Util.replaceAll(line, "%argument_rank%", rank);
				player.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messageSetRankSuccessSender(ConsoleCommandSender console, String target, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.rank_set_sender");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_target%", target);
				line = Util.replaceAll(line, "%argument_rank%", rank);
				console.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messageSetRankSuccessTarget(Player target, String sender, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.rank_set_target");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_sender%", sender);
				line = Util.replaceAll(line, "%argument_rank%", rank);
				target.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}

	}

	public void messagePlayerNotFound(Player player, String target) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.player_not_found");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_target%", target);
				player.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messagePlayerNotFound(ConsoleCommandSender console, String target) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.player_not_found");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_target%", target);
				console.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messageGroupNotFound(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.group_not_found");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_rank%", rank);
				player.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messageGroupNotFound(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.group_not_found");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_rank%", rank);
				console.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messagePlayerCheckRank(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.player_check_rank");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_rank%", rank);
				player.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messagePlayerCheckRank(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();

		String line = langYaml.getString("messages.player_check_rank");
		if (line != null) {
			if (line.length() > 0) {
				String prefix = langYaml.getString("general.prefix");
				line = Util.replaceAll(line, "%plugin_prefix%", prefix);
				line = Util.replaceAll(line, "%plugin_name%", this.pdf.getName());
				line = Util.replaceAll(line, "%argument_rank%", rank);
				console.sendMessage(chatColor(this.colorChar.charAt(0), line));
			}
		}
	}

	public void messageCommandUsageReload(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_reload"));
	}

	public void messageCommandUsageReload(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_reload"));
	}

	public void messageCommandReloadConfig(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.reload_config"));
	}

	public void messageCommandReloadConfig(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.reload_config"));
	}

	public void messageCommandReloadConfigDone(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.reload_config_done"));
	}

	public void messageCommandReloadConfigDone(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.reload_config_done"));
	}

	public void messageCommandReloadPlugin(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.reload_plugin"));
	}

	public void messageCommandReloadPlugin(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.reload_plugin"));
	}

	public void messageCommandReloadPluginDone(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.reload_plugin_done"));
	}

	public void messageCommandReloadPluginDone(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.reload_plugin_done"));
	}

	public void messageCommandUsageSet(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set"));
	}

	public void messageCommandUsageSet(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set"));
	}

	public void messageCommandUsageSetown(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_setown"));
	}

	public void messageCommandUsageSetown(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_setown"));
	}

	public void messageCommandUsageCheck(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_check"));
	}

	public void messageCommandUsageCheck(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_check"));
	}

	public void messageCommandUsageAddperm(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_add_permission"));
	}

	public void messageCommandUsageAddperm(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_add_permission"));
	}

	public void messageCommandUsageDelperm(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_remove_permission"));
	}

	public void messageCommandUsageDelperm(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_remove_permission"));
	}

	public void messageCommandPermissionAdded(Player player, String permission, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		player.sendMessage(msg);
	}

	public void messageCommandPermissionAdded(ConsoleCommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_added");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		console.sendMessage(msg);
	}

	public void messageCommandPermissionRemoved(Player player, String permission, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		player.sendMessage(msg);
	}

	public void messageCommandPermissionRemoved(ConsoleCommandSender console, String permission, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.permission_removed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		msg = Util.replaceAll(msg, "%argument_permission%", permission);
		console.sendMessage(msg);
	}

	public void messageConfigVersionUpdated(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "messages.config_version_updated"));
	}

	public void messageConfigVersionUpdated(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "messages.config_version_updated"));
	}

	public void messageCommandUsageAddInheritance(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_add_inheritance"));
	}

	public void messageCommandUsageAddInheritance(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_add_inheritance"));
	}

	public void messageCommandUsageRemoveInheritance(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance"));
	}

	public void messageCommandUsageRemoveInheritance(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_remove_inheritance"));
	}

	public void messageCommandUsageSetPrefix(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_prefix"));
	}

	public void messageCommandUsageSetPrefix(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_prefix"));
	}

	public void messageCommandUsageSetSuffix(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_suffix"));
	}

	public void messageCommandUsageSetSuffix(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_suffix"));
	}

	public void messageCommandUsageSetChatColor(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_chat_color"));
	}

	public void messageCommandUsageSetChatColor(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_chat_color"));
	}

	public void messageCommandUsageSetNameColor(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_name_color"));
	}

	public void messageCommandUsageSetNameColor(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_set_name_color"));
	}

	public void messageCommandUsageCreateRank(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_create_rank"));
	}

	public void messageCommandUsageCreateRank(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_create_rank"));
	}

	public void messageCommandUsageDeleteRank(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_delete_rank"));
	}

	public void messageCommandUsageDeleteRank(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_delete_rank"));
	}

	public void messageCommandUsageEnableBuild(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_enable_build"));
	}

	public void messageCommandUsageEnableBuild(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_enable_build"));
	}

	public void messageCommandUsageDisableBuild(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_disable_build"));
	}

	public void messageCommandUsageDisableBuild(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_disable_build"));
	}

	public void messageCommandUsagePromote(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_promote"));
	}

	public void messageCommandUsagePromote(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_promote"));
	}

	public void messageCommandUsageDemote(Player player) {
		YamlConfiguration langYaml = loadLangFile();
		player.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_demote"));
	}

	public void messageCommandUsageDemote(ConsoleCommandSender console) {
		YamlConfiguration langYaml = loadLangFile();
		console.sendMessage(getGeneralMessage(langYaml, "commands.usage_command_demote"));
	}

	public void messageCommandInheritanceAdded(Player player, String inheritance, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandInheritanceAdded(ConsoleCommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_added");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandInheritanceRemoved(Player player, String inheritance, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandInheritanceRemoved(ConsoleCommandSender console, String inheritance, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.inheritance_removed");
		msg = Util.replaceAll(msg, "%argument_inheritance%", inheritance);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandSetPrefix(Player player, String prefix, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandSetPrefix(ConsoleCommandSender console, String prefix, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_prefix");
		msg = Util.replaceAll(msg, "%argument_prefix%", prefix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandSetSuffix(Player player, String suffix, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandSetSuffix(ConsoleCommandSender console, String suffix, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_suffix");
		msg = Util.replaceAll(msg, "%argument_suffix%", suffix);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandSetChatColor(Player player, String color, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandSetChatColor(ConsoleCommandSender console, String color, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_chat_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandSetNameColor(Player player, String color, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandSetNameColor(ConsoleCommandSender console, String color, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_set_name_color");
		msg = Util.replaceAll(msg, "%argument_color%", color);
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandCreateRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandCreateRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_created");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandCreateRankError(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandCreateRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_create_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandDeleteRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandDeleteRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_deleted");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandDeleteRankError(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}

	public void messageCommandDeleteRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_delete_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandPromoteSuccess(Player player, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		player.sendMessage(msg);
	}

	public void messageCommandPromoteSuccess(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_promoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		console.sendMessage(msg);
	}
	
	public void messageCommandPromoteError(Player player, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		player.sendMessage(msg);
	}

	public void messageCommandPromoteError(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_promote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		console.sendMessage(msg);
	}

	public void messageCommandDemoteSuccess(Player player, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		player.sendMessage(msg);
	}

	public void messageCommandDemoteSuccess(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.player_demoted");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		console.sendMessage(msg);
	}

	public void messageCommandDemoteError(Player player, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		player.sendMessage(msg);
	}

	public void messageCommandDemoteError(ConsoleCommandSender console, String playername) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_player_demote");
		msg = Util.replaceAll(msg, "%argument_target%", playername);
		console.sendMessage(msg);
	}

	public void messageCommandBuildEnabled(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}
	
	public void messageCommandBuildEnabled(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_enabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandBuildDisabled(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}
	
	public void messageCommandBuildDisabled(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.build_disabled");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandRenameRankSuccess(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}
	
	public void messageCommandRenameRankSuccess(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.rank_renamed");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}

	public void messageCommandRenameRankError(Player player, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		player.sendMessage(msg);
	}
	
	public void messageCommandRenameRankError(ConsoleCommandSender console, String rank) {
		YamlConfiguration langYaml = loadLangFile();
		String msg = getGeneralMessage(langYaml, "messages.error_renaming_rank");
		msg = Util.replaceAll(msg, "%argument_rank%", rank);
		console.sendMessage(msg);
	}
}