package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Lootbag;
import com.optimuseprime.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class LootbagManager {

    public static void setLootBags(Game g) {
        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> commonLootbagLootTable = new HashMap<>();
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_INGOT, new AbstractMap.SimpleEntry<>(10, 40)));
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.APPLE, new AbstractMap.SimpleEntry<>(7, 100)));
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.OAK_LOG, new AbstractMap.SimpleEntry<>(12, 96)));
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.COOKED_PORKCHOP, new AbstractMap.SimpleEntry<>(8, 88)));
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE, new AbstractMap.SimpleEntry<>(1, 80)));
        commonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_SWORD, new AbstractMap.SimpleEntry<>(1, 8)));
        HashMap<EntityType, Integer> commonLootbagDropTable = new HashMap<>();
        commonLootbagDropTable.put(EntityType.ZOMBIE, 80);
        commonLootbagDropTable.put(EntityType.SKELETON, 90);
        commonLootbagDropTable.put(EntityType.CREEPER, 100);
        commonLootbagDropTable.put(EntityType.DONKEY, 30);
        commonLootbagDropTable.put(EntityType.PIG, 30);
        commonLootbagDropTable.put(EntityType.CHICKEN, 10);
        commonLootbagDropTable.put(EntityType.COW, 20);
        Lootbag commonLootbag = new Lootbag(ChatColor.WHITE + "Common Lootbag", 5673210, commonLootbagDropTable);
        commonLootbag.setLootTable(commonLootbagLootTable);

        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> uncommonLootbagLootTable = new HashMap<>();
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_INGOT, new AbstractMap.SimpleEntry<>(13, 68)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_CHESTPLATE, new AbstractMap.SimpleEntry<>(1, 10)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(1, 7)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLD_INGOT, new AbstractMap.SimpleEntry<>(5, 8)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_SWORD, new AbstractMap.SimpleEntry<>(1, 22)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.COOKED_BEEF, new AbstractMap.SimpleEntry<>(11, 92)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_LEGGINGS, new AbstractMap.SimpleEntry<>(1, 14)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND, new AbstractMap.SimpleEntry<>(1, 2)));
        uncommonLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.EXPERIENCE_BOTTLE, new AbstractMap.SimpleEntry<>(4, 10)));
        HashMap<EntityType, Integer> uncommonLootbagDropTable = new HashMap<>();
        uncommonLootbagDropTable.put(EntityType.ZOMBIE, 40);
        uncommonLootbagDropTable.put(EntityType.SKELETON, 50);
        uncommonLootbagDropTable.put(EntityType.CREEPER, 80);
        Lootbag uncommonLootbag = new Lootbag(ChatColor.GREEN + "Uncommon Lootbag", 5673220, uncommonLootbagDropTable);
        uncommonLootbag.setLootTable(uncommonLootbagLootTable);

        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> rareLootbagLootTable = new HashMap<>();
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_INGOT, new AbstractMap.SimpleEntry<>(22, 70)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.EXPERIENCE_BOTTLE, new AbstractMap.SimpleEntry<>(8, 45)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND, new AbstractMap.SimpleEntry<>(2, 8)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_SWORD, new AbstractMap.SimpleEntry<>(1, 45)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(2, 14)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_CHESTPLATE, new AbstractMap.SimpleEntry<>(1, 22)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_LEGGINGS, new AbstractMap.SimpleEntry<>(1, 25)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_HELMET, new AbstractMap.SimpleEntry<>(1, 25)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_BOOTS, new AbstractMap.SimpleEntry<>(1, 25)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD, new AbstractMap.SimpleEntry<>(1, 6)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND, new AbstractMap.SimpleEntry<>(3, 5)));
        rareLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.LAPIS_LAZULI, new AbstractMap.SimpleEntry<>(10, 20)));
        HashMap<EntityType, Integer> rareDropTable = new HashMap<>();
        rareDropTable.put(EntityType.CREEPER, 40);
        rareDropTable.put(EntityType.SKELETON, 10);
        Lootbag rareLootbag = new Lootbag(ChatColor.BLUE + "Rare Lootbag", 5673230, rareDropTable);
        rareLootbag.setLootTable(rareLootbagLootTable);

        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> epicLootbagLootTable = new HashMap<>();
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.EXPERIENCE_BOTTLE, new AbstractMap.SimpleEntry<>(15, 70)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND, new AbstractMap.SimpleEntry<>(4, 16)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(2, 14)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_CHESTPLATE, new AbstractMap.SimpleEntry<>(1, 60)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.IRON_LEGGINGS, new AbstractMap.SimpleEntry<>(1, 60)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_BOOTS, new AbstractMap.SimpleEntry<>(1, 10)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_HELMET, new AbstractMap.SimpleEntry<>(1, 12)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD, new AbstractMap.SimpleEntry<>(1, 10)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.ENCHANTED_GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(1, 5)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.LAPIS_LAZULI, new AbstractMap.SimpleEntry<>(10, 40)));
        epicLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLD_INGOT, new AbstractMap.SimpleEntry<>(8, 50)));
        HashMap<EntityType, Integer> epicDropTable = new HashMap<>();
        epicDropTable.put(EntityType.CREEPER, 10);
        epicDropTable.put(EntityType.SKELETON, 4);
        Lootbag epicLootbag = new Lootbag(ChatColor.DARK_PURPLE + "Epic Lootbag", 5673240, epicDropTable);
        epicLootbag.setLootTable(epicLootbagLootTable);

        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> legendaryLootbagLootTable = new HashMap<>();
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.EXPERIENCE_BOTTLE, new AbstractMap.SimpleEntry<>(40, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND, new AbstractMap.SimpleEntry<>(8, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(5, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_CHESTPLATE, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_LEGGINGS, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_BOOTS, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_HELMET, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.ENCHANTED_GOLDEN_APPLE, new AbstractMap.SimpleEntry<>(1, 100)));
        legendaryLootbagLootTable.put(UUID.randomUUID().toString(), new AbstractMap.SimpleEntry<>(Material.LAPIS_LAZULI, new AbstractMap.SimpleEntry<>(25, 100)));
        HashMap<EntityType, Integer> legendaryDropTable = new HashMap<>();
        legendaryDropTable.put(EntityType.PLAYER, 100);
        Lootbag legendaryLootbag = new Lootbag(ChatColor.GOLD + "Legendary Lootbag", 5673250, legendaryDropTable);
        legendaryLootbag.setLootTable(legendaryLootbagLootTable);

        g.setLootbags(Arrays.asList(commonLootbag, uncommonLootbag, rareLootbag, epicLootbag, legendaryLootbag));
    }

    public static List<ItemStack> getItems(Lootbag l) {
        Random r = new Random();
        List<ItemStack> items = new ArrayList<>();
        for(String uuid : l.getLootTable().keySet()) {
            int j = r.nextInt(100);
            if(j <= l.getLootTable().get(uuid).getValue().getValue()) {
                ItemStack is = new ItemStack(l.getLootTable().get(uuid).getKey(), l.getLootTable().get(uuid).getValue().getKey());
                items.add(is);
            }
        }
        return items;
    }

    public static List<ItemStack> getLootbags(Game g, EntityType et) {
        Random r = new Random();
        List<Lootbag> availableLootbags = new ArrayList<>();
        int i = r.nextInt(100);
        for(Lootbag l : g.getLootbags()) {
            for(EntityType e : l.getEntites().keySet()) {
                if(e.equals(et)) {
                    if(i <= l.getEntites().get(e)) {
                        availableLootbags.add(l);
                    }
                }
            }
        }
        List<ItemStack> lootbags = new ArrayList<>();
        for(Lootbag l : availableLootbags) {
            ItemStack is = new ItemStack(Material.STICK, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(l.getName());
            im.setCustomModelData(l.getCustomModelData());
            im.setLore(Arrays.asList(UHC.convertToInvisibleString(UUID.randomUUID().toString())));
            is.setItemMeta(im);
            lootbags.add(is);
        }
        return lootbags;
    }

    public static Inventory getLootbagEditInventoryPageOne(Game g) {
        Inventory inv = Bukkit.createInventory(null, 27, "Edit Lootbags");
        for(Lootbag l : g.getLootbags()) {
            ItemStack is = new ItemStack(Material.STICK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(l.getName());
            im.setCustomModelData(l.getCustomModelData());
            is.setItemMeta(im);
            inv.addItem(is);
        }
        return inv;
    }

    public static Inventory getLootbagEditInventoryPageTwo(Lootbag l) {
        Inventory inv = Bukkit.createInventory(null, 54, "Edit Lootbags - " + ChatColor.stripColor(l.getName()));
        for(String uuid : l.getLootTable().keySet()) {
            ItemStack is = new ItemStack(l.getLootTable().get(uuid).getKey(), l.getLootTable().get(uuid).getValue().getKey());
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(ChatColor.GRAY + "Chance: " + l.getLootTable().get(uuid).getValue().getValue() + "%"));
            im.getPersistentDataContainer().set(NamespacedKey.fromString("uuid"), PersistentDataType.STRING, uuid);
            is.setItemMeta(im);
            inv.addItem(is);
        }
        return inv;
    }

    public static void startItemTransferCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Game g : GameManager.getGames()) {
                    Player p = Bukkit.getPlayer(g.getCreator());
                    if(p != null) {
                        if(p.getOpenInventory() != null) {
                            InventoryView invView = p.getOpenInventory();
                            Inventory tInv = invView.getTopInventory();
                            Lootbag lootbag = getLootbagByName(g, invView.getTitle());
                            if(p.getInventory().getItemInMainHand() != null) {
                                if(p.getInventory().getItemInMainHand().getItemMeta() != null) {
                                    if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Lootbag")) {
                                        if(tInv != null) {
                                            if(invView.getTitle().contains("Lootbag")) {
                                                if(tInv.isEmpty()) {
                                                    p.getInventory().remove(p.getInventory().getItemInMainHand());
                                                    p.closeInventory();
                                                }
                                            }
                                        }
                                        return;
                                    }
                                }
                            }
                            for(ItemStack is : p.getOpenInventory().getBottomInventory().getContents()) {
                                if(is != null) {
                                    if(is.getItemMeta() != null) {
                                        ItemMeta im = is.getItemMeta();
                                        if(im.getLore() != null) {
                                            if(im.getPersistentDataContainer() != null) {
                                                if(im.getPersistentDataContainer().get(NamespacedKey.fromString("uuid"), PersistentDataType.STRING) != null) {
                                                    im.getPersistentDataContainer().remove(NamespacedKey.fromString("uuid"));
                                                }
                                            }
                                            im.setLore(new ArrayList<>());
                                            is.setItemMeta(im);
                                        }
                                    }
                                }
                            }
                            if(lootbag != null) {
                                if(tInv.isEmpty()) {
                                    lootbag.setLootTable(new HashMap<>());
                                    try {
                                        GameManager.saveGame(g);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> hm = new HashMap<>();
                                for(int i = 0; i < tInv.getStorageContents().length; i++) {
                                    ItemStack is = tInv.getItem(i);
                                    if(is != null) {
                                        ItemMeta im = is.getItemMeta();
                                        if(im != null) {
                                            String uuid = im.getPersistentDataContainer().get(NamespacedKey.fromString("uuid"), PersistentDataType.STRING);
                                            if(uuid != null) {
                                                String clean = ChatColor.stripColor(im.getLore().get(0)).replaceAll("\\D+","");
                                                int chance = Integer.parseInt(clean);
                                                hm.put(uuid, new AbstractMap.SimpleEntry <>(is.getType(), new AbstractMap.SimpleEntry<>(is.getAmount(), chance)));
                                            } else {
                                                uuid = UUID.randomUUID().toString();
                                                im.getPersistentDataContainer().set(NamespacedKey.fromString("uuid"), PersistentDataType.STRING, uuid);
                                                im.setLore(Arrays.asList(ChatColor.GRAY + "Chance: 4%"));
                                                is.setItemMeta(im);
                                                hm.put(uuid, new AbstractMap.SimpleEntry <>(is.getType(), new AbstractMap.SimpleEntry<>(is.getAmount(), 4)));
                                            }
                                        }
                                    }
                                }
                                lootbag.setLootTable(hm);
                            }
                        }
                    }
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(UHC.getInstance(), 0, 5);
    }

    public static Lootbag getLootbagByName(Game g, String s) {
        for(Lootbag b : g.getLootbags()) {
            if(s.contains(ChatColor.stripColor(b.getName()))) {
                return b;
            }
        }
        return null;
    }

    private static boolean isInventoryEmpty(Inventory inv) {
        for(ItemStack is : inv.getContents()) {
            if(is != null) {
                return false;
            }
        }
        return true;
    }
}
