package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.Scenario;
import com.optimuseprime.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ScenariosManager {

    private static List<Scenario> registeredScenarios = new ArrayList<>();

    public static void registerScenarios(List<Scenario> l) {
        for(Scenario s : l) {
            Bukkit.getPluginManager().registerEvents(s, UHC.getInstance());
            getRegisteredScenarios().add(s);
        }
    }

    public static void createScenarios() {
        Scenario doubleOreScenario = new Scenario() {
            @Override
            public String getName() {
                return ChatColor.GOLD + "Double Ores";
            }

            @Override
            public String getId() {
                return "doubleOres";
            }

            @Override
            public List<String> getLore() {
                return super.getLore();
            }

            @Override
            public ItemStack getDisplayItem() {
                return new ItemStack(Material.GOLD_ORE);
            }

            @Override
            public Boolean getEnabled() {
                return false;
            }

            @EventHandler
            public void execute(BlockBreakEvent e) {
                Game g = GameManager.getGameByPlayer(e.getPlayer().getUniqueId());
                if(g == null) {
                    return;
                }
                if(!g.getScenarios().get(getId())) {
                    return;
                }
                if(g.getScenarios().get("cutClean")) {
                    return;
                }
                Material m = e.getBlock().getType();
                switch (m) {
                    case GOLD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_GOLD, 2)); break;
                    case DIAMOND_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND, 2)); break;
                    case EMERALD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.EMERALD, 2)); break;
                    case REDSTONE_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE, 2)); break;
                    case COAL_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COAL, 2)); break;
                    case LAPIS_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.LAPIS_LAZULI, 2)); break;
                    case IRON_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_IRON, 2)); break;
                    case COPPER_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_COPPER, 2)); break;
                }
            }
        };

        Scenario tripleOreScenario = new Scenario() {
            @Override
            public String getName() {
                return ChatColor.AQUA + "Triple Ores";
            }

            @Override
            public String getId() {
                return "tripleOres";
            }

            @Override
            public List<String> getLore() {
                return super.getLore();
            }

            @Override
            public ItemStack getDisplayItem() {
                return new ItemStack(Material.DIAMOND_ORE);
            }

            @Override
            public Boolean getEnabled() {
                return false;
            }

            @EventHandler
            public void execute(BlockBreakEvent e) {
                Game g = GameManager.getGameByPlayer(e.getPlayer().getUniqueId());
                if(g == null) {
                    return;
                }
                if(!g.getScenarios().get(getId())) {
                    return;
                }
                if(g.getScenarios().get("cutClean")) {
                    return;
                }
                Material m = e.getBlock().getType();

                switch (m) {
                    case GOLD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_GOLD, 3)); break;
                    case DIAMOND_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND, 3)); break;
                    case EMERALD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.EMERALD, 3)); break;
                    case REDSTONE_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE, 3)); break;
                    case COAL_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COAL, 3)); break;
                    case LAPIS_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.LAPIS_LAZULI, 3)); break;
                    case IRON_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_IRON, 3)); break;
                    case COPPER_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.RAW_COPPER, 3)); break;
                }
            }
        };

        Scenario cutCleanScenario = new Scenario() {
            @Override
            public String getName() {
                return ChatColor.WHITE + "Cut Clean";
            }

            @Override
            public String getId() {
                return "cutClean";
            }

            @Override
            public List<String> getLore() {
                return super.getLore();
            }

            @Override
            public ItemStack getDisplayItem() {
                return new ItemStack(Material.IRON_INGOT);
            }

            @Override
            public Boolean getEnabled() {
                return false;
            }

            @EventHandler
            public void execute(BlockBreakEvent e) {
                Game g = GameManager.getGameByPlayer(e.getPlayer().getUniqueId());
                if(g == null) {
                    return;
                }
                if(!g.getScenarios().get(getId())) {
                    return;
                }
                int dropAmount = 1;
                if(g.getScenarios().get("doubleOres")) {
                    dropAmount = 2;
                } else if(g.getScenarios().get("tripleOres")) {
                    dropAmount = 3;
                }
                Material m = e.getBlock().getType();
                switch (m) {
                    case GOLD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, dropAmount)); break;
                    case DIAMOND_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND, dropAmount)); break;
                    case EMERALD_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.EMERALD, dropAmount)); break;
                    case REDSTONE_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE, dropAmount)); break;
                    case COAL_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COAL, dropAmount)); break;
                    case LAPIS_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.LAPIS_LAZULI, dropAmount)); break;
                    case IRON_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, dropAmount)); break;
                    case COPPER_ORE:
                        e.setDropItems(false);
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COPPER_INGOT, dropAmount)); break;
                }
            }
        };

        Scenario timberScenario = new Scenario() {
            @Override
            public String getName() {
                return ChatColor.GREEN + "Timber";
            }

            @Override
            public String getId() {
                return "timber";
            }

            @Override
            public List<String> getLore() {
                return super.getLore();
            }

            @Override
            public ItemStack getDisplayItem() {
                return new ItemStack(Material.OAK_SAPLING);
            }

            @Override
            public Boolean getEnabled() {
                return false;
            }

            @EventHandler
            public void execute(BlockBreakEvent e) {
                Game g = GameManager.getGameByPlayer(e.getPlayer().getUniqueId());
                if(g == null) {
                    return;
                }
                if(!g.getScenarios().get(getId())) {
                    return;
                }
                if(!e.getPlayer().isSneaking()) {
                    return;
                }
                Material m = e.getBlock().getType();
                List<Material> allowedMats = Arrays.asList(Material.ACACIA_LOG, Material.OAK_LOG, Material.DARK_OAK_LOG, Material. BIRCH_LOG, Material.JUNGLE_LOG, Material.SPRUCE_LOG);
                if(allowedMats.contains(m)) {
                    Set<Block> blocks = ScenariosManager.getTree(e.getBlock(), allowedMats);
                    for(Block b : blocks) {
                        b.breakNaturally();
                    }
                }
            }
        };


        registerScenarios(Arrays.asList(doubleOreScenario, tripleOreScenario, cutCleanScenario, timberScenario));
    }

    public static void registerScenariosInGame(Game g) {
        for(Scenario s : getRegisteredScenarios()) {
            g.getScenarios().put(s.getId(), s.getEnabled());
        }
    }

    public static Scenario getScenarioById(String id) {
        for(Scenario s : getRegisteredScenarios()) {
            if(s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public static void openScenariosInventory(Player p) {
        Game g = GameManager.getGameByPlayer(p.getUniqueId());
        if(g == null) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(), p));
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 27, "Scenarios");
        for(String id : g.getScenarios().keySet()) {
            Scenario s = getScenarioById(id);
            ItemStack is = new ItemStack(s.getDisplayItem());
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(s.getName());
            im.setLore(Arrays.asList(ChatColor.GRAY + "Enabled: " + g.getScenarios().get(id)));
            im.getPersistentDataContainer().set(NamespacedKey.fromString("id"), PersistentDataType.STRING, s.getId());
            is.setItemMeta(im);
            inv.addItem(is);
        }
        p.openInventory(inv);
    }

    private static Set<Block> getNearbyBlocks(Block start, List<Material> allowedMaterials, HashSet<Block> blocks) {
        for (int x = -1; x < 4; x++) {
            for (int y = -5; y < 6; y++) {
                for (int z = -1; z < 4; z++) {
                    Block block = start.getLocation().clone().add(x, y, z).getBlock();
                    if (block != null && !blocks.contains(block) && allowedMaterials.contains(block.getType())) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
                    }
                }
            }
        }
        return blocks;
    }

   public static Set<Block> getTree(Block start, List<Material> allowedMaterials) {
        return getNearbyBlocks(start, allowedMaterials, new HashSet<Block>());
    }

    public static List<Scenario> getRegisteredScenarios() {
        return registeredScenarios;
    }
}
