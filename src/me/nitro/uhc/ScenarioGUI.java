package me.nitro.uhc;

import me.nitro.uhc.scenarios.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ScenarioGUI implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("scenarios")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(UHC.getMessage("playerOnly"));
            return false;
        }

        Player p = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 18, Utils.colour("&c&lScenarios"));
        if (CutClean.state) {
            inv.addItem(CutClean.getIcon());
        }
        if (NoClean.state) {
            inv.addItem(NoClean.getIcon());
        }
        if (Timber.state) {
            inv.addItem(Timber.getIcon());
        }
        if (HasteyBoys.state) {
            inv.addItem(HasteyBoys.getIcon());
        }
        if (Backpacks.state) {
            inv.addItem(Backpacks.getIcon());
        }

        p.openInventory(inv);
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals(Utils.colour("&c&lScenarios"))) {
            e.setCancelled(true);
        }
    }
}
