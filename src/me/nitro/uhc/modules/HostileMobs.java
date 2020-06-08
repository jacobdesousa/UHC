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
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class HostileMobs implements CommandExecutor, Listener, Module {

    private static boolean state;

    public HostileMobs() {
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
        if (!cmd.getName().equalsIgnoreCase("hostilemobs")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("hostileMobsDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("hostileMobsEnable"));
            return false;
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        if (getState()) {
            return;
        }
        if (e.getEntity().getType() == EntityType.ZOMBIE ||
                e.getEntity().getType() == EntityType.SKELETON ||
                e.getEntity().getType() == EntityType.CREEPER ||
                e.getEntity().getType() == EntityType.ENDERMAN ||
                e.getEntity().getType() == EntityType.SPIDER ||
                e.getEntity().getType() == EntityType.CAVE_SPIDER ||
                e.getEntity().getType() == EntityType.WITCH ||
                e.getEntity().getType() == EntityType.SLIME ||
                e.getEntity().getType() == EntityType.BLAZE ||
                e.getEntity().getType() == EntityType.GHAST ||
                e.getEntity().getType() == EntityType.MAGMA_CUBE ||
                e.getEntity().getType() == EntityType.SILVERFISH ||
                e.getEntity().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.PORK, "&c&lHostile Mobs", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
