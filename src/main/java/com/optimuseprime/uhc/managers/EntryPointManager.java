package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.Game;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;


public class EntryPointManager {

    public static void placeExitPoint(Location loc) {
        ArmorStand armorStand = loc.getWorld().spawn(loc.add(0,-1,0), ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setBodyPose(EulerAngle.ZERO);
        armorStand.getLocation().setPitch(0);
        armorStand.getLocation().setYaw(0);
        ItemStack is = new ItemStack(Material.COAL_BLOCK);
        is.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        armorStand.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "Leave UHC");
        armorStand.getEquipment().setHelmet(is);
    }

    public static void openInventory(Player p) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Join UHC");
        for(Game g : GameManager.getGames()) {
            ItemStack is = new ItemStack(Material.QUARTZ_BLOCK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.WHITE + Bukkit.getPlayer(g.getCreator()).getName() + "'s UHC");
            im.getPersistentDataContainer().set(NamespacedKey.fromString("author"), PersistentDataType.STRING, g.getCreator().toString());
            is.setItemMeta(im);
            inventory.addItem(is);
        }
        p.openInventory(inventory);
    }
}
