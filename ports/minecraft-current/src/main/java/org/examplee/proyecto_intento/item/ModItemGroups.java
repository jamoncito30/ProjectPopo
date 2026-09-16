package org.examplee.proyecto_intento.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.entity.ModEntities;

public final class ModItemGroups {
    public static final SpawnEggItem BEETLE_EGG = Registry.register(Registries.ITEM,
            Identifier.of("proyecto_intento", "dung_beetle_spawn_egg"),
            new SpawnEggItem(ModEntities.DUNG_BEETLE, 0x292D18, 0xAB8542, new net.minecraft.item.Item.Settings()));
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of("proyecto_intento", "popocraft"),
                FabricItemGroup.builder().displayName(Text.translatable("itemGroup.proyecto_intento.popocraft"))
                        .icon(() -> new ItemStack(ModItems.POPO)).entries((context, entries) -> {
                            entries.add(ModItems.POPO);
                            entries.add(ModBlocks.INODORO);
                            entries.add(ModItems.DESATASCADOR);
                            entries.add(ModItems.ESTIERCOL);
                            entries.add(ModBlocks.WET_POPO);
                            entries.add(ModBlocks.DRY_POPO);
                            entries.add(ModBlocks.POPO_PILE);
                            entries.add(ModBlocks.BEETLE_NEST);
                            
                            
                            
                            
                            entries.add(BEETLE_EGG);
                            for (var bottle : new net.minecraft.item.Item[]{net.minecraft.item.Items.POTION,
                                    net.minecraft.item.Items.SPLASH_POTION, net.minecraft.item.Items.LINGERING_POTION}) {
                                entries.add(net.minecraft.component.type.PotionContentsComponent.createStack(bottle, ModPotions.BOTTLED_FART));
                            }
                        }).build());
    }
    private ModItemGroups() { }
}
