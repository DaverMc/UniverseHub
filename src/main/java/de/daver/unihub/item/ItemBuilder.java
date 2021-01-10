package de.daver.unihub.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.daver.unihub.util.Reflections;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    @Getter
    private final List<Interaction> interactions;

    public ItemStack itemStack;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        interactions = new ArrayList<>();
    }

    public ItemBuilder(ItemStack stack) {
        this.itemStack = stack;
        interactions = new ArrayList<>();
    }

    public ItemBuilder(String jsonItem) {
        this.itemStack = ItemBuilder.deserialize(jsonItem);
        interactions = new ArrayList<>();
    }

    public ItemBuilder(Material material, String name) {
        this(material);
        this.setName(name);
    }

    public ItemBuilder(Material material, int durability) {
        this(material);
        this.itemStack.setDurability((byte) durability);
    }

    public ItemBuilder(Material material, String name, int durability) {
        this(material, name);
        this.itemStack.setDurability((byte) durability);
    }

    public ItemBuilder addInteraction(Interaction interaction){
        this.interactions.add(interaction);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(name);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setHeadTexture(String texture) {
        this.itemStack.setDurability((byte) 3);
        SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "BuildinGalaxy");
        Property property = new Property("textures", texture);
        profile.getProperties().put("textures", property);
        Reflections.setFieldValue(skullMeta, "profile", profile);
        this.itemStack.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setGlowing(boolean glowing) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(this.itemStack);
        NBTTagCompound nbtTagCompound = nmsStack.getTag();
        if (nbtTagCompound == null) nbtTagCompound = new NBTTagCompound();
        if (glowing) {
            NBTTagList nbtTagList = new NBTTagList();
            nbtTagCompound.set("ench", nbtTagList);
        } else {
            nbtTagCompound.remove("ench");
        }
        nmsStack.setTag(nbtTagCompound);
        this.itemStack = CraftItemStack.asCraftMirror(nmsStack);
        return this;
    }

    public ItemStack toStack() {
        return this.itemStack;
    }

    public String serialize() {
        String base64 = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(out);
            bukkitOut.writeObject(this.itemStack);
            bukkitOut.close();
            base64 = Base64Coder.encodeLines(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item-data", base64);
        return new Gson().toJson(jsonObject);
    }

    public static boolean equals(ItemBuilder builder, ItemStack comparable){
        return equals(builder.toStack(), comparable);
    }

    public static boolean equals(ItemStack stack, ItemStack comparable) {
        if (stack.getType() != comparable.getType()) return false;
        String name = stack.getItemMeta().getDisplayName();
        String comparableName = comparable.getItemMeta().getDisplayName();
        return name.equals(comparableName);
    }

    public static boolean isGlowing(ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tagCompound = nmsStack.getTag();
        if (tagCompound == null) return false;

        return tagCompound.get("ench") != null;
    }

    public static String serialize(ItemStack itemStack) {
        return new ItemBuilder(itemStack).serialize();
    }

    public static ItemStack deserialize(String jsonText) {
        JsonObject jsonObject = new Gson().fromJson(jsonText, JsonObject.class);
        ItemStack stack = null;
        String itemData = jsonObject.get("item-data").getAsString();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(itemData));
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(in);
            stack = (ItemStack) bukkitIn.readObject();
            bukkitIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stack;
    }




}
