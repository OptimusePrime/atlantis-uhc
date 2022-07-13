package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class EntryPointEvents implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked() == null) {
            return;
        }
        if(e.getRightClicked().getCustomName() == null) {
            return;
        }
        if (e.getRightClicked().getCustomName().equals(ChatColor.RED + "" + ChatColor.BOLD + "Leave UHC")) {
            e.setCancelled(true);
            PlayerManager.unInitPlayer(e.getPlayer(), GameManager.getGameByPlayer(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("Join UHC")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().getItemMeta().getPersistentDataContainer() != null) {
                    if(GameManager.getGameByPlayer(e.getWhoClicked().getUniqueId()) != null) {
                        e.getWhoClicked().sendMessage(Messages.placePlaceholders(UHC.getMessages().getAlreadyJoinedGameMsg(), e.getWhoClicked()));
                        return;
                    }
                    PlayerManager.initPlayer((Player) e.getWhoClicked(), GameManager.getGameByCreator(UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("author"), PersistentDataType.STRING))), true);
                }
            }
        }
    }
}
