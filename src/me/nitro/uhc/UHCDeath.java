package me.nitro.uhc;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class UHCDeath {

    private ItemStack[] contents;
    private ItemStack[] armour;
    private Location location;

    public UHCDeath(PlayerInventory inventory, Location location) {
        this.contents = inventory.getContents();
        this.armour = inventory.getArmorContents();
        this.location = location;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public ItemStack[] getArmour() {
        return armour;
    }

    public Location getLocation() {
        return location;
    }
}
