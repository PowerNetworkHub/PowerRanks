package nl.svenar.PowerRanks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.PowerRanks.StorageType;
import nl.svenar.PowerRanks.PowerRanksExceptionsHandler;

public class PowerDatabase {
	private Connection mysqlConnection;
	private String host, database, username, password, table_users, table_ranks, table_usertags;
	private int port;
	private StorageType storageType;

	public PowerDatabase(StorageType storageType, String host, int port, String username, String password, String database) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.storageType = storageType;

		this.table_users = "users";
		this.table_ranks = "ranks";
		this.table_usertags = "usertags";
	}

	public boolean connect() {
		if (storageType == StorageType.MySQL) {
			try {
				synchronized (this) {
					if (getMYSQLConnection() != null && !getMYSQLConnection().isClosed()) {
						return false;
					}

					Class.forName("com.mysql.jdbc.Driver");
					this.mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port/* + "/" + this.database */ + "?autoReconnect=true&useSSL=false", this.username, this.password);

					setupMYSQLDatabase();

//					PowerRanks.log.info(ChatColor.GREEN + "MYSQL CONNECTED");
				}
			} catch (SQLException e) {
				PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
				return false;
			} catch (ClassNotFoundException e) {
				PowerRanksExceptionsHandler.except(this.getClass().getName(), e.toString());
				return false;
			}
		}
		return true;
	}

	private void setupMYSQLDatabase() throws SQLException {
		String sql_create_database = "CREATE DATABASE " + this.database;

		String sql_create_table_users = "CREATE TABLE `" + this.database + "`.`" + this.table_users
				+ "` ( `uuid` VARCHAR(50) NOT NULL , `name` VARCHAR(30) NOT NULL , `rank` VARCHAR(32) NOT NULL , `subranks` LONGTEXT NOT NULL , `usertag` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `playtime` INT NOT NULL , UNIQUE `uuid` (`uuid`));";

		String sql_create_table_ranks = "CREATE TABLE `" + this.database + "`.`" + this.table_ranks
				+ "` ( `name` VARCHAR(32) NOT NULL , `permissions` LONGTEXT NOT NULL , `inheritance` LONGTEXT NOT NULL , `build` BOOLEAN NOT NULL , `prefix` VARCHAR(64) NOT NULL , `suffix` VARCHAR(64) NOT NULL , `chat_color` VARCHAR(16) NOT NULL , `name_color` VARCHAR(16) NOT NULL , `level_promote` VARCHAR(32) NOT NULL , `level_demote` VARCHAR(32) NOT NULL , `economy_buyable` LONGTEXT NOT NULL , `economy_cost` INT NOT NULL , `gui_icon` VARCHAR(32) NOT NULL , UNIQUE `name` (`name`));";

		String sql_create_table_usertags = "CREATE TABLE `" + this.database + "`.`" + this.table_usertags + "` ( `name` VARCHAR(32) NOT NULL , `value` VARCHAR(64) NOT NULL , UNIQUE `name` (`name`))";

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

			} else {
				PowerRanksExceptionsHandler.exceptCustom(this.getClass().getName(), "There was a error creating the database: " + this.database + " are the permissions set correctly?");
			}
			PowerRanks.log.info("===--------------------===");
		}
	}

	public Connection getMYSQLConnection() {
		return mysqlConnection;
	}

	public boolean isDatabase() {
		return storageType == StorageType.MySQL || storageType == StorageType.SQLite;
	}
}
