package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.UHCDeath;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class Revive implements Listener, CommandExecutor {

    HashMap<UUID, UHCDeath> storedDeaths = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("revive")){
            return false;
        }
        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }
        if (args.length != 1) {
            sender.sendMessage(UHC.getMessage("reviveUsage"));
            return false;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(UHC.getMessage("invalidPlayer"));
            return false;
        }
        Player p = Bukkit.getPlayer(args[0]);
        UUID playerUUID = p.getUniqueId();
        if (!(storedDeaths.containsKey(playerUUID))) {
            sender.sendMessage(UHC.getMessage("noDeathFound"));
            return false;
        }
        UHCDeath death = storedDeaths.get(playerUUID);

        p.teleport(death.getLocation());
        p.getInventory().setContents(death.getContents());
        p.getInventory().setArmorContents(death.getArmour());
        p.sendMessage(UHC.getMessage("revived"));
        sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + p.getName() + " &7has been revived."));
        return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        UHCDeath death = new UHCDeath(e.getEntity().getInventory(), e.getEntity().getLocation());
        storedDeaths.put(e.getEntity().getUniqueId(), death);
    }
}
