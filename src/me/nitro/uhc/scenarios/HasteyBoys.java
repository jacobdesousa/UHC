package me.nitro.uhc.scenarios;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class HasteyBoys implements Listener, CommandExecutor {

    public static boolean state;

    public HasteyBoys() {
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
        if (!cmd.getName().equalsIgnoreCase("hasteyboys")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }
            if (getState()) {
                disable();
                Bukkit.broadcastMessage(UHC.getMessage("hasteyBoysDisable"));
            } else {
                enable();
                Bukkit.broadcastMessage(UHC.getMessage("hasteyBoysEnable"));
            }
        return false;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (!getState()) {
            return;
        }

        ItemStack result = e.getRecipe().getResult();
        Material type = result.getType();
        switch (type) {
            case WOOD_SPADE:
            case WOOD_PICKAXE:
            case WOOD_AXE:
            case STONE_SPADE:
            case STONE_PICKAXE:
            case STONE_AXE:
            case IRON_SPADE:
            case IRON_PICKAXE:
            case IRON_AXE:
            case GOLD_SPADE:
            case GOLD_PICKAXE:
            case GOLD_AXE:
            case DIAMOND_SPADE:
            case DIAMOND_PICKAXE:
            case DIAMOND_AXE:
                ItemStack item = new ItemStack(result.getType());
                item.addEnchantment(Enchantment.DIG_SPEED, 3);
                item.addEnchantment(Enchantment.DURABILITY, 1);
                e.getInventory().setResult(item);
                break;
        }
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.IRON_INGOT, "&c&lHastey Boys", "&8&m----------~&7All tools crafted receive Efficiency III and Unbreaking I~&8&m----------");
    }
}
