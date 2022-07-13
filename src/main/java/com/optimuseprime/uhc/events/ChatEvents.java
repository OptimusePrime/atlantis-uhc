package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.Team;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatEvents implements Listener {

    @EventHandler
    public void onTeamChat(AsyncPlayerChatEvent e) {
        if(GameManager.getGameByPlayer(e.getPlayer().getUniqueId()) != null) {
            e.setCancelled(true);
            if(!e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                if(TeamManager.getTeamByPlayer(GameManager.getGameByPlayer(e.getPlayer().getUniqueId()), e.getPlayer().getUniqueId()) != null) {
                    Team t = TeamManager.getTeamByPlayer(GameManager.getGameByPlayer(e.getPlayer().getUniqueId()), e.getPlayer().getUniqueId());
                    if(t == null) {
                        return;
                    }
                    if(!e.getMessage().startsWith(UHC.getMessages().getGlobalChatPrefix())) {
                        for(UUID uuid : t.getPlayers()) {
                            String msg = Messages.placePlaceholders(UHC.getMessages().getChat(), t, e.getPlayer(), e.getMessage());
                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                        }
                    } else {
                        StringBuilder stringBuilder = new StringBuilder(e.getMessage());
                        stringBuilder.deleteCharAt(0);
                        String msg = Messages.placePlaceholders(UHC.getMessages().getChat(), t, e.getPlayer(), stringBuilder.toString());
                        for(UUID u : GameManager.getGameByPlayer(e.getPlayer().getUniqueId()).getPlayers()) {
                            Bukkit.getPlayer(u).sendMessage(msg);
                        }
                    }
                } else {
                    String msg = Messages.placePlaceholders(UHC.getMessages().getChat(), e.getPlayer(), e.getMessage());
                    msg = msg
                            .replace("%team%", "")
                            .replace("%teamColor%", "")
                            .replace("[]", "")
                            .substring(1);
                    for(UUID u : GameManager.getGameByPlayer(e.getPlayer().getUniqueId()).getPlayers()) {
                        Bukkit.getPlayer(u).sendMessage(msg);
                    }
                }
            } else {
                String msg = Messages.placePlaceholders(UHC.getMessages().getChat(), e.getPlayer(), ChatColor.GRAY + e.getMessage());
                msg = msg
                        .replace("%team%", "")
                        .replace("%teamColor%", "")
                        .replace("[]", "");
                for(UUID u : GameManager.getGameByPlayer(e.getPlayer().getUniqueId()).getPlayers()) {
                    if(Bukkit.getPlayer(u).getGameMode().equals(GameMode.SPECTATOR)) {
                        Bukkit.getPlayer(u).sendMessage(msg);
                    }
                }
            }
        }
    }
}
