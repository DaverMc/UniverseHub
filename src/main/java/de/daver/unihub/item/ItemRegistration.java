package de.daver.unihub.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRegistration implements Listener {

    @Getter
    private final Map<String,ItemBuilder> interactionItems;

    public ItemRegistration(){
        this.interactionItems = new HashMap<>();
        this.init();
    }

    private void init(){
        for(int i = 0 ; i < 16; i++){
            interactionItems.put("BORDER_ITEM_" + i, new ItemBuilder(Material.STAINED_GLASS_PANE, " ", i));
        }
    }

    private ItemStack getItem(int color){
        return this.interactionItems.get(color).toStack();
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event){
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() == Material.AIR) return;

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)) return;
        if(((Player) event.getDamager()).getItemInHand() == null || ((Player) event.getDamager()).getItemInHand().getType() == Material.AIR) return;

    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event){
        if(event.getInventory() == null) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        Map<ItemBuilder, List<Interaction>> guiItems = getInteractives(GuiInteraction.class);
        for(ItemBuilder builder : guiItems.keySet()){
            if(!ItemBuilder.equals(builder, event.getCurrentItem())) continue;
            for(Interaction interaction : guiItems.get(builder)) {
                ((GuiInteraction) interaction).onGuiClick(event);
            }
        }
    }

    public Map<ItemBuilder, List<Interaction>> getInteractives(Class<? extends Interaction> clazz){
        Map<ItemBuilder, List<Interaction>> map = new HashMap<>();
        for(ItemBuilder builder : interactionItems.values()){
            List<Interaction> list = new ArrayList<>();
            for(Interaction interaction : builder.getInteractions()){
                if(interaction.getClass().equals(clazz)){
                    list.add(interaction);
                }
            }
            if(!list.isEmpty())map.put(builder, list);
        }
        return map;
    }

}
