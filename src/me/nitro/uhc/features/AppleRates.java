package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class AppleRates implements CommandExecutor, Listener {

    private static int rate;

    public AppleRates() {
        rate = 2;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("applerates")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
        }

        if (args.length != 1) {
            sender.sendMessage(UHC.getMessage("appleRatesUsage"));
            return false;
        }

        if (!Utils.isInt(args[0])) {
            sender.sendMessage(UHC.getMessage("appleRatesUsage"));
            return false;
        }

        rate = Integer.parseInt(args[0]);
        sender.sendMessage(UHC.getMessage("appleRatesChanged"));
        return false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2) {
            e.setCancelled(true);
            if (e.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) {
                return;
            }
            e.getBlock().setType(Material.AIR);
            Random rand = new Random();
            if (rand.nextInt(100) < rate) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
        if (e.getBlock().getWorld().getName().equalsIgnoreCase("lobby")) {
            return;
        }
        e.getBlock().setType(Material.AIR);
        Random rand = new Random();
        if (rand.nextInt(100) < rate) {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
        }
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.APPLE, "&c&lApple Rates", "&7&m----------~&c" + rate + "%~&7&m----------");
    }
}
