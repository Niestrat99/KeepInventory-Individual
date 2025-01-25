package io.github.niestrat99.keepinvindividual.configuration;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class KeepInvLocal {

    public static File keepInvFile;
    public static FileConfiguration keepInv;
    public static List<String> keepInvList;

    public static void initLocalFile() {
        keepInvFile = new File(KeepInvIndividual.get().getDataFolder(), "keepInvList.yml");
        keepInv = YamlConfiguration.loadConfiguration(keepInvFile);
        keepInvList = keepInv.getStringList("players");
    }

    public static void createFile() throws IOException {
        if (!KeepInvIndividual.get().getDataFolder().exists()) {
            if (!KeepInvIndividual.get().getDataFolder().mkdirs()) {
                Logger.log(Level.WARNING, "Failed to create directory!");
            }
        }
        if (!keepInvFile.exists()) {
            keepInv.set("players", keepInvList);
            keepInv.save(keepInvFile);
        }
    }
    public static boolean deleteFile() {
        if (keepInvFile.exists()) {
            return keepInvFile.delete();
        }
        return false;
    }

    public static boolean getUniqueID(Player player) {return keepInvList.contains(player.getUniqueId().toString());}
    public static void addUniqueID(Player player) throws IOException {
        if (!getUniqueID(player)) {
            keepInvList.add(player.getUniqueId().toString());
            createFile();
        }
    }

    public static void removeUniqueID(Player player) throws IOException {
        if (player != null && getUniqueID(player)) {
            keepInvList.remove(player.getUniqueId().toString());
            createFile();
        }
    }
}
