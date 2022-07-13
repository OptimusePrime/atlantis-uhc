package com.optimuseprime.uhc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.optimuseprime.uhc.commands.Scenarios;
import com.optimuseprime.uhc.commands.TeamInventory;
import com.optimuseprime.uhc.commands.UHCCommand;
import com.optimuseprime.uhc.events.*;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.events.RightClickEvents;
import com.optimuseprime.uhc.managers.LootbagManager;
import com.optimuseprime.uhc.managers.ScenariosManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class UHC extends JavaPlugin {

    private static Gson g;
    private static UHC instance;
    private static Messages messages;

    @Override
    public void onEnable() {
        instance = this;
        g = new GsonBuilder().setPrettyPrinting().create();
        registerListeners();
        registerCommands();
        createFolders();
        try {
            saveDefaultGameConfig();
            loadMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            GameManager.loadGames();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Game g : GameManager.getGames()) {
            GameManager.displayBorderInfo(g);
        }
        LootbagManager.startItemTransferCheck();
        ScenariosManager.createScenarios();
    }

    @Override
    public void onDisable() {
        GameManager.getGames().clear();
        this.getServer().getScheduler().cancelTasks(this);
    }

    public static UHC getInstance() {
        return instance;
    }

    public static Gson getGson() {
        return g;
    }

    private void registerListeners() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EntryPointEvents(), this);
        pm.registerEvents(new PlayerRespawnsEvent(), this);
        pm.registerEvents(new PlayerConnectionEvents(), this );
        pm.registerEvents(new NoPVPEvent(), this);
        pm.registerEvents(new InventoryEvents(), this);
        pm.registerEvents(new ChatEvents(), this);
        pm.registerEvents(new EntityDeathEvent(), this);
        pm.registerEvents(new RightClickEvents(), this);
    }

    private void registerCommands() {
        getCommand("uhc").setExecutor(new UHCCommand());
        getCommand("scenarios").setExecutor(new Scenarios());
        getCommand("teaminventory").setExecutor(new TeamInventory());
        getCommand("ti").setExecutor(new TeamInventory());
    }

    private void createFolders() {
        File games = new File(getDataFolder(), "games");
        if(!games.exists()) {
            games.mkdirs();
        }
    }

    private void saveDefaultGameConfig() throws IOException {
        File f = new File(getDataFolder(),"defaultConfig.json");
        if(f.exists()) {
            return;
        }
        Game game = new Game();
        game.setPlayers(null);
        game.setGameStatus(null);
        game.setTeams(null);
        LootbagManager.setLootBags(game);
        FileWriter fw = new FileWriter(f);
        getGson().toJson(game, fw);
        fw.flush();
        fw.close();
    }

    private void loadMessages() throws IOException {
        File f = new File(getDataFolder(),"messages.json");
        if(!f.exists()) {
            FileWriter fw = new FileWriter(f);
            getGson().toJson(new Messages(), fw);
            fw.flush();
            fw.close();
        }
        FileReader fr = new FileReader(f);
        messages = getGson().fromJson(fr, Messages.class);
    }

    public static Messages getMessages() {
        return messages;
    }

    public static String convertToInvisibleString(String s) {
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray()) hidden.append(ChatColor.COLOR_CHAR + "").append(c);
        return hidden.toString();
    }
}
