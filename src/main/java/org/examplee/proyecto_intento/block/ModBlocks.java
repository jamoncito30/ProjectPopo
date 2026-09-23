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
    public static final Block SEWAGE = Registry.register(Registries.BLOCK,Identifier.of(Proyecto_intento.MOD_ID,"sewage"),
            new SewageBlock(AbstractBlock.Settings.copy(Blocks.MUD).noCollision().nonOpaque().strength(.5F).dropsNothing()));
    public static final Block PESTILENT_TORCH = register("pestilent_torch",new PestilentTorchBlock(AbstractBlock.Settings.copy(Blocks.TORCH).luminance(s->12)));
    public static final BlockEntityType<PestilentTorchBlockEntity> PESTILENT_TORCH_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Proyecto_intento.MOD_ID,"pestilent_torch"),FabricBlockEntityTypeBuilder.create(PestilentTorchBlockEntity::new,PESTILENT_TORCH).build());
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
    public static final BlockEntityType<ToiletBlockEntity> TOILET_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Proyecto_intento.MOD_ID,"toilet"),FabricBlockEntityTypeBuilder.create(ToiletBlockEntity::new,INODORO).build());
        public static final Block EXTRACTOR_ESTIERCOL = register("extractor_estiercol", new ExtractorBlock(AbstractBlock.Settings.create().strength(2.0f).nonOpaque()));
    public static final BlockEntityType<ExtractorBlockEntity> EXTRACTOR_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Proyecto_intento.MOD_ID,"extractor_estiercol"),FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new,EXTRACTOR_ESTIERCOL).build());
    public static final Block FERTILIZED_FARMLAND = Registry.register(Registries.BLOCK,Identifier.of(Proyecto_intento.MOD_ID,"fertilized_farmland"),new FertilizedFarmlandBlock(AbstractBlock.Settings.copy(Blocks.FARMLAND)));

    public static void initialize() {
    }

    private ModBlocks() { }
}

