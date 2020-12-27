package nl.svenar.PowerRanks.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.PowerRanks.StorageType;
import nl.svenar.PowerRanks.PowerRanksExceptionsHandler;

@SuppressWarnings("unchecked")
public class PowerDatabase {

	private Connection mysqlConnection;
	private String host, database, username, password;
	public String table_users, table_ranks, table_usertags, table_data;
	private int port;
	private StorageType storageType;
	private PowerRanks plugin;
	private boolean connection_error = false;

	public PowerDatabase(PowerRanks plugin, StorageType storageType, String host, int port, String username, String password, String database) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.storageType = storageType;
		this.plugin = plugin;

		this.table_users = "users";
		this.table_ranks = "ranks";
		this.table_usertags = "usertags";
		this.table_data = "data";
	}

	public boolean connectMYSQL() {
		if (storageType == StorageType.MySQL) {
			try {
				synchronized (this) {
					if (getMYSQLConnection() != null && !getMYSQLConnection().isClosed()) {
						return false;
					}

					Class.forName("com.mysql.jdbc.Driver");
					this.mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port/* + "/" + this.database */ + "?autoReconnect=true&useSSL=false", this.username, this.password);
				}
			} catch (SQLException e) {
				PowerRanksExceptionsHandler.silent_except(this.getClass().getName(), e.toString());
				PowerRanks.log.severe("");
				PowerRanks.log.severe("===------------------------===");
				PowerRanks.log.severe("Connection to database failed!");
				PowerRanks.log.severe(e.toString());
				PowerRanks.log.severe("===------------------------===");
				PowerRanks.log.severe("");
				connection_error = true;
				return false;
			} catch (ClassNotFoundException e) {
				PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
				return false;
			}

			if (!connection_error) {
				try {
					// PowerRanks.log.info("Connected: " + this.mysqlConnection.isValid(1));
					setupMYSQLDatabase();

					PowerRanks.log.info("MYSQL CONNECTED");
				} catch (SQLException e) {
					PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
					return false;
				}
			}
		}
		return true;
	}

	public boolean connectSQLITE() {
		return false; // TODO: SQLite
	}

	private void setupMYSQLDatabase() throws SQLException {
		String sql_create_database = "CREATE DATABASE " + this.database;

		String sql_create_table_users = "CREATE TABLE `" + this.database + "`.`" + this.table_users
				+ "` ( `uuid` VARCHAR(50) NOT NULL , `name` VARCHAR(30) NOT NULL , `rank` VARCHAR(32) NOT NULL , `subranks` LONGTEXT NOT NULL , `usertag` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `playtime` INT NOT NULL , UNIQUE `uuid` (`uuid`));";
//
//		String sql_create_table_ranks = "CREATE TABLE `" + this.database + "`.`" + this.table_ranks
//				+ "` ( `name` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `inheritance` LONGTEXT NOT NULL , `build` BOOLEAN NOT NULL , `prefix` VARCHAR(64) NOT NULL , `suffix` VARCHAR(64) NOT NULL , `chat_color` VARCHAR(16) NOT NULL , `name_color` VARCHAR(16) NOT NULL , `level_promote` VARCHAR(32) NOT NULL , `level_demote` VARCHAR(32) NOT NULL , `economy_buyable` LONGTEXT NOT NULL , `economy_cost` INT NOT NULL , `gui_icon` VARCHAR(32) NOT NULL , UNIQUE `name` (`name`));";
		
		String sql_create_table_ranks = "CREATE TABLE `" + this.database + "`.`" + this.table_ranks
				+ "` ( `name` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `inheritance` LONGTEXT NOT NULL , `prefix` VARCHAR(64) NOT NULL , `suffix` VARCHAR(64) NOT NULL , `chat_color` VARCHAR(16) NOT NULL , `name_color` VARCHAR(16) NOT NULL , `level_promote` VARCHAR(32) NOT NULL , `level_demote` VARCHAR(32) NOT NULL , `economy_buyable` LONGTEXT NOT NULL , `economy_cost` INT NOT NULL , `gui_icon` VARCHAR(32) NOT NULL , UNIQUE `name` (`name`));";

		String sql_create_table_usertags = "CREATE TABLE `" + this.database + "`.`" + this.table_usertags + "` ( `name` VARCHAR(32) NOT NULL , `value` VARCHAR(64) NOT NULL , UNIQUE `name` (`name`));";

		String sql_create_table_data = "CREATE TABLE `" + this.database + "`.`" + this.table_data + "` ( `key` VARCHAR(32) NOT NULL , `value` VARCHAR(64) NOT NULL , UNIQUE `key` (`key`));";

		ResultSet resultSet = this.mysqlConnection.getMetaData().getCatalogs();
		boolean exists = false;
		while (resultSet.next()) {

			String databaseName = resultSet.getString(1);
			if (databaseName.equals(this.database)) {
				exists = true;
				break;
			}
		}
		resultSet.close();

		PowerRanks.log.info("Database " + (exists ? "exists" : "does not exist"));

		if (!exists) {
			int result = -1;
			result = this.mysqlConnection.createStatement().executeUpdate(sql_create_database);

			PowerRanks.log.info("===--------------------===");
			if (result == 1) {
				PowerRanks.log.info("Database: " + this.database + " created!");

				result = this.mysqlConnection.createStatement().executeUpdate(sql_create_table_users);
				PowerRanks.log.info("Table: " + this.table_users + " created! (status: " + result + ")");

				result = this.mysqlConnection.createStatement().executeUpdate(sql_create_table_ranks);
				PowerRanks.log.info("Table: " + this.table_ranks + " created! (status: " + result + ")");

				result = this.mysqlConnection.createStatement().executeUpdate(sql_create_table_usertags);
				PowerRanks.log.info("Table: " + this.table_usertags + " created! (status: " + result + ")");

				result = this.mysqlConnection.createStatement().executeUpdate(sql_create_table_data);
				PowerRanks.log.info("Table: " + this.table_data + " created! (status: " + result + ")");

				if (!mergeYAMLtoDatabase()) {
					PowerRanks.log.info("[DB] Created default configuration");
					setupMYSQLDefaultData();
				} else {
					PowerRanks.log.info("[DB] Merged existing configuration");
				}

			} else {
				PowerRanksExceptionsHandler.exceptCustom(this.getClass().getName(), "There was a error creating the database: " + this.database + " are the permissions set correctly?");
			}
			PowerRanks.log.info("===--------------------===");
		}
	}

	private boolean mergeYAMLtoDatabase() throws SQLException {
		File ranksFile = new File(PowerRanks.fileLoc, "Ranks.yml");
		File playersFile = new File(PowerRanks.fileLoc, "Players.yml");
		boolean files_exists = false;

		files_exists = ranksFile.exists() && playersFile.exists();
		if (files_exists) {
			YamlConfiguration ranksYaml = new YamlConfiguration();
			YamlConfiguration playersYaml = new YamlConfiguration();

			try {
				ranksYaml.load(ranksFile);
				playersYaml.load(playersFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}

			// === Ranks.yml ===
			String sql_set_default_rank = "INSERT INTO `" + this.database + "`.`" + this.table_data + "`(`key`, `value`) VALUES ('default_rank', '" + ranksYaml.getString("Default") + "');";
			String sql_create_rank = "INSERT INTO `" + this.database + "`.`" + this.table_ranks
					+ "` (`name`, `permissions`, `inheritance`, `prefix`, `suffix`, `chat_color`, `name_color`, `level_promote`, `level_demote`, `economy_buyable`, `economy_cost`, `gui_icon`) VALUES ('%rank_name%', '%rank_permissions%', '%rank_inheritance%', '%rank_prefix%', '%rank_suffix%', '%rank_chatcolor%', '%rank_namecolor%', '%rank_promote%', '%rank_demote%', '%rank_buyable%', '%rank_cost%', '%rank_gui_icon%')";
			String sql_create_usertag = "INSERT INTO `" + this.database + "`.`" + this.table_usertags + "`(`name`, `value`) VALUES ('%name%', '%value%');";
			
			this.mysqlConnection.createStatement().executeUpdate(sql_set_default_rank);

			for (String key : ranksYaml.getConfigurationSection("Groups").getKeys(false)) {
				this.mysqlConnection.createStatement()
						.executeUpdate(sql_create_rank
								.replace("%rank_name%", key)
								.replace("%rank_permissions%", String.join(",", ranksYaml.getStringList("Groups." + key + ".permissions")))
								.replace("%rank_inheritance%", String.join(",", ranksYaml.getStringList("Groups." + key + ".inheritance")))
//								.replace("%rank_build%", ranksYaml.getBoolean("Groups." + key + ".build") ? "1" : "0")
								.replace("%rank_prefix%", (String) ranksYaml.get("Groups." + key + ".chat.prefix"))
								.replace("%rank_suffix%", (String) ranksYaml.get("Groups." + key + ".chat.suffix"))
								.replace("%rank_chatcolor%", (String) ranksYaml.get("Groups." + key + ".chat.chatColor"))
								.replace("%rank_namecolor%", (String) ranksYaml.get("Groups." + key + ".chat.nameColor"))
								.replace("%rank_promote%", (String) ranksYaml.get("Groups." + key + ".level.promote"))
								.replace("%rank_demote%", (String) ranksYaml.get("Groups." + key + ".level.demote"))
								.replace("%rank_buyable%", String.join(",", ranksYaml.getStringList("Groups." + key + ".economy.buyable")))
								.replace("%rank_cost%", String.valueOf(ranksYaml.get("Groups." + key + ".economy.cost")))
								.replace("%rank_gui_icon%", (String) ranksYaml.get("Groups." + key + ".gui.icon")));
			}
			
			if (ranksYaml.isConfigurationSection("Usertags")) {
				for (String key : ranksYaml.getConfigurationSection("Usertags").getKeys(false)) {
					this.mysqlConnection.createStatement()
					.executeUpdate(sql_create_usertag
							.replace("%name%", key)
							.replace("%value%", ranksYaml.getString("Usertags." + key))
							);
				}
			}
			// === Ranks.yml ===

			// === Players.yml ===
			String sql_create_player = "INSERT INTO `" + this.database + "`.`" + this.table_users + "`(`uuid`, `name`, `rank`, `subranks`, `usertag`, `permissions`, `playtime`) VALUES ('%uuid%', '%name%', '%rank%', '%subranks%', '%usertag%', '%permissions%', '%playtime%');";
			for (String key : playersYaml.getConfigurationSection("players").getKeys(false)) {
				String subranks = "";
				
				// TODO subranks; test if it works
				if (playersYaml.isConfigurationSection("players." + key + ".subranks")) {
					for (String subrank_key : playersYaml.getConfigurationSection("players." + key + ".subranks").getKeys(false)) {
						if (subranks.length() > 0)
							subranks += "&";
						
						subranks += subrank_key + ";";
						subranks += "use_prefix:" + (playersYaml.getBoolean("players." + key + ".subranks." + subrank_key + ".use_prefix") ? "1" : "0") + ";";
						subranks += "use_suffix:" + (playersYaml.getBoolean("players." + key + ".subranks." + subrank_key + ".use_suffix") ? "1" : "0") + ";";
						subranks += "use_permissions:" + (playersYaml.getBoolean("players." + key + ".subranks." + subrank_key + ".use_permissions") ? "1" : "0") + ";";
						subranks += "worlds:(" + String.join(",", playersYaml.getStringList("players." + key + ".subranks." + subrank_key + ".worlds")) + ")";
					}
				}
				
				this.mysqlConnection.createStatement()
				.executeUpdate(sql_create_player
						.replace("%uuid%", key)
						.replace("%name%", playersYaml.getString("players." + key + ".name"))
						.replace("%rank%", playersYaml.getString("players." + key + ".rank"))
						.replace("%subranks%", subranks)
						.replace("%usertag%", playersYaml.getString("players." + key + ".usertag"))
						.replace("%permissions%", String.join(",", playersYaml.getStringList("players." + key + ".permissions")))
						.replace("%playtime%", String.valueOf(playersYaml.get("players." + key + ".playtime")))
						);
			}
			// === Players.yml ===

			ranksFile.delete();
			playersFile.delete();
		}

		return files_exists;
	}

	private void setupMYSQLDefaultData() throws SQLException {
		copyTmpFile(plugin, "Ranks.yml");
		final File tmpFile = new File(plugin.getDataFolder() + File.separator + "tmp", "Ranks.yml");
		final YamlConfiguration tmpYamlConf = new YamlConfiguration();
		try {
			tmpYamlConf.load(tmpFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		String sql_set_default_rank = "INSERT INTO `" + this.database + "`.`" + this.table_data + "`(`key`, `value`) VALUES ('default_rank', '" + tmpYamlConf.getString("Default") + "');";
		String sql_create_rank = "INSERT INTO `" + this.database + "`.`" + this.table_ranks
				+ "` (`name`, `permissions`, `inheritance`, `prefix`, `suffix`, `chat_color`, `name_color`, `level_promote`, `level_demote`, `economy_buyable`, `economy_cost`, `gui_icon`) VALUES ('%rank_name%', '%rank_permissions%', '%rank_inheritance%', '%rank_prefix%', '%rank_suffix%', '%rank_chatcolor%', '%rank_namecolor%', '%rank_promote%', '%rank_demote%', '%rank_buyable%', '%rank_cost%', '%rank_gui_icon%')";
		this.mysqlConnection.createStatement().executeUpdate(sql_set_default_rank);

		for (String key : tmpYamlConf.getConfigurationSection("Groups").getKeys(false)) {
			this.mysqlConnection.createStatement()
					.executeUpdate(sql_create_rank.replace("%rank_name%", key).replace("%rank_permissions%", "").replace("%rank_inheritance%", "")
//							.replace("%rank_build%", tmpYamlConf.getBoolean("Groups." + key + ".build") ? "1" : "0")
							.replace("%rank_prefix%", (String) tmpYamlConf.get("Groups." + key + ".chat.prefix")).replace("%rank_suffix%", (String) tmpYamlConf.get("Groups." + key + ".chat.suffix"))
							.replace("%rank_chatcolor%", (String) tmpYamlConf.get("Groups." + key + ".chat.chatColor")).replace("%rank_namecolor%", (String) tmpYamlConf.get("Groups." + key + ".chat.nameColor"))
							.replace("%rank_promote%", (String) tmpYamlConf.get("Groups." + key + ".level.promote")).replace("%rank_demote%", (String) tmpYamlConf.get("Groups." + key + ".level.demote")).replace("%rank_buyable%", "")
							.replace("%rank_cost%", "0").replace("%rank_gui_icon%", (String) tmpYamlConf.get("Groups." + key + ".gui.icon")));
		}
		deleteTmpFile(plugin, "Ranks.yml");
	}

	public String getDefaultRank() {
		String default_rank = "";
		String sql_get_default_rank = "SELECT `value` FROM `" + this.database + "`.`" + this.table_data + "` WHERE `key`='default_rank';";
		try {
			Statement st = this.mysqlConnection.createStatement();
			ResultSet rs = st.executeQuery(sql_get_default_rank);
			while (rs.next()) {
				default_rank = rs.getString("value");
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return default_rank;
	}

	public void updatePlayer(Player player, String field, String value) {
		String sql_insert_new_player = "INSERT INTO `" + this.database + "`.`" + this.table_users
				+ "` (`uuid`, `name`, `rank`, `subranks`, `usertag`, `permissions`, `playtime`) VALUES ('%player_uuid%', '%player_name%', '%player_rank%', '%player_subranks%', '%player_usertag%', '%player_permissions%', '%player_playtime%')";
		if (!playerExists(player)) {
			PowerRanks.log.info("---------- Creating player");
			PowerRanks.log.warning(sql_insert_new_player.replace("%player_uuid%", player.getUniqueId().toString()).replace("%player_name%", player.getName()).replace("%player_rank%", getDefaultRank()).replace("%player_subranks%", "")
					.replace("%player_usertag%", "").replace("%player_permissions%", "").replace("%player_playtime%", "0"));
			try {
				this.mysqlConnection.createStatement().executeUpdate(sql_insert_new_player.replace("%player_uuid%", player.getUniqueId().toString()).replace("%player_name%", player.getName()).replace("%player_rank%", getDefaultRank())
						.replace("%player_subranks%", "").replace("%player_usertag%", "").replace("%player_permissions%", "").replace("%player_playtime%", "0"));
				PowerRanks.log.info("---------- Player created");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			PowerRanks.log.info("---------- Player exists");
		}
	}

	public boolean playerExists(Player player) {
		try {
			Statement st = this.mysqlConnection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM `" + this.database + "`.`" + this.table_users + "` WHERE `uuid`='" + player.getUniqueId().toString() + "';");
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Connection getMYSQLConnection() {
		return mysqlConnection;
	}

	public boolean isDatabase() {
		return storageType == StorageType.MySQL || storageType == StorageType.SQLite;
	}

	private void copyTmpFile(PowerRanks plugin, String yamlFileName) {
		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (!tmp_file.exists())
			tmp_file.getParentFile().mkdirs();
		plugin.copy(plugin.getResource(yamlFileName), tmp_file);
	}

	private void deleteTmpFile(PowerRanks plugin, String yamlFileName) {
		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (tmp_file.exists())
			tmp_file.delete();
	}

	public void setField(String table_name, String key, Object value) {
		String[] _split = key.split("\\.");
//		PowerRanks.log.info("--->>> " + key + " (" + _split.length + "): " + value);

		if (table_name == table_ranks) {
			String rankname = _split[1];
			String target_key = _split[_split.length - 1];
			if (value instanceof ArrayList<?>) {
				value = String.join(",", (ArrayList<String>) value);
			}
//			target_key = target_key == "chatcolor" ? "chat_color" : target_key;
//			target_key = target_key == "namecolor" ? "name_color" : target_key;
//			target_key = target_key == "promote" ? "level_promote" : target_key;
//			target_key = target_key == "demote" ? "level_demote" : target_key;
//			target_key = target_key == "buyable" ? "economy_buyable" : target_key;
//			target_key = target_key == "cost" ? "economy_cost" : target_key;
//			target_key = target_key == "icon" ? "gui_icon" : target_key;
//			if (target_key.equals("build")) {
//				value = (boolean) value ? 1 : 0;
//			}

			String sql = "INSERT INTO `" + table_name + "` (`name`, `" + target_key + "`) VALUES('" + rankname + "', '" + value + "') ON DUPLICATE KEY UPDATE `" + target_key + "`='" + value + "';";
//			PowerRanks.log.info("<<<--- " + sql);
			try {
				Statement st = this.mysqlConnection.createStatement();
				st.executeQuery("USE " + this.database + ";");
				st.executeUpdate(sql);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (table_name == table_users) {
			String uuid = _split[1];
			if (value instanceof ArrayList<?>) {
				value = String.join(",", (ArrayList<String>) value);
			}
			String sql = "INSERT INTO `" + table_name + "` (`uuid`, `" + _split[_split.length - 1] + "`) VALUES('" + uuid + "', '" + value + "') ON DUPLICATE KEY UPDATE `" + _split[_split.length - 1] + "`='" + value + "';";
//			PowerRanks.log.info("<<<--- " + sql);
			try {
				Statement st = this.mysqlConnection.createStatement();
				st.executeQuery("USE " + this.database + ";");
				st.executeUpdate(sql);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<String, Object> getAllFields(String table_name) {
		String sql = "SELECT * FROM " + table_name + ";";

		HashMap<String, Object> output = new HashMap<String, Object>();

		try {
			Statement st = this.mysqlConnection.createStatement();
			ResultSet rs;
			st.executeQuery("USE " + this.database + ";");
			rs = st.executeQuery(sql);

			if (table_name == table_ranks) {
				ArrayList<String> ranknames = new ArrayList<String>();
				while (rs.next()) {
					String rankname = rs.getString("name");
					String permissions = rs.getString("permissions");
					String inheritance = rs.getString("inheritance");
//					boolean build = rs.getBoolean("build");
					String prefix = rs.getString("prefix");
					String suffix = rs.getString("suffix");
					String chatcolor = rs.getString("chat_color");
					String namecolor = rs.getString("name_color");
					String promote = rs.getString("level_promote");
					String demote = rs.getString("level_demote");
					String buyable = rs.getString("economy_buyable");
					int cost = rs.getInt("economy_cost");
					String icon = rs.getString("gui_icon");

					ranknames.add(rankname);

					output.put("Groups." + rankname, "0");
					output.put("Groups." + rankname + ".permissions", permissions);
					output.put("Groups." + rankname + ".inheritance", inheritance);
//					output.put("Groups." + rankname + ".build", build);
					output.put("Groups." + rankname + ".chat.prefix", prefix);
					output.put("Groups." + rankname + ".chat.suffix", suffix);
					output.put("Groups." + rankname + ".chat.chatcolor", chatcolor);
					output.put("Groups." + rankname + ".chat.namecolor", namecolor);
					output.put("Groups." + rankname + ".level.promote", promote);
					output.put("Groups." + rankname + ".level.demote", demote);
					output.put("Groups." + rankname + ".economy.buyable", buyable);
					output.put("Groups." + rankname + ".economy.cost", cost);
					output.put("Groups." + rankname + ".gui.icon", icon);
				}

				output.put("Groups", ranknames);
			}

			if (table_name == table_users) {
				ArrayList<String> players = new ArrayList<String>();
				while (rs.next()) {
					String uuid = rs.getString("uuid");
					String name = rs.getString("name");
					String rank = rs.getString("rank");
					String subranks = rs.getString("subranks");
					String usertag = rs.getString("usertag");
					String permissions = rs.getString("permissions");
					int playtime = rs.getInt("playtime");

					players.add(uuid);

					output.put("players." + uuid + ".name", name);
					output.put("players." + uuid + ".rank", rank);
					output.put("players." + uuid + ".subranks", subranks);
					output.put("players." + uuid + ".usertag", usertag);
					output.put("players." + uuid + ".permissions", permissions.split(","));
					output.put("players." + uuid + ".playtime", playtime);
				}
				output.put("players", players);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return output;
	}

	public int removeRank(String rank) {
		String sql = "DELETE FROM `" + table_ranks + "` WHERE `name`='" + rank + "';";

		try {
			Statement st = this.mysqlConnection.createStatement();
			st.executeQuery("USE " + this.database + ";");
			return st.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}