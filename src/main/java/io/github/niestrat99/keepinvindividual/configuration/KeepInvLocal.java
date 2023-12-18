package io.github.niestrat99.keepinvindividual.configuration;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class KeepInvLocal {
    /*
    Idea:
    - If there is no connection to a MySQL database then make a file with information instead.
    - If there is a connection then it shall take the local file's data and delete it once done.
    */

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
                KeepInvIndividual.get().getLogger().warning(KeepInvIndividual.plTitle + "Failed to create directory!");
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
        if (getUniqueID(player)) {
            keepInvList.remove(player.getUniqueId().toString());
            createFile();
        }
    }
}
