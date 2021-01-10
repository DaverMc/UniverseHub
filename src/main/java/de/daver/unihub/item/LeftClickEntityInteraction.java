package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface LeftClickEntityInteraction extends Interaction{

    void onLeft(PlayerInteractEntityEvent event);

}
