package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class NotchApples implements CommandExecutor, Listener, Module {

    private static boolean state;

    public NotchApples() {
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
        if (!cmd.getName().equalsIgnoreCase("notchapples")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("notchAppleDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("notchAppleEnable"));
            return false;
        }
    }

    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {
        if (getState()) {
            return;
        }

        Material itemType = e.getRecipe().getResult().getType();
        Byte itemData = e.getRecipe().getResult().getData().getData();
        if (itemType == Material.GOLDEN_APPLE && itemData == 1) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for (HumanEntity he : e.getViewers()) {
                if (he instanceof Player) {
                    ((Player) he).sendMessage(UHC.getMessage("notchApplesDisabled"));
                }
            }
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colour("&c&lNotch Apples"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.colour("&7&m----------"));
        lore.add(Utils.colour("&c" + status));
        lore.add(Utils.colour("&7&m----------"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
