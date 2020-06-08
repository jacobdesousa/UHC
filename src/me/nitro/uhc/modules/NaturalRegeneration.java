package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;

public class NaturalRegeneration implements CommandExecutor, Listener, Module {

    private static boolean state;

    public NaturalRegeneration() {
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
        if (!cmd.getName().equalsIgnoreCase("naturalregeneration")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("regenDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("regenEnable"));
            return false;
        }
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e) {
        if (getState()) {
            return;
        }

        if (e.getEntity() instanceof Player) {
            if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                e.setCancelled(true);
            }
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.COOKED_BEEF, "&c&lNatural Regeneration", "&7&m----------\n&c" + status + "\n&7&m----------");
    }
}
