package io.github.niestrat99.keepinvindividual.configuration;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeepInvSQL {
    /*
    This class is for the SQL part of saving data.
    If MySQL is either disabled or can't connect then we make the plugin use the local file instead until there is a connection.
    */

    public static MysqlDataSource keepInvDataSource = new MysqlConnectionPoolDataSource();

    public static void initializeDataSource() throws SQLException {

        keepInvDataSource.setServerName(Config.config.getString("mysql.host")); // DB Host
        keepInvDataSource.setPortNumber(Config.config.getInt("mysql.port")); // Default port is 3306
        keepInvDataSource.setDatabaseName(Config.config.getString("mysql.database")); // DB Name
        keepInvDataSource.setUser(Config.config.getString("mysql.user"));
        keepInvDataSource.setPassword(Config.config.getString("mysql.password"));

        testConnection(keepInvDataSource);
    }

    private static void testConnection(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT some from stuff"
        )) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
    }

    public static void addUniqueID(Player player) {
        if (!CacheList.isInList(player)) {
            CacheList.addToList(player);
        }

        Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
            try (Connection conn = keepInvDataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO players(uuid) VALUES(?)"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void removeUniqueID(Player player) {
        CacheList.removeFromList(player);

        Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
            try (Connection conn = keepInvDataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM players WHERE uuid = ?;"
            )) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static boolean importFromLocalFile() {
        try (Connection conn = keepInvDataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO players(uuid) VALUES(?)"
        )) {
            for (String uniqueID : KeepInvLocal.keepInvList) {
                stmt.setString(1, uniqueID);
                stmt.addBatch();
                stmt.clearParameters();
            }
            stmt.executeBatch();

            KeepInvLocal.deleteFile();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkForTable() {
        try (Connection conn = keepInvDataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS players(uuid CHAR(36) NOT NULL)"
        )) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
