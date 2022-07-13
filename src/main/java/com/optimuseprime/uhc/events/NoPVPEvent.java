package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Team;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.TeamManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class NoPVPEvent implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType().equals(EntityType.PLAYER) && e.getEntity().getType().equals(EntityType.PLAYER)) {
            Game g1 = GameManager.getGameByPlayer(e.getDamager().getUniqueId());
            Game g2 = GameManager.getGameByPlayer(e.getEntity().getUniqueId());
            if(g1 == null) {
                return;
            }
            if(g2 == null) {
                return;
            }
            if(!g1.getCreator().equals(g2.getCreator())) {
                return;
            }
            if (!g1.isPvPEnabled()) {
                e.setDamage(0);
                e.setCancelled(true);
            } else if(!g1.getFriendlyFire()) {
                Team t1 = TeamManager.getTeamByPlayer(g1, e.getEntity().getUniqueId());
                Team t2 = TeamManager.getTeamByPlayer(g1, e.getDamager().getUniqueId());
                if (t1 == null) {
                    return;
                }
                if (t2 == null) {
                    return;
                }
                if (t1.getTeamName().equals(t2.getTeamName())) {
                    e.setDamage(0);
                    e.setCancelled(true);
                }
            }
        }
    }
}
