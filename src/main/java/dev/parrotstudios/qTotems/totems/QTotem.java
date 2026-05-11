package dev.parrotstudios.qTotems.totems;

import dev.parrotstudios.qTotems.QTotems;
import dev.parrotstudios.qTotems.QUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Registry;
import java.util.HashMap;
import java.util.List;

public class QTotem {
    private String name;
    private ItemStack totemItem;
    private ItemMeta totemMeta;
    private NamespacedKey key;
    private final HashMap<PotionEffectType, Integer> equipEffects = new HashMap<>();
    private final HashMap<PotionEffectType, Integer> popEffects = new HashMap<>();

    public static QTotem create(String name){
        return new QTotem(name);
    }

    private QTotem(String name){
        this.name = name;
        totemItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        totemMeta = totemItem.getItemMeta();
        key = new NamespacedKey(QTotems.getInstance(), name);
        totemMeta.setEnchantmentGlintOverride(true);
        totemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
    }

    public String getName() {
        return name;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public HashMap<PotionEffectType, Integer> getEquipEffects() {
        return new HashMap<>(equipEffects);
    }

    public HashMap<PotionEffectType, Integer> getPopEffects() {
        return new HashMap<>(popEffects);
    }

    public QTotem displayName(String name){
        totemMeta.displayName(QUtils.text(name));
        return this;
    }

    public QTotem lore(List<String> lore){
        List<Component> loreFormat = lore.stream().map(QUtils::text).toList();
        totemMeta.lore(loreFormat);
        return this;
    }

    public QTotem addEquipEffect(String potionEffectName, int level){
        PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(potionEffectName));
        if(type == null){
            QTotems.getInstance().getLogger().warning("Invalid pop effect name: " + potionEffectName + " for totem: " + this.getName());
            return this;
        }
        equipEffects.put(type, level);
        return this;
    }

    public QTotem addPopEffect(String potionEffectName, int level){
        PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(potionEffectName));
        if(type == null){
            QTotems.getInstance().getLogger().warning("Invalid pop effect name: " + potionEffectName + " for totem: " + this.getName());
            return this;
        }
        popEffects.put(type, level);
        return this;
    }

    public void register(){
        totemItem.setItemMeta(totemMeta);
        TotemRegistry.add(this);
    }

}
