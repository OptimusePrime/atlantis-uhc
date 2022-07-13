package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    public static void initPlayer(Player p, Game game, boolean save) {
        if(save) {
            try {
                savePlayer(p, game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        p.teleport(new Location(game.getGameWorld(), game.getLobbyDiameter()/2+5, 201, game.getLobbyDiameter()/2+5));
        p.setInvulnerable(true);
        for(PotionEffect e : p.getActivePotionEffects()) {
            p.removePotionEffect(e.getType());
        }
        p.getInventory().clear();
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 255, false, false));
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setExp(0);
        p.setLevel(0);
        p.setResourcePack("https://www.dropbox.com/sh/j63dg55ewmzo33u/AADOavSdYnK1x3eIB66YaGsEa?dl=1");
    }

    public static void unInitPlayer(Player p, Game game) {
        List<String> l = game.getPlayersHashMap().get(p.getUniqueId());
        Location origin = new Location(Bukkit.getWorld(l.get(0)), Integer.parseInt(l.get(1)), Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3)));
        p.setInvulnerable(false);
        for(PotionEffect e : p.getActivePotionEffects()) {
            p.removePotionEffect(e.getType());
        }
        p.setGameMode(GameMode.SURVIVAL);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setExp(Integer.parseInt(l.get(4)));
        p.setLevel(Integer.parseInt(l.get(5)));
        p.teleport(origin);
        try {
            removePlayer(p,game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(Player p, Game game) throws IOException {
        List<String> l = new ArrayList<>();
        l.add(0, p.getWorld().getName());
        l.add(1, String.valueOf((int)p.getLocation().getX()));
        l.add(2, String.valueOf((int)p.getLocation().getY()));
        l.add(3, String.valueOf((int)p.getLocation().getZ()));
        l.add(4, String.valueOf((int)p.getExp()));
        l.add(5, String.valueOf(p.getLevel()));
        game.getPlayersHashMap().put(p.getUniqueId(), l);
        FileWriter fw = new FileWriter(new File(UHC.getInstance().getDataFolder(), "games/" + game.getCreator().toString() + ".json"));
        UHC.getGson().toJson(game, fw);
        fw.flush();
        fw.close();
    }

    public static void removePlayer(Player p, Game game) throws IOException {
        game.getPlayersHashMap().remove(p.getUniqueId());
        FileWriter fw = new FileWriter(new File(UHC.getInstance().getDataFolder(), "games/" + game.getCreator().toString() + ".json"));
        UHC.getGson().toJson(game, fw);
        fw.flush();
        fw.close();
    }

    public static Game isInGame(UUID uuid) {
        for(Game g : GameManager.getGames()) {
            for(UUID uuid1 : g.getPlayers()) {
                if(uuid.equals(uuid1)) {
                    return g;
                }
            }
        }
        return null;
    }
}
