package dev.parrotstudios.qtotems;

import dev.parrotstudios.qtotems.command.QTotemsCommand;
import dev.parrotstudios.qtotems.config.ConfigManager;
import dev.parrotstudios.qtotems.listener.EventListener;
import dev.parrotstudios.qtotems.totem.QTotemRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class QTotems extends JavaPlugin {

    public static QTotems getInstance(){
        return JavaPlugin.getPlugin(QTotems.class);
    }

    @Override
    public void onEnable() {
        ConfigManager.init(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(getCommand("totems")).setExecutor(new QTotemsCommand());
        QTotemRegistry.populate();
        getLogger().info("Plugins is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugins is disabled.");
    }
}
