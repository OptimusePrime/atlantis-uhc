package com.optimuseprime.uhc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Scenario implements Listener {

    public String getName() {
        return "";
    }

    public String getId() { return ""; }

    public List<String> getLore() {
        return new ArrayList<>();
    }

    public ItemStack getDisplayItem() {
        return null;
    }

    public Boolean getEnabled() {
        return false;
    }

    @EventHandler
    public void execute(GenericGameEvent e) { }
}
