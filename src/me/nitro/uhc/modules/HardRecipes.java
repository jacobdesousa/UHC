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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Collection;
import java.util.Iterator;

public class HardRecipes implements CommandExecutor, Listener, Module {

    private static boolean state;
    ShapelessRecipe hardMelonRecipe = new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON))
            .addIngredient(1, Material.GOLD_BLOCK).addIngredient(1, Material.MELON);
    ShapelessRecipe melonRecipe = new ShapelessRecipe(new ItemStack(Material.SPECKLED_MELON))
            .addIngredient(1, Material.GOLD_NUGGET).addIngredient(1, Material.MELON);

    public HardRecipes() {
        enable();
    }

    public boolean getState() {
        return state;
    }

    public void enable() {
        state = true;
        Bukkit.addRecipe(hardMelonRecipe);
    }

    public void disable() {
        state = false;
        Bukkit.addRecipe(melonRecipe);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("hardrecipes")) {
            return false;
        }

        if (!sender.hasPermission("uhc.admin")) {
            sender.sendMessage(UHC.getMessage("noPermission"));
            return false;
        }

        if (getState()) {
            disable();
            Bukkit.broadcastMessage(UHC.getMessage("hardRecipesDisable"));
            return false;
        } else {
            enable();
            Bukkit.broadcastMessage(UHC.getMessage("hardRecipesEnable"));
            return false;
        }
    }

    @EventHandler
    public void onPrepareCraftItemEvent(PrepareItemCraftEvent e) {
        Recipe recipe = e.getRecipe();
        if (getState()) {
            if (recipe.getResult().getType().equals(Material.SPECKLED_MELON)) {
                if (recipeContainsMaterial(recipe, Material.GOLD_NUGGET)) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        } else {
            if (recipe.getResult().getType().equals(Material.SPECKLED_MELON)) {
                if (recipeContainsMaterial(recipe, Material.GOLD_BLOCK)) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }

    private boolean recipeContainsMaterial(Recipe recipe, Material material) {
        Object localObject = null;
        if ((recipe instanceof ShapedRecipe)) {
            localObject = ((ShapedRecipe) recipe).getIngredientMap().values();
        }
        if ((recipe instanceof ShapelessRecipe)) {
            localObject = ((ShapelessRecipe) recipe).getIngredientList();
        }
        if (localObject == null) {
            return false;
        }
        Iterator localIterator = ((Collection) localObject).iterator();
        while (localIterator.hasNext()) {
            ItemStack localItemStack = (ItemStack) localIterator.next();
            if (localItemStack.getType().equals(material)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getIcon() {
        String status;
        status = state ? "&aEnabled" : "&cDisabled";
        return Utils.createItemStack(Material.WORKBENCH, "&c&lHard Recipes", "&7&m----------~&c" + status + "~&7&m----------");
    }
}
