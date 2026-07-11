package io.github.niestrat99.keepinvindividual.commands.subcommands;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.functions.LocalFunctions;
import io.github.niestrat99.keepinvindividual.functions.MySQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KeepInventoryToggle {
    // Subcommand function for the KeepInventory toggle.

    public static void toggleKeepInventory(Boolean toggle, Player player, Player target) {
        if (player == null || toggle == null || target == null) {return;} // Checking requirements.
        if (toggle) {
            if (KeepInvIndividual.mySqlEnabled) {
                Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.addPlayer(player, target));
            } else {
                LocalFunctions.addPlayer(player, target);
            }
        } else {
            if (KeepInvIndividual.mySqlEnabled) {
                Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.removePlayer(player, target));
            } else {
                LocalFunctions.removePlayer(player, target);
            }
        }
    }
}
