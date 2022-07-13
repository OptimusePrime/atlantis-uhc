package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Lootbag;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.LootbagManager;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.List;


public class RightClickEvents implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Game g = GameManager.getGameByPlayer(e.getPlayer().getUniqueId());
            if(g == null) {
                return;
            }
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null) {
                return;
            }
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) {
                return;
            }
            Lootbag l = LootbagManager.getLootbagByName(g, e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
            if(l == null) {
                return;
            }
            List<ItemStack> list = LootbagManager.getItems(l);
            if(list.size() <= 0 ) {
                e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
                return;
            }
            for(ItemStack is : list) {
                e.getPlayer().getInventory().addItem(is);
            }
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 3);
            e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
        }
    }

    private Integer getSlot(Inventory inv, ItemStack is) {
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i).equals(is)) {
                return i;
            }
        }
        return 0;
    }
}
