package de.daver.unihub.scoreboard;

import de.daver.unihub.util.PacketSender;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TablistScoreboard {

   private final Scoreboard scoreboard;
   private final HashMap<UUID, String> playerTeams;

   public TablistScoreboard(){
       this.scoreboard = new Scoreboard();
       this.playerTeams = new HashMap<>();
   }

   public void registerTeam(Player player, String prefix, String suffix, int level){
        String teamName = level + player.getUniqueId().toString().substring(1,6);
        if(this.scoreboard.getTeam(teamName) != null){
            scoreboard.removeTeam(scoreboard.getTeam(teamName));
        }
        ScoreboardTeam scoreboardTeam = scoreboard.createTeam(teamName);
        suffix = (suffix.length() > 16)? suffix.substring(0, 15) : suffix;
        prefix = (prefix.length() > 16)? prefix.substring(0, 15) : prefix;
        scoreboardTeam.setPrefix(prefix);
        scoreboardTeam.setSuffix(suffix);

        playerTeams.put(player.getUniqueId(), teamName);

        update();
   }

   private void update(){
       for(Player player : Bukkit.getOnlinePlayers()){
           if(!containsPlayer(player)){
               getScoreboardTeam(player).getPlayerNameSet().add(player.getName());
           }
           Packet<?> packet1 = new PacketPlayOutScoreboardTeam(getScoreboardTeam(player), 1);
           Packet<?> packet0 = new PacketPlayOutScoreboardTeam(getScoreboardTeam(player), 0);
           PacketSender.broadcastPackets(packet1, packet0);
       }
   }

   private boolean containsPlayer(Player player){
       return getScoreboardTeam(player).getPlayerNameSet().contains(player.getName());
   }

   private ScoreboardTeam getScoreboardTeam(Player player){
       return scoreboard.getTeam(playerTeams.get(player.getUniqueId()));
   }
}
