package com.optimuseprime.uhc.commands;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.GameStatus;
import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UHCCommand implements CommandExecutor {

    Player p;
    String[] args;

    @Override
    public boolean onCommand(CommandSender sender,Command command, String label, String[] arg) {
        if(sender instanceof Player) {
            p = (Player) sender;
        } else {
            return false;
        }
        args = arg;
        if(args.length == 0) {
            help();
            return false;
        }
        switch (args[0]) {
            case "create" -> create();
            case "start" -> start();
            case "stop" -> stop();
            case "jointeam" -> showGUI();
            case "teaminventory" -> openTeamInventory();
            case "join" -> EntryPointManager.openInventory(p);
            case "settings" -> settings();
            case "delete" -> delete();
            default -> help();
        }

        return false;
    }

    private void help() {
        for(String s : UHC.getMessages().getHelpPage()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.placePlaceholders(s, p)));
        }
    }

    private void create() {
        if(!p.hasPermission("uhc.admin")) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getNoPermissionMsg(), p));
            return;
        }
        if(GameManager.getGameByCreator(p.getUniqueId()) != null) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getUhcAlreadyCreatedMsg(), p));
            return;
        }
        File f = new File(UHC.getInstance().getDataFolder(), "/defaultConfig.json");
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getUhcCreationStartMsg(), p));
        Game g = UHC.getGson().fromJson(fr,Game.class);
        g.setPlayers(new HashMap<>());
        g.setCreator(p.getUniqueId());
        g.setGameStatus(GameStatus.IN_LOBBY);
        try {
            GameManager.create(p, g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PlayerManager.initPlayer(p, GameManager.getGameByCreator(p.getUniqueId()), true);
        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getUhcCreationSuccessMsg(), p));
    }

    private void delete() {
        Game g = GameManager.getGameByPlayer(p.getUniqueId());
        if(g == null) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(), p));
            return;
        }
        try {
            GameManager.deleteUHC(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        for(Game g : GameManager.getGames()) {
            if(g.getCreator().equals(p.getUniqueId()) || p.isOp() || p.hasPermission("uhc.admin")) {
                if(g.getPlayers().size() < 2) {
                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getNotEnoughPlayersToStartMsg(), p));
                    return;
                }
                if(!g.getGameStatus().equals(GameStatus.IN_LOBBY)) {
                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGameAlreadyStartMsg(), p));
                    return;
                }
                GameManager.startGame(g);
            } else {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getNoPermissionMsg(), p));
            }
        }
    }

    private void stop() {
        for(Game g : GameManager.getGames()) {
            if(g.getCreator().equals(p.getUniqueId()) || p.isOp() || p.hasPermission("uhc.admin")) {
                if(!g.getGameStatus().equals(GameStatus.IN_GAME)) {
                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getGameNeverStartMsg(), p));
                } else if(g.getGameStatus().equals(GameStatus.IN_GAME)){
                    GameManager.stopGame(g, true, null, false);
                }
            } else {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getNoPermissionMsg(), p));
            }
        }
    }

    private void showGUI() {
        if(PlayerManager.isInGame(p.getUniqueId()) != null) {
            TeamManager.showTeamGUI(PlayerManager.isInGame(p.getUniqueId()), p);
        } else {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(), p));
        }
    }

    private void openTeamInventory() {
        if(GameManager.getGameByPlayer(p.getUniqueId()) != null) {
            if(!GameManager.getGameByPlayer(p.getUniqueId()).isTeamsEnabled()) {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getTeamsHaveToBeEnabledMsg(), p));
                return;
            }
            if(TeamManager.getTeamByPlayer(GameManager.getGameByPlayer(p.getUniqueId()), p.getUniqueId()) != null) {
                TeamManager.openTeamInventory(PlayerManager.isInGame(p.getUniqueId()), p);
            } else {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinTeamMsg(), p));
            }
        } else {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(), p));
        }
    }

    private void settings() {
        Game g = GameManager.getGameByPlayer(p.getUniqueId());
        if(g == null) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(), p));
            return;
        }
        if(g.getCreator().equals(p.getUniqueId()) || p.isOp() || p.hasPermission("uhc.admin")) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("worldbordersize") || args[1].equalsIgnoreCase("bordersize")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", String.valueOf(g.getBorderSize()))
                            .replace("%newValue%", String.valueOf(Integer.parseInt(args[2])))
                            .replace("%setting%", "World Border Size");
                    g.setBorderSize(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("worldbordershrinkduration") || args[1].equalsIgnoreCase("bordershrinkduration")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.getBorderShrinkSpeed() + " seconds")
                            .replace("%newValue%", Integer.parseInt(args[2]) + " seconds")
                            .replace("%setting%", "World Border Shrink Duration");
                    g.setBorderShrinkSpeed(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("worldborderdamageamount") || args[1].equalsIgnoreCase("borderdamageamount")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", String.valueOf(g.getBorderDamageAmount()))
                            .replace("%newValue%", String.valueOf(Integer.parseInt(args[2])))
                            .replace("%setting%", "World Border Damage Amount");
                    g.setBorderDamageAmount(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("worldborderdamagebuffer") || args[1].equalsIgnoreCase("borderdamagebuffer")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", String.valueOf(g.getBorderDamageBuffer()))
                            .replace("%newValue%", String.valueOf(Integer.parseInt(args[2])))
                            .replace("%setting%", "World Border Damage Buffer");
                    g.setBorderDamageBuffer(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("graceperiodduration") || args[1].equalsIgnoreCase("graceperioduration")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.getGracePeriodDuration() + " seconds")
                            .replace("%newValue%", Integer.parseInt(args[2]) + " seconds")
                            .replace("%setting%", "Grace Period Duration");
                    g.setGracePeriodDuration(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("graceperiodhealthamplifier")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", String.valueOf(g.getGracePeriodHealthAmplifier()))
                            .replace("%newValue%", String.valueOf(Integer.parseInt(args[2])))
                            .replace("%setting%", "Grace Period Health Amplifier");
                    g.setGracePeriodHealthAmplifier(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("teamsenabled")) {
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.isTeamsEnabled().toString().toLowerCase())
                            .replace("%newValue%", String.valueOf(Boolean.valueOf(args[2].toLowerCase())))
                            .replace("%setting%", "Teams Enabled");
                    g.setTeamsEnabled(Boolean.valueOf(args[2].toLowerCase()));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("friendlyfire")) {
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.getFriendlyFire().toString().toLowerCase())
                            .replace("%newValue%", String.valueOf(Boolean.valueOf(args[2].toLowerCase())))
                            .replace("%setting%", "Friendly Fire");
                    g.setFriendlyFire(Boolean.valueOf(args[2].toLowerCase()));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("teammembersmax") || args[1].equalsIgnoreCase("teamembersmax")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", String.valueOf(g.getTeamMembersMax()))
                            .replace("%newValue%", String.valueOf(Integer.parseInt(args[2])))
                            .replace("%setting%", "Team Members Max");
                    g.setTeamMembersMax(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("teaminventorysize")) {
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                    }
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.getTeamInventorySize() + " slots")
                            .replace("%newValue%", Integer.parseInt(args[2]) + " slots")
                            .replace("%setting%", "Team Inventory Size");
                    g.setTeamInventorySize(Integer.parseInt(args[2]));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                } else if (args[1].equalsIgnoreCase("naturalregeneration")) {
                    String msg = Messages.placePlaceholders(UHC.getMessages().getSettingChangeMsg(), p);
                    msg = msg
                            .replace("%oldValue%", g.getNaturalRegeneration().toString().toLowerCase())
                            .replace("%newValue%", String.valueOf(Boolean.valueOf(args[2].toLowerCase())))
                            .replace("%setting%", "Natural Regeneration");
                    g.setNaturalRegeneration(Boolean.valueOf(args[2].toLowerCase()));
                    GameManager.spawnGameInfo(g);
                    try {
                        GameManager.saveGame(g);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(msg);
                }
            } else if(args.length == 2) {
                if(args[1].equalsIgnoreCase("view")) {
                    for(String s : UHC.getMessages().getSettingsViewMsg()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.placeSettingsPlaceholders(s, g)));
                    }
                } else if(args[1].equalsIgnoreCase("lootbags")) {
                    p.openInventory(LootbagManager.getLootbagEditInventoryPageOne(g));
                } else if(args[1].equalsIgnoreCase("scenarios")) {
                    ScenariosManager.openScenariosInventory(p);
                } else {
                    p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
                }
            } else {
                p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getInvalidArgumentsMsg(), p));
            }
        } else {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getNoPermissionMsg(), p));
        }
    }
}
