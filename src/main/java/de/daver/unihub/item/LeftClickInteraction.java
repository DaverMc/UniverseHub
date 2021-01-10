package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEvent;

public interface LeftClickInteraction extends Interaction{

    void onLeftClick(PlayerInteractEvent event);
}
