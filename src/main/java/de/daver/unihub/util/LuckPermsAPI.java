package de.daver.unihub.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

public class LuckPermsAPI {

    private final static LuckPerms luckPerms = LuckPermsProvider.get();

    private LuckPermsAPI(){}

    public static int getPermissionPower(String permission, Player player){
        return 0;
    }

    public static Group getGroup(Player player){
        return null;
    }

    public static String getPrefix(Group group){
        return "prefix";
    }

    public static int weight(Group group){
        return (group.getWeight().isPresent())? 0 : group.getWeight().getAsInt();
    }


}
