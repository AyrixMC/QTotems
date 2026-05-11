package dev.parrotstudios.qTotems.totems;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class TotemRegistry {
    private static final List<QTotem> qTotems = new ArrayList<>();

    public static List<QTotem> getQTotems(){
        return new ArrayList<>(qTotems);
    }

    public static void add(QTotem qTotem){
        qTotems.add(qTotem);
    }

    public static List<String> getTotemNames(){
        return qTotems.stream().map(QTotem::getName).toList();
    }

    public boolean isQTotem(ItemStack stack){
        if (stack == null || !stack.hasItemMeta()) return false;
        PersistentDataContainer pdc = stack.getItemMeta().getPersistentDataContainer();
        return qTotems.stream().map(QTotem::getKey).anyMatch(pdc::has);
    }
}
