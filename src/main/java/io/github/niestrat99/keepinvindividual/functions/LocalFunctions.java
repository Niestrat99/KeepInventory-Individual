package io.github.niestrat99.keepinvindividual.functions;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

public class LocalFunctions {
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
            DebugModule.info("Adding player to local list.");
            try {
                KeepInvLocal.addUniqueID(player);
                CacheList.addToList(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Logger.msg(player, Messages.getMsg("info.enabled").replace("{player}", player.getName()));
            DebugModule.info("Player has been added to the local list.");
        } else {
            Logger.msg(player, Messages.getMsg("error.already-enabled.self").replace("{player}", player.getName()));
            DebugModule.info("Player is already on local list.");
        }
    }

    private static void removePlayer(Player player) {
        DebugModule.info("Player info: " + player.getName() + " (" + player.getUniqueId() + ")");
        if (CacheList.isInList(player)) {
            DebugModule.info("Removing player from local list.");
            try {
                KeepInvLocal.removeUniqueID(player);
                CacheList.removeFromList(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Logger.msg(player, Messages.getMsg("info.disabled").replace("{player}", player.getName()));
            DebugModule.info("Player has been removed from the local list.");
        } else {
            Logger.msg(player, Messages.getMsg("error.already-disabled").replace("{player}", player.getName()));
            DebugModule.info("Player is not on the local list.");
        }
    }
}
