package io.github.niestrat99.keepinvindividual;

import io.github.niestrat99.keepinvindividual.commands.KeepInventory;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.listeners.PlayerListener;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

public class KeepInvIndividual extends JavaPlugin {

    private static KeepInvIndividual instance;
    public static String plTitle = ChatColor.translateAlternateColorCodes('&', "&6[&bK&7II&6]&r ");

    public static boolean mySqlEnabled;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info(plTitle + "KeepInventoryIndividuals is startin' up!");

        // Commands
        getLogger().info(plTitle + "Registering commands...");
        Objects.requireNonNull(getCommand("keepinventory")).setExecutor(new KeepInventory());
        Objects.requireNonNull(getCommand("keepinventory")).setTabCompleter(new KeepInventory());

        // Events
        getLogger().info(plTitle + "Registering events...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Config
        KeepInvLocal.initLocalFile();
        Config.initConfigFile();
        Messages.initMessagesFile();

        mySqlEnabled = Config.config.getBoolean("mysql.enabled");

        getLogger().info(plTitle + "Setting up config files...");
        try {
            Config.setDefaults();
            Messages.setMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // MySQL
        if (mySqlEnabled) {
            getLogger().info(plTitle + "Loading up databases...");
            try {
                KeepInvSQL.initializeDataSource();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            KeepInvSQL.checkForTable();

            getLogger().info(plTitle + "Database successfully loaded!");

            if (KeepInvLocal.keepInvFile.exists()) {
                getLogger().info(plTitle + "Local data file found. Importing...");

                if (KeepInvSQL.importFromLocalFile()) {
                    getLogger().info(plTitle + "Successfully imported local data into the MySQL Database!");
                } else {
                    getLogger().warning(plTitle + "Something went wrong when importing local data...");
                }
            }

        } else {
            getLogger().info(plTitle + "MySQL disabled, moving on to local storage instead.");
        }

        // Cache Build
        getLogger().info(plTitle + "Building cache");
        CacheList.buildCache();

        // When everything's done
        getLogger().info(plTitle + "And we're up!");
    }

    public static KeepInvIndividual get() {return instance;}

    public static void log(Level level, String msg, Class<?> sourceClass, Exception stacktrace) {
        String message = msg;
        if (sourceClass != null) {
            message = message.concat("\n(" + sourceClass.getName() + ")");
        }
        if (stacktrace != null) {
            message = message.concat("\nStacktrace:\n" + stacktrace);
        }
        instance.getLogger().log(level, message);
    }
}
