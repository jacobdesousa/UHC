package me.nitro.uhc;

import me.nitro.uhc.features.AppleRates;
import me.nitro.uhc.features.Border;
import me.nitro.uhc.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class Config implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(cmd.getName().equalsIgnoreCase("uhc") || cmd.getName().equalsIgnoreCase("config"))) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(UHC.getMessage("playerOnly"));
            return false;
        }

        Player p = (Player) sender;
        Inventory inv = Bukkit.createInventory(null , 27, Utils.colour("&c&lUHC Config"));
        inv.setItem(1, PVP.getIcon());
        inv.setItem(3, AppleRates.getIcon());
        inv.setItem(5, Nether.getIcon());
        inv.setItem(7, Border.getIcon());
        inv.setItem(11, GoldenHeads.getIcon());
        inv.setItem(13, PearlDamage.getIcon());
        inv.setItem(15, Absorption.getIcon());
        inv.setItem(19, HostileMobs.getIcon());
        inv.setItem(21, HeadDrop.getIcon());
        inv.setItem(23, NotchApples.getIcon());
        inv.setItem(25, GhastTears.getIcon());

        p.openInventory(inv);

        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals(Utils.colour("&c&lUHC Config"))) {
            e.setCancelled(true);
        }
    }
}
