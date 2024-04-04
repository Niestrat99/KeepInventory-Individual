package io.github.niestrat99.keepinvindividual.functions;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MySQLFunctions {
    public static void addPlayer(Player player, Player target) {
        if (target == null) {
            addPlayer(player);
        } else {
            addPlayer(target);
        }
    }

    public static void removePlayer(Player player, Player target) {
        if (target == null) {
            removePlayer(player);
        } else {
            removePlayer(target);
        }
    }

    private static void addPlayer(Player player) {
        DebugModule.info("Player info: " + player.getName() + " (" + player.getUniqueId() + ")");
        if (!CacheList.isInList(player)) {
            DebugModule.info("Adding player to SQl Database.");
            KeepInvSQL.addUniqueID(player);
            player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.enabled.self"))));
            DebugModule.info("Player has been added to the SQL Database.");
        } else {
            player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.already-enabled.self"))));
            DebugModule.info("Player is already in the SQL Database.");
        }
    }

    private static void removePlayer(Player player) {
        DebugModule.info("Player info: " + player.getName() + " (" + player.getUniqueId() + ")");
        if (CacheList.isInList(player)) {
            DebugModule.info("Removing player from the SQL Database.");
            KeepInvSQL.removeUniqueID(player);
            player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.disabled.self"))));
        } else {
            player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.already-disabled.self"))));
        }
    }
}
