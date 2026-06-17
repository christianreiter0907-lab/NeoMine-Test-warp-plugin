package de.neomine.warp;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NeoWarp extends JavaPlugin implements Listener {
    private WarpManager warpManager;
    private TeleportManager teleportManager;
    private final Map<UUID, Long> combatLog = new HashMap<>();

    @Override
    public void onEnable() {
        this.warpManager = new WarpManager(this);
        this.teleportManager = new TeleportManager(this);

        Commands cmds = new Commands(this);
        getCommand("setwarp").setExecutor(cmds);
        getCommand("warp").setExecutor(cmds);
        getCommand("delwarp").setExecutor(cmds);
        getCommand("warpgui").setExecutor(cmds);
        getCommand("warplist").setExecutor(cmds);

        getCommand("warp").setTabCompleter(cmds);
        getCommand("delwarp").setTabCompleter(cmds);

        getServer().getPluginManager().registerEvents(this, this);
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public boolean isInCombat(Player player) {
        return combatLog.containsKey(player.getUniqueId()) && 
               (System.currentTimeMillis() - combatLog.get(player.getUniqueId()) < 10000);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            teleportManager.cancelTeleport(player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            combatLog.put(player.getUniqueId(), System.currentTimeMillis());
        }
        if (event.getDamager() instanceof Player player) {
            combatLog.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof WarpGUI gui)) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        NamespacedKey key = new NamespacedKey(this, "warp_name");
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String warpName = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
            player.closeInventory();
            Warp warp = warpManager.getWarp(warpName);
            if (warp != null) teleportManager.startTeleport(player, warp);
            return;
        }

        if (event.getRawSlot() == 45) {
            player.openInventory(new WarpGUI(gui.getPage() - 1, this).getInventory());
        } else if (event.getRawSlot() == 53) {
            player.openInventory(new WarpGUI(gui.getPage() + 1, this).getInventory());
        }
    }
}