package com.optimuseprime.uhc;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public class Team {

    private List<UUID> players;
    private String teamName;
    private ChatColor color;
    private Material material;
    private HashMap<Integer, Map.Entry<Material, Integer>> teamInventoryHashMap;

    public Team(String teamName, ChatColor color, Material material, Integer teamInventorySize) {
        this.players = new ArrayList<>();
        this.teamName = teamName;
        this.color = color;
        this.material = material;
        this.teamInventoryHashMap = new HashMap<>();
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public HashMap<Integer, Map.Entry<Material, Integer>> getTeamInventoryHashMap() {
        return teamInventoryHashMap;
    }

    public void setTeamInventoryHashMap(HashMap<Integer, Map.Entry<Material, Integer>> teamInventory) {
        this.teamInventoryHashMap = teamInventory;
    }
}
