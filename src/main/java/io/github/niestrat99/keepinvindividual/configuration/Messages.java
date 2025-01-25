package io.github.niestrat99.keepinvindividual.configuration;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Messages {
    public static File messagesFile;
    public static FileConfiguration messages;

    public static void initMessagesFile() throws IOException {
        messagesFile = new File(KeepInvIndividual.get().getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            Logger.log(Level.WARNING, "messages.yml is missing, creating a new file!");
            boolean createSuccessful = messagesFile.createNewFile();
            if (!createSuccessful) {
                Logger.log(Level.SEVERE, "Could not create config file!");
                return;
            }
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        setMessages();
    }

    public static void setMessages() throws IOException {
        messages.addDefault("prefix", "&bK&7II &8>>");

        messages.addDefault("info.enabled", "&7Successfully enabled KeepInventory for &e{player}&7!");
        messages.addDefault("info.disabled", "&7Successfully disabled KeepInventory for &e{player}&7!");
        messages.addDefault("info.reload.process", "&7Reloading configurations...");
        messages.addDefault("info.reload.success", "&7Configurations were successfully reloaded!");
        messages.addDefault("info.on-join.enabled", "&7KeepInventory has been enabled for you by default!");
        messages.addDefault("info.on-join.disabled", "&7KeepInventory has been disabled for you by default!");
        messages.addDefault("info.on-join.blacklisted", "&7KeepInventory disabled, because you're in a blacklisted world!");

        messages.addDefault("error.no-permission", "&cYou do not have permission to use this command!");
        messages.addDefault("error.no-such-player", "&cPlayer &e{player} &cdoes not exist.");
        messages.addDefault("error.invalid-arguments", "&cInvalid arguments. Usage: &e/keepinventory <on/off/reload> (player)");
        messages.addDefault("error.not-enough-arguments", "&cNot enough arguments. Usage: &e/keepinventory <on/off/reload> (player)");
        messages.addDefault("error.already-enabled", "&cIt is already enabled for &e{player}&c.");
        messages.addDefault("error.already-disabled","&cIt is already disabled for &e{player}&c.");
        messages.addDefault("error.empty-list", "&cThe list is empty.");
        messages.addDefault("error.must-be-number", "&cThe argument must be a number!");
        messages.addDefault("error.page-out-of-bounds", "&cThe list only goes up to {number} pages.");
        save();
    }

    public static void save() throws IOException {
        messages.options().copyDefaults(true);
        messages.save(messagesFile);
    }

    public static void reload() throws IOException {
        initMessagesFile();
    }

    public static String getMsg(String msgPreset) {
        String output = messages.getString(msgPreset);
        if (output != null) {
            return output;
        } else {
            Logger.log(Level.SEVERE, "Something went wrong getting the message from messages.yml!");
            return msgPreset + ": null";
        }

    }
}
