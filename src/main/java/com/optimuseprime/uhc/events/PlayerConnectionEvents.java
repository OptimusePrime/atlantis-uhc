package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionEvents implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(GameManager.getGameByPlayer(e.getPlayer().getUniqueId()) == null) {
            return;
        }
        PlayerManager.unInitPlayer(e.getPlayer(), GameManager.getGameByPlayer(e.getPlayer().getUniqueId()));
    }
}
