package com.optimuseprime.uhc.commands;

import com.optimuseprime.uhc.Game;
import com.optimuseprime.uhc.Messages;
import com.optimuseprime.uhc.UHC;
import com.optimuseprime.uhc.managers.GameManager;
import com.optimuseprime.uhc.managers.ScenariosManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Scenarios implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        Player p;
        if(sender instanceof Player) {
            p = (Player) sender;
        } else {
            return false;
        }

        Game g = GameManager.getGameByPlayer(p.getUniqueId());
        if(g == null) {
            p.sendMessage(Messages.placePlaceholders(UHC.getMessages().getHaveToJoinUHCMsg(),p));
        }
        ScenariosManager.openScenariosInventory(p);
        return false;
    }
}
