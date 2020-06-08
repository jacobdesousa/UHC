package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class CustomChat implements Listener {

    private UHC uhc;

    public CustomChat(UHC uhc) {
        this.uhc = uhc;
    }

    private Set<Player> cooldownPlayers = new HashSet<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        if (!cooldownPlayers.contains(e.getPlayer())) {
            Bukkit.broadcastMessage(p.getDisplayName() + Utils.colour("&7: &r") + e.getMessage());
        } else {
           e.getPlayer().sendMessage(UHC.getMessage("chatCooldown"));
        }

        if (e.getPlayer().hasPermission("uhc.admin")) {
            return;
        }
        cooldownPlayers.add(p);
        Bukkit.getScheduler().runTaskLater(uhc, () -> {
            cooldownPlayers.remove(p);
        }, 20 * 5);
    }
}