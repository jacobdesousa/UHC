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
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Absorption implements CommandExecutor, Listener, Module {

    private static boolean state;
    private UHC uhc;

    public Absorption(UHC uhc) {
        enable();
        this.uhc = uhc;
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
        if (!cmd.getName().equalsIgnoreCase("absorption")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("absorptionDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("absorptionEnable"));
            return false;
        }
    }

    @EventHandler
    public void onAbsorptionEffect(PlayerItemConsumeEvent e) {
        if (getState()) {
            return;
        }

        if (e.getItem().getType() != Material.GOLDEN_APPLE) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(uhc, new Runnable() {
            @Override
            public void run() {
                e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
            }
        }, 1);
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.GOLDEN_CARROT, "&c&lAbsorption", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
