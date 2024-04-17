package io.github.niestrat99.keepinvindividual.utilities;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CacheList {
    public static List<String> cacheList = new ArrayList<>();

    public static boolean isInList(Player player) {
        String uuid = player.getUniqueId().toString();
        KeepInvIndividual.get().getLogger().info(String.valueOf(cacheList.contains(uuid)));
        return cacheList.contains(uuid);
    }

    public static void addToList(Player player) {
        String uuid = player.getUniqueId().toString();
        DebugModule.info("Adding player " + player.getName() + " (" + uuid + ") to cache list.");

        if (isInList(player)) {return;}
        cacheList.add(uuid);
    }

    public static void removeFromList(Player player) {
        String uuid = player.getUniqueId().toString();

        if (!isInList(player)) {return;}
        DebugModule.info("Removing player " + player.getName() + " (" + uuid + ") from cache list.");
        cacheList.remove(uuid);
    }

    public static void buildCache() {
        DebugModule.info("Creating cache file.");
        if (KeepInvIndividual.mySqlEnabled) {
            DebugModule.info("MySQL is enabled, getting data for cache.");
            try (Connection conn = KeepInvSQL.keepInvDataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "SELECT uuid FROM players"
            )) {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    if (!cacheList.contains(resultSet.getString(1))) {
                        cacheList.add(resultSet.getString(1));
                    }
                }
                DebugModule.info("Caching successful!");
            } catch (SQLException e) {
                DebugModule.warn(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            DebugModule.info("MySQL is disabled, getting local data for cache file.");
            cacheList.addAll(KeepInvLocal.keepInv.getStringList("players"));
            DebugModule.info("Caching successful!");
        }
    }
}
