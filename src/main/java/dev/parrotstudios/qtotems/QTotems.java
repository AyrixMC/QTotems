package dev.parrotstudios.qtotems;

import dev.parrotstudios.qtotems.command.QTotemsCommand;
import dev.parrotstudios.qtotems.config.ConfigManager;
import dev.parrotstudios.qtotems.listener.EventListener;
import dev.parrotstudios.qtotems.totem.QTotemRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main plugin class for QTotems.
 * Manages the plugin lifecycle, configuration initialization, event registration, and commands.
 */
public final class QTotems extends JavaPlugin {

    /**
     * Retrieves the singleton instance of the QTotems plugin.
     *
     * @return The active QTotems plugin instance.
     */
    public static QTotems getInstance(){
        return JavaPlugin.getPlugin(QTotems.class);
    }

    /**
     * Called when the plugin is enabled by the server.
     * Initializes configuration, registers event listeners, binds command executors,
     * and populates the totem registry.
     */
    @Override
    public void onEnable() {
        ConfigManager.init(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(getCommand("totems")).setExecutor(new QTotemsCommand());
        QTotemRegistry.populate();
        getLogger().info("Plugins is enabled.");
    }

    /**
     * Called when the plugin is disabled by the server.
     * Performs cleanup and logs the disabling message.
     */
    @Override
    public void onDisable() {
        getLogger().info("Plugins is disabled.");
    }
}
