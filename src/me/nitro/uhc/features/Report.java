package me.nitro.uhc.features;

import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Report implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("report")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(UHC.getMessage("playerOnly"));
                return false;
            }
            if (args.length != 1) {
                sender.sendMessage(UHC.getMessage("reportUsage"));
                return false;
            }
            Player reported = Bukkit.getPlayer(args[0]);
            if (reported == null) {
                sender.sendMessage(UHC.getMessage("invalidPlayer"));
                return false;
            }

            report((Player) sender, reported);
        }
        return false;
    }

    private void report(Player reporter, Player reported) {
        Inventory inv = Bukkit.createInventory(null, 45, Utils.colour("&cReport ") + reported.getName());

        ItemStack report = Utils.createItemStack(Material.SKULL_ITEM, "&7" + reported.getName(), null);

        ItemStack aura = Utils.createItemStack(Material.IRON_SWORD, "&cKill Aura/Reach", null);

        ItemStack antikb = Utils.createItemStack(Material.LEASH, "&cAnti-Knockback", null);

        ItemStack speed = Utils.createItemStack(Material.SUGAR, "&cSpeed Hacks", null);

        ItemStack flight = Utils.createItemStack(Material.FEATHER, "&cFly Hacks", null);

        ItemStack iPVP = Utils.createItemStack(Material.FLINT_AND_STEEL,"&ciPVP/Stalking", null);

        ItemStack teaming = Utils.createItemStack(Material.DIAMOND_BARDING, "&cTeaming", null);

        ItemStack other = Utils.createItemStack(Material.SULPHUR, "&cOther", null);

        inv.setItem(13, report);
        inv.setItem(28, aura);
        inv.setItem(30, antikb);
        inv.setItem(32, speed);
        inv.setItem(34, flight);
        inv.setItem(38, iPVP);
        inv.setItem(40, teaming);
        inv.setItem(42, other);

        reporter.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getSize() >= 13 && e.getClickedInventory().getItem(13) != null && e.getClickedInventory().getItem(13).getType() != null && e.getClickedInventory().getItem(13).getType() == Material.SKULL_ITEM) {
            String reportedName = ChatColor.stripColor(e.getClickedInventory().getItem(13).getItemMeta().getDisplayName());
            String reportedReason = "";

            if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.SKULL_ITEM) {
                e.setCancelled(true);
                return;
            } else if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
                reportedReason = Utils.colour("&cKillAura/Reach");
            } else if (e.getCurrentItem().getType() == Material.LEASH) {
                reportedReason = Utils.colour("&cAnti-Knockback");
            } else if (e.getCurrentItem().getType() == Material.SUGAR) {
                reportedReason = Utils.colour("&cSpeed Hacks");
            } else if (e.getCurrentItem().getType() == Material.FEATHER) {
                reportedReason = Utils.colour("&cFly Hacks");
            } else if (e.getCurrentItem().getType() == Material.FLINT_AND_STEEL) {
                reportedReason = Utils.colour("&ciPVP/Stalking");
            } else if (e.getCurrentItem().getType() == Material.DIAMOND_BARDING) {
                reportedReason = Utils.colour("&cTeaming");
            } else if (e.getCurrentItem().getType() == Material.SULPHUR) {
                reportedReason = Utils.colour("&cOther");
            } else {
                return;
            }

            Player reporter = (Player) e.getWhoClicked();
            reporter.closeInventory();
            reporter.sendMessage(UHC.getMessage("reportConfirm"));
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.hasPermission("uhc.admin")) {
                    p.sendMessage(UHC.getPrefix() + Utils.colour("&7Report: &r&c" + reportedName + " &7has been reported by &c" + reporter.getName() + " &7for &4" + reportedReason + "&7."));
                }
            }
        } else {
            return;
        }
    }
}