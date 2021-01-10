package de.daver.unihub.util;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface PacketSender {

    void send(Player player);
    void broadcast();

    public static void sendPackets(Player player, Packet<?>...packets){
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        for(Packet<?> packet : packets){
            connection.sendPacket(packet);
        }
    }

    public static void broadcastPackets(Packet<?>...packets){
        for(Player on : Bukkit.getOnlinePlayers()){
            sendPackets(on, packets);
        }
    }
}
