package de.blablubbabc.shopkeepersAPIMMOItems.itemUpdates;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.nisovin.shopkeepers.api.events.UpdateItemEvent;

public record ShopkeeperItemUpdateListener(ItemUpdater itemUpdater) implements Listener {

    @EventHandler
    public void onPluginEnabled(PluginEnableEvent event) {
        String name = event.getPlugin().getName();
        if (name.equalsIgnoreCase("Shopkeepers")
                || name.equalsIgnoreCase("MMOItems")) {
            itemUpdater.updateItemsDelayed();
        }
    }

    @EventHandler
    public void onUpdateItem(UpdateItemEvent event) {
        itemUpdater.handleItemUpdateEvent(event);
    }
}
