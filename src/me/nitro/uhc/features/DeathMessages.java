package me.nitro.uhc.features;

import me.nitro.uhc.Utils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessages implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getDeathMessage().contains("fell")) {
            e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7hit the ground too hard"));
        } else if (e.getDeathMessage().contains("slain")) {
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player) {
                e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7was slain by &c" + e.getEntity().getKiller().getName()));
            } else {
                e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7was slain"));
            }
        } else if (e.getDeathMessage().contains("shot")) {
            if (e.getEntity().getKiller() != null) {
                e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7was shot by &c" + e.getEntity().getKiller().getName()));
            } else {
                e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7was shot"));
            }
        } else {
            e.setDeathMessage(Utils.colour("&c" + e.getEntity().getName() + " &7died"));
        }
    }
}
