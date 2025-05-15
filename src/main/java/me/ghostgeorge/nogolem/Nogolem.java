package me.ghostgeorge.nogolem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Nogolem extends JavaPlugin implements Listener{
    boolean blockGolems;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        blockGolems = getConfig().getBoolean("block-golems", false);
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("NoGolems plugin enabled. Golem blocking: " + blockGolems);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!blockGolems) return;

        EntityType type = event.getEntityType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (type == EntityType.IRON_GOLEM &&
                (reason == CreatureSpawnEvent.SpawnReason.BUILD_IRONGOLEM || reason == CreatureSpawnEvent.SpawnReason.NATURAL)) {
            event.setCancelled(true);
        }

        if (type == EntityType.SNOW_GOLEM && reason == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /nogolem <enable|disable>");
            return true;
        }

        FileConfiguration config = getConfig();

        if (args[0].equalsIgnoreCase("enable")) {
            blockGolems = true;
            config.set("block-golems", true);
            saveConfig();
            sender.sendMessage("§aGolem spawning is now disabled.");
        } else if (args[0].equalsIgnoreCase("disable")) {
            blockGolems = false;
            config.set("block-golems", false);
            saveConfig();
            sender.sendMessage("§aGolem spawning is now allowed.");
        } else {
            sender.sendMessage("§cUnknown argument. Use /nogolem <enable|disable>");
        }

        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("NoGolems plugin disabled.");
    }
}
