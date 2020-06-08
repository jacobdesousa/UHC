package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PVP implements CommandExecutor, Listener, Module {

    public static boolean state;

    public PVP() {
        disable();
    }

    public boolean getState() {
        return state;
    }

    public static void setState(boolean toState) {
        state = toState;
    }

    public void enable() {
        state = true;
    }

    public void disable() {
        state = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("pvp")) {
            return false;
        }

        if (args.length > 1) {
            sender.sendMessage(UHC.getMessage("pvpUsage"));
            return false;
        }

        if (args.length == 0) {
            messagePVPState(sender);
            return false;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("status")) {
                messagePVPState(sender);
            } else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) {
                if (!sender.hasPermission("uhc.pvp")) {
                    sender.sendMessage(UHC.getMessage("noPermission"));
                    return false;
                }
                if (getState()) {
                    sender.sendMessage(UHC.getMessage("pvpAlreadyEnabled"));
                    return false;
                }
                enable();
                Bukkit.broadcastMessage(UHC.getMessage("pvpEnable"));
                return false;
            } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                if (!sender.hasPermission("uhc.pvp")) {
                    sender.sendMessage(UHC.getMessage("noPermission"));
                    return false;
                }
                if (!getState()) {
                    sender.sendMessage(UHC.getMessage("pvpAlreadyDisabled"));
                    return false;
                }
                disable();
                Bukkit.broadcastMessage(UHC.getMessage("pvpDisable"));
                return false;
            } else {
                sender.sendMessage(UHC.getMessage("pvpUsage"));
                return false;
            }
        }
        return false;
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if (getState()) {
            return;
        }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            e.setCancelled(true);
            ((Player) e.getDamager()).sendMessage(UHC.getMessage("pvpDisabled"));
        } else if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                e.setCancelled(true);
                shooter.sendMessage(UHC.getMessage("pvpDisabled"));
            }
        } else if (e.getDamager() instanceof FishHook && e.getEntity() instanceof Player) {
            e.setCancelled(true);
            FishHook fishHook = (FishHook) e.getDamager();
            Player shooter = (Player) fishHook.getShooter();
            shooter.sendMessage(UHC.getMessage("pvpDisabled"));
        } else {
            return;
        }
    }

    private void messagePVPState(CommandSender sender) {
        if (getState()) {
            sender.sendMessage(UHC.getMessage("pvpEnabled"));
        } else {
            sender.sendMessage(UHC.getMessage("pvpDisabled"));
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled: Enables at 20 Minutes";
        return Utils.createItemStack(Material.DIAMOND_SWORD, "&c&lPVP", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
