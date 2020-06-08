package me.nitro.uhc.features;

import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class TabHealth implements Listener {

    public static void initTabHealth() {
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        if (sm.getMainScoreboard().getObjective("health") == null) {
            Objective health = sm.getMainScoreboard().registerNewObjective("health", "health");
            health.setDisplayName(Utils.colour("&c❤"));
            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
    }

    /*
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        sm.getMainScoreboard().getObjective("health").getScore(e.getPlayer().getName()).setScore((int) );
        sm.getMainScoreboard().getTeam(e.getPlayer().getName()).setDisplayName(Utils.colour(Math.round(e.getPlayer().getHealth() / e.getPlayer().getMaxHealth() * e.getPlayer().getHealthScale()) + "&c❤"));
        sm.getMainScoreboard().getTeam(e.getPlayer().getName()).
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Player p = (Player) e.getEntity();

        Bukkit.broadcastMessage("" + (int) Math.round(p.getHealth() / p.getMaxHealth() * p.getHealthScale()));
        sm.getMainScoreboard().getObjective("health").getScore(p.getName()).setScore((int) Math.round(p.getHealth() / p.getMaxHealth() * p.getHealthScale()));
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Player p = (Player) e.getEntity();

        Bukkit.broadcastMessage("" + (int) Math.round(p.getHealth() / p.getMaxHealth() * p.getHealthScale()));
        sm.getMainScoreboard().getObjective("health").getScore(p.getName()).setScore((int) Math.round(p.getHealth() / p.getMaxHealth() * p.getHealthScale()));
    }

     */
}
