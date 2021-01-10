package de.daver.unihub.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiRegistrationHandler implements Listener {


    @Getter
    private final Map<String, Gui> guiMap;

    @Getter
    private final Map<UUID, Integer> playerPageMap;

    public GuiRegistrationHandler(Plugin plugin){
        guiMap = new HashMap<>();
        playerPageMap = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void register(Gui gui){
        guiMap.put(gui.getName(), gui);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        Gui gui = guiMap.get(inventory.getTitle());
        if(gui == null) return;
        if(gui instanceof Clickable){
            Clickable clickable = (Clickable) gui;
            ItemStack currItem = event.getCurrentItem();
            System.out.println(gui.getName());
            if(currItem != null){
                if(clickable.onClick(currItem, inventory)){
                    event.setCancelled(true);
                }
            }
            clickable.useEvent(event);
        }else{
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event){
        Gui gui = guiMap.get(event.getInventory().getTitle());
        if(gui == null) return;
        Player player = (Player) event.getPlayer();
        playerPageMap.remove(player.getUniqueId());
    }

}
