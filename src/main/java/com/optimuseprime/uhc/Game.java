package com.optimuseprime.uhc;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.*;

public class Game {

    private Map<UUID, List<String>> players;
    private List<Team> teams;
    private GameStatus gameStatus;

    private UUID creator;

    private Integer borderSize = 1512;
    private Integer borderShrinkDuration = 5400;
    private Integer borderDamageAmount = 1;
    private Integer borderDamageBuffer = 1;

    private Integer lobbyDiameter = 50;
    private Integer maxPlayers = 24;

    private Integer gracePeriodDuration = 1200;
    private Integer gracePeriodHealthAmplifier = 4;
    private Boolean isPvPEnabled = false;

    private Boolean teamsEnabled = false;
    private Integer teamMembersMax = 2;
    private Boolean friendlyFire = false;
    private Integer teamInventorySize = 27;

    private Boolean naturalRegeneration = false;

    private Boolean lootBagsEnabled = true;
    private List<Lootbag> lootbags;

    private Map<String, Boolean> scenarios;

    private transient int gracePeriodTaskId;
    private transient int deleteGameTaskId;

    public Game() {
        this.players = new HashMap<>();
        this.teams = new ArrayList<>();
        this.lootbags = new ArrayList<>();
        this.scenarios = new HashMap<>();
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }

    public Integer getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(Integer borderSize) {
        this.borderSize = borderSize;
    }

    public Integer getBorderShrinkSpeed() {
        return borderShrinkDuration;
    }

    public void setBorderShrinkSpeed(Integer shrinkSpeed) {
        this.borderShrinkDuration = shrinkSpeed;
    }

    public Integer getBorderDamageAmount() {
        return borderDamageAmount;
    }

    public void setBorderDamageAmount(Integer borderDamageAmount) {
        this.borderDamageAmount = borderDamageAmount;
    }

    public Integer getBorderDamageBuffer() {
        return borderDamageBuffer;
    }

    public void setBorderDamageBuffer(Integer borderDamageBuffer) {
        this.borderDamageBuffer = borderDamageBuffer;
    }

    public Integer getLobbyDiameter() {
        return lobbyDiameter;
    }

    public void setLobbyDiameter(Integer lobbyDiameter) {
        this.lobbyDiameter = lobbyDiameter;
    }

    public World getGameWorld() {
        return Bukkit.getWorld(creator.toString());
    }

    public Map<UUID, List<String>> getPlayersHashMap() {
        return players;
    }

    public List<UUID> getPlayers() {

        return players.keySet().stream().toList();
    }

    public void setPlayers(Map<UUID, List<String>> players) {
        this.players = players;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Integer getGracePeriodDuration() {
        return gracePeriodDuration;
    }

    public void setGracePeriodDuration(Integer gracePeriodDuration) {
        this.gracePeriodDuration = gracePeriodDuration;
    }

    public Integer getGracePeriodHealthAmplifier() {
        return gracePeriodHealthAmplifier;
    }

    public void setGracePeriodHealthAmplifier(Integer gracePeriodHealthAmplifier) {
        this.gracePeriodHealthAmplifier = gracePeriodHealthAmplifier;
    }

    public Boolean isPvPEnabled() {
        return isPvPEnabled;
    }

    public void setPvPEnabled(Boolean PvPEnabled) {
        isPvPEnabled = PvPEnabled;
    }

    public Boolean isTeamsEnabled() {
        return teamsEnabled;
    }

    public void setTeamsEnabled(Boolean yesOrNo) {
        this.teamsEnabled = yesOrNo;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Integer getTeamMembersMax() {
        return teamMembersMax;
    }

    public void setTeamMembersMax(Integer teamMembersMax) {
        this.teamMembersMax = teamMembersMax;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Boolean getFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(Boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public Integer getTeamInventorySize() {
        return teamInventorySize;
    }

    public void setTeamInventorySize(Integer teamInventorySize) {
        this.teamInventorySize = teamInventorySize;
    }

    public Boolean getNaturalRegeneration() {
        return naturalRegeneration;
    }

    public void setNaturalRegeneration(Boolean naturalRegeneration) {
        this.naturalRegeneration = naturalRegeneration;
        getGameWorld().setGameRule(GameRule.NATURAL_REGENERATION, naturalRegeneration);
    }

    public List<Lootbag> getLootbags() {
        return lootbags;
    }

    public void setLootbags(List<Lootbag> lootbags) {
        this.lootbags = lootbags;
    }

    public Boolean getLootBagsEnabled() {
        return lootBagsEnabled;
    }

    public void setLootBagsEnabled(Boolean lootBagsEnabled) {
        this.lootBagsEnabled = lootBagsEnabled;
    }

    public int getGracePeriodTaskId() {
        return gracePeriodTaskId;
    }

    public void setGracePeriodTaskId(int gracePeriodTaskId) {
        this.gracePeriodTaskId = gracePeriodTaskId;
    }

    public int getDeleteGameTaskId() {
        return deleteGameTaskId;
    }

    public void setDeleteGameTaskId(int deleteGameTaskId) {
        this.deleteGameTaskId = deleteGameTaskId;
    }

    public Map<String, Boolean> getScenarios() {
        return scenarios;
    }

    public void setScenarios(Map<String, Boolean> scenarios) {
        this.scenarios = scenarios;
    }
}
