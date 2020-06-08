package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Permaday implements CommandExecutor {

    Set<World> permaday = new HashSet<World>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("permaday")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage("noPermission");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(UHC.getMessage("playerOnly"));
            return false;
        }

        Player p = (Player) sender;
        if (permaday.contains(p.getWorld())) {
            permaday.remove(p.getWorld());
            p.getWorld().setGameRuleValue("doDaylightCycle", "true");
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Permaday disabled in world: &c" + p.getWorld().getName() + "&7."));
            return false;
        } else {
            permaday.add(p.getWorld());
            p.getWorld().setGameRuleValue("doDaylightCycle", "false");
            p.getWorld().setTime(6000);
            Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7Permaday enabled in world: &c" + p.getWorld().getName() + "&7."));
            return false;
        }
    }
}
