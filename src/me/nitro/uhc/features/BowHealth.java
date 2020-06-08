package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BowHealth implements Listener {

    private UHC uhc;

    public BowHealth(UHC uhc) {
        this.uhc = uhc;
    }

    @EventHandler
    public void onBowShot(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player)) {
            return;
        }
        if (!(((Arrow) e.getDamager()).getShooter() instanceof Player)) {
            return;
        }

        Player damager = (Player) ((Arrow) e.getDamager()).getShooter();
        Player damaged = (Player) e.getEntity();
        Bukkit.getScheduler().runTaskLater(uhc, new Runnable() {
            @Override
            public void run() {
                if (e.getEntity().isDead()) {
                    damager.sendMessage(UHC.getPrefix() + Utils.colour("&7" + damaged.getName() + "'s Health: &c" + "0.0❤&7."));
                } else {
                    /*Double health = (damaged.getHealth()) / 2;
                    String[] splitter = health.toString().split("\\.");
                    if (splitter[1].length() > 1) {
                        health = health + 0.25;
                    }*/
                    double displayedHealth = damaged.getHealth() / damaged.getMaxHealth() * damaged.getHealthScale();
                    damager.sendMessage(UHC.getPrefix() + Utils.colour("&7" + damaged.getName() + "'s Health: &c" + displayedHealth/2 + "❤&7."));
                }
            }
        }, 1);
    }
}
