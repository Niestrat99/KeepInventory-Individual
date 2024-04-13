package io.github.niestrat99.keepinvindividual.listeners;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        DebugModule.info(player.getName() + " (" + player.getUniqueId() + ") died.");
        if (KeepInvIndividual.mySqlEnabled) {
            if (CacheList.isInList(player)) {
                if (checkForConfigStuff(player)) {
                    handleDeath(player, e);
                }
            }
        } else {
            if (KeepInvLocal.getUniqueID(player)) {
                if (checkForConfigStuff(player)) {
                    handleDeath(player, e);
                }
            }
        }
    }

    // Functions
    public static boolean checkForConfigStuff(Player player) {
        if (Config.config.getBoolean("world-blacklist.blacklist.enabled") && Config.config.getStringList("world-blacklist.blacklist").contains(player.getWorld().getName())) {
            DebugModule.info("Player " + player.getName() + " (" + player.getUniqueId() + ") is in a blacklisted world (" + player.getWorld().getName() + ").");
            return false;
        }
        if (player.getLastDamageCause() != null && Config.config.getBoolean("deathcause-blacklist.enabled") && Config.config.getStringList("deathcause-blacklist.blackist").contains(player.getLastDamageCause().getCause().toString())) {
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
}
