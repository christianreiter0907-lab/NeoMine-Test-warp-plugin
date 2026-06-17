package de.neomine.warp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WarpGUI implements InventoryHolder {
    private final Inventory inv;
    private final int page;
    private static final int[] SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    public WarpGUI(int page, NeoWarp plugin) {
        this.page = page;
        this.inv = Bukkit.createInventory(this, 54, Component.text("Warps - Seite " + (page + 1)));
        setupBorders();
        populateWarps(plugin);
    }

    private void setupBorders() {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        meta.displayName(Component.text(" "));
        glass.setItemMeta(meta);

        for (int i = 0; i < 54; i++) {
            if (i < 10 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i > 43) {
                inv.setItem(i, glass);
            }
        }
    }

    private void populateWarps(NeoWarp plugin) {
        List<Warp> warps = plugin.getWarpManager().getAllWarps();
        int startIndex = page * SLOTS.length;
        int endIndex = Math.min(startIndex + SLOTS.length, warps.size());

        for (int i = startIndex; i < endIndex; i++) {
            Warp warp = warps.get(i);
            ItemStack item = new ItemStack(getMaterialForWorld(warp.world()));
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(warp.name()).color(NamedTextColor.GOLD));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            meta.lore(List.of(
                Component.text("Welt: " + warp.world()).color(NamedTextColor.GRAY),
                Component.text(String.format("XYZ: %.1f / %.1f / %.1f", warp.x(), warp.y(), warp.z())).color(NamedTextColor.GRAY),
                Component.text("Ersteller: " + Bukkit.getOfflinePlayer(warp.creator()).getName()).color(NamedTextColor.GRAY),
                Component.text("Datum: " + sdf.format(new Date(warp.date()))).color(NamedTextColor.GRAY)
            ));

            NamespacedKey key = new NamespacedKey(plugin, "warp_name");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, warp.name());
            item.setItemMeta(meta);
            inv.setItem(SLOTS[i - startIndex], item);
        }

        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta pMeta = prev.getItemMeta();
            pMeta.displayName(Component.text("Vorherige Seite").color(NamedTextColor.YELLOW));
            prev.setItemMeta(pMeta);
            inv.setItem(45, prev);
        }

        if (endIndex < warps.size()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nMeta = next.getItemMeta();
            nMeta.displayName(Component.text("Nächste Seite").color(NamedTextColor.YELLOW));
            next.setItemMeta(nMeta);
            inv.setItem(53, next);
        }
    }

    private Material getMaterialForWorld(String world) {
        if (world.contains("nether")) return Material.NETHERRACK;
        if (world.contains("the_end")) return Material.END_STONE;
        return Material.GRASS_BLOCK;
    }

    public int getPage() {
        return page;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}