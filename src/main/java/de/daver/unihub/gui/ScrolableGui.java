package de.daver.unihub.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class ScrolableGui extends Gui implements Clickable{


    private List<ItemStack> items;
    @Getter
    private final static Map<UUID, Integer> playerPage = new HashMap<>();

    public ScrolableGui(String name, int rows, ItemStack borderItem) {
        super(name, rows, borderItem);
        items = new ArrayList<>();
        if(!super.bordered) super.createBorder(borderItem);
    }

    public void addItem(ItemStack itemStack){
        items.add(itemStack);
    }

    private void changePage(int newPage, Inventory inventory){
        List<ItemStack> tmp = (items.size() >= (newPage + 1) * 27)? items.subList(newPage * 27, (newPage + 1) * 27) : items.subList(newPage * 27, items.size() - 1);
        for(int i = 0; i < tmp.size(); i++){
            setItem(i, tmp.get(i));
        };
        updateContents(inventory);
    }

    public void open(Player player){
        playerPage.put(player.getUniqueId(), 0);
        super.open(player);
    }

    public void setCursor(ItemStack right, ItemStack left, ItemStack back){
        super.contents[rows * 9 - 4] = back;
        super.contents[rows * 9 - 5] = left;
        super.contents[rows * 9 - 6] = right;
    }

    @Override
    public boolean onClick(ItemStack currItem, Inventory inventory) {
        int oldPage = 0;
        if(currItem == super.contents[rows * 9 - 5]){
            changePage(oldPage - 1, inventory);
            return true;
        }
        if(currItem == super.contents[rows * 9 - 6]){
            changePage(oldPage + 1, inventory);
            return true;
        }
        if(currItem == super.contents[rows * 9 - 4]){
            goBack();
            return true;
        }
        return onItemClick(currItem);
    }

    public abstract boolean onItemClick(ItemStack currItem);

    public abstract Gui goBack();

}
