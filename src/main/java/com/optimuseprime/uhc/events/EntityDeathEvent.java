package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.LootbagManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityDeathEvent implements Listener {

    @EventHandler
    public void onDeath(org.bukkit.event.entity.EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            if(GameManager.getGameByPlayer(e.getEntity().getKiller().getUniqueId()) != null) {
                if(GameManager.getGameByPlayer(e.getEntity().getKiller().getUniqueId()).getLootBagsEnabled()) {
                    List<ItemStack> is = LootbagManager.getLootbags(GameManager.getGameByPlayer(e.getEntity().getKiller().getUniqueId()), e.getEntityType());
                    for(ItemStack i : is) {
                        e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), i);
                    }
                }
            }
        }
    }
}
