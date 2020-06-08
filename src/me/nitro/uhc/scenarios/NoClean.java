package me.nitro.uhc.scenarios;

import me.nitro.uhc.Scenario;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class NoClean implements Scenario, CommandExecutor, Listener {

    private UHC uhc;
    public static boolean state;
    private Set<String> invincible;

    public NoClean(UHC uhc) {
        disable();
        this.uhc = uhc;
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
        invincible = new HashSet<>();
    }

    public void disable() {
        state = false;
        if (invincible != null) {
            invincible.clear();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("noclean")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("noCleanDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("noCleanEnable"));
            return false;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!getState()) {
            return;
        }

        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            String killerName = killer.getName();
            invincible.add(killerName);
            killer.sendMessage(UHC.getMessage("noCleanEnabled"));
            Bukkit.getScheduler().runTaskLater(uhc, new Runnable() {
                @Override
                public void run() {
                    if (invincible.contains(killerName)) {
                        invincible.remove(killerName);
                        killer.sendMessage(UHC.getMessage("noCleanExpired"));
                    }
                }
            }, 20 * 15);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!getState()) {
            return;
        }

        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player) {
                if (invincible.contains(((Player) e.getEntity()).getName())) {
                    e.setCancelled(true);
                    ((Player) e.getDamager()).sendMessage(UHC.getMessage("noDamage"));
                }
                if (invincible.contains(((Player) e.getDamager()).getName())) {
                    invincible.remove(((Player) e.getDamager()).getName());
                    ((Player) e.getDamager()).sendMessage(UHC.getMessage("noCleanLost"));
                }
            } else if (e.getDamager() instanceof Arrow && (((Arrow) e.getDamager()).getShooter() instanceof Player)) {
                if (invincible.contains(((Player) e.getEntity()).getName())) {
                    e.setCancelled(true);
                    ((Player) ((Arrow) e.getDamager()).getShooter()).sendMessage(UHC.getMessage("noDamage"));
                }
                if (invincible.contains(((Player) ((Arrow) e.getDamager()).getShooter()).getName())) {
                    invincible.remove(((Player) ((Arrow) e.getDamager()).getShooter()).getName());
                    ((Player) ((Arrow) e.getDamager()).getShooter()).sendMessage(UHC.getMessage("noCleanLost"));
                }
            }
        }
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.IRON_SWORD, "&c&lNoClean", "&8&m----------~&7NoClean is a scenario where when a~&7player gets a kill, they are invulnerable~&7for 15 seconds, or until they attack.~&8&m----------");
    }
}
