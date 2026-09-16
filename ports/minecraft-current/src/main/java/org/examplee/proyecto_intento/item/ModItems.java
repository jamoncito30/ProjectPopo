package org.examplee.proyecto_intento.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.Proyecto_intento;

public class ModItems {

    // Registrando el ítem principal: Popó
    public static final Item POPO = registerItem("popo", new PopoItem(new Item.Settings()));

    // Método de utilidad para simplificar el registro
    private static Item registerItem(String name, Item item) {
        // En versiones 1.21+, se utiliza Identifier.of
        return Registry.register(Registries.ITEM, Identifier.of(Proyecto_intento.MOD_ID, name), item);
    }

    public static final net.minecraft.item.Item BEETLE_SHELL = registerItem("beetle_shell", new net.minecraft.item.Item(new net.minecraft.item.Item.Settings()));
    public static final Item ESTIERCOL = registerItem("estiercol", new FertilizerItem(new Item.Settings()));
    public static final Item DESATASCADOR = registerItem("desatascador", new PlungerItem(new Item.Settings().maxDamage(128)));

    public static void registerModItems() {
        Proyecto_intento.LOGGER.info("Registrando Mod Items para " + Proyecto_intento.MOD_ID);

        // La pestaña creativa se registra en ModItemGroups.
    }
}
