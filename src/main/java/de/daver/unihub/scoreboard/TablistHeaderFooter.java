package de.daver.unihub.scoreboard;

import de.daver.unihub.json.JsonMessages;
import de.daver.unihub.util.PacketSender;
import de.daver.unihub.util.Reflections;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TablistHeaderFooter implements PacketSender {

    private final PacketPlayOutPlayerListHeaderFooter packet;

    public TablistHeaderFooter(){
        this.packet = new PacketPlayOutPlayerListHeaderFooter();
    }

    public void setHeader(String...header){
        Reflections.setFieldValue(this.packet, "a", JsonMessages.createText(header));
    }

    public void setFooter(String...footer){
        Reflections.setFieldValue(this.packet, "b", JsonMessages.createText(footer));
    }

    @Override
    public void send(Player player) {
        PacketSender.sendPackets(player, packet);
    }

    @Override
    public void broadcast() {
        for(Player player : Bukkit.getOnlinePlayers()){
            send(player);
        }
    }
}
