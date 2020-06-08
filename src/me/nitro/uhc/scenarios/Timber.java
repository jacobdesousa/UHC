package me.nitro.uhc.scenarios;

import me.nitro.uhc.Cuboid;
import me.nitro.uhc.Scenario;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Timber implements Scenario, CommandExecutor, Listener {

    public static boolean state;

    public Timber() {
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
        if (!cmd.getName().equalsIgnoreCase("timber")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("timberDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("timberEnable"));
            return false;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!getState()) {
            return;
        }

        if (!(e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)) {
            return;
        }

        Block brokenBlock = e.getBlock();
        int yOffset = 0;

        if (brokenBlock.getType() == Material.LOG && (brokenBlock.getData() == (byte) 0 || brokenBlock.getData() == (byte) 2)) {
            yOffset = 10;
        } else {
            yOffset = 30;
        }

        Location loc1 = new Location(brokenBlock.getWorld(), brokenBlock.getX() + 2, brokenBlock.getY(), brokenBlock.getZ() + 2);
        Location loc2 = new Location(brokenBlock.getWorld(), brokenBlock.getX() - 2, brokenBlock.getY() + yOffset, brokenBlock.getZ() - 2);

        Cuboid cube = new Cuboid(loc1, loc2);
        Material material = brokenBlock.getType();
        int quantity = 0;
        byte damage = brokenBlock.getData();

        for (Block block : cube.getBlocks()) {
            if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
                block.setType(Material.AIR);
                quantity++;
            }
        }

        for (int x = 0; x < quantity; x++) {
            brokenBlock.getWorld().dropItemNaturally(brokenBlock.getLocation(), new ItemStack(material, 1, damage));
        }
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.LOG, "&c&lTimber", "&8&m----------~&7Timber is a scenario where you can fell~&7entire trees with one swing.~&8&m----------");
    }
}
