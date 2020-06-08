package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveAll implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("giveall")) {
            return false;
        }

        if (!sender.hasPermission("uhc.giveall")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(UHC.getMessage("giveallUsage"));
            return false;
        }

        String itemName = args[0].toUpperCase();
        if (Material.matchMaterial(itemName) == null) {
            sender.sendMessage(UHC.getMessage("invalidItem"));
            return false;
        }

        if (!Utils.isInt(args[1])) {
            sender.sendMessage(UHC.getMessage("invalidQuantity"));
            return false;
        }

        Material itemType = Material.matchMaterial(itemName);
        int quantity = Integer.parseInt(args[1]);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("uhc.admin")) {
                p.getInventory().addItem(new ItemStack(itemType, quantity));
            }
        }

        Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&7All players have been given &c") + quantity + " " + itemName + Utils.colour("&7."));

        return false;
    }
}
