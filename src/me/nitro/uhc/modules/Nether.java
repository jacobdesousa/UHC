package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;

public class Nether implements CommandExecutor, Listener, Module {

    private static boolean state;

    public Nether() {
        disable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
        Bukkit.broadcastMessage(UHC.getMessage("netherEnable"));
    }

    public void disable() {
        state = false;
        Bukkit.broadcastMessage(UHC.getMessage("netherDisable"));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("nether")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            return false;
        } else {
            enable();
            return false;
        }
    }

    @EventHandler
    public void onPortalTravel(PlayerPortalEvent e) {
        if (getState()) {
            return;
        }

        e.setCancelled(true);
        e.getPlayer().sendMessage(UHC.getMessage("netherDisabled"));
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.OBSIDIAN, "&c&lNether", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
