package dev.parrotstudios.qTotems.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onPop(EntityResurrectEvent event){
        if(!(event.getEntity() instanceof Player player)) return;

    }
}
