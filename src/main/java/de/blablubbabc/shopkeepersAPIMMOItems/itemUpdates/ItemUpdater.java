package de.blablubbabc.shopkeepersAPIMMOItems.itemUpdates;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.events.UpdateItemEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;

import de.blablubbabc.shopkeepersAPIMMOItems.ShopkeepersAPIMMOItems;
import org.bukkit.plugin.Plugin;

public class ItemUpdater {
    private final ShopkeepersAPIMMOItems plugin;
    private final ShopkeeperItemUpdateListener pluginListener;
    private final boolean debug;

    public ItemUpdater(ShopkeepersAPIMMOItems plugin) {
        this.plugin = plugin;
        this.pluginListener = new ShopkeeperItemUpdateListener(this);
        this.debug = plugin.getConfig().getBoolean("debug", false);
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(pluginListener, plugin);
        this.updateItemsDelayed();
    }

    public void onDisable() {
        HandlerList.unregisterAll(pluginListener);
    }

    public void updateItemsDelayed() {
        Bukkit.getScheduler().runTaskLater(plugin, this::updateItems, 60L);
    }

    public void updateItems() {
        if (!ShopkeepersAPI.isEnabled()) {
            return;
        }

        Plugin mmo = Bukkit.getPluginManager().getPlugin("MMOItems");
        if (!(mmo instanceof MMOItems) || !mmo.isEnabled()) {
            return;
        }

        plugin.getLogger().info("Shopkeepers Items (MMOItems) Update...");
        ShopkeepersAPI.updateItems();
    }

    public void handleItemUpdateEvent(UpdateItemEvent event) {
        UnmodifiableItemStack item = event.getItem();
        ItemStack currentItem = item.copy();

        io.lumine.mythic.lib.api.item.NBTItem nbtItem = io.lumine.mythic.lib.api.item.NBTItem.get(currentItem);

        if (!nbtItem.hasType() || !nbtItem.hasTag("MMOITEMS_ITEM_ID")) {
            return;
        }

        String mmoType = nbtItem.getType();
        String mmoId = nbtItem.getString("MMOITEMS_ITEM_ID");

        ItemStack expectedItem = this.getExpectedItemData(currentItem);

        if (expectedItem != null && !currentItem.isSimilar(expectedItem)) {
            expectedItem.setAmount(currentItem.getAmount());

            if (debug) {
                plugin.getLogger().info(String.format(
                        "Item updated: [%s] ID: %s",
                        mmoType,
                        mmoId
                ));
            }

            event.setItem(UnmodifiableItemStack.ofNonNull(expectedItem));
        }
    }

    private ItemStack getExpectedItemData(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return item;
        }

        NBTItem nbtItem = NBTItem.get(item);

        if (!nbtItem.hasType() || !nbtItem.hasTag("MMOITEMS_ITEM_ID")) {
            return item;
        }

        String typeStr = nbtItem.getType();
        String id = nbtItem.getString("MMOITEMS_ITEM_ID");

        Type type = MMOItems.plugin.getTypes().get(typeStr);
        if (type == null) {
            return item;
        }

        ItemStack newItem = MMOItems.plugin.getItem(type, id);

        if (newItem == null) {
            return item;
        }

        return newItem;
    }
}