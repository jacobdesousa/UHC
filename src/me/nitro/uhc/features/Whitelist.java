package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Set;

public class Whitelist implements CommandExecutor, Listener {

    public static Set<String> whitelist;
    private static boolean wlStatus;

    private boolean getStatus() {
        return wlStatus;
    }

    public static void enable() {
        wlStatus = true;
    }

    public static void disable() {
        wlStatus = false;
    }

    public static boolean addPlayer(String name) {
        if (whitelist.contains(name.toLowerCase())) {
            return false;
        } else {
            whitelist.add(name.toLowerCase());
            return true;
        }
    }

    public static boolean removePlayer(String name) {
        if (whitelist.contains(name.toLowerCase())) {
            whitelist.remove(name.toLowerCase());
            return true;
        } else {
            return false;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("wl") && !cmd.getName().equalsIgnoreCase("whitelist")) {
            return false;
        }

        if (!sender.hasPermission("uhc.wl")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(UHC.getMessage("wlUsage"));
            return false;
        }

        if (args[0].equalsIgnoreCase("on")) {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("wlEnable"));
            return false;
        } else if (args[0].equalsIgnoreCase("off")) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("wlDisable"));
            return false;
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 2) {
                if (addPlayer(args[1])) {
                    sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 added to the whitelist."));
                } else {
                    sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 is already on the whitelist."));
                }
                return false;
            } else {
                sender.sendMessage(UHC.getMessage("wlAddUsage"));
                return false;
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                if (removePlayer(args[1])) {
                    sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 removed from the whitelist."));
                } else {
                    sender.sendMessage(UHC.getPrefix() + Utils.colour("&c" + args[1] + "&7 is not on the whitelist."));
                }
                return false;
            } else {
                sender.sendMessage(UHC.getMessage("wlRemoveUsage"));
                return false;
            }
        } else if (args[0].equalsIgnoreCase("all")) {
            Bukkit.broadcastMessage(UHC.getMessage("wlAllAdded"));
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                addPlayer(p.getName());
            }
            return false;
        } else if (args[0].equalsIgnoreCase("clear")) {
            Bukkit.broadcastMessage(UHC.getMessage("wlClear"));
            whitelist.clear();
            return false;
        } else if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(UHC.getMessage("wlList"));
            for (String name : whitelist) {
                sender.sendMessage(Utils.colour("&7- " + name));
            }
            return false;
        } else {
            sender.sendMessage(UHC.getMessage("wlUsage"));
            return false;
        }
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (!getStatus()) {
            return;
        }
        if (e.getPlayer().hasPermission("uhc.admin")){
            return;
        }

        if (!whitelist.contains(e.getPlayer().getName().toLowerCase())) {
            e.setKickMessage(UHC.getMessage("notWhitelisted"));
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}
