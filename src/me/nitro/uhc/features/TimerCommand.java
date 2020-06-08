package me.nitro.uhc.features;

import me.nitro.uhc.GameManager;
import me.nitro.uhc.GameState;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("gametime")) {
            return false;
        }
        if (GameManager.getState() == GameState.STOPPED) {
            sender.sendMessage(UHC.getMessage("noGame"));
            return false;
        }
        sender.sendMessage(UHC.getPrefix() + Utils.colour("&7The elapsed time is: &c" + GameManager.getElapsedTime() + "&7."));
        return false;
    }
}

