package io.github.niestrat99.keepinvindividual.commands;

import io.github.niestrat99.keepinvindividual.KeepInvIndividual;
import io.github.niestrat99.keepinvindividual.configuration.Config;
import io.github.niestrat99.keepinvindividual.configuration.Messages;
import io.github.niestrat99.keepinvindividual.functions.HelpCommandFunction;
import io.github.niestrat99.keepinvindividual.functions.LocalFunctions;
import io.github.niestrat99.keepinvindividual.functions.MySQLFunctions;
import io.github.niestrat99.keepinvindividual.utilities.CacheList;
import io.github.niestrat99.keepinvindividual.utilities.DebugModule;
import io.github.niestrat99.keepinvindividual.utilities.Logger;
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
            DebugModule.info("Player " + sender.getName() + " (" + ((Player) sender).getUniqueId() + ") executed command.");
            if (args.length > 0) {
                switch (args[0]) {
                    case "on" -> {
                        DebugModule.info("Player called subcommand 'on'.");
                        if (!checkPermission(sender, args[0])) { return false; }
                        if (args.length > 1) {
                            if (!sender.hasPermission("ki.admin.cmd.other")) {
                                Logger.msg(player, Messages.getMsg("error.no-permission"));
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
                                Logger.msg(player, Messages.getMsg("error.no-such-player").replace("{player}", args[1]));
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
                            DebugModule.info("Player called subcommand 'off'.");
                            if (!checkPermission(sender, args[0])) { return false; }
                            Player target = Bukkit.getPlayerExact(args[1]);
                            if (target != null) {
                                if (KeepInvIndividual.mySqlEnabled) {
                                    Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> MySQLFunctions.removePlayer(player, target));
                                } else {
                                    LocalFunctions.removePlayer(player, target);
                                    return true;
                                }
                            } else {
                                Logger.msg(player, Messages.getMsg("error.no-such-player").replace("{player}", args[1]));
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
                        DebugModule.info("Player called subcommand 'reload'.");
                        if (!checkPermission(sender, args[0])) { return false; }
                        Logger.msg(player, Messages.getMsg("info.reload.process"));
                        try {
                            DebugModule.info("Reloading config.yml.");
                            Config.reload();
                            DebugModule.info("Reloading messages.yml.");
                            Messages.reload();
                        } catch (IOException e) {
                            DebugModule.warn(e.getMessage());
                            throw new RuntimeException(e);
                        }
                        DebugModule.info("Reload finished!");
                        Logger.msg(player, Messages.getMsg("info.reload.success"));
                        return true;

                    }

                    case "list" -> {
                        DebugModule.info("Player called subcommand 'list'.");
                        if (!checkPermission(sender, args[0])) { return false; }
                        DebugModule.info("Gathering data from cache list.");
                        PagedLists<String> cachedList = new PagedLists<>(CacheList.cacheList, 8);
                        if (cachedList.getTotalContents() < 1) {
                            Logger.msg(player, Messages.getMsg("error.empty-list"));
                            DebugModule.info("Cache list is empty.");
                            return false;
                        }
                        if (args.length > 1) {
                            if (!args[1].matches("^[\\d]$")) {
                                Logger.msg(player, Messages.getMsg("error.must-be-number"));
                                return false;
                            }
                            if (cachedList.getTotalPages() < Integer.parseInt(args[1])) {
                                Logger.msg(player, Messages.getMsg("error.page-out-of-bounds").replace("{number}", String.valueOf(cachedList.getTotalPages())));
                                return false;
                            }
                            DebugModule.info("Sending list to player.");
                            Logger.msg(player, "&7Page &6" + cachedList.getCurrentPage() + "&7 / &6" + cachedList.getTotalPages());
                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
                                for (String uuid : cachedList.getContentsInPage(Integer.parseInt(args[1]))) {
                                    String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                                    Logger.msg(player, "&7> &6" + name);
                                }

                                if (cachedList.getTotalPages() > 1) {
                                    Logger.msg(player, "&7Type &6/keepinventory list (page) &7to get to view another page.");
                                }
                            });
                        } else {
                            Logger.msg(player, "&7Page &6" + cachedList.getCurrentPage() + "&7 / &6" + cachedList.getTotalPages());
                            Bukkit.getScheduler().runTaskAsynchronously(KeepInvIndividual.get(), () -> {
                                for (String uuid : cachedList.getContentsInPage(1)) {
                                    String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                                    Logger.msg(player, "&7> &6" + name);
                                }

                                if (cachedList.getTotalPages() > 1) {
                                    Logger.msg(player, "&7Type &6/keepinventory list (page) &7to get to view another page.");
                                }
                            });
                        }

                    }

                    case "help" -> {
                        DebugModule.info("Player called subcommand 'help'.");
                        if (!checkPermission(sender, args[0])) { return false; }
                        HelpCommandFunction.sendHelp(player);
                    }

                    default -> {
                        Logger.msg(player, Messages.getMsg("error.invalid-arguments"));
                        return false;
                    }
                }
            } else {
                Logger.msg(player, Messages.getMsg("error.not-enough-arguments"));
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

    private static boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission("ki.admin.cmd." + permission)) {
            DebugModule.info("Player has no permisson to use this command.");
            Logger.msg((Player) sender, Messages.getMsg("error.no-permission"));
            return false;
        }
        return true;
    }
}
