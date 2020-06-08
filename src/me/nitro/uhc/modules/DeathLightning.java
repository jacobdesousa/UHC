package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathLightning implements CommandExecutor, Listener, Module {

    private boolean state;

    public DeathLightning() {
        enable();
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
        if (!cmd.getName().equalsIgnoreCase("deathlightning")) {
            return false;
        }

        if (!sender.hasPermission("uhc.deathlightning")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("deathLightningDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("deathLightningEnable"));
            return false;
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!getState()) {
            return;
        }
        e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
    }
}
