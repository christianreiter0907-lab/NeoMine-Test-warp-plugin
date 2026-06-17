package de.neomine.warp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeleportManager {
    private final NeoWarp plugin;
    private final Set<UUID> activeTeleports = new HashSet<>();

    public TeleportManager(NeoWarp plugin) {
        this.plugin = plugin;
    }

    public void startTeleport(Player player, Warp warp) {
        if (plugin.isInCombat(player)) {
            player.sendMessage(Component.text("Du kannst dich im Kampf nicht teleportieren!").color(NamedTextColor.RED));
            return;
        }

        UUID uuid = player.getUniqueId();
        activeTeleports.add(uuid);
        Location startLoc = player.getLocation();
        Location targetLoc = plugin.getWarpManager().getLocation(warp);

        new BukkitRunnable() {
            int time = 3;

            @Override
            public void run() {
                if (!activeTeleports.contains(uuid)) {
                    cancel();
                    return;
                }

                if (player.getLocation().distanceSquared(startLoc) > 0.1) {
                    player.sendMessage(Component.text("Teleport abgebrochen (Bewegung).").color(NamedTextColor.RED));
                    activeTeleports.remove(uuid);
                    cancel();
                    return;
                }

                if (time <= 0) {
                    player.teleportAsync(targetLoc).thenAccept(success -> {
                        if (success) {
                            player.sendMessage(Component.text("Du wurdest zu " + warp.name() + " teleportiert!").color(NamedTextColor.GREEN));
                        }
                    });
                    activeTeleports.remove(uuid);
                    cancel();
                    return;
                }

                player.sendActionBar(Component.text("Teleport in " + time + " Sekunden...").color(NamedTextColor.YELLOW));
                time--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void cancelTeleport(Player player) {
        if (activeTeleports.remove(player.getUniqueId())) {
            player.sendMessage(Component.text("Teleport abgebrochen (Schaden erhalten).").color(NamedTextColor.RED));
        }
    }
}