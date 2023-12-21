package io.github.niestrat99.keepinvindividual.configuration;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static File configFile;
    public static FileConfiguration config;

    public static List<String> deathCauses = new ArrayList<>();
    public static List<String> worldblacklist = new ArrayList<>();

    public static void initConfigFile() {
        configFile = new File(KeepInvIndividual.get().getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
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
        config.addDefault("world-blacklist.blackist", new ArrayList<>());
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
        deathCauses.addAll(config.getStringList("deathcause-blacklist.blacklist"));
        worldblacklist.addAll(config.getStringList("world-blacklist.blacklist"));
        config.save(configFile);
    }

    public static void reload() throws IOException {
        initConfigFile();
        save();
    }
}
