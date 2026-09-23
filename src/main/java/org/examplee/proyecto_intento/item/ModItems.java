package org.examplee.proyecto_intento.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.Proyecto_intento;

public class ModItems {

    // Registrando el ítem principal: Popó
    public static final Item INVASION_DUNG_SHOT=net.minecraft.registry.Registry.register(net.minecraft.registry.Registries.ITEM,net.minecraft.util.Identifier.of("proyecto_intento","invasion_dung_shot"),new Item(new Item.Settings()));
    public static final Item INVASION_ACID_SPIT=net.minecraft.registry.Registry.register(net.minecraft.registry.Registries.ITEM,net.minecraft.util.Identifier.of("proyecto_intento","invasion_acid_spit"),new Item(new Item.Settings()));
    public static final Item POPO = registerItem("popo", new PopoItem(new Item.Settings()));

    // Método de utilidad para simplificar el registro
    private static Item registerItem(String name, Item item) {
        // En versiones 1.21+, se utiliza Identifier.of
        return Registry.register(Registries.ITEM, Identifier.of(Proyecto_intento.MOD_ID, name), item);
    }

    public static final net.minecraft.item.Item BEETLE_SHELL = registerItem("beetle_shell", new net.minecraft.item.Item(new net.minecraft.item.Item.Settings()));
    public static final Item ESTIERCOL = registerItem("estiercol", new FertilizerItem(new Item.Settings()));
    public static final Item VISCOUS_BIOMASS = registerItem("viscous_biomass",new Item(new Item.Settings()));
    public static final Item TOXIC_PLUNGER = registerItem("toxic_plunger",new ToxicPlungerItem(new Item.Settings().maxDamage(250)
            .attributeModifiers(net.minecraft.item.SwordItem.createAttributeModifiers(net.minecraft.item.ToolMaterials.IRON,2,-2.4F))));
    public static final Item SEWAGE_BUCKET = registerItem("sewage_bucket",new SewageBucketItem(org.examplee.proyecto_intento.block.ModBlocks.SEWAGE,new Item.Settings().maxCount(1)));
    public static final Item DESATASCADOR = registerItem("desatascador", new PlungerItem(new Item.Settings().maxDamage(128).attributeModifiers(net.minecraft.item.SwordItem.createAttributeModifiers(net.minecraft.item.ToolMaterials.WOOD, 2, -2.4f))));

    public static void registerModItems() {
        Proyecto_intento.LOGGER.info("Registrando Mod Items para " + Proyecto_intento.MOD_ID);

        // La pestaña creativa se registra en ModItemGroups.
    }
}
