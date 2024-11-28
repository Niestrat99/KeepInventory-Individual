package io.github.niestrat99.keepinvindividual.api;

import org.bukkit.entity.Player;

public interface IKeepInvAPI {
    /**
     * Enables KeepInventory for the specified player.
     * @param player The player who shall have KeepInventory enabled.
     */
    void enableKeepInventory(Player player);

    /**
     * Disables KeepInventory for the specified player.
     * @param player The player who shall have KeepInventory disabled.
     */
    void disableKeepInventory(Player player);
}
