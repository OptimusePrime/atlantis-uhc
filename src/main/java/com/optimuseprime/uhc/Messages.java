package com.optimuseprime.uhc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Messages {

    private String prefix = "[" + "&bUHC&f" + "]-";
    private List<String> helpPage = Arrays.asList("%prefix%---------------[&bUHC Help Page&f]---------------", "/uhc create", "/uhc settings", "/uhc settings view", "/scenarios",
            "/uhc settings borderSize <diameter>",
            "/uhc settings borderShrinkDuration <shrink duration in seconds>",
            "/uhc settings borderDamageAmount <damage amount>", "/uhc settings borderDamageBuffer <damage buffer>", "/uhc settings gracePeriodDuration <duration in seconds>",
            "/uhc settings gracePeriodHealthAmplifier <health amplifier>", "/uhc settings teamsEnabled <true/false>", "/uhc settings teamMembersMax <max members per team>",
            "/uhc settings friendlyFire <true/false>", "/uhc settings teamInventorySize <size in increments of 9>", "/uhc settings lootBags", "/uhc settings scenarios",
            "/uhc join", "/uhc joinTeam", "/uhc start", "/teamInventory", "/uhc stop", "%prefix%---------------[&bUHC Help Page&f]---------------");
    private String noPermissionMsg = "%prefix%You do not have permission to execute this command!";
    private String haveToJoinUHCMsg = "%prefix%In order to use this command you need to join a UHC game.";
    private String uhcCreationStartMsg = "%prefix%Please wait while your UHC world is being generated...";
    private String uhcCreationSuccessMsg = "%prefix%UHC world has been successfully generated!";
    private String joinedTeamMsg = "%prefix%You have joined %team%.";
    private String globalChatPrefix = "!";
    private String chat = "%teamColor%[%team%] &f[&b%player%&f]-%msg%";
    private String alreadyJoinedTeamMsg = "%prefix%You have already joined this team.";
    private String teamsHaveToBeEnabledMsg = "%prefix%Teams have to be enabled in order to use this command.";
    private String startCountdownMsg = "&aStart in %countdown% seconds!";
    private String gameStartMsg = "%prefix%Game has started, good luck!";
    private String gracePeriodCountdownMsg = "%prefix%Grace Period ends in %countdown% minutes!";
    private String gracePeriodEndedMsg = "%prefix%Grace Period has ended, World Border has started to shrink. PvP has been enabled!";
    private List<String> tipsAndJokesButMostlyJokes = Arrays.asList("Why can't you trust atoms? They make up everything.", "There are 4 fundamental forces in the universe. Air, water...No!");
    private String gameManualStopMsg = "%prefix%Game has been manually stopped.";
    private String gameWinMsg = "%prefix%%winner% has won the game!";
    private String gameAlreadyStartMsg = "%prefix%Game has already started.";
    private String gameNeverStartMsg = "%prefix%Game has never started.";
    private String uhcAlreadyCreatedMsg = "%prefix%UHC world has already been created.";
    private String alreadyJoinedGameMsg = "%prefix%You have already joined this game.";
    private String settingChangeMsg = "%prefix%The %setting% has been changed from %oldValue% to %newValue%.";
    private String invalidArgumentsMsg = "%prefix%Invalid arguments.";
    private String haveToJoinTeamMsg = "%prefix%You have to join a team first.";
    private List<String> settingsViewMsg = Arrays.asList("%prefix%---------------[&bUHC Settings&f]---------------", "World Border Diameter: &b%borderSize% blocks",
            "World Border Shrink Duration: &b%borderShrinkDuration%", "World Border Damage Amount: &b%borderDamageAmount%", "World Border Damage Buffer: &b%borderDamageBuffer%",
            "Grace Period Duration: &b%gracePeriodDuration%", "Grace Period Health Amplifier: &b%gracePeriodHealthAmplifier%", "Teams Enabled: &b%teamsEnabled%",
            "Maximum Amount of Players Per Team: &b%teamMembersMax%", "Friendly Fire: &b%friendlyFire%", "Team Inventory Size: &b%teamInventorySize%", "Natural Regeneration: &b%naturalRegeneration%",
            "Lootbags Enabled: &b%lootbagsEnabled%",
            "%prefix%---------------[&bUHC Settings&f]---------------");
    private String borderUpdateMsg = "&bBorder Diameter: %borderSize%";
    private String playerNotInTeamKickMsg = "You have been kicked from the game because you were not in any team.";
    private String uhcDeletionSuccessMsg = "%prefix%UHC world has been successfully deleted.";
    private String uhcWillBeDeletedInMsg = "%prefix%UHC world will be deleted in %countdown% minutes.";
    private String notEnoughPlayersToStartMsg = "%prefix%There is not enough players to start the game, a minimum of two players must be present.";
    private String teamFullMsg = "%prefix%The team is full.";

    public static String placePlaceholders(String str, Object... objs) {
        String s = str;
        s = s.replace("%prefix%", UHC.getMessages().getPrefix());
        for(Object obj : objs) {
            if(obj instanceof Player) {
                s = s.replace("%player%", ((Player)obj).getName());
            } else if(obj instanceof Team t) {
                s = s.replace("%team%", t.getTeamName());
                s = s.replace("%teamColor%", "" + t.getColor());
            } else if(obj instanceof Integer) {
                s = s.replace("%countdown%", String.valueOf(obj));
            } else if(obj instanceof String) {
                s = s.replace("%msg%", (String)obj);
            }
        }
        return s;
    }

    public static String placeSettingsPlaceholders(String str, Game g) {
        String s = str;
        s = s.replace("%borderSize%", String.valueOf(g.getBorderSize())).replace("%borderShrinkDuration%", String.valueOf(g.getBorderShrinkSpeed()/60 + " minutes"))
             .replace("%borderDamageAmount%", String.valueOf(g.getBorderDamageAmount())).replace("%borderDamageBuffer%", String.valueOf(g.getBorderDamageBuffer()))
             .replace("%gracePeriodDuration%", String.valueOf(g.getGracePeriodDuration()/60) + " minutes")
             .replace("%gracePeriodHealthAmplifier%", String.valueOf(g.getGracePeriodHealthAmplifier())).replace("%teamsEnabled%", String.valueOf(g.isTeamsEnabled()))
             .replace("%teamMembersMax%", String.valueOf(g.getTeamMembersMax())).replace("%friendlyFire%", String.valueOf(g.getFriendlyFire()))
             .replace("%teamInventorySize%", String.valueOf(g.getTeamInventorySize() + " slots")).replace("%naturalRegeneration%", String.valueOf(g.getNaturalRegeneration())).replace("%lootbagsEnabled%", String.valueOf(g.getLootBagsEnabled()))
             .replace("%prefix%", UHC.getMessages().getPrefix());
        return s;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public List<String> getHelpPage() {
        return helpPage;
    }

    public String getNoPermissionMsg() {
        return ChatColor.translateAlternateColorCodes('&', noPermissionMsg);
    }

    public String getHaveToJoinUHCMsg() {
        return ChatColor.translateAlternateColorCodes('&', haveToJoinUHCMsg);
    }

    public String getUhcCreationStartMsg() {
        return ChatColor.translateAlternateColorCodes('&', uhcCreationStartMsg);
    }

    public String getUhcCreationSuccessMsg() {
        return ChatColor.translateAlternateColorCodes('&', uhcCreationSuccessMsg);
    }

    public String getJoinedTeamMsg() {
        return ChatColor.translateAlternateColorCodes('&', joinedTeamMsg);
    }

    public String getGlobalChatPrefix() {
        return ChatColor.translateAlternateColorCodes('&', globalChatPrefix);
    }

    public String getChat() {
        return ChatColor.translateAlternateColorCodes('&', chat);
    }

    public String getAlreadyJoinedTeamMsg() {
        return ChatColor.translateAlternateColorCodes('&', alreadyJoinedTeamMsg);
    }

    public String getStartCountdownMsg() {
        return ChatColor.translateAlternateColorCodes('&', startCountdownMsg);
    }

    public String getGameStartMsg() {
        return ChatColor.translateAlternateColorCodes('&', gameStartMsg);
    }

    public String getGracePeriodCountdownMsg() {
        return ChatColor.translateAlternateColorCodes('&', gracePeriodCountdownMsg);
    }

    public String getGracePeriodEndedMsg() {
        return ChatColor.translateAlternateColorCodes('&', gracePeriodEndedMsg);
    }

    public List<String> getTipsAndJokesButMostlyJokes() {
        return tipsAndJokesButMostlyJokes;
    }

    public String getGameManualStopMsg() {
        return ChatColor.translateAlternateColorCodes('&', gameManualStopMsg);
    }

    public String getGameWinMsg() {
        return ChatColor.translateAlternateColorCodes('&', gameWinMsg);
    }

    public String getGameAlreadyStartMsg() {
        return ChatColor.translateAlternateColorCodes('&', gameAlreadyStartMsg);
    }

    public String getGameNeverStartMsg() {
        return ChatColor.translateAlternateColorCodes('&', gameNeverStartMsg);
    }

    public String getTeamsHaveToBeEnabledMsg() {
        return ChatColor.translateAlternateColorCodes('&', teamsHaveToBeEnabledMsg);
    }

    public String getUhcAlreadyCreatedMsg() {
        return ChatColor.translateAlternateColorCodes('&', uhcAlreadyCreatedMsg);
    }

    public String getAlreadyJoinedGameMsg() {
        return ChatColor.translateAlternateColorCodes('&', alreadyJoinedGameMsg);
    }

    public String getSettingChangeMsg() {
        return ChatColor.translateAlternateColorCodes('&', settingChangeMsg);
    }

    public String getInvalidArgumentsMsg() {
        return ChatColor.translateAlternateColorCodes('&', invalidArgumentsMsg);
    }

    public String getHaveToJoinTeamMsg() {
        return ChatColor.translateAlternateColorCodes('&', haveToJoinTeamMsg);
    }

    public List<String> getSettingsViewMsg() {
        return settingsViewMsg;
    }

    public String getBorderUpdateMsg() {
        return ChatColor.translateAlternateColorCodes('&', borderUpdateMsg);
    }

    public String getPlayerNotInTeamKickMsg() {
        return ChatColor.translateAlternateColorCodes('&', playerNotInTeamKickMsg);
    }

    public String getUhcDeletionSuccessMsg() {
        return ChatColor.translateAlternateColorCodes('&', uhcDeletionSuccessMsg);
    }

    public String getUhcWillBeDeletedInMsg() {
        return ChatColor.translateAlternateColorCodes('&', uhcWillBeDeletedInMsg);
    }

    public String getNotEnoughPlayersToStartMsg() {
        return ChatColor.translateAlternateColorCodes('&', notEnoughPlayersToStartMsg);
    }

    public String getTeamFullMsg() {
        return teamFullMsg;
    }
}
