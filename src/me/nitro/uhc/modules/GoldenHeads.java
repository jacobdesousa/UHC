package me.nitro.uhc.modules;

import me.nitro.uhc.Module;
import me.nitro.uhc.UHC;
import me.nitro.uhc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldenHeads implements CommandExecutor, Listener, Module {

    private static boolean state;

    public GoldenHeads() {
        enable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;

        ItemStack goldenHead = Utils.createItemStack(Material.GOLDEN_APPLE, "&cGolden Head", "&7Some say that consuming the head of a~&7fallen foe strengthens the blood.");
        ShapedRecipe recipe = new ShapedRecipe(goldenHead);
        recipe.shape("***", "*&*", "***");
        recipe.setIngredient('*', Material.GOLD_INGOT);
        recipe.setIngredient('&', Material.SKULL_ITEM, 3);
        Bukkit.addRecipe(recipe);
    }

    public void disable() {
        state = false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("goldenheads")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("goldenHeadDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("goldenHeadEnable"));
            return false;
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (!getState()) {
            if (e.getRecipe().getResult().hasItemMeta() &&
                    e.getRecipe().getResult().getItemMeta().hasDisplayName() &&
                    e.getRecipe().getResult().getItemMeta().getDisplayName().equals(Utils.colour("&cGolden Head"))) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (!getState()) {
            return;
        }

        if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().getDisplayName().equals(Utils.colour("&cGolden Head"))) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
        }
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled: Heals 4 Hearts" : "&cDisabled";
        return Utils.createItemStack(Material.GOLDEN_APPLE, "&c&lGolden Heads", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
