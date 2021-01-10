package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEvent;

public interface ShiftLeftClickInteraction extends Interaction {

    void onShiftLeftClick(PlayerInteractEvent event);

}
