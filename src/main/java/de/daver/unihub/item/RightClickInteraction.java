package de.daver.unihub.item;

import org.bukkit.event.player.PlayerInteractEvent;

public interface RightClickInteraction extends Interaction{
    void onRightClick(PlayerInteractEvent event);
}
