package me.nitro.uhc.scenarios;

import me.nitro.uhc.Scenario;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import me.nitro.uhc.teams.Team;
import me.nitro.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Backpacks implements Scenario, CommandExecutor {

    public static boolean state;
    private static HashMap<String, Inventory> backpacks = new HashMap<>();

    public Backpacks() {
        disable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
    }

    public void disable() {
        state = false;
        backpacks.clear();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("backpacks")) {
            if (!sender.hasPermission("uhc.admin")) {
                sender.sendMessage(UHC.getMessage("noPermission"));
                return false;
            }

            if (getState()) {
                disable();
                Bukkit.broadcastMessage(UHC.getMessage("backpacksDisable"));
                return false;
            } else {
                if (TeamManager.getTeamLimit() <= 1) {
                    sender.sendMessage(UHC.getMessage("noTeamGame"));
                    return false;
                }
                enable();
                Bukkit.broadcastMessage(UHC.getMessage("backpacksEnable"));
                return false;
            }
        } else if (cmd.getName().equalsIgnoreCase("bp") || cmd.getName().equalsIgnoreCase("backpack")) {
            if (!getState()) {
                sender.sendMessage(UHC.getMessage("backpacksDisabled"));
                return false;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(UHC.getMessage("playerOnly"));
                return false;
            }
            Player p = (Player) sender;
            if (Team.getTeam(p.getName()) == null) {
                p.sendMessage(UHC.getMessage("noTeam"));
                return false;
            }
            String teamName = Team.getTeam(p.getName());
            if (!backpacks.containsKey(teamName)) {
                backpacks.put(teamName, Bukkit.createInventory(null, 27, Utils.colour("&c" + teamName + "'s &7Team Backpack")));
            }
            p.openInventory(backpacks.get(teamName));
        }
        return false;
    }

    public static void removeBackpack(String teamName) {
        backpacks.remove(teamName);
    }

    public static ItemStack getIcon() {
        return Utils.createItemStack(Material.CHEST, "&c&lBackpacks", "&8&m----------~&7Backpacks is a scenario where teams~&7have instant access to a shared inventory.~&8&m----------");
    }
}
