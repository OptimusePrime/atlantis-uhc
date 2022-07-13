package com.optimuseprime.uhc.managers;

import com.optimuseprime.uhc.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    private static final List<Game> games = new ArrayList<>();

    public static void create(Player creator, Game game) throws IOException {
        World world = WorldCreator.name(creator.getUniqueId().toString()).createWorld();
        world.getWorldBorder().setCenter(0,0);
        world.getWorldBorder().setSize(game.getBorderSize());
        world.getWorldBorder().setDamageAmount(game.getBorderDamageAmount());
        world.getWorldBorder().setDamageBuffer(game.getBorderDamageBuffer());
        world.getWorldBorder().setWarningDistance(8);
        world.getWorldBorder().setWarningTime(20);
        world.setTime(0);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.NATURAL_REGENERATION, game.getNaturalRegeneration());
        game.setPvPEnabled(false);
        generateLobby(Material.BARRIER, game);
        TeamManager.createTeams(game);
        ScenariosManager.registerScenariosInGame(game);
        games.add(game);
        saveGames();
    }

    public static void generateLobby(Material material, Game game) {
        for(int i = 0; i <= game.getLobbyDiameter(); i++ ) {
            game.getGameWorld().getBlockAt(0,200,i).setType(material);
            game.getGameWorld().getBlockAt(i,200,0).setType(material);
            for(int x = 0; x <= game.getLobbyDiameter(); x++) {
                game.getGameWorld().getBlockAt(x, 200, i).setType(material);
            }
            for(int z = 200; z <= 220; z++) {
                game.getGameWorld().getBlockAt(0,z,i).setType(material);
                game.getGameWorld().getBlockAt(i,z,0).setType(material);
                game.getGameWorld().getBlockAt(game.getLobbyDiameter(),z,i).setType(material);
                game.getGameWorld().getBlockAt(i,z,game.getLobbyDiameter()).setType(material);
            }
        }
        spawnGameInfo(game);
        EntryPointManager.placeExitPoint(new Location(game.getGameWorld(), (game.getLobbyDiameter()/2)+5, 201, (game.getLobbyDiameter()/2)-2));
    }

    public static void destroyLobby(Game game) {
        generateLobby(Material.AIR, game);
        for(Entity e : game.getGameWorld().getEntitiesByClass(ArmorStand.class)) {
            e.remove();
        }
    }

    public static void saveGame(Game game) throws IOException {
        FileWriter fw = new FileWriter(new File(UHC.getInstance().getDataFolder(), "games/" + game.getCreator().toString() + ".json"));
        UHC.getGson().toJson(game, fw);
        fw.flush();
        fw.close();
    }


    public static void saveGames() throws IOException {
        for(Game game : games) {
            saveGame(game);
        }
    }

    public static void loadGames() throws FileNotFoundException {
        File folder = new File(UHC.getInstance().getDataFolder(), "games");
        for(File f : folder.listFiles()) {
            FileReader fr = new FileReader(f);
            Game g = UHC.getGson().fromJson(fr, Game.class);
            UHC.getInstance().getServer().createWorld(new WorldCreator(g.getCreator().toString()));
            GameManager.getGames().add(g);
        }
    }

    public static void startGame(Game game) {
        Set<Player> pls = new HashSet<>();
        for(UUID uuid : game.getPlayers()) {
            pls.add(Bukkit.getPlayer(uuid));
        }
        AtomicInteger startCountdown = new AtomicInteger(11);
        game.setGameStatus(GameStatus.STARTING);
        game.getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        if(game.isTeamsEnabled()) {
            for(Player p : pls) {
                if(TeamManager.getTeamByPlayer(game, p.getUniqueId()) == null) {
                    p.kickPlayer(ChatColor.translateAlternateColorCodes('&', Messages.placePlaceholders(UHC.getMessages().getPlayerNotInTeamKickMsg(), p)));
                }
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                startCountdown.getAndDecrement();
                if(startCountdown.get() > 0) {
                    for(Player p : pls) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getStartCountdownMsg(), p, startCountdown.get()));
                    }
                } else if(startCountdown.get() <= 0) {
                    for(Player p : pls) {
                        if(!game.isTeamsEnabled()) {
                            for (Location loc : randomTp(game, game.getBorderSize()/2)) {
                                p.teleport(loc);
                            }
                        } else {
                            int count = 0;
                            for(Team t : game.getTeams()) {
                                for(UUID u : t.getPlayers()) {
                                    Bukkit.getPlayer(u).teleport(randomTp(game, game.getBorderSize()/2).get(count));
                                }
                                count++;
                            }
                        }
                        p.setInvulnerable(false);
                        for(PotionEffect e : p.getActivePotionEffects()) {
                            p.removePotionEffect(e.getType());
                        }
                        p.setGameMode(GameMode.SURVIVAL);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 255, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 255, false, false));
                        game.getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                        game.getGameWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, true);
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGameStartMsg(), p));
                        Bukkit.getServer().getScheduler().cancelTask(game.getDeleteGameTaskId());
                    }
                    if(game.getGracePeriodDuration() > 0) {
                        for(Player p : pls) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, game.getGracePeriodDuration() * 20, game.getGracePeriodHealthAmplifier(), false, false));
                        }
                        AtomicInteger gracePeriodCountdown = new AtomicInteger((game.getGracePeriodDuration()/60)+1);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                game.setGracePeriodTaskId(getTaskId());
                                gracePeriodCountdown.getAndDecrement();
                                if(gracePeriodCountdown.get() <= 0) {
                                    game.setPvPEnabled(true);
                                    game.getGameWorld().getWorldBorder().setSize(2, game.getBorderShrinkSpeed());
                                    for(Player p : pls) {
                                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGracePeriodEndedMsg(), p));
                                        p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                                    }
                                    try {
                                        saveGame(game);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    cancel();
                                }
                                else if(gracePeriodCountdown.get() > 0) {
                                    game.setPvPEnabled(false);
                                    if(gracePeriodCountdown.get() % 5 == 0 || gracePeriodCountdown.get() == game.getGracePeriodDuration()/60) {
                                        for(Player p : pls) {
                                            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGracePeriodCountdownMsg(), p, gracePeriodCountdown.get()));
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(UHC.getInstance(), 0, 1200);
                    } else if(game.getGracePeriodDuration() <= 0) {
                        game.getGameWorld().getWorldBorder().setSize(game.getBorderSize(), game.getBorderShrinkSpeed());
                        game.setPvPEnabled(true);
                        try {
                            saveGame(game);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int i = 0;
//                            Team ti = null;
                            List<Team> teams = new ArrayList<>();
                            String s = "";
                            AtomicInteger countdown = new AtomicInteger(16);
                            if(!game.isTeamsEnabled()) {
                                for (Player p : pls) {
                                    if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                                        i++;
                                        s = p.getName();
                                    }
                                }
                                if (i <= 1) {
                                    for(Player p : pls) {
                                        if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                                            s = p.getName();
                                        }
                                    }
                                    if(!game.getGameStatus().equals(GameStatus.IN_LOBBY)) {
                                        GameManager.stopGame(game, false, s, true);
                                    }
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if(game.getGameStatus().equals(GameStatus.IN_GAME)  || game.getGameStatus().equals(GameStatus.STARTING)) {
                                                cancel();
                                            }
                                            countdown.getAndDecrement();
                                            if(countdown.get() <= 0) {
                                                try {
                                                    deleteUHC(game);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (countdown.get() % 5 == 0) {
                                                for(Player p : pls) {
                                                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getUhcWillBeDeletedInMsg(), p, countdown.get()));
                                                }
                                            }
                                        }
                                    }.runTaskTimer(UHC.getInstance(), 0, 1200);
                                    cancel();
                                }
                            } else {
                                for(Team t : game.getTeams()) {
                                    for(UUID u : t.getPlayers()) {
                                        if(Bukkit.getPlayer(u).getGameMode().equals(GameMode.SURVIVAL)) {
                                            if(!teams.contains(t)) {
                                                teams.add(t);
                                            }
                                        }
                                    }
                                }
                                if(teams.size() <= 1) {
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            game.setDeleteGameTaskId(getTaskId());
                                            if(game.getGameStatus().equals(GameStatus.IN_GAME) || game.getGameStatus().equals(GameStatus.STARTING)) {
                                                cancel();
                                            }
                                            countdown.getAndDecrement();
                                            if(countdown.get() <= 0) {
                                                try {
                                                    deleteUHC(game);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (countdown.get() % 5 == 0) {
                                                for(Player p : pls) {
                                                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getUhcWillBeDeletedInMsg(), p, countdown.get()));
                                                }
                                            }
                                        }
                                    }.runTaskTimer(UHC.getInstance(), 0, 1200);
                                    //cancel();
                                    if(!game.getGameStatus().equals(GameStatus.IN_LOBBY)) {
                                        GameManager.stopGame(game, false, teams.get(0).getTeamName(), true);
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(UHC.getInstance(), 0, 20);
                    game.getGameWorld().setTime(0);
                    destroyLobby(game);
                    game.setGameStatus(GameStatus.IN_GAME);
                    try {
                        saveGame(game);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cancel();
                }
            }
        }.runTaskTimer(UHC.getInstance(), 0, 20);
    }

    public static void deleteUHC(Game game) throws IOException {
        //Saves gameworld name
        if(game.getGameWorld() == null) {
            return;
        }
        String s = game.getGameWorld().getName();
        //Loops through all the players and removes them from the game
        for(UUID u : game.getPlayers()) {
            PlayerManager.unInitPlayer(Bukkit.getPlayer(u), game);
        }
        //Unloads the gameworld
        Bukkit.unloadWorld(game.getGameWorld(), true);
        //Deletes gameworld directory
        FileUtils.deleteDirectory( new File(s));
        //Removes the game from the arraylist
        GameManager.getGames().remove(game);
        //Deletes game file
        File gameF = new File(UHC.getInstance().getDataFolder(), "games/" + game.getCreator().toString() + ".json");
        FileUtils.forceDelete(gameF);
        //Alerts the creator of the executed action
        Bukkit.getPlayer(game.getCreator()).sendMessage(Messages.
        placePlaceholders(UHC.getMessages().getUhcDeletionSuccessMsg(), Bukkit.getPlayer(game.getCreator())));
    }

    public static void stopGame(Game game, boolean msg, String s, boolean doWin) {
        game.setGameStatus(GameStatus.IN_LOBBY);
        Set<Player> pls = new HashSet<>();
        for(UUID uuid : game.getPlayers()) {
            pls.add(Bukkit.getPlayer(uuid));
        }
        generateLobby(Material.BARRIER, game);
        for(Player p : pls) {
            PlayerManager.initPlayer(p, game, false);
            if(msg) {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGameManualStopMsg(), p));
            }
            if (doWin) {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGameWinMsg().replace("%winner%", s)));
            }
        }
        game.getGameWorld().setTime(0);
        game.getGameWorld().getWorldBorder().setSize(game.getBorderSize());
        game.getGameWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        game.getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        UHC.getInstance().getServer().getScheduler().cancelTask(game.getGracePeriodTaskId());
        for(Game g : GameManager.getGames()) {
            GameManager.displayBorderInfo(g);
        }
        TeamManager.clearTeams(game);
        try {
            saveGames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Location> randomTp(Game game, int borderSize) {
        List<Location> locs = new ArrayList<>();
        Random r = new Random();
        Location loc;
        int x = r.nextInt(borderSize/2-(borderSize/2/10));
        int z = r.nextInt(borderSize/2-(borderSize/2/10));
        int y = game.getGameWorld().getHighestBlockYAt(x, z) + 1;
        if(!game.isTeamsEnabled()) {
            for(int j = 0; j < game.getPlayers().size(); j++) {
                loc = new Location(game.getGameWorld(), x, y, z);
                locs.add(loc);
            }
        } else {
            for(int j = 0; j < game.getTeams().size(); j++) {
                loc = new Location(game.getGameWorld(), x, y, z);
                locs.add(loc);
            }
        }
        return locs;
    }

    public static Game getGameByCreator(UUID uuid) {
        for(Game g : games) {
            if(g.getCreator().equals(uuid)) {
                return g;
            }
        }
        return null;
    }

    public static Game getGameByPlayer(UUID uuid) {
        for(Game g : games) {
            if(g.getPlayers().contains(uuid)) {
                return g;
            }
        }
        return null;
    }

    public static List<Game> getGames() {
        return games;
    }

    public static void displayBorderInfo(Game game) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(UUID uuid : game.getPlayers()) {
                    if(Bukkit.getPlayer(uuid) != null) {
                        if(game.getGameWorld() != null) {
                            String msg = ChatColor.translateAlternateColorCodes('&', UHC.getMessages().getBorderUpdateMsg().replace("%borderSize%", String.valueOf((int)game.getGameWorld().getWorldBorder().getSize())));
                            Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                        }
                    }
                }
            }
        }.runTaskTimer(UHC.getInstance(), 0, 20);
    }

    public static void spawnGameInfo(Game g) {
        for(Entity e : g.getGameWorld().getEntitiesByClass(ArmorStand.class)) {
            if(!e.getCustomName().equals(ChatColor.RED + "" + ChatColor.BOLD + "Leave UHC")) {
                e.remove();
            }
        }
        //35, 35
        ArmorStand worldBorderSizeInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+10, 200, g.getLobbyDiameter()/2+10), ArmorStand.class);
        worldBorderSizeInfo.setInvisible(true);
        worldBorderSizeInfo.setGravity(false);
        worldBorderSizeInfo.setInvulnerable(true);
        worldBorderSizeInfo.setCustomNameVisible(true);
        worldBorderSizeInfo.setCustomName("World Border Diameter: " + ChatColor.AQUA + (int)g.getBorderSize() + " blocks");
        //25, 35
        ArmorStand worldBorderShrinkSpeedInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2, 200, g.getLobbyDiameter()/2+10), ArmorStand.class);
        worldBorderShrinkSpeedInfo.setInvisible(true);
        worldBorderShrinkSpeedInfo.setGravity(false);
        worldBorderShrinkSpeedInfo.setInvulnerable(true);
        worldBorderShrinkSpeedInfo.setCustomNameVisible(true);
        worldBorderShrinkSpeedInfo.setCustomName("World Border Shrink Duration: " + ChatColor.AQUA + g.getBorderShrinkSpeed()/60 + " minutes");
        //35, 25
        ArmorStand gracePeriodDurationInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+10, 200, g.getLobbyDiameter()/2), ArmorStand.class);
        gracePeriodDurationInfo.setInvisible(true);
        gracePeriodDurationInfo.setGravity(false);
        gracePeriodDurationInfo.setInvulnerable(true);
        gracePeriodDurationInfo.setCustomNameVisible(true);
        gracePeriodDurationInfo.setCustomName("Grace Period Duration: " + ChatColor.AQUA + g.getGracePeriodDuration()/60 + " minutes");
        //25, 25
        ArmorStand gracePeriodMultiplierInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2, 200, g.getLobbyDiameter()/2), ArmorStand.class);
        gracePeriodMultiplierInfo.setInvisible(true);
        gracePeriodMultiplierInfo.setGravity(false);
        gracePeriodMultiplierInfo.setInvulnerable(true);
        gracePeriodMultiplierInfo.setCustomNameVisible(true);
        gracePeriodMultiplierInfo.setCustomName("Grace Period Health Amplifier: " + ChatColor.AQUA + g.getGracePeriodHealthAmplifier());
        //30, 30
        ArmorStand friendlyFireInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+5, 200, g.getLobbyDiameter()/2+5), ArmorStand.class);
        friendlyFireInfo.setInvisible(true);
        friendlyFireInfo.setGravity(false);
        friendlyFireInfo.setInvulnerable(true);
        friendlyFireInfo.setCustomNameVisible(true);
        friendlyFireInfo.setCustomName("Friendly Fire: " + ChatColor.AQUA + g.getFriendlyFire());
        //25, 30
        ArmorStand naturalRegenerationInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2, 200, g.getLobbyDiameter()/2+5), ArmorStand.class);
        naturalRegenerationInfo.setInvisible(true);
        naturalRegenerationInfo.setGravity(false);
        naturalRegenerationInfo.setInvulnerable(true);
        naturalRegenerationInfo.setCustomNameVisible(true);
        naturalRegenerationInfo.setCustomName("Natural Regeneration: " + ChatColor.AQUA + g.getNaturalRegeneration());
        //30, 25
        ArmorStand scenariosInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+5, 200, g.getLobbyDiameter()/2), ArmorStand.class);
        scenariosInfo.setInvisible(true);
        scenariosInfo.setGravity(false);
        scenariosInfo.setInvulnerable(true);
        scenariosInfo.setCustomNameVisible(true);
        scenariosInfo.setCustomName("Type " + ChatColor.AQUA + "/scenarios " + ChatColor.WHITE + "for more info");
        ArmorStand teamsInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+5, 199.5, g.getLobbyDiameter()/2), ArmorStand.class);
        teamsInfo.setInvisible(true);
        teamsInfo.setGravity(false);
        teamsInfo.setInvulnerable(true);
        teamsInfo.setCustomNameVisible(true);
        teamsInfo.setCustomName("Type " + ChatColor.AQUA + "/uhc jointeam " + ChatColor.WHITE + "in order to join a team");
        //30, 35
        ArmorStand teamsEnabledInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+5, 200, g.getLobbyDiameter()/2+10), ArmorStand.class);
        teamsEnabledInfo.setInvisible(true);
        teamsEnabledInfo.setGravity(false);
        teamsEnabledInfo.setInvulnerable(true);
        teamsEnabledInfo.setCustomNameVisible(true);
        teamsEnabledInfo.setCustomName("Teams Enabled: " + ChatColor.AQUA + g.isTeamsEnabled());
        //35, 30
        ArmorStand maxTeamMembersInfo = g.getGameWorld().spawn(new Location(g.getGameWorld(), g.getLobbyDiameter()/2+10, 200, g.getLobbyDiameter()/2+5), ArmorStand.class);
        maxTeamMembersInfo.setInvisible(true);
        maxTeamMembersInfo.setGravity(false);
        maxTeamMembersInfo.setInvulnerable(true);
        maxTeamMembersInfo.setCustomNameVisible(true);
        maxTeamMembersInfo.setCustomName("Maximum Amount of Players Per Team: " + ChatColor.AQUA + g.getTeamMembersMax());
    }

}
