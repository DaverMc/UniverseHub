package de.daver.unihub.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ListenerRegistry {

    private final Plugin plugin;

    public ListenerRegistry(Plugin plugin){
        this.plugin = plugin;
    }

    public void register(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

}
