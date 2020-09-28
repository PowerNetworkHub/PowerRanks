package nl.svenar.PowerRanks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.PowerRanksExceptionsHandler;

public class PowerDatabase {
	private Connection connection;
	private String host, database, username, password, table_users, table_ranks, table_usertags;
	private int port;

	public PowerDatabase(String host, int port, String username, String password, String database) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;

		this.table_users = "users";
		this.table_ranks = "ranks";
		this.table_usertags = "usertags";
	}

	public boolean connect() {
		try {
			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return false;
				}

				Class.forName("com.mysql.jdbc.Driver");
				this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port/* + "/" + this.database */, this.username, this.password);

				setupDatabase();

				PowerRanks.log.info(ChatColor.GREEN + "MYSQL CONNECTED");
			}
		} catch (SQLException e) {
			PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
			return false;
		} catch (ClassNotFoundException e) {
			PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
			return false;
		}

		return true;
	}

	private void setupDatabase() throws SQLException {
		String sql_create_database = "CREATE DATABASE " + this.database;

		String sql_create_table_users = "CREATE TABLE `" + this.database + "`.`" + this.table_users
				+ "` ( `uuid` VARCHAR(50) NOT NULL , `name` VARCHAR(30) NOT NULL , `rank` VARCHAR(32) NOT NULL , `subranks` LONGTEXT NOT NULL , `usertag` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `playtime` INT NOT NULL , UNIQUE `uuid` (`uuid`));";

		String sql_create_table_ranks = "CREATE TABLE `" + this.database + "`.`" + this.table_ranks
				+ "` ( `name` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `inheritance` LONGTEXT NOT NULL , `build` BOOLEAN NOT NULL , `prefix` VARCHAR(64) NOT NULL , `suffix` VARCHAR(64) NOT NULL , `chat_color` VARCHAR(16) NOT NULL , `name_color` VARCHAR(16) NOT NULL , `level_promote` VARCHAR(32) NOT NULL , `level_demote` VARCHAR(32) NOT NULL , `economy_buyable` LONGTEXT NOT NULL , `economy_cost` INT NOT NULL , `gui_icon` VARCHAR(32) NOT NULL , UNIQUE `name` (`name`));";

		String sql_create_table_usertags = "CREATE TABLE `" + this.database + "`.`" + this.table_usertags + "` ( `name` VARCHAR(32) NOT NULL , `value` VARCHAR(64) NOT NULL , UNIQUE `name` (`name`))";

		ResultSet resultSet = this.connection.getMetaData().getCatalogs();
		boolean exists = false;
		while (resultSet.next()) {

			String databaseName = resultSet.getString(1);
			if (databaseName.equals(this.database)) {
				exists = true;
				break;
			}
		}
		resultSet.close();

		if (!exists) {
			int result = -1;
			result = this.connection.createStatement().executeUpdate(sql_create_database);

			if (result == 1) {
				PowerRanks.log.info(ChatColor.GREEN + "Database: " + this.database + " created!");

				result = this.connection.createStatement().executeUpdate(sql_create_table_users);
//				if (result == 1) {
				PowerRanks.log.info(ChatColor.GREEN + "Table: " + this.table_users + " created!");
//				}

				result = this.connection.createStatement().executeUpdate(sql_create_table_ranks);
//				if (result == 1) {
				PowerRanks.log.info(ChatColor.GREEN + "Table: " + this.table_ranks + " created!");
//				}

				result = this.connection.createStatement().executeUpdate(sql_create_table_usertags);
//				if (result == 1) {
				PowerRanks.log.info(ChatColor.GREEN + "Table: " + this.table_usertags + " created!");
//				}

			} else {
//				PowerRanks.log.warning(ChatColor.RED + "There was a error creating the database: " + this.database + " are the permissions set correctly?");
				PowerRanksExceptionsHandler.exceptCustom(this.getClass().getName(), "There was a error creating the database: " + this.database + " are the permissions set correctly?");
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}
}
