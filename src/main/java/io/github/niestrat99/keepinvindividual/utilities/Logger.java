package io.github.niestrat99.keepinvindividual.utilities;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Logger {
    public static void log(Level level, String msg, Class<?> sourceClass, Exception stacktrace) {
        String message = msg;
        if (sourceClass != null) {
            message = message.concat("\n(" + sourceClass.getName() + ")");
        }
        if (stacktrace != null) {
            message = message.concat("\nStacktrace:\n" + stacktrace);
        }
        KeepInvIndividual.get().getLogger().log(level, message);
    }

    public static void log(Level level, String msg) {
        KeepInvIndividual.get().getLogger().log(level, msg);
    }

    public static void msg(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.getMsg("prefix") + " " + message));
    }
}
