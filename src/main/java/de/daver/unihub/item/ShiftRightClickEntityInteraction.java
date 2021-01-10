package de.daver.unihub.item;

import de.daver.unihub.item.Interaction;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface ShiftRightClickEntityInteraction extends Interaction {

    public void onShiftRight(PlayerInteractEntityEvent event);

}
