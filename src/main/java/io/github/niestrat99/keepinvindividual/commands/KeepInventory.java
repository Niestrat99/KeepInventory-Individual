package io.github.niestrat99.keepinvindividual.commands;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.functions.HelpCommandFunction;
import io.github.niestrat99.keepinvindividual.functions.LocalFunctions;
import io.github.niestrat99.keepinvindividual.functions.MySQLFunctions;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.PagedLists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class KeepInventory implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!sender.hasPermission("ki.admin.cmd")) {
                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                return false;
            }
            if (args.length > 0) {
                switch (args[0]) {
                    case "on" -> {
                        if (args.length > 1) {
                            if (!sender.hasPermission("ki.admin.cmd.other")) {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                                return false;
                            }
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
                            if (!sender.hasPermission("ki.admin.cmd.other")) {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                                return false;
                            }
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
                            if (KeepInvIndividual.mySqlEnabled) {
                                Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.removePlayer(player, null));
                            } else {
                                LocalFunctions.removePlayer(player, null);
                                return true;
                            }
                        }
                    }

                    case "reload" -> {
                        if (!sender.hasPermission("ki.admin.cmd.reload")) {
                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                            return false;
                        }
                        sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.reload.process"))));
                        try {
                            Config.reload();
                            Messages.reload();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("info.reload.success"))));
                        return true;

                    }

                    case "list" -> {
                        if (!sender.hasPermission("ki.admin.cmd.list")) {
                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                            return false;
                        }
                        PagedLists<String> cachedList = new PagedLists<>(CacheList.cacheList, 8);
                        if (cachedList.getTotalContents() < 1) {
                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.empty-list"))));
                            return false;
                        }
                        if (args.length > 1) {
                            if (!args[1].matches("^[\\d]$")) {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.must-be-number"))));
                                return false;
                            }
                            if (cachedList.getTotalPages() < Integer.parseInt(args[1])) {
                                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.page-out-of-bounds")).replace("{number}", String.valueOf(cachedList.getTotalPages()))));
                                return false;
                            }

                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7Page &6" + cachedList.getCurrentPage() + "&7 / &6" + cachedList.getTotalPages()));
                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
                                for (String uuid : cachedList.getContentsInPage(Integer.parseInt(args[1]))) {
                                    String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7> &6" + name));
                                }

                                if (cachedList.getTotalPages() > 1) {
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7Type &6/keepinventory list (page) &7to get to view another page."));
                                }
                            });
                        } else {
                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7Page &6" + cachedList.getCurrentPage() + "&7 / &6" + cachedList.getTotalPages()));
                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
                                for (String uuid : cachedList.getContentsInPage(1)) {
                                    String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7> &6" + name));
                                }

                                if (cachedList.getTotalPages() > 1) {
                                    sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', "&7Type &6/keepinventory list (page) &7to get to view another page."));
                                }
                            });
                        }

                    }

                    case "help" -> {
                        if (!sender.hasPermission("ki.admin.cmd.help")) {
                            sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.no-permission"))));
                            return false;
                        }
                        HelpCommandFunction.sendHelp(player);
                    }

                    default -> {
                        sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.invalid-arguments"))));
                        return false;
                    }
                }
            } else {
                sender.sendMessage(KeepInvIndividual.plTitle + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Messages.messages.getString("error.not-enough-arguments"))));
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
            suggestion.add("list");
            suggestion.add("help");
        }
        Collections.sort(suggestion);
        return suggestion;
    }
}
