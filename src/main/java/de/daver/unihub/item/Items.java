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

public class Items implements Listener {

    @Getter
    private final Map<Integer,ItemBuilder> interactionItems;

    public Items(){
        this.interactionItems = new HashMap<>();
    }

    private void init(){
        for(int i = 0 ; i < 16; i++){
            interactionItems.put(i, new ItemBuilder(Material.STAINED_GLASS_PANE, " ", i));
        }
        interactionItems.put(16, new ItemBuilder(Material.SKULL_ITEM, "§6\u21E8 Page", 3)
                .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0="));
        interactionItems.put(17, new ItemBuilder(Material.SKULL_ITEM, "§6Page \u21E6", 3)
                .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19"));
        interactionItems.put(18, new ItemBuilder(Material.BARRIER, "§cBack"));
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
