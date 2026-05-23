package dev.parrotstudios.qtotems.utils;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.parrotstudios.qtotems.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Text parsing and component utilities supporting legacy color code translation
 * and fast caching for deserialized Adventure Components.
 */
public class Utils {
    private static final Map<Character, String> LEGACY_TO_MINI = new HashMap<>();
    private static final Cache<String, String> MESSAGE_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    static {
        LEGACY_TO_MINI.put('0', "<black>");
        LEGACY_TO_MINI.put('1', "<dark_blue>");
        LEGACY_TO_MINI.put('2', "<dark_green>");
        LEGACY_TO_MINI.put('3', "<dark_aqua>");
        LEGACY_TO_MINI.put('4', "<dark_red>");
        LEGACY_TO_MINI.put('5', "<dark_purple>");
        LEGACY_TO_MINI.put('6', "<gold>");
        LEGACY_TO_MINI.put('7', "<gray>");
        LEGACY_TO_MINI.put('8', "<dark_gray>");
        LEGACY_TO_MINI.put('9', "<blue>");
        LEGACY_TO_MINI.put('a', "<green>");
        LEGACY_TO_MINI.put('b', "<aqua>");
        LEGACY_TO_MINI.put('c', "<red>");
        LEGACY_TO_MINI.put('d', "<light_purple>");
        LEGACY_TO_MINI.put('e', "<yellow>");
        LEGACY_TO_MINI.put('f', "<white>");
        LEGACY_TO_MINI.put('k', "<obfuscated>");
        LEGACY_TO_MINI.put('l', "<bold>");
        LEGACY_TO_MINI.put('m', "<strikethrough>");
        LEGACY_TO_MINI.put('n', "<underlined>");
        LEGACY_TO_MINI.put('o', "<italic>");
        LEGACY_TO_MINI.put('r', "<reset>");
    }

    /**
     * Converts legacy Minecraft color codes (& or §) and hex colors (e.g. &#ff00ff)
     * into Adventure MiniMessage XML-like tags.
     *
     * @param input Raw text containing legacy codes.
     * @return Converted MiniMessage string.
     */
    public static String convertLegacyToMiniMessage(String input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '#' && i + 6 < input.length()) {
                final char prev = i > 0 ? input.charAt(i - 1) : 0;
                if (prev != '<' && prev != ':') {
                    final String hex = input.substring(i + 1, i + 7);
                    if (hex.matches("[0-9a-fA-F]{6}")) {
                        sb.append('<').append('#').append(hex).append('>');
                        i += 6;
                        continue;
                    }
                }
            }
            if (c == '&' || c == '§') {
                if (i + 7 < input.length() && input.charAt(i + 1) == '#') {
                    final String hex = input.substring(i + 2, i + 8);
                    if (hex.matches("[0-9a-fA-F]{6}")) {
                        sb.append('<').append('#').append(hex).append('>');
                        i += 7;
                        continue;
                    }
                }
                if (i + 1 < input.length()) {
                    final char code = Character.toLowerCase(input.charAt(i + 1));
                    final String replacement = LEGACY_TO_MINI.get(code);

                    if (replacement != null) {
                        sb.append(replacement);
                        i++;
                        continue;
                    }
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Deserializes a message string into an Adventure Component.
     * Caches parsed components for 15 minutes to maximize performance.
     *
     * @param message Raw string to translate.
     * @return Deserialized net.kyori Component.
     */
    public static Component text(String message) {
        if (message == null || message.isEmpty()) return Component.empty();
        try {
            return MiniMessage.miniMessage().deserialize(MESSAGE_CACHE.get(message, ()
                    -> convertLegacyToMiniMessage(message)));
        } catch (Exception e) {
            return MiniMessage.miniMessage().deserialize(convertLegacyToMiniMessage(message));
        }
    }

    /**
     * Deserializes a message, automatically prefixing it with the configured plugin prefix.
     *
     * @param message Raw message string.
     * @return Deserialized net.kyori Component containing prefix.
     */
    public static Component textWithPrefix(String message) {
        if (message == null || message.isEmpty()) return Component.empty();
        try {
            String prefix = ConfigManager.getString("prefix","");
            return MiniMessage.miniMessage().deserialize(MESSAGE_CACHE.get(message, ()
                    -> convertLegacyToMiniMessage(prefix + message)));
        } catch (Exception e) {
            return MiniMessage.miniMessage().deserialize(convertLegacyToMiniMessage(message));
        }
    }
}
