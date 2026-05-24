package dev.parrotstudios.qtotems.totem;

import dev.parrotstudios.qtotems.QTotems;
import dev.parrotstudios.qtotems.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a custom totem configuration, wrapping item properties
 * and registering associated passive/active potion effects.
 */
@SuppressWarnings("UnusedReturnValue")
public class QTotem {
    private final String name;
    private final ItemStack totemItem;
    private final ItemMeta totemMeta;
    private final NamespacedKey key;

    private final List<PotionEffect> equipEffects = new ArrayList<>();
    private final List<PotionEffect> popEffects = new ArrayList<>();

    /**
     * Factory method to create a new QTotem instance.
     *
     * @param name The raw name ID of the custom totem.
     * @return A new QTotem builder instance.
     */
    public static QTotem create(String name){
        return new QTotem(name);
    }

    /**
     * Constructor for QTotem. Initializes item stack properties
     * and sets persistent metadata tags for identification.
     *
     * @param name Unique key identifier of the custom totem.
     */
    private QTotem(String name){
        this.name = name;
        totemItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        totemMeta = totemItem.getItemMeta();
        key = new NamespacedKey(QTotems.getInstance(), name);
        totemMeta.setEnchantmentGlintOverride(true);
        totemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
    }

    /**
     * Retrieves the raw ID name of the totem.
     *
     * @return Totem name ID.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the namespaced key associated with this custom totem's NBT identity.
     *
     * @return NamespacedKey instance.
     */
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Generates a cloned ItemStack of the custom totem with custom display name,
     * lore, and metadata tags set.
     *
     * @return A cloned ItemStack instance.
     */
    public ItemStack getTotemItem(){
        return totemItem.clone();
    }

    /**
     * Retrieves an immutable copy of the registered passive equip effects.
     *
     * @return Read-only list of PotionEffects.
     */
    public List<PotionEffect> getEquipEffects() {
        return List.copyOf(equipEffects);
    }

    /**
     * Retrieves an immutable copy of the registered active pop effects.
     *
     * @return Read-only list of PotionEffects.
     */
    public List<PotionEffect> getPopEffects() {
        return List.copyOf(popEffects);
    }

    /**
     * Builder method to set the custom display name of the totem.
     * Supports MiniMessage format.
     *
     * @param name Custom display name.
     * @return The current QTotem builder instance.
     */
    public QTotem displayName(String name){
        totemMeta.displayName(Utils.text(name));
        return this;
    }

    /**
     * Builder method to set the custom lore/description lines of the totem.
     * Supports MiniMessage format.
     *
     * @param lore List of custom lore strings.
     * @return The current QTotem builder instance.
     */
    public QTotem lore(List<String> lore){
        List<Component> loreFormat = lore.stream().map(Utils::text).toList();
        totemMeta.lore(loreFormat);
        return this;
    }

    /**
     * Parses and registers a passive equip effect to the totem.
     *
     * @param potionEffectName Registry name of the Minecraft potion effect.
     * @param level Amplifier level of the potion effect.
     * @return The current QTotem builder instance.
     */
    public QTotem addEquipEffect(String potionEffectName, int level){
        PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(potionEffectName));
        if(type == null){
            QTotems.getInstance().getLogger().warning("Invalid pop effect name: " + potionEffectName + " for totem: " + this.getName());
            return this;
        }
        equipEffects.add(new PotionEffect(type, Integer.MAX_VALUE, level,false,false,false));
        return this;
    }

    /**
     * Parses and registers a timed pop effect to the totem.
     *
     * @param potionEffectName Registry name of the Minecraft potion effect.
     * @param level Amplifier level of the potion effect.
     * @param duration Duration in ticks.
     * @return The current QTotem builder instance.
     */
    public QTotem addPopEffect(String potionEffectName, int level, int duration){
        PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(potionEffectName));
        if(type == null){
            QTotems.getInstance().getLogger().warning("Invalid pop effect name: " + potionEffectName + " for totem: " + this.getName());
            return this;
        }
        popEffects.add(new PotionEffect(type, duration, level,false,false,true));
        return this;
    }

    /**
     * Applies all passive equip effects to the player.
     *
     * @param player Target player.
     */
    public void provideEquipEffects(Player player){
        this.getEquipEffects().forEach(player::addPotionEffect);
    }

    /**
     * Applies all active pop effects to the player.
     *
     * @param player Target player.
     */
    public void providePopEffects(Player player){
        this.getPopEffects().forEach(player::addPotionEffect);
    }

    /**
     * Removes active passive equip effects from the player safely.
     *
     * @param player Target player.
     */
    public void removeEquipEffects(Player player){
        this.getEquipEffects().forEach(effect -> {
            if(!player.hasPotionEffect(effect.getType())) return;
            if(Objects.requireNonNull(player.getPotionEffect(effect.getType())).getAmplifier() > effect.getAmplifier()) return;
            player.removePotionEffect(effect.getType());
        });
    }

    /**
     * Finalizes construction, applies the modified ItemMeta,
     * and registers this custom totem inside QTotemRegistry.
     */
    public void register(){
        totemItem.setItemMeta(totemMeta);
        QTotemRegistry.add(this);
    }
}
