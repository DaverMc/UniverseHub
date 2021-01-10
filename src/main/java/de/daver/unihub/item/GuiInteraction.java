package de.daver.unihub.item;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiInteraction extends Interaction {

    void onGuiClick(InventoryClickEvent event);

}
