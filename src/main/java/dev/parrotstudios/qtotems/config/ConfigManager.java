package dev.parrotstudios.qtotems.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Manages configuration reading, saving, and reloading.
 * Acts as a wrapper around Spigot's FileConfiguration.
 */
public class ConfigManager {
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    /**
     * Initializes the config manager, saves the default config, and loads configuration.
     *
     * @param plugin The JavaPlugin instance.
     */
    public static void init(JavaPlugin plugin) {
        ConfigManager.plugin = plugin;
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    /**
     * Reloads configuration from disk and updates the configuration instance.
     */
    public static void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    /**
     * Retrieves a string value from configuration.
     *
     * @param path Path to the string configuration key.
     * @return The configured string value, or null if not found.
     */
    public static String getString(String path) {
        return config.getString(path);
    }

    /**
     * Retrieves a string value from configuration with a fallback default.
     *
     * @param path Path to the string configuration key.
     * @param def Default fallback value.
     * @return The configured string value, or default fallback.
     */
    public static String getString(String path, String def) {
        return config.getString(path, def);
    }

    /**
     * Retrieves an integer value from configuration.
     *
     * @param path Path to the configuration key.
     * @return The configured integer value.
     */
    public static int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Retrieves an integer value from configuration with a fallback default.
     *
     * @param path Path to the configuration key.
     * @param def Default fallback value.
     * @return The configured integer value, or default fallback.
     */
    public static int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Retrieves a double value from configuration.
     *
     * @param path Path to the configuration key.
     * @return The configured double value.
     */
    public static double getDouble(String path) {
        return config.getDouble(path);
    }

    /**
     * Retrieves a double value from configuration with a fallback default.
     *
     * @param path Path to the configuration key.
     * @param def Default fallback value.
     * @return The configured double value, or default fallback.
     */
    public static double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Retrieves a long value from configuration.
     *
     * @param path Path to the configuration key.
     * @return The configured long value.
     */
    public static long getLong(String path) {
        return config.getLong(path);
    }

    /**
     * Retrieves a long value from configuration with a fallback default.
     *
     * @param path Path to the configuration key.
     * @param def Default fallback value.
     * @return The configured long value, or default fallback.
     */
    public static long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    /**
     * Retrieves a boolean value from configuration.
     *
     * @param path Path to the configuration key.
     * @return The configured boolean value.
     */
    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Retrieves a boolean value from configuration with a fallback default.
     *
     * @param path Path to the configuration key.
     * @param def Default fallback value.
     * @return The configured boolean value, or default fallback.
     */
    public static boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Retrieves a list of strings from configuration.
     *
     * @param path Path to the configuration key.
     * @return The configured string list.
     */
    public static List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    /**
     * Retrieves a configuration section.
     *
     * @param path Path to the configuration section key.
     * @return The ConfigurationSection instance, or null if not found.
     */
    public static ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    /**
     * Dynamically sets a value at the specified path in the configuration.
     *
     * @param path Path to the configuration key.
     * @param value The value object to set.
     */
    public static void set(String path, Object value) {
        config.set(path, value);
    }

    /**
     * Saves the current active configuration state to the config file on disk and reloads it.
     */
    public static void save(){
        plugin.saveConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
