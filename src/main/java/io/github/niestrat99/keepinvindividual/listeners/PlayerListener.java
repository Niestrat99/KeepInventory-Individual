package io.github.niestrat99.keepinvindividual.listeners;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (KeepInvIndividual.mySqlEnabled) {
            if (KeepInvSQL.isInList(player)) {
                if (Config.config.getBoolean("deathcause-blacklist.enabled")) {
                    if (player.getLastDamageCause() != null) {
                        if (!Config.config.getStringList("deathcause-blacklist.blacklist").contains(Objects.requireNonNull(player.getLastDamageCause()).getCause().toString())) {
                            e.setKeepInventory(true);
                            e.getDrops().clear();
                        } else {
                            e.setKeepInventory(false);
                        }
                    }
                } else {
                    e.setKeepInventory(true);
                    e.getDrops().clear();
                }
            }
        } else {
            if (KeepInvLocal.getUniqueID(player)) {
                if (Config.config.getBoolean("deathcause-blacklist.enabled")) {
                    if (player.getLastDamageCause() != null) {
                        if (!Config.config.getStringList("deathcause-blacklist.blacklist").contains(Objects.requireNonNull(player.getLastDamageCause()).getCause().toString())) {
                            e.setKeepInventory(true);
                            e.getDrops().clear();
                        }
                    }
                } else {
                    e.setKeepInventory(true);
                    e.getDrops().clear();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> KeepInvSQL.storeUniqueID(e.getPlayer()));
    }
}
