package org.examplee.proyecto_intento.item;

import java.util.List;
import java.util.Map;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.Proyecto_intento;
import org.examplee.proyecto_intento.block.ModBlocks;

public final class ModArmor {
    public static final RegistryEntry<ArmorMaterial> MATERIAL = Registry.registerReference(
            Registries.ARMOR_MATERIAL, Identifier.of(Proyecto_intento.MOD_ID, "popo"),
            new ArmorMaterial(Map.of(ArmorItem.Type.HELMET, 1, ArmorItem.Type.CHESTPLATE, 3,
                    ArmorItem.Type.LEGGINGS, 2, ArmorItem.Type.BOOTS, 1, ArmorItem.Type.BODY, 3),
                    12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> Ingredient.ofItems(ModBlocks.DRY_POPO),
                    List.of(new ArmorMaterial.Layer(Identifier.of(Proyecto_intento.MOD_ID, "popo"))), 0, 0));

    public static final Item HELMET = register("popo_helmet", ArmorItem.Type.HELMET, 110);
    public static final Item CHESTPLATE = register("popo_chestplate", ArmorItem.Type.CHESTPLATE, 160);
    public static final Item LEGGINGS = register("popo_leggings", ArmorItem.Type.LEGGINGS, 150);
    public static final Item BOOTS = register("popo_boots", ArmorItem.Type.BOOTS, 130);

    private static Item register(String name, ArmorItem.Type type, int durability) {
        return Registry.register(Registries.ITEM, Identifier.of(Proyecto_intento.MOD_ID, name),
                new ArmorItem(MATERIAL, type, new Item.Settings().maxDamage(durability)));
    }

    public static boolean hasFullSet(LivingEntity entity) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).isOf(HELMET)
                && entity.getEquippedStack(EquipmentSlot.CHEST).isOf(CHESTPLATE)
                && entity.getEquippedStack(EquipmentSlot.LEGS).isOf(LEGGINGS)
                && entity.getEquippedStack(EquipmentSlot.FEET).isOf(BOOTS);
    }

    public static void initialize() {
    }

    public static boolean isPopoArmor(ItemStack stack) {
        return stack.isOf(HELMET) || stack.isOf(CHESTPLATE) || stack.isOf(LEGGINGS) || stack.isOf(BOOTS);
    }

    private ModArmor() { }
}
