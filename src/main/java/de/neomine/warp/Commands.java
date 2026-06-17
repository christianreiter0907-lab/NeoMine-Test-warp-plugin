package de.neomine.warp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    private final NeoWarp plugin;

    public Commands(NeoWarp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        String cmd = command.getName().toLowerCase();

        if (cmd.equals("setwarp")) {
            if (args.length != 1) {
                player.sendMessage(Component.text("Nutzung: /setwarp <name>").color(NamedTextColor.RED));
                return true;
            }
            String name = args[0];
            Warp warp = new Warp(
                name,
                player.getWorld().getName(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getYaw(),
                player.getLocation().getPitch(),
                player.getUniqueId(),
                System.currentTimeMillis()
            );
            plugin.getWarpManager().saveWarp(warp);
            player.sendMessage(Component.text("Warp '" + name + "' gesetzt!").color(NamedTextColor.GREEN));
            return true;
        }

        if (cmd.equals("warp")) {
            if (args.length != 1) {
                player.sendMessage(Component.text("Nutzung: /warp <name>").color(NamedTextColor.RED));
                return true;
            }
            Warp warp = plugin.getWarpManager().getWarp(args[0]);
            if (warp == null) {
                player.sendMessage(Component.text("Warp existiert nicht.").color(NamedTextColor.RED));
                return true;
            }
            plugin.getTeleportManager().startTeleport(player, warp);
            return true;
        }

        if (cmd.equals("delwarp")) {
            if (args.length != 1) {
                player.sendMessage(Component.text("Nutzung: /delwarp <name>").color(NamedTextColor.RED));
                return true;
            }
            Warp warp = plugin.getWarpManager().getWarp(args[0]);
            if (warp == null) {
                player.sendMessage(Component.text("Warp existiert nicht.").color(NamedTextColor.RED));
                return true;
            }
            if (!player.hasPermission("warptest.admin") && !warp.creator().equals(player.getUniqueId())) {
                player.sendMessage(Component.text("Du darfst diesen Warp nicht löschen!").color(NamedTextColor.RED));
                return true;
            }
            plugin.getWarpManager().deleteWarp(args[0]);
            player.sendMessage(Component.text("Warp '" + args[0] + "' gelöscht!").color(NamedTextColor.GREEN));
            return true;
        }

        if (cmd.equals("warpgui")) {
            player.openInventory(new WarpGUI(0, plugin).getInventory());
            return true;
        }

        if (cmd.equals("warplist")) {
            List<Warp> warps = plugin.getWarpManager().getAllWarps();
            player.sendMessage(Component.text("Warps (" + warps.size() + "):").color(NamedTextColor.GOLD));
            for (Warp w : warps) {
                player.sendMessage(Component.text("- " + w.name()).color(NamedTextColor.YELLOW));
            }
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String cmd = command.getName().toLowerCase();
            if (cmd.equals("warp") || cmd.equals("delwarp")) {
                List<String> list = new ArrayList<>();
                for (Warp w : plugin.getWarpManager().getAllWarps()) {
                    if (w.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(w.name());
                    }
                }
                return list;
            }
        }
        return Collections.emptyList();
    }
}