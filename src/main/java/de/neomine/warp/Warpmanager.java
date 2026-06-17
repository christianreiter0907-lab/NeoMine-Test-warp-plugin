package de.neomine.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpManager {
    private final File file;
    private final FileConfiguration config;
    private final Map<String, Warp> warps = new HashMap<>();

    public WarpManager(NeoWarp plugin) {
        this.file = new File(plugin.getDataFolder(), "warps.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try { file.createNewFile(); } catch (IOException ignored) {}
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        loadWarps();
    }

    private void loadWarps() {
        if (config.getConfigurationSection("warps") == null) return;
        for (String key : config.getConfigurationSection("warps").getKeys(false)) {
            warps.put(key.toLowerCase(), new Warp(
                config.getString("warps." + key + ".name"),
                config.getString("warps." + key + ".world"),
                config.getDouble("warps." + key + ".x"),
                config.getDouble("warps." + key + ".y"),
                config.getDouble("warps." + key + ".z"),
                (float) config.getDouble("warps." + key + ".yaw"),
                (float) config.getDouble("warps." + key + ".pitch"),
                UUID.fromString(config.getString("warps." + key + ".creator")),
                config.getLong("warps." + key + ".date")
            ));
        }
    }

    public void saveWarp(Warp warp) {
        warps.put(warp.name().toLowerCase(), warp);
        String path = "warps." + warp.name().toLowerCase() + ".";
        config.set(path + "name", warp.name());
        config.set(path + "world", warp.world());
        config.set(path + "x", warp.x());
        config.set(path + "y", warp.y());
        config.set(path + "z", warp.z());
        config.set(path + "yaw", warp.yaw());
        config.set(path + "pitch", warp.pitch());
        config.set(path + "creator", warp.creator().toString());
        config.set(path + "date", warp.date());
        try { config.save(file); } catch (IOException ignored) {}
    }

    public void deleteWarp(String name) {
        warps.remove(name.toLowerCase());
        config.set("warps." + name.toLowerCase(), null);
        try { config.save(file); } catch (IOException ignored) {}
    }

    public Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public List<Warp> getAllWarps() {
        return new ArrayList<>(warps.values());
    }

    public Location getLocation(Warp warp) {
        return new Location(
            Bukkit.getWorld(warp.world()),
            warp.x(), warp.y(), warp.z(),
            warp.yaw(), warp.pitch()
        );
    }
}