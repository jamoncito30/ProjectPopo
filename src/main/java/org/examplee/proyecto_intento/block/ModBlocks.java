package org.examplee.proyecto_intento.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.Proyecto_intento;

public final class ModBlocks {
    public static final Block GRAND_BEETLE_NEST_CONTROLLER = Registry.register(Registries.BLOCK,
            Identifier.of(Proyecto_intento.MOD_ID, "grand_beetle_nest_controller"),
            new GrandBeetleNestControllerBlock(AbstractBlock.Settings.copy(Blocks.MUD).strength(0.8F).nonOpaque()));
    public static final BlockEntityType<GrandBeetleNestBlockEntity> GRAND_BEETLE_NEST_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Proyecto_intento.MOD_ID, "grand_beetle_nest"),
            FabricBlockEntityTypeBuilder.create(GrandBeetleNestBlockEntity::new, GRAND_BEETLE_NEST_CONTROLLER).build());
    public static final Block GRAND_BEETLE_NEST_PIECE = Registry.register(Registries.BLOCK,
            Identifier.of(Proyecto_intento.MOD_ID, "grand_beetle_nest_piece"),
            new GrandBeetleNestPieceBlock(AbstractBlock.Settings.copy(Blocks.MUD).strength(0.8F).nonOpaque()));
    public static final Block WET_POPO = register("wet_popo", new WetPopoBlock(
            AbstractBlock.Settings.copy(Blocks.MUD).strength(2.5F).nonOpaque()
                    .solidBlock((state, world, pos) -> false)
                    .suffocates((state, world, pos) -> false)
                    .blockVision((state, world, pos) -> false).sounds(BlockSoundGroup.MUD)));
    public static final Block DRY_POPO = register("dry_popo", new Block(
            AbstractBlock.Settings.copy(Blocks.MUD_BRICKS).strength(1.5F, 3.0F)));
    public static final Block POPO_PILE = register("popo_pile", new PopoPileBlock(
            AbstractBlock.Settings.copy(Blocks.MUD).strength(0.3F).nonOpaque()));
    public static final Block BEETLE_NEST = register("beetle_nest", new BeetleNestBlock(
            AbstractBlock.Settings.copy(Blocks.MUD).strength(0.8F).nonOpaque()));
    public static final BlockEntityType<BeetleNestBlockEntity> BEETLE_NEST_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Proyecto_intento.MOD_ID, "beetle_nest"),
            FabricBlockEntityTypeBuilder.create(BeetleNestBlockEntity::new, BEETLE_NEST).build());

    private static Block register(String name, Block block) {
        Identifier id = Identifier.of(Proyecto_intento.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static final Block INODORO = register("inodoro", new ToiletBlock(AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK).nonOpaque()));
    public static final Block EXTRACTOR_ESTIERCOL = Registry.register(Registries.BLOCK,Identifier.of(Proyecto_intento.MOD_ID,"extractor_estiercol"),new Block(AbstractBlock.Settings.create().strength(2.0f)));
    public static final Block FERTILIZED_FARMLAND = Registry.register(Registries.BLOCK,Identifier.of(Proyecto_intento.MOD_ID,"fertilized_farmland"),new FertilizedFarmlandBlock(AbstractBlock.Settings.copy(Blocks.FARMLAND)));

    public static void initialize() {
    }

    private ModBlocks() { }
}
