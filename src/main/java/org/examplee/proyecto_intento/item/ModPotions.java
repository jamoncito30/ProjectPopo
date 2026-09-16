package org.examplee.proyecto_intento.item;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.entity.ModEffects;

public final class ModPotions {
    public static final RegistryEntry<Potion> BOTTLED_FART = Registry.registerReference(Registries.POTION,
            Identifier.of("proyecto_intento", "bottled_fart"),
            new Potion("bottled_fart", new StatusEffectInstance(ModEffects.STINKY, 600)));

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder ->
                builder.registerPotionRecipe(Potions.AWKWARD, ModItems.POPO, BOTTLED_FART));
    }
    private ModPotions() { }
}
