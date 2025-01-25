package io.github.niestrat99.keepinvindividual.configuration;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Config {
    public static File configFile;
    public static FileConfiguration config;

    public static void initConfigFile() throws IOException {
        configFile = new File(KeepInvIndividual.get().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            Logger.log(Level.WARNING, "config.yml is missing, creating a new file!");
            boolean createSuccessful = configFile.createNewFile();
            if (!createSuccessful) {
                Logger.log(Level.SEVERE, "Could not create config file!");
                return;
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }

    public static void setDefaults() throws IOException {
        //Deathcause Blacklist
        config.options().setHeader(new ArrayList<>(Arrays.asList(
                "NOTE: When using the blacklist, please make sure to write the causes in caps like this:",
                "blacklist:",
                "- VOID",
                "- EXPLOSION",
                "----------------------------------------------------------------------------------------",
                "If you want to leave the list empty, please enter the list like this:",
                "blacklist: []",
                "----------------------------------------------------------------------------------------",
                "You can find a list of all damage causes here:",
                "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html",
                "----------------------------------------------------------------------------------------"
        )));
        //World Blacklist
        config.addDefault("world-blacklist.enabled", false);
        config.addDefault("world-blacklist.blacklist", new ArrayList<>());
        //DeathCause Blacklist
        config.addDefault("deathcause-blacklist.enabled", false);
        config.addDefault("deathcause-blacklist.blacklist", new ArrayList<>());
        //MySQL
        config.addDefault("mysql.enabled", false);
        config.addDefault("mysql.host", "HOST NAME");
        config.addDefault("mysql.port", "PORT");
        config.addDefault("mysql.database", "DATABASE NAME");
        config.addDefault("mysql.user", "USERNAME");
        config.addDefault("mysql.password", "PASSWORD");
        save();
    }

    public static void save() throws IOException {
        config.options().copyDefaults(true);
        config.save(configFile);

        DebugModule.info("World blacklist content:");
        for (String world : Config.config.getStringList("world-blacklist.blacklist")) {
            DebugModule.info("- " + world);
        }
        DebugModule.info("-----------------------");
        DebugModule.info("Death cause blacklist content:");
        for (String cause : config.getStringList("deathcause-blacklist.blacklist")) {
            DebugModule.info("- " + cause);
        }
    }

    public static void reload() throws IOException {
        initConfigFile();
    }
}
