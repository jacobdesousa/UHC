package me.nitro.uhc.features;

import me.nitro.uhc.GameManager;
import me.nitro.uhc.GameState;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogoutTimer implements Listener {

    private static UHC uhc;

    public LogoutTimer(UHC uhc) {
        this.uhc = uhc;
    }

    static HashMap<UUID, Long> logoutTime = new HashMap<>();

    public static void clearLogoutTimers() {
        logoutTime.clear();
    }

    public static void initLogoutTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(uhc, () -> {
            for (Map.Entry<UUID, Long> entry : logoutTime.entrySet()) {
                if ((System.currentTimeMillis() - entry.getValue()) >= 300000) {
                    Whitelist.removePlayer(Bukkit.getOfflinePlayer(entry.getKey()).getName());
                    GameManager.removePlayer(Bukkit.getOfflinePlayer(entry.getKey()).getName());
                    logoutTime.remove(entry.getKey());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("uhc.admin")) {
                            p.sendMessage(UHC.getPrefix() + Utils.colour("&c" + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " &7has been removed from the game for being logged out for more than 5 minutes."));
                        }
                    }
                }
            }
        }, 0, 20 * 5);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GameManager.getState() != GameState.RUNNING) {
            return;
        }
        if (e.getPlayer().hasPermission("uhc.admin")) {
            return;
        }
        if (!GameManager.getAlivePlayers().contains(e.getPlayer().getUniqueId())) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("uhc.admin")) {
                p.sendMessage(UHC.getPrefix() + Utils.colour("&c" + e.getPlayer().getName() + " &7has logged out. They have 5 minutes to return."));
            }
        }
        logoutTime.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (logoutTime.containsKey(e.getPlayer().getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("uhc.admin")) {
                    p.sendMessage(UHC.getPrefix() + Utils.colour("&c" + e.getPlayer().getName() + " &7has returned."));
                }
            }
        }
        logoutTime.remove(e.getPlayer().getUniqueId());
    }

}
