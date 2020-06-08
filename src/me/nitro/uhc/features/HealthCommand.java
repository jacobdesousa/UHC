package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("h") && !cmd.getName().equalsIgnoreCase("health")) {
            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(UHC.getMessage("healthUsage"));
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(UHC.getMessage("invalidPlayer"));
            return false;
        }

        /*Double health = (Bukkit.getPlayer(args[0]).getHealth()) / 2;
        String[] splitter = health.toString().split("\\.");
        if (splitter[1].length() > 1) {
            health = health + 0.25;
        }*/
        Player p = Bukkit.getPlayer(args[0]);
        double displayedHealth = p.getHealth() / p.getMaxHealth() * p.getHealthScale();
        sender.sendMessage(UHC.getPrefix() + Utils.colour("&7" + Bukkit.getPlayer(args[0]).getName() + "'s Health: &c" + displayedHealth/2 + "❤&7."));
        return false;
    }
}
