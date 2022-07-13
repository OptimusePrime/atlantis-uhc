package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.GameStatus;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class PlayerRespawnsEvent implements Listener {

    @EventHandler
    public void onDeathSkipDeathScreen(PlayerDeathEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(UHC.getInstance(), () -> {
            e.getEntity().spigot().respawn();
        }, 2L);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        for(Game g : GameManager.getGames()) {
            for(UUID uuid : g.getPlayers()) {
                if(e.getPlayer().getUniqueId().equals(uuid)) {
                    if(g.getGameStatus().equals(GameStatus.IN_GAME)) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        e.setRespawnLocation(new Location(g.getGameWorld(),0,150,0));
                    } else {
                        e.setRespawnLocation(new Location(g.getGameWorld(),g.getLobbyDiameter()/2,201,g.getLobbyDiameter()/2));
                        PlayerManager.initPlayer(e.getPlayer(), g, false);
                    }
                }
            }
        }
    }
}
