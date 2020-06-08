package me.nitro.uhc.features;

import me.nitro.uhc.GameManager;
import me.nitro.uhc.GameState;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class KillCounter implements Listener, CommandExecutor {

    static HashMap<UUID, Integer> kills = new HashMap<>();

    public static int getKills(UUID uuid) {
        if (kills.containsKey(uuid)) {
            return kills.get(uuid);
        } else {
            return 0;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(cmd.getName().equalsIgnoreCase("kills") || cmd.getName().equalsIgnoreCase("kt") || cmd.getName().equalsIgnoreCase("killtop"))) {
            return false;
        }

        sender.sendMessage(UHC.getPrefix() + Utils.colour("&7Kills Leaderboard:"));
        for (Map.Entry<UUID, Integer> entry : getSortedKills().entrySet()) {
            sender.sendMessage(Utils.colour("&7" + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " - &c" + entry.getValue()));
        }
        return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            if (kills.get(e.getEntity().getKiller().getUniqueId()) == null) {
                kills.put(e.getEntity().getKiller().getUniqueId(), 1);
            } else {
                kills.put(e.getEntity().getKiller().getUniqueId(), kills.get(e.getEntity().getKiller().getUniqueId()) + 1);
            }
        }
    }

    public HashMap<UUID, Integer> getSortedKills() {
        List<Map.Entry<UUID, Integer>> list = new LinkedList<Map.Entry<UUID, Integer>>(kills.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<UUID, Integer>>() {
            public int compare(Map.Entry<UUID, Integer> o1, Map.Entry<UUID, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        Collections.reverse(list);
        HashMap<UUID, Integer> temp = new LinkedHashMap<UUID, Integer>();
        for (Map.Entry<UUID, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
