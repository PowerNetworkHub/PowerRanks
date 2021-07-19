package nl.svenar.powerranks.storage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.ErrorManager;

public class SQLITEDataHandler extends BaseDataHandler {

    private String dbname = "PowerRanks";
    private Connection conn = null;

    // private String sql_create_players_table = "CREATE TABLE IF NOT EXISTS players
    // (\n" //
    // + " id integer PRIMARY KEY AUTO_INCREMENT,\n" //
    // + " name VARCHAR(16),\n" //
    // + " uuid VARCHAR(48),\n" //
    // + " RANKS text\n" //
    // + ");"; //

    private String sql_create_players_table = "CREATE TABLE IF NOT EXISTS players (" //
            + "id    INTEGER      PRIMARY KEY AUTOINCREMENT," //
            + "name  VARCHAR (16)," //
            + "uuid  VARCHAR (48)," //
            + "ranks TEXT," //
            + "permissions TEXT" + ");"; //

    private String sql_create_ranks_table = "CREATE TABLE IF NOT EXISTS ranks (" //
            + "id           INTEGER      PRIMARY KEY AUTOINCREMENT," //
            + "name         VARCHAR (32)," //
            + "prefix       VARCHAR (32)," //
            + "suffix       VARCHAR (32)," //
            + "permissions  TEXT," //
            + "inheritances TEXT," //
            + "isdefault    INTEGER      DEFAULT (0)," //
            + "weight       INTEGER      DEFAULT (0) " //
            + ");"; //

    @Override
    public void setup(PowerRanks plugin) {
        super.setup(plugin);

        setupTables(getConnection());
    }

    private void setupTables(Connection connection) {
        try {
            Statement stmt = conn.createStatement();

            stmt.execute(sql_create_players_table);
            stmt.execute(sql_create_ranks_table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        File sqliteFile = new File(plugin.getDataFolder(), dbname + ".db");

        if (!sqliteFile.exists()) {
            try {
                sqliteFile.createNewFile();
            } catch (IOException e) {
                ErrorManager.logError("File write error: " + dbname + ".db");
            }
        }

        //

        try {
            if (conn != null && !conn.isClosed()) {
                return conn;
            }

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);

        } catch (SQLException ex) {
            ErrorManager.logError("SQLite exception on initialize!\n" + ex);
        } catch (ClassNotFoundException ex) {
            ErrorManager.logError("SQLite JDBC library not found!\n" + ex);
        }

        return conn;
    }

    @Override
    public Collection<PRRank> loadRanks() {
        ArrayList<PRRank> ranks = new ArrayList<PRRank>();

        String select_ranks_sql = "SELECT * from ranks;";

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(select_ranks_sql);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                String name = rs.getString("name");
                String prefix = rs.getString("prefix");
                String suffix = rs.getString("suffix");
                int weight = rs.getInt("weight");
                Boolean isDefault = rs.getInt("isdefault") == 1;
                ArrayList<String> inheritances = new ArrayList<String>(
                        Arrays.asList(rs.getString("inheritances").split(",")));
                Collection<PRPermission> permissions = deserializePermissions(rs.getString("permissions"));

                inheritances.removeAll(Collections.singleton(null));
                inheritances.removeAll(Collections.singleton(""));

                PRRank rank = new PRRank(name);
                rank.setDefault(isDefault);
                rank.setWeight(weight);
                rank.setPrefix(prefix);
                rank.setSuffix(suffix);
                rank.setInheritedRanks(inheritances);
                rank.setPermissions(permissions);

                ranks.add(rank);

            }
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error loading rank data!\n" + ex);
        }

        return ranks;
    }

    @Override
    public Collection<PRPlayer> loadPlayers() {
        ArrayList<PRPlayer> players = new ArrayList<PRPlayer>();

        String select_players_sql = "SELECT * from players;";

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(select_players_sql);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                String name = rs.getString("name");
                String uuid = rs.getString("uuid");
                ArrayList<String> ranks = new ArrayList<String>(Arrays.asList(rs.getString("ranks").split(",")));
                Collection<PRPermission> permissions = deserializePermissions(rs.getString("permissions"));

                PRPlayer player = new PRPlayer();
                player.setName(name);
                player.setUuid(PRPlayer.stringToUUID(uuid));
                player.setPermissions(permissions);

                for (String rankName : ranks) {
                    PRRank rank = BaseDataHandler.getRank(rankName);
                    if (rank != null) {
                        player.addRank(rank);
                    } else {
                        ErrorManager.logWarning("Player '" + player.getName() + "' (" + player.getUuid().toString()
                                + ") has a non-existing rank: " + rankName);
                    }
                }

                players.add(player);
            }
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error loading player data!\n" + ex);
        }

        return players;
    }

    @Override
    public void saveRanks(Collection<PRRank> ranks) {
        for (PRRank rank : ranks) {
            saveRank(rank);
        }
    }

    @Override
    public void savePlayers(Collection<PRPlayer> players) {
        for (PRPlayer player : players) {
            savePlayer(player);
        }
    }

    @Override
    public void saveRank(PRRank rank) {
        String name = rank.getName();
        String prefix = rank.getPrefix();
        String suffix = rank.getSuffix();
        int weight = rank.getWeight();
        boolean isDefault = rank.getDefault();
        String permissions = serializePermissions(rank.getPermissions());
        String inheritances = String.join(",", rank.getInheritedRanks());

        String insert_rank_sql_base = "INSERT INTO ranks (name, prefix, suffix, weight, isdefault, permissions, inheritances) VALUES ('%name%', '%prefix%', '%suffix%', %weight%, %isdefault%, '%permissions%', '%inheritances%');";

        String select_rank_sql = "SELECT * from ranks WHERE name='%name%';";
        select_rank_sql = select_rank_sql.replace("%name%", name);

        boolean rankExists = false;

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(select_rank_sql);
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                rankExists = true;
            }
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error saving rank data for " + name + "!\n" + ex);
        }

        if (rankExists) {
            insert_rank_sql_base = "UPDATE ranks SET prefix = '%prefix%', suffix = '%suffix%', weight = %weight%, isdefault = %isdefault%, permissions = '%permissions%', inheritances = '%inheritances%' WHERE name == '%name%';";
        }

        insert_rank_sql_base = insert_rank_sql_base.replace("%name%", name);
        insert_rank_sql_base = insert_rank_sql_base.replace("%prefix%", prefix);
        insert_rank_sql_base = insert_rank_sql_base.replace("%suffix%", suffix);
        insert_rank_sql_base = insert_rank_sql_base.replace("%weight%", String.valueOf(weight));
        insert_rank_sql_base = insert_rank_sql_base.replace("%isdefault%", isDefault ? "1" : "0");
        insert_rank_sql_base = insert_rank_sql_base.replace("%permissions%", permissions);
        insert_rank_sql_base = insert_rank_sql_base.replace("%inheritances%", inheritances);

        // ErrorManager.logWarning(insert_rank_sql_base);

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(insert_rank_sql_base);
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error saving rank data for " + name + "!\n" + ex);
        }

    }

    @Override
    public void savePlayer(PRPlayer player) {
        String name = player.getName();
        String uuid = player.getUuid().toString();
        String ranks = String.join(",", player.getRankNames());
        String permissions = serializePermissions(player.getPermissions());

        String insert_player_sql_base = "INSERT INTO players (name, uuid, ranks, permissions) VALUES ('%name%', '%uuid%', '%ranks%', '%permissions%');";

        String select_player_sql = "SELECT * from players WHERE uuid='%uuid%';";
        select_player_sql = select_player_sql.replace("%uuid%", uuid);

        boolean playerExists = false;

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(select_player_sql);
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                playerExists = true;
            }
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error saving rank data for " + name + "!\n" + ex);
        }

        if (playerExists) {
            insert_player_sql_base = "UPDATE players SET name = '%name%', ranks = '%ranks%', permissions = '%permissions%' WHERE uuid == '%uuid%';";
        }

        insert_player_sql_base = insert_player_sql_base.replace("%name%", name);
        insert_player_sql_base = insert_player_sql_base.replace("%uuid%", uuid);
        insert_player_sql_base = insert_player_sql_base.replace("%ranks%", ranks);
        insert_player_sql_base = insert_player_sql_base.replace("%permissions%", permissions);

        // ErrorManager.logWarning(insert_player_sql_base);

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(insert_player_sql_base);
            stmt.close();
        } catch (SQLException ex) {
            ErrorManager.logError("[SQLite] Error saving player data for " + name + " (" + uuid + ")!\n" + ex);
        }
    }

    private String serializePermissions(Collection<PRPermission> permissions) {
        ArrayList<String> output = new ArrayList<>();

        for (PRPermission permission : permissions) {
            if (permission.isAllowed(null) && permission.getWorlds().size() == 0) {
                output.add(permission.getName());
            } else {
                String formattedPermission = permission.getName() + "[";

                ArrayList<String> worlds = new ArrayList<String>();
                for (String world : permission.getWorlds()) {
                    worlds.add(world);
                }

                if (!permission.isAllowed(null)) {
                    formattedPermission += "allowed:" + permission.isAllowed(null);
                }

                if (worlds.size() > 0) {
                    formattedPermission += (permission.isAllowed(null) ? ";" : "") + "worlds:"
                            + String.join("&", worlds);
                }

                formattedPermission += "]";
                output.add(formattedPermission);
            }
        }

        return String.join(",", output);
    }

    private Collection<PRPermission> deserializePermissions(String serializedPermissions) {
        Collection<PRPermission> output = new ArrayList<>();

        for (String serializedPermission : serializedPermissions.split(",")) {
            String permissionName = serializedPermission.contains("\\[") ? serializedPermission.split("\\[")[0]
                    : serializedPermission;
            PRPermission permission = new PRPermission(permissionName);

            if (serializedPermission.contains("[")) {
                String permissionArguments = serializedPermission.split("\\[")[1].replace("]", "");

                if (permissionArguments.contains(";")) {
                    String[] permissionArgumentsSplit = permissionArguments.split(";");

                    for (int i = 0; i < permissionArgumentsSplit.length; i++) {
                        String cmd = permissionArgumentsSplit[i].split(":")[0];
                        String arg = permissionArgumentsSplit[i].split(":")[1];

                        if (cmd.equalsIgnoreCase("allowed")) {
                            permission.setAllowed(arg.equalsIgnoreCase("true") || arg.equals("1"));
                        }

                        if (cmd.equalsIgnoreCase("worlds")) {
                            for (String worldName : arg.split("\\&")) {
                                permission.addWorld(worldName);
                            }
                        }

                    }
                } else {
                    if (permissionArguments.contains(":")) {
                        String cmd = permissionArguments.split(":")[0];
                        String arg = permissionArguments.split(":")[1];

                        if (cmd.equalsIgnoreCase("allowed")) {
                            permission.setAllowed(arg.equalsIgnoreCase("true") || arg.equals("1"));
                        }

                        if (cmd.equalsIgnoreCase("worlds")) {
                            for (String worldName : arg.split("\\&")) {
                                permission.addWorld(worldName);
                            }
                        }
                    }
                }
            }

            output.add(permission);
        }

        return output;
    }
}
