package io.github.niestrat99.keepinvindividual;

import io.github.niestrat99.keepinvindividual.commands.KeepInventory;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvLocal;
import io.github.niestrat99.keepinvindividual.configuration.KeepInvSQL;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.listeners.PlayerListener;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

public class KeepInvIndividual extends JavaPlugin {

    private static KeepInvIndividual instance;
    public static boolean mySqlEnabled;

    @Override
    public void onEnable() {
        instance = this;

        Logger.log(Level.INFO, "KeepInventory Individual is starting up.");

        // Commands
        Logger.log(Level.INFO, "Registering commands...");
        Objects.requireNonNull(getCommand("keepinventory")).setExecutor(new KeepInventory());
        Objects.requireNonNull(getCommand("keepinventory")).setTabCompleter(new KeepInventory());

        // Events
        Logger.log(Level.INFO, "Registering events...");
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Config
        KeepInvLocal.initLocalFile();

        try {
            Config.initConfigFile();
            Messages.initMessagesFile();
        } catch (IOException e) {
            Logger.log(Level.SEVERE, "Something went wrong initializing a configuration file!", this.getClass(), e);
        }

        mySqlEnabled = Config.config.getBoolean("mysql.enabled");

        Logger.log(Level.INFO, "Setting up config files...");
        try {
            Config.setDefaults();
            Messages.setMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // MySQL
        if (mySqlEnabled) {
            Logger.log(Level.INFO, "Setting up databases...");
            try {
                KeepInvSQL.initializeDataSource();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            KeepInvSQL.checkForTable();

            Logger.log(Level.INFO, "Database successfully loaded!");

            if (KeepInvLocal.keepInvFile.exists()) {
                Logger.log(Level.INFO, "Local data file found. Importing...");

                if (KeepInvSQL.importFromLocalFile()) {
                    Logger.log(Level.INFO, "Successfully imported local data into the MySQL Database!");
                } else {
                    Logger.log(Level.INFO, "Something went wrong when importing local data...");
                }
            }

        } else {
            Logger.log(Level.INFO, "MySQL disabled, moving on to local storage instead.");
        }

        // Cache Build
        Logger.log(Level.INFO, "Building cache");
        CacheList.buildCache();

        // When everything's done
        Logger.log(Level.INFO, "KeepInventory Individual finished setup!");
    }

    public static KeepInvIndividual get() {return instance;}
}
