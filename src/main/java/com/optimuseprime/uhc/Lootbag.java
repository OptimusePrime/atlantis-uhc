package com.optimuseprime.uhc;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.*;

public class Lootbag {

    private String name;
    private Integer customModelData;
    private List<String> lootTable;
    private HashMap<EntityType, Integer> entites;

    public Lootbag(String name, Integer customModelData, HashMap<EntityType, Integer> entities) {
        this.name = name;
        this.customModelData = customModelData;
        this.entites = entities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    //Material, Integer|String, Integer
    //New: UUID|Material|Amount, Chance
    public HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> getLootTable() {
        HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> hm = new HashMap<>();
        String[] pom;
        for(String s : lootTable) {
            pom = s.split("\\|");
            hm.put(pom[0], new AbstractMap.SimpleEntry<>(Material.getMaterial(pom[1]), new AbstractMap.SimpleEntry<>(Integer.valueOf(pom[2]), Integer.valueOf(pom[3]))));
        }
        return hm;
    }

    public void setLootTable(HashMap<String, Map.Entry<Material, Map.Entry<Integer, Integer>>> lootTable) {
        List<String> l = new ArrayList<>();
        for(String value : lootTable.keySet()) {
            String s = value + "|" + lootTable.get(value).getKey().toString() + "|" + lootTable.get(value).getValue().getKey() + "|" + lootTable.get(value).getValue().getValue();
            l.add(s);
        }
        this.lootTable = l;
    }

    public HashMap<EntityType, Integer> getEntites() {
        return entites;
    }

    public void setEntites(HashMap<EntityType, Integer> entites) {
        this.entites = entites;
    }
}
