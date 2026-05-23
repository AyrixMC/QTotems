package dev.parrotstudios.qtotems.listener;

import dev.parrotstudios.qtotems.totem.QTotemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event listener class for QTotems.
 * Intercepts user action events to apply or clear totem effects.
 */
public class EventListener implements Listener {

    /**
     * Intercepts totem resurrection events.
     * Applies the configured pop effects when a custom totem is popped.
     *
     * @param event The EntityResurrectEvent event.
     */
    @EventHandler
    public void onPop(EntityResurrectEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        if (event.getHand() == null) return;
        QTotemRegistry.handlePop(player, player.getInventory().getItem(event.getHand()));
    }

    /**
     * Listens for inventory slot clicks.
     * Triggers off-hand equipping/unequipping updates when a user manipulates the off-hand slot.
     *
     * @param event The InventoryClickEvent event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getSlot() != 40) return;
        if(event.getClick() == ClickType.SWAP_OFFHAND) return;
        ItemStack stack = event.getCursor();
        QTotemRegistry.handleEquip((Player) event.getWhoClicked(), stack);
    }

    /**
     * Listens for off-hand swap keyboard commands/clicks inside inventories.
     * Updates equipped totem status accordingly.
     *
     * @param event The InventoryClickEvent event.
     */
    @EventHandler
    public void onSwapInInventory(InventoryClickEvent event){
        if(event.getClick() !=  ClickType.SWAP_OFFHAND) return;
        if (event.getSlot() == 40) return;
        ItemStack stack = event.getCurrentItem();
        QTotemRegistry.handleEquip((Player) event.getWhoClicked(), stack);
    }

    /**
     * Listens for key swaps during active gameplay (default key 'F').
     * Evaluates the item moved into the offhand and updates totem effects.
     *
     * @param event The PlayerSwapHandItemsEvent event.
     */
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        ItemStack itemToOffhand = event.getOffHandItem();
        QTotemRegistry.handleEquip(event.getPlayer(), itemToOffhand);

    }

    /**
     * Cleans up player totem status and active effects upon disconnect.
     *
     * @param event The PlayerQuitEvent event.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        QTotemRegistry.handleLeave(event.getPlayer());
    }

    /**
     * Triggers upon player connection.
     * Instantly evaluates if they held a custom totem on logout, and re-applies effects.
     *
     * @param event The PlayerJoinEvent event.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        QTotemRegistry.handleJoin(event.getPlayer());
    }

    /**
     * Listens for active potion effects being cleared/removed.
     * Ensures passive totem equip effects are re-applied if holding a custom totem.
     *
     * @param event The EntityPotionEffectEvent event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEffectRemove(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getAction() != EntityPotionEffectEvent.Action.CLEARED && event.getAction() != EntityPotionEffectEvent.Action.REMOVED) return;
        QTotemRegistry.handleEffectChange(player);

    }

}
