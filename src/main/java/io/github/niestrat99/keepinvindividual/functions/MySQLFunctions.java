package io.github.niestrat99.keepinvindividual.functions;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MySQLFunctions {
    public static void addPlayer(Player player, Player target) {
        if (target == null) {
            if (KeepInvSQL.storeUniqueID(player) == null) {
                KeepInvSQL.addUniqueID(player);
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.enabled.self"))));
            } else {
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.already-enabled.self"))));
            }
        } else {
            if (KeepInvSQL.storeUniqueID(target) == null) {
                KeepInvSQL.addUniqueID(target);
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.enabled.other")).replace("{player}", target.getName())));
            } else {
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Objects.requireNonNull(Messages.messages.getString("error.already-enabled.other")).replace("{player}", target.getName()))));
            }
        }
    }

    public static void removePlayer(Player player, Player target) {
        if (target == null) {
            if (KeepInvSQL.storeUniqueID(player) != null) {
                KeepInvSQL.removeUniqueID(player);
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.disabled.self"))));
            } else {
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.already-disabled.self"))));
            }
        } else {
            if (KeepInvSQL.storeUniqueID(target) != null) {
                KeepInvSQL.removeUniqueID(target);
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.disabled.other")).replace("{player}", target.getName())));
            } else {
                player.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Objects.requireNonNull(Messages.messages.getString("error.already-disabled.other")).replace("{player}", target.getName()))));
            }
        }
    }
}
