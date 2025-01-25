package io.github.niestrat99.keepinvindividual.functions;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommandFunction {
    public static void sendHelp(Player sender) {
        Logger.msg(sender, "&7--{&6KeepInventory Individual Commands&7}--");
        for (String message : commandList) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            sender.sendMessage(" ");
        }
    }

    private static final List<String> commandList = new ArrayList<>(Arrays.asList(
            cmd("/keepinventory help"           , "Shows this list of commands."),
            cmd("/keepinventory on (player)"    , "Enables KeepInventory for you or the specified player."),
            cmd("/keepinventory off (player)"   , "Disables KeepInventory for you or the specified player."),
            cmd("/keepinventory list"           , "Shows a list of players who currently got KeepInventory enabled."),
            cmd("/keepinventory reload"         , "Reloads the config files and applies changes to the plugin.")
    ));

    private static String cmd (String command, String description) {
        return "&8> &6" + command + " &7- &6"  + description;
    }
}
