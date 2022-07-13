package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.Team;
import com.optimuseprime.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

    private static ChatColor[] colors = {
            ChatColor.AQUA,
            ChatColor.BLACK,
            ChatColor.BLUE,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_GRAY,
            ChatColor.DARK_GREEN,
            ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED,
            ChatColor.GOLD,
            ChatColor.GRAY,
            ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE,
            ChatColor.WHITE,
            ChatColor.YELLOW,
    };

    private static Material[] wools = {
            Material.LIGHT_BLUE_WOOL,
            Material.BLACK_WOOL,
            Material.BLUE_WOOL,
            Material.CYAN_WOOL,
            Material.GRAY_WOOL,
            Material.GREEN_WOOL,
            Material.PURPLE_WOOL,
            Material.RED_WOOL,
            Material.ORANGE_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.LIME_WOOL,
            Material.MAGENTA_WOOL,
            Material.WHITE_WOOL,
            Material.YELLOW_WOOL
    };

    public static void createTeams(Game game) {
        int color = 0;
        for(int i = 0; i < colors.length; i++) {
            int teamNum = color+1;
            game.getTeams().add(new Team("Team " + teamNum, colors[color], wools[color], game.getTeamInventorySize()));
            color++;
            if(color >= colors.length) {
                color = 0;
            }
        }
    }

    public static void joinTeam(Game game, Player p, Team t) throws IOException {
        //Checks if the player is in game
        if(game.getPlayers().contains(p.getUniqueId())) {
            //Checks if the team size exceeds the maximum team size
            if(t.getPlayers().size() >= game.getTeamMembersMax()) {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getTeamFullMsg(), p, t));
                return;
            }
            //Checks if the player has already joined this team
            if(t.getPlayers().contains(p.getUniqueId())) {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getAlreadyJoinedTeamMsg(), p, t));
                return;
            }
            //Removes player from all other teams
            leaveTeams(game, p);
            //Adds a player to the list of team member
            t.getPlayers().add(p.getUniqueId());
            //Alerts the player of the executed action
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getJoinedTeamMsg(), p, t));
        }
        //Saves this game
        GameManager.saveGame(game);
    }

    public static void leaveTeams(Game game, Player p) {
        for(Team tts : game.getTeams()) {
            Iterator<UUID> pl = tts.getPlayers().iterator();
            if(pl.hasNext()) {
                if(pl.next().equals(p.getUniqueId())) {
                    pl.remove();
                }
            }
        }
    }

    public static void showTeamGUI(Game g, Player p) {
        if(g.getPlayers().contains(p.getUniqueId())) {
            Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, "Select Team");
            for(Team t : g.getTeams()) {
                ItemStack is = new ItemStack(t.getMaterial());
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(t.getColor() + t.getTeamName());
                UHC.getInstance().getLogger().info(t.getTeamName());
                is.setItemMeta(im);
                inv.addItem(is);
            }
            p.openInventory(inv);
        }
    }

    public static void openTeamInventory(Game g, Player p) {
        if(g.getPlayers().contains(p.getUniqueId())) {
            if(g.isTeamsEnabled()) {
                Inventory inv = UHC.getInstance().getServer().createInventory(null, g.getTeamInventorySize(), "Team Inventory");
                Team t = getTeamByPlayer(g, p.getUniqueId());
                for(Integer i : t.getTeamInventoryHashMap().keySet()) {
                    Map.Entry<Material, Integer> se = t.getTeamInventoryHashMap().get(i);
                    inv.setItem(i, new ItemStack(se.getKey(), se.getValue()));
                }
                p.openInventory(inv);
            } else {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getTeamsHaveToBeEnabledMsg(), p));
            }
        }
    }

    public static Team getTeamByName(Game g, String name) {
        for(Team t : g.getTeams()) {
            if(t.getTeamName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static Team getTeamByPlayer(Game g, UUID uuid) {
        for(Team t : g.getTeams()) {
            if(t.getPlayers().contains(uuid)) {
                return t;
            }
        }
        return null;
    }

    public static void clearTeams(Game g) {
        for(Team t : g.getTeams()) {
            t.getPlayers().clear();
            t.getTeamInventoryHashMap().clear();
        }
    }
}
