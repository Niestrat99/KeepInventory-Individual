package io.github.niestrat99.keepinvindividual.listeners;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.Objects;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        DebugModule.info(
                player.getName() + " (" + player.getUniqueId() + ") died."
                        + "\nCause of death: " + Objects.requireNonNull(player.getLastDamageCause()).getCause().toString()
                        + "\nDied in world: " + Objects.requireNonNull(player.getWorld().getName())
        );

        if (playerIsInList(player) && (checkForConfigStuff(player))) {
            handleDeath(player, e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();
        if (player.hasPermission("ki.onjoin.on")) {
            DebugModule.info("Player " + player.getName() + " has permission to have KeepInv enabled by default.");
            if (!checkForConfigStuff(player) && playerIsInList(player)) {
                DebugModule.info("Player is in a blacklisted world. Aborting task.");
                if (KeepInvIndividual.mySqlEnabled) {
                    CacheList.removeFromList(player);
                } else {
                    KeepInvLocal.removeUniqueID(player);
                }
                if (Config.config.getBoolean("debug.send-on-join-notification")) {
                    Logger.msg(player, Messages.getMsg("info.on-join.blacklisted"));
                }
                return;
            }
            if (!playerIsInList(player)) {
                if (KeepInvIndividual.mySqlEnabled) {
                    CacheList.addToList(player);
                } else {
                    KeepInvLocal.addUniqueID(player);
                }
                if (Config.config.getBoolean("debug.send-on-join-notification")) {
                    Logger.msg(player, Messages.getMsg("info.on-join.enabled"));
                }
            }

        } else if (player.hasPermission("ki.onjoin.off")) {
            if (playerIsInList(player)) {
                DebugModule.info("Player " + player.getName() + " has permission to have KeepInv disabled by default.");
                if (KeepInvIndividual.mySqlEnabled) {
                    CacheList.removeFromList(player);
                } else {
                    KeepInvLocal.removeUniqueID(player);
                }
                DebugModule.info("Disabled KeepInventory for player " + player.getName() + ".");
                if (Config.config.getBoolean("debug.send-on-join-notification")) {
                    Logger.msg(player, Messages.getMsg("info.on-join.disabled"));
                }
            }
        }

    }

    // Functions
    public static boolean checkForConfigStuff(Player player) {
        if (Config.config.getBoolean("world-blacklist.enabled")
                && Config.config.getStringList("world-blacklist.blacklist").contains(player.getWorld().getName())) {
            DebugModule.info("Player " + player.getName() + " (" + player.getUniqueId() + ") is in a blacklisted world (" + player.getWorld().getName() + ").");
            return false;
        }
        if (player.getLastDamageCause() != null
                && Config.config.getBoolean("deathcause-blacklist.enabled")
                && Config.config.getStringList("deathcause-blacklist.blacklist").contains(player.getLastDamageCause().getCause().toString())) {
            DebugModule.info("Player " + player.getName() + " (" + player.getUniqueId() + ") died from a blacklisted deathcause (" + player.getLastDamageCause().getCause().toString() + ").");
            return false;
        }
        DebugModule.info("No blacklisted world or deathcause detected, moving on.");
        return true;
    }

    public static void handleDeath(Player player, PlayerDeathEvent e) {
        if (player.hasPermission("ki.keepxp")) {
            DebugModule.info("Player has permission to keep their XP-Level.");
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        }
        DebugModule.info("Player does not have permission to keep their XP-Level.");
        e.setKeepInventory(true);
        e.getDrops().clear();
    }

    public boolean playerIsInList(Player player) {
        if (KeepInvIndividual.mySqlEnabled) {
            return CacheList.isInList(player);
        } else {
            return KeepInvLocal.getUniqueID(player);
        }
    }
}
