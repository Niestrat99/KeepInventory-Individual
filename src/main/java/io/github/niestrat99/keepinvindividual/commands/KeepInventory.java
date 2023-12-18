package io.github.niestrat99.keepinvindividual.commands;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.functions.LocalFunctions;
import io.github.niestrat99.keepinvindividual.functions.MySQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KeepInventory implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("ki.admin.cmd")) {
                if (args.length > 0) {
                    switch(args[0]) {
                        case "on" -> {
                            if (args.length > 1) {
                                if (sender.hasPermission("ki.admin.cmd.other")) {
                                    Player target = Bukkit.getPlayerExact(args[1]);
                                    if (target != null) {
                                        if (KeepInvIndividual.mySqlEnabled) {
                                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.addPlayer(player, target));
                                        } else {
                                            LocalFunctions.addPlayer(player, target);
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-such-player"))));
                                        return false;
                                    }
                                } else {
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                                    return false;
                                }
                            } else {
                                if (KeepInvIndividual.mySqlEnabled) {
                                    Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.addPlayer(player, null));
                                } else {
                                    LocalFunctions.addPlayer(player, null);
                                    return true;
                                }
                            }
                        }
                        case "off" -> {
                            if (args.length > 1) {
                                if (sender.hasPermission("ki.admin.cmd.other")) {
                                    Player target = Bukkit.getPlayerExact(args[1]);
                                    if (target != null) {
                                        if (KeepInvIndividual.mySqlEnabled) {
                                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.removePlayer(player, target));
                                        } else {
                                            LocalFunctions.removePlayer(player, target);
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-such-player"))));
                                        return false;
                                    }
                                } else {
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                                    return false;
                                }
                            } else {
                                if (KeepInvIndividual.mySqlEnabled) {
                                    Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.removePlayer(player, null));
                                } else {
                                    LocalFunctions.removePlayer(player, null);
                                    return true;
                                }
                            }
                        }

                        case "reload" -> {
                            if (sender.hasPermission("ki.admin.cmd.reload")) {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.reload.process"))));
                                try {
                                    Config.reload();
                                    Messages.reload();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.reload.success"))));
                                return true;
                            } else {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                                return false;
                            }
                        }

                        default -> {sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.invalid-arguments")))); return false;}
                    }
                } else {
                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.not-enough-arguments"))));
                    return false;
                }

            } else {
                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                return false;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestion = new ArrayList<>();
        if (args.length == 1) {
            suggestion.add("on");
            suggestion.add("off");
            suggestion.add("reload");
        }
        Collections.sort(suggestion);
        return suggestion;
    }
}
