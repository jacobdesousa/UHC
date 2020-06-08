package me.nitro.uhc;

import me.nitro.uhc.features.Border;
import me.nitro.uhc.features.LogoutTimer;
import me.nitro.uhc.features.Whitelist;
import me.nitro.uhc.teams.Team;
import me.nitro.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameManager implements CommandExecutor, Listener {

    private static GameState state;
    private static long startTime = 0;
    private static Set<UUID> alivePlayers;
    private int healTime = 10;
    private int pvpTime = 20;
    private int shrinkTime = 45;
    private World gameWorld;

    private UHC uhc;

    public GameManager(UHC uhc) {
        this.uhc = uhc;
        setState(GameState.STOPPED);
    }

    public static GameState getState() {
        return state;
    }

    public static Set<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public static void setState(GameState toState) {
        state = toState;
        if (toState == GameState.STOPPED) {
            UHC.pvp.setState(false);
            LogoutTimer.clearLogoutTimers();
            Border.cancelShrink();
        }
    }

    public static String getElapsedTime() {
        if (startTime == 0) {
            return "00:00:00";
        }
        long elapsedMillis = System.currentTimeMillis() - startTime;
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedMillis),
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedMillis)),
                TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("game")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(UHC.getMessage("gameUsage"));
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (getState() == GameState.RUNNING) {
                sender.sendMessage(UHC.getMessage("gameAlreadyRunning"));
                return false;
            }
            if (gameWorld == null) {
                sender.sendMessage(UHC.getMessage("gameWorldNotSet"));
                return false;
            }
            setState(GameState.RUNNING);
            alivePlayers = new HashSet<>();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!p.hasPermission("uhc.admin")) {
                    alivePlayers.add(p.getUniqueId());
                }
            }
            if (TeamManager.getTeamLimit() > 1) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!p.hasPermission("uhc.admin")) {
                        if (Team.getTeam(p.getName()) == null) {
                            Team.newTeam(p.getName());
                        }
                    }
                }
            }
            startTimer();
            Border.startBorder(shrinkTime, gameWorld);
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setSaturation(20);
                p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
            }

            Bukkit.broadcastMessage(UHC.getMessage("gameStart"));
        } else if (args[0].equalsIgnoreCase("addplayer")) {
            if (getState() == GameState.STOPPED) {
                sender.sendMessage(UHC.getMessage("noGame"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(UHC.getMessage("addPlayerUsage"));
                return false;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(UHC.getMessage("invalidPlayer"));
                return false;
            }
            if (alivePlayers.contains(Bukkit.getPlayer(args[1]).getUniqueId())) {
                sender.sendMessage(UHC.getMessage("playerAlreadyInGame"));
            }
            alivePlayers.add(Bukkit.getPlayer(args[1]).getUniqueId());
            if (TeamManager.getTeamLimit() > 1) {
                if (Team.getTeam(Bukkit.getPlayer(args[1]).getName()) == null) {
                    Team.newTeam(Bukkit.getPlayer(args[1]).getName());
                }
            }
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + Bukkit.getPlayer(args[1]).getName() + "&7 added to the game."));
        } else if (args[0].equalsIgnoreCase("removeplayer")) {
            if (getState() == GameState.STOPPED) {
                sender.sendMessage(UHC.getMessage("noGame"));
                return false;
            }
            if (args.length < 2) {
                sender.sendMessage(UHC.getMessage("removePlayerUsage"));
                return false;
            }
            removePlayer(args[1]);
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 removed from the game."));
        } else if (args[0].equalsIgnoreCase("healtime") ||
                args[0].equalsIgnoreCase("pvptime") ||
                args[0].equalsIgnoreCase("bordersize") ||
                args[0].equalsIgnoreCase("shrinktime")) {
            if (args.length < 2) {
                sender.sendMessage(UHC.getMessage("gameUsage"));
                return false;
            }
            if (!Utils.isInt(args[1])) {
                sender.sendMessage(UHC.getMessage("invalidInput"));
                return false;
            }
            int entry = Integer.parseInt(args[1]);
            if (args[0].equalsIgnoreCase("healtime")) {
                healTime = entry;
                sender.sendMessage(UHC.getMessage("healTimeUpdated"));
            } else if (args[0].equalsIgnoreCase("pvptime")) {
                pvpTime = entry;
                sender.sendMessage(UHC.getMessage("pvpTimeUpdated"));
            } else if (args[0].equalsIgnoreCase("bordersize")) {
                Border.setCurrentSize(entry);
                sender.sendMessage(UHC.getMessage("borderSizeUpdated"));
            } else if (args[0].equalsIgnoreCase("shrinktime")) {
                shrinkTime = entry;
                sender.sendMessage(UHC.getMessage("shrinkTimeUpdated"));
            }
        } else if (args[0].equalsIgnoreCase("setworld")) {
            if (args.length < 2) {
                sender.sendMessage(UHC.getMessage("gameUsage"));
                return false;
            }
            if (Bukkit.getWorld(args[1]) == null) {
                sender.sendMessage(UHC.getMessage("invalidWorld"));
                return false;
            }
            gameWorld = Bukkit.getWorld(args[1]);
            sender.sendMessage(UHC.getMessage("worldUpdated"));
        } else if (args[0].equalsIgnoreCase("generateborder")) {
            UHC.border.setBorder(Border.getCurrentSize(), gameWorld);
            sender.sendMessage(UHC.getMessage("borderGenerated"));
        } else if (args[0].equalsIgnoreCase("time")) {
            if (getState() == GameState.RUNNING) {
                sender.sendMessage(UHC.getPrefix() + Utils.colour("&7The elapsed time is: &c" + getElapsedTime() + "&7."));
            } else {
                sender.sendMessage(UHC.getMessage("noGame"));
                return false;
            }
        } else if (args[0].equalsIgnoreCase("players")) {
            if (getState() != GameState.RUNNING) {
                sender.sendMessage(UHC.getMessage("noGame"));
                return false;
            }
            sender.sendMessage(UHC.getPrefix() + Utils.colour("&7Alive Players:"));
            for (UUID uuid : alivePlayers) {
                sender.sendMessage(Utils.colour("&7- " + Bukkit.getOfflinePlayer(uuid).getName()));
            }
            return false;
        } else {
            sender.sendMessage(UHC.getMessage("gameUsage"));
            return false;
        }
        return false;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskLater(uhc, () -> {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setSaturation(20);
            }
            Bukkit.broadcastMessage(UHC.getMessage("healGiven"));
        }, 20 * 60 * healTime);
        Bukkit.getScheduler().runTaskLater(uhc, () -> {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.setHealth(p.getMaxHealth());
            }
            UHC.pvp.setState(true);
            Bukkit.broadcastMessage(UHC.getMessage("pvpEnable"));
        }, 20 * 60 * pvpTime);
    }

    public static void removePlayer(String pName) {
        alivePlayers.remove(Bukkit.getOfflinePlayer(pName).getUniqueId());
        if (TeamManager.getTeamLimit() > 1) {
            if (Team.getTeam(pName) != null) {
                if (Team.getMembers(Team.getTeam(pName)).size() == 1) {
                    Team.deleteTeam(pName);
                } else {
                    Team.getMembers(Team.getTeam(pName)).remove(pName);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (getState() == GameState.STOPPED) {
            return;
        }
        Whitelist.removePlayer(e.getEntity().getName());
        if (TeamManager.getTeamLimit() <= 1) {
            alivePlayers.remove(e.getEntity().getUniqueId());
            if (alivePlayers.size() == 1) {
                Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&6" + Bukkit.getPlayer(alivePlayers.iterator().next()).getName() + " &7is the winner!"));
                setState(GameState.STOPPED);
            }
        } else {
            removePlayer(e.getEntity().getName());
            if (Team.getAllTeams().size() == 1) {
                for (String teamName : Team.getAllTeams().keySet()) {
                    Bukkit.broadcastMessage(UHC.getPrefix() + Utils.colour("&6Team " + teamName + " &7is the winner!"));
                    setState(GameState.STOPPED);
                }
            }
        }
    }
}
