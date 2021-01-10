package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface ShiftLeftClickEntityInteraction extends Interaction {

    void onLeft(PlayerInteractEntityEvent event);

}
