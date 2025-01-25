package io.github.niestrat99.keepinvindividual.api;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class KeepInvAPI implements IKeepInvAPI {
    private final boolean sql = KeepInvIndividual.mySqlEnabled;

    public void enableKeepInventory(Player player) {
        if (sql) {
            CacheList.addToList(player);
        } else {
            try {
                KeepInvLocal.removeUniqueID(player);
            } catch (Exception e) {
                Logger.log(Level.SEVERE, "Something went wrong adding player to local list!", KeepInvAPI.class, e);
            }
        }
    }

    public void disableKeepInventory(Player player) {
        if (sql) {
            CacheList.removeFromList(player);
        } else {
            try {
                KeepInvLocal.removeUniqueID(player);
            } catch (Exception e) {
                Logger.log(Level.SEVERE, "Something went wrong removing player from local list!", KeepInvAPI.class, e);
            }
        }
    }
}
