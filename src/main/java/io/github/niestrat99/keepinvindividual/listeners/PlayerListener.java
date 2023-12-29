package io.github.niestrat99.keepinvindividual.listeners;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (KeepInvIndividual.mySqlEnabled) {
            if (CacheList.isInList(player)) {
                if (checkForConfigStuff(player)) {
                    if (player.hasPermission("ki.keepxp")) {
                        e.setKeepLevel(true);
                        e.setDroppedExp(0);
                    }
                    e.setKeepInventory(true);
                    e.getDrops().clear();
                }
            }
        } else {
            if (KeepInvLocal.getUniqueID(player)) {
                if (checkForConfigStuff(player)) {
                    if (player.hasPermission("ki.keepxp")) {
                        e.setKeepLevel(true);
                        e.setDroppedExp(0);
                    }
                    e.setKeepInventory(true);
                    e.getDrops().clear();
                }
            }
        }
    }

    // Functions
    public static boolean checkForConfigStuff(Player player) {
        if (Config.config.getBoolean("world-blacklist.blacklist.enabled") && Config.config.getStringList("world-blacklist.blacklist").contains(player.getWorld().getName())) {
            return false;
        }
        if (player.getLastDamageCause() != null && Config.config.getBoolean("deathcause-blacklist.enabled") && Config.config.getStringList("deathcause-blacklist.blackist").contains(player.getLastDamageCause().getCause().toString())) {
            return false;
        }
        return true;
    }
}
