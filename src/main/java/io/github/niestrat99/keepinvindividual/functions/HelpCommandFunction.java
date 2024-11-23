package io.github.niestrat99.keepinvindividual.functions;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommandFunction {
    public static void sendHelp(Player sender) {
        for (String message : commandList) {
            sender.sendMessage(message);
        }
    }

    private static List<String> commandList = new ArrayList<>(Arrays.asList(
             msg("&7--{&6KeepInventory Individual Commands&7}--"),
            cmd("/keepinventory help"           , "Shows this list of commands."),
            cmd("/keepinventory on (player)"    , "Enables KeepInventory for you or the specified player."),
            cmd("/keepinventory off (player)"   , "Disables KeepInventory for you or the specified player."),
            cmd("/keepinventory list"           , "Shows a list of players who currently got KeepInventory enabled."),
            cmd("/keepinventory reload"         , "Reloads the config files and applies changes to the plugin.")
    ));

    private static String msg (String message) {
        return KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', message);
    }
    private static String cmd (String command, String description) {
        return KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7> &6" + command + " &7- &6"  + description);
    }
}
