package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PearlDamage implements CommandExecutor, Listener, Module {

    private static boolean state;

    public PearlDamage() {
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
        if (!cmd.getName().equalsIgnoreCase("pearldamage")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("pearlDamageDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("pearlDamageEnable"));
            return false;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (getState()) {
            return;
        }
        if (e.getDamager().getType() == EntityType.ENDER_PEARL) {
            e.setCancelled(true);
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.ENDER_PEARL, "&c&lPearl Damage", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
