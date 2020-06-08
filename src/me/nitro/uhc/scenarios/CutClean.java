package me.nitro.uhc.scenarios;

import me.nitro.uhc.Scenario;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CutClean implements Scenario, CommandExecutor, Listener {

    public static boolean state;

    public CutClean() {
        disable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
    }

    public void disable() {
        state = false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("cutclean")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("cutCleanDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("cutCleanEnable"));
            return false;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (!getState()) {
            return;
        }

        for (ItemStack drops : e.getDrops()) {
            if (drops.getType() == Material.RAW_CHICKEN) {
                drops.setType(Material.COOKED_CHICKEN);
            } else if (drops.getType() == Material.PORK) {
                drops.setType(Material.GRILLED_PORK);
            } else if (drops.getType() == Material.RAW_BEEF) {
                drops.setType(Material.COOKED_BEEF);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (!getState()) {
            return;
        }

        if (block.getType() == Material.IRON_ORE) {
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
            e.getPlayer().giveExp(1);
        } else if (block.getType() == Material.GOLD_ORE) {
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
            e.getPlayer().giveExp(1);
        } else if (block.getType() == Material.GRAVEL) {
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
        }
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.IRON_INGOT, "&c&lCutClean", "&8&m----------~&7The CutClean scenario is a time saving scenario~&7where un-smelted items such as Iron and Gold Ores~&7are dropped as their refined variants.~&8&m----------");
    }
}
