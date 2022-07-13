package com.optimuseprime.uhc.events;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Lootbag;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.LootbagManager;
import com.optimuseprime.uhc.managers.PlayerManager;
import com.optimuseprime.uhc.managers.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Game g = GameManager.getGameByPlayer(e.getWhoClicked().getUniqueId());
        if(g == null) {
            return;
        }
        if(e.getView().getTitle().equals("Select Team")) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null) {
                return;
            }
            try {
                TeamManager.joinTeam(PlayerManager.isInGame(e.getWhoClicked().getUniqueId()), (Player) e.getWhoClicked(), TeamManager.getTeamByName(PlayerManager.isInGame(e.getWhoClicked().getUniqueId()), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if(e.getView().getTitle().contains("Edit Lootbags")) {
            if(!g.getCreator().equals(e.getWhoClicked().getUniqueId()) || !e.getWhoClicked().hasPermission("uhc.admin") || !e.getWhoClicked().isOp()) {
                return;
            }
            ItemStack is = e.getCurrentItem();
            if(is == null) {
                return;
            }
            Lootbag l1 = LootbagManager.getLootbagByName(g, is.getItemMeta().getDisplayName());
            if(l1 != null) {
                e.getWhoClicked().openInventory(LootbagManager.getLootbagEditInventoryPageTwo(l1));
                return;
            }
            Lootbag l = LootbagManager.getLootbagByName(g, e.getView().getTitle());
            if(l != null) {
                ItemMeta im = is.getItemMeta();
                String uuid = im.getPersistentDataContainer().get(NamespacedKey.fromString("uuid"), PersistentDataType.STRING);
                if(uuid == null) {
                    return;
                }
                String clean = ChatColor.stripColor(im.getLore().get(0)).replaceAll("\\D+","");
                int chance = Integer.parseInt(clean);
                UHC.getInstance().getLogger().info(chance + "");
                UHC.getInstance().getLogger().info(uuid + "");
                if(l.getLootTable().get(uuid) == null) {
                    return;
                }
                if(e.getClick().isShiftClick()) {
                    e.setCancelled(true);
                    int newChance = chance+4;
                    HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> hm = l.getLootTable();
                    hm.replace(uuid, new AbstractMap.SimpleEntry<>(is.getType(), new AbstractMap.SimpleEntry<>(is.getAmount(), newChance)));
                    l.setLootTable(hm);
                    if(chance >= 100) {
                        im.setLore(Arrays.asList(ChatColor.GRAY + "Chance: 4%"));
                        is.setItemMeta(im);
                        try {
                            GameManager.saveGame(g);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else {
                        im.setLore(Arrays.asList(ChatColor.GRAY + "Chance: " + newChance + "%"));
                        is.setItemMeta(im);
                    }
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else if(e.getClick().isRightClick()) {
                    e.setCancelled(true);
                    int newChance = chance-4;
                    if(newChance <= 0) {
                        return;
                    }
                    HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> hm = l.getLootTable();
                    hm.replace(uuid, new AbstractMap.SimpleEntry<>(is.getType(), new AbstractMap.SimpleEntry<>(is.getAmount(), newChance)));
                    l.setLootTable(hm);
                    im.setLore(Arrays.asList(ChatColor.GRAY + "Chance: " + newChance + "%"));
                    is.setItemMeta(im);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }

    }

    @EventHandler
    public void onScenarios(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("Scenarios")) {
            e.setCancelled(true);
            Game g = GameManager.getGameByCreator(e.getWhoClicked().getUniqueId());
            if(g == null) {
                return;
            }
            ItemStack is = e.getCurrentItem();
            ItemMeta im = is.getItemMeta();
            if(im == null) {
                return;
            }
            if(im.getLore() == null) {
                return;
            }
            if(im.getLore().get(0) == null) {
                return;
            }
            String enabled = ChatColor.stripColor(im.getLore().get(0));
            if(enabled.contains("true")) {
                im.setLore(Arrays.asList(ChatColor.GRAY + "Enabled: false"));
                g.getScenarios().replace(im.getPersistentDataContainer().get(NamespacedKey.fromString("id"), PersistentDataType.STRING), false);
            } else {
                im.setLore(Arrays.asList(ChatColor.GRAY + "Enabled: true"));
                g.getScenarios().replace(im.getPersistentDataContainer().get(NamespacedKey.fromString("id"), PersistentDataType.STRING), true);
            }
            is.setItemMeta(im);
        }
    }

    @EventHandler
    public void onTransferItem(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("Team Inventory")) {
            for(int i = 0; i < e.getInventory().getContents().length; i++) {
                HashMap<Integer, Map.Entry<Material, Integer>> map =  TeamManager.getTeamByPlayer(GameManager.getGameByPlayer(e.getPlayer().getUniqueId()), e.getPlayer()
                        .getUniqueId()).getTeamInventoryHashMap();
                if(e.getInventory().getItem(i) != null) {
                    map.put(i, new AbstractMap.SimpleEntry<>(e.getInventory().getItem(i).getType(), e.getInventory().getItem(i).getAmount()));
                } else if (e.getInventory().getItem(i) == null && map.containsKey(i)) {
                    map.remove(i);
                }
            }
        }
    }
}
