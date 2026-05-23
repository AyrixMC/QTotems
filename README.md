# 🛡️ QTotems

Hey there! Welcome to **QTotems**—a highly customizable Minecraft plugin that lets you configure custom Totems of Undying with their own unique passive (equipped) buffs and active (pop) effects. Whether you want to give players a speed boost while holding an Ice Totem or resurrect them with giant explosions of absorption hearts, QTotems handles it all directly via a simple config file.

No hardcoded effects. No messy setups. Just customize, reload, and start popping!

---

## ✨ Features at a Glance

*   **Config-Driven Custom Totems:** Define as many custom totems as you like directly under `totems:` in `config.yml`.
*   **Dual-State Potion Effects:**
    *   **Equip Effects (Passive):** Buffs applied continuously to the player while they are holding/equipping the totem in their hand.
    *   **Pop Effects (Active):** Custom potion effects that trigger immediately when a player pops their totem to cheat death.
*   **Beautiful Lore & Name Formatting:** Full support for modern Adventure/MiniMessage styling (colors, gradients, bolding) and legacy Minecraft formatting (`&` color codes).
*   **Dynamic Registries:** Instantly enable or disable totems using the `enabled: true/false` config flag.
*   **Seamless Reloading:** Run `/totems reload` in-game or from the console to apply configuration changes instantly without server restarts.
*   **Failsafe Mechanics:** Safely handles missing config parameters, prints clear warnings to console instead of crashing, and safely resets player effects on disconnect.

---

## 🛠️ Command Reference

The plugin registers the main command `/totems` with aliases `/qtotems` and `/qtotem`. 

| Command | Permission | Description |
|:---|:---|:---|
| `/totems` | `qtotems.command` | Displays plugin usage message. |
| `/totems reload` | `qtotems.command` | Reloads the configuration and re-registers all active totems. *(Can be run from console!)* |
| `/totems <totem>` | `qtotems.command` | Spawns one custom totem item into your own inventory. |
| `/totems <totem> <player>` | `qtotems.command` | Spawns one custom totem item into target player's inventory. |

### Tab Completion
The plugin supports complete tab completion:
*   First argument: Completes active totem IDs registered in the system (and `reload`).
*   Second argument: Completes online player names (only if a valid totem ID is selected).

---

## 🔑 Permissions

*   `qtotems.command`
    *   **Description:** Allows access to the main command structure (`/totems`, reload, and giving/spawning totems).
    *   **Default:** `op` (Admin only by default; can be assigned via LuckPerms or other permission managers).

---

## ⚙️ Configuration Guide (`config.yml`)

The plugin configuration is located at [src/main/resources/config.yml](file:///home/qsssaf/IdeaProjects/QTotems/src/main/resources/config.yml) (or `plugins/QTotems/config.yml` on a live server).

### Main Sections

1.  **`prefix`**: The message prefix prefixed to all plugin messages. Supports MiniMessage XML formatting.
2.  **`totems`**: A dictionary map of totem IDs containing:
    *   `enabled`: If `false`, the totem is ignored and not registered.
    *   `name`: The display name of the item.
    *   `lore`: List of lines shown on the item tooltip.
    *   `popEffects`: Format `"effect_name;amplifier;duration_in_ticks"`.
    *   `equipEffects`: Format `"effect_name;amplifier"`.
3.  **`messages`**: Customizable translation messages for plugin feedback.

### Format Details

> [!NOTE]
> *   **Amplifiers:** In Minecraft/Spigot, amplifiers are **0-indexed**. This means `0` represents Level I, `1` represents Level II, etc.
> *   **Durations:** Pop durations are measured in **server ticks** (20 ticks = 1 second).

---

## 💻 Codebase Reference (Classes & Methods)

Here is a detailed breakdown of all classes and methods inside the plugin's source code.

### 📍 [QTotems.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/QTotems.java)
The main entry point class of the plugin, extending Bukkit's `JavaPlugin`.

#### `public static QTotems getInstance()`
*   Retrieves the singleton instance of the running plugin.

#### `public void onEnable()`
*   Handles plugin startup:
    *   Initializes the `ConfigManager`.
    *   Registers the `EventListener` to handle gameplay interactions.
    *   Binds `QTotemsCommand` to the `/totems` command.
    *   Populates all custom totems from the configuration files.

#### `public void onDisable()`
*   Handles plugin shutdown steps, logging clean exit signals.

---

### 📍 [QTotemsCommand.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/command/QTotemsCommand.java)
Implements `CommandExecutor` and `TabCompleter` to manage command execution.

#### `private static String msgOnlyPlayers()`
*   Utility method to fetch the `messages.onlyPlayers` locale from config.

#### `private static String msgUsage()`
*   Utility method to fetch the `messages.usage` command syntax guide.

#### `private static String msgReloaded()`
*   Utility method to fetch the `messages.reloaded` success message.

#### `private static String msgInvalidTotem()`
*   Utility method to fetch the `messages.invalidTotem` error message.

#### `private static String msgGaveSelf()`
*   Utility method to fetch the `messages.gaveSelf` confirmation message.

#### `private static String msgGaveTarget(String targetName)`
*   Utility method to fetch and format the `messages.gaveTarget` message, replacing the `%target%` placeholder.

#### `private static String msgInvalidTarget()`
*   Utility method to fetch the `messages.invalidTarget` error message.

#### `public boolean onCommand(...)`
*   Parses incoming commands. Safely processes `/totems reload` (supporting console execution), and awards customized totems to players.

#### `public List<String> onTabComplete(...)`
*   Provides contextual tab completions for totem IDs and online player usernames.

---

### 📍 [ConfigManager.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/config/ConfigManager.java)
Static wrapper around the Spigot `FileConfiguration` instance to simplify retrieval.

#### `public static void init(JavaPlugin plugin)`
*   Initializes the configuration system and copies the default `config.yml` file if it doesn't already exist.

#### `public static void reloadConfig()`
*   Reloads configurations from the file on disk into active server memory.

#### `public static String getString(String path, ...)` / `getInt(...)` / `getDouble(...)` / `getLong(...)` / `getBoolean(...)`
*   Helper methods to retrieve specific primitives/types directly from the config sections safely.

#### `public static List<String> getStringList(String path)`
*   Fetches string list entries (essential for populating lore lists and potion effects).

#### `public static ConfigurationSection getSection(String path)`
*   Returns configuration sub-structures (such as the main `totems` key-value pairs).

#### `public static void set(String path, Object value)`
*   Directly updates values in the active configuration map.

#### `public static void save()`
*   Saves active configuration values back to `config.yml` on disk, then performs a reload.

---

### 📍 [EventListener.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/listener/EventListener.java)
Listens to Bukkit/Spigot game events to intercept totem consumption, item swaps, and inventories.

#### `public void onPop(EntityResurrectEvent event)`
*   Intercepts a player's death-prevention trigger and applies customized pop effects if they popped a registered custom totem.

#### `public void onInventoryClick(InventoryClickEvent event)`
*   Triggers when a player places or swaps a totem into/out of their off-hand slot inside inventory menus.

#### `public void onSwapInInventory(InventoryClickEvent event)`
*   Detects off-hand item shifts inside the inventory using custom keybinds.

#### `public void onSwap(PlayerSwapHandItemsEvent event)`
*   Detects main-hand to off-hand item swaps during normal gameplay (pressing `F`) and handles equip updates.

#### `public void onQuit(PlayerQuitEvent event)`
*   Removes active passive effects from players who disconnect to prevent effect persistence.

#### `public void onJoin(PlayerJoinEvent event)`
*   Checks if a newly logged-in player is holding a custom totem in their off-hand and applies the appropriate passive effects.

#### `public void onEffectRemove(EntityPotionEffectEvent event)`
*   Listens for potion effects being cleared/expired, ensuring active equip effects are immediately re-applied if a player is holding a custom totem.

---

### 📍 [QTotem.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/totem/QTotem.java)
Defines the `QTotem` instance structure, tracking custom lore, names, item-tags, and potion effects.

#### `public static QTotem create(String name)`
*   Instantiates a new builder instance of `QTotem` with a unique ID name.

#### `public String getName()`
*   Retrieves the raw totem ID name.

#### `public NamespacedKey getKey()`
*   Retrieves the Bukkit `NamespacedKey` assigned to this totem.

#### `public ItemStack getTotemItem()`
*   Retrieves a clone of the physical `TOTEM_OF_UNDYING` item stack with custom metadata, enchants, namespaced tags, and colored lore applied.

#### `public List<PotionEffect> getEquipEffects()`
*   Returns a read-only list containing the totem's passive equip effects.

#### `public List<PotionEffect> getPopEffects()`
*   Returns a read-only list containing the totem's active pop effects.

#### `public QTotem displayName(String name)`
*   Sets the displayed item name, parsing MiniMessage tags.

#### `public QTotem lore(List<String> lore)`
*   Sets the formatted lore list, parsing MiniMessage tags line by line.

#### `public QTotem addEquipEffect(String potionEffectName, int level)`
*   Resolves a potion effect type and registers it as an infinite-duration passive equip effect.

#### `public QTotem addPopEffect(String potionEffectName, int level, int duration)`
*   Resolves a potion effect type and registers it as a timed active pop effect.

#### `public void provideEquipEffects(Player player)`
*   Applies all registered passive equip effects to the target player.

#### `public void providePopEffects(Player player)`
*   Applies all registered active pop effects to the target player.

#### `public void removeEquipEffects(Player player)`
*   Safely removes the totem's passive equip effects from the player.

#### `public void register()`
*   Commits all metadata changes to the physical ItemStack and saves this instance to the central registry.

---

### 📍 [QTotemRegistry.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/totem/QTotemRegistry.java)
Main registry class that maintains the collection of loaded custom totems and tracks active player status.

#### `public static List<QTotem> getQTotems()`
*   Returns a list of all active registered custom totems.

#### `public static void add(QTotem qTotem)`
*   Registers a new custom totem.

#### `public static List<String> getTotemNames()`
*   Returns the name list of all active registered totems (useful for commands).

#### `public static boolean isQTotem(ItemStack stack)`
*   Checks if the provided item stack contains any registered custom totem metadata tags.

#### `public static void clearPastEffects(Player player)`
*   Removes passive equip effects of the player's previously equipped totem, logging/clearing safely.

#### `public static void handleEquip(Player player, ItemStack stack)`
*   Updates a player's equipped totem status, resetting old effects and applying new ones if they are holding a valid totem.

#### `public static void handlePop(Player player, ItemStack stack)`
*   Triggers active pop effects for the player, clearing their active offhand equip association.

#### `public static void handleLeave(Player player)`
*   Clears passive equip effects when a player leaves the server.

#### `public static void handleJoin(Player player)`
*   Applies passive equip effects if the player joins the server with a custom totem in their hand.

#### `public static void handleEffectChange(Player player)`
*   Handles re-applying passive equip effects if other plugins or server actions clear a player's potion effects while they are holding a totem.

#### `public static QTotem getTotem(String totemName)`
*   Fetches a registered `QTotem` instance by its ID name.

#### `public static void populate()`
*   Reads `config.yml`, filtering active entries, creating `QTotem` instances, assigning effects/lore, and registering them.

#### `public static void reload()`
*   Clears the registry cache and rebuilds the active custom totems from the configuration file.

---

### 📍 [Utils.java](file:///home/qsssaf/IdeaProjects/QTotems/src/main/java/dev/parrotstudios/qtotems/utils/Utils.java)
Text formatting utility providing legacy compatibility and fast performance through parsing caching.

#### `public static String convertLegacyToMiniMessage(String input)`
*   Parses traditional color codes (`&d`, `&l`, `§a`) and hex patterns (`&#ff00ff`) and translates them into modern MiniMessage XML-like tags.

#### `public static Component text(String message)`
*   Deserializes raw strings into net.kyori Component instances, utilizing a built-in LRU cache to speed up repetitive string translations.

#### `public static Component textWithPrefix(String message)`
*   Wraps the input text with the configured plugin prefix, deserializing both into a single net.kyori Component.

---

## 🛠️ Build and Development

To compile the plugin from source, simply build the project using Gradle.

```bash
./gradlew build -x test
```

The compiled jar file will be output to the build directory:
👉 `build/libs/QTotems-x.x.x.jar`

Copy the jar file to your Minecraft server's `/plugins` folder and run `/reload` or restart the server.
