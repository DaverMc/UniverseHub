package de.daver.unihub.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Clickable {

    void useEvent(InventoryClickEvent event);

    boolean onClick(ItemStack currItem, Inventory inventory);

}
