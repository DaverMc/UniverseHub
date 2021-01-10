package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface RightClickEntityInteraction extends Interaction{

    void onRight(PlayerInteractEntityEvent event);

}
