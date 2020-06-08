package me.nitro.uhc;

import me.nitro.uhc.teams.Team;
import me.nitro.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Scatter implements CommandExecutor, Listener {

    private HashMap<UUID, Location> offlinePlayers = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("scatter") && !cmd.getName().equalsIgnoreCase("sca")) {
            return false;
        }

        if (!sender.hasPermission("uhc.scatter")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (args.length != 5) {
            sender.sendMessage(UHC.getMessage("scatterUsage"));
            return false;
        }

        if (Bukkit.getWorld(args[0]) == null) {
            sender.sendMessage(UHC.getMessage("invalidWorld"));
            return false;
        }

        if (!Utils.isInt(args[1])) {
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&7" + args[1] + " is not a valid input."));
            return false;
        }

        if (!Utils.isInt(args[2])) {
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&7" + args[2] + " is not a valid input."));
            return false;
        }

        if (!Utils.isInt(args[3])) {
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&7" + args[3] + " is not a valid input."));
            return false;
        }

        if (!Utils.isInt(args[4])) {
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&7" + args[4] + " is not a valid input."));
            return false;
        }

        World world = Bukkit.getWorld(args[0]);
        int radius = Integer.parseInt(args[1]);
        int mindist = Integer.parseInt(args[2]);
        int midX = Integer.parseInt(args[3]);
        int midZ = Integer.parseInt(args[4]);
        int teamLimit = TeamManager.getTeamLimit();

        if (teamLimit == 0) {
            Set<Set<UUID>> players = new HashSet<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("uhc.noscatter")) {
                    Set<UUID> player = new HashSet<>();
                    player.add(p.getUniqueId());
                    players.add(player);
                }
            }
            Bukkit.broadcastMessage(UHC.getMessage("scatteringSolos"));
            scatter(players, world, radius, mindist, midX, midZ);
        } else {
            Set<Set<UUID>> players = new HashSet<>();
            for (String teamName : Team.getAllTeams().keySet()) {
                Set<UUID> team = new HashSet<>();
                for (String name : Team.getMembers(teamName)) {
                    team.add(Bukkit.getPlayer(name).getUniqueId());
                }
                players.add(team);
            }
            Bukkit.broadcastMessage(UHC.getMessage("scatteringTeams"));
            scatter(players, world, radius, mindist, midX, midZ);
        }
        return false;
    }

    private void scatter(Set<Set<UUID>> players, World world, int radius, int mindist, int midX, int midZ) {
        Location midSky = new Location(world, midX, 255, midZ);
        Set<Location> positions = new HashSet<>();
        for (int i = 0; i < players.size(); i++) {
            Random rand = new Random();
            int scatterX = rand.nextInt(midX + (2 * radius)) - radius;
            int scatterZ = rand.nextInt(midZ + (2 * radius)) - radius;
            int scatterY = world.getHighestBlockYAt(scatterX, scatterZ) + 15;
            Location scatterLoc = new Location(world, scatterX, scatterY, scatterZ);

            Block spawnBlock = world.getBlockAt(scatterX, world.getHighestBlockYAt(scatterX, scatterZ) - 1, scatterZ);

            if (spawnBlock.isLiquid() || spawnBlock.getType() == Material.CACTUS) {
                i--;
            } else if (Utils.distanceApart(midSky, scatterLoc) < mindist) {
                i--;
            } else {
                positions.add(scatterLoc);
            }
        }

        Bukkit.broadcastMessage(UHC.getMessage("scatterTeleport"));

        for (Set<UUID> team : players) {
            Location scatterLoc = positions.iterator().next();
            for (UUID uuid : team) {
                if (Bukkit.getPlayer(uuid) == null) {
                    offlinePlayers.put(uuid, scatterLoc);
                } else {
                    Player p = Bukkit.getPlayer(uuid);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 200));
                    scatterLoc.getWorld().getChunkAt(scatterLoc).load();
                    p.teleport(scatterLoc);
                    p.sendMessage(UHC.getMessage("scattered"));
                    Bukkit.broadcastMessage(Utils.colour("&7- " + p.getName()));
                }
            }
            positions.remove(scatterLoc);
        }

        Bukkit.broadcastMessage(UHC.getMessage("scatterComplete"));
        if (offlinePlayers.size() > 0) {
            Bukkit.broadcastMessage(UHC.getMessage("missedScatter"));
            for (UUID uuid : offlinePlayers.keySet()) {
                Bukkit.broadcastMessage(Utils.colour("&7- " + Bukkit.getOfflinePlayer(uuid).getName()));
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (offlinePlayers.containsKey(p.getUniqueId())) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 200));
            offlinePlayers.get(p.getUniqueId()).getWorld().getChunkAt(offlinePlayers.get(p.getUniqueId())).load();
            p.teleport(offlinePlayers.get(p.getUniqueId()));
            Bukkit.broadcastMessage(UHC.getPrefix() + "&c" + p.getName() + " &7has been scattered late.");
            offlinePlayers.remove(p.getUniqueId());
        }
    }
}
