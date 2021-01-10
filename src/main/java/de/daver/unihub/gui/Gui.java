package de.daver.unihub.gui;

import de.daver.unihub.UniverseException;
import de.daver.unihub.util.PacketSender;
import de.daver.unihub.util.Reflections;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Gui {


    private String name;
    protected int rows;
    protected ItemStack[] contents;
    protected boolean bordered;

    public abstract void onOpen(Player player, Inventory inventory);

    public Gui(String name, int rows, ItemStack borderItem){
        this.name = name;
        this.rows = rows;
        this.contents = new ItemStack[9 * rows];
        if(borderItem != null) {
            createBorder(borderItem);
            bordered = true;
        }
    }

    protected void createBorder(ItemStack borderItem){
        for(int i = 0; i < 9; i++){
            contents[i] = borderItem;
            contents[contents.length - i - 1] = borderItem;
        }
        for(int i = 1; i < rows - 1; i++){
            contents[i * 9] = borderItem;
            contents[i * 9 + 8] = borderItem;
        }
    }

    public void fill(ItemStack stack){
        for(int i = 0; i < contents.length; i++){
            if(contents[i] == null){
                contents[i] = stack;
            }
        }
    }

    public void setItem(int index, ItemStack stack){
        if(bordered){
            contents[10 + index + (index / 7) * 2] = stack;
        }else{
            contents[index] = stack;
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void open(Player player){
        Inventory inv = Bukkit.createInventory(player, rows * 9, this.name);
        inv.setContents(this.contents);
        onOpen(player,inv);
        player.openInventory(inv);
    }

    public void setRawItem(int index, ItemStack stack){
        contents[index] = stack;
    }

    public String getName(){
        return this.name;
    }

    public void updateContents(Inventory inventory){
        inventory.setContents(contents);
    }

    public void open(Inventory inventory){
        if(inventory.getSize() != this.contents.length){
            try {
                throw new UniverseException("Failed Changing GUI: Incompatible GUI-Sizes!");
            }catch (UniverseException e){
                e.printStackTrace();
                return;
            }
        }

        Player player = (Player) inventory.getHolder();
        Container container = ((CraftPlayer) player).getHandle().activeContainer;
        int windowId = container.windowId;

        IInventory nmsInventory = ((CraftInventory) inventory).getInventory();
        Reflections.setFieldValue(nmsInventory, "title", this.name);
        inventory.setContents(this.contents);

        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, "minecraft:chest", CraftChatMessage.fromString(this.name)[0], this.contents.length);
        PacketSender.sendPackets((Player) inventory.getHolder(), packet);
        onOpen(player,inventory);
    }

}
