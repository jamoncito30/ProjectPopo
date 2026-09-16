package org.examplee.proyecto_intento.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public final class ModEntities {
    public static final EntityType<ToiletSeatEntity> TOILET_SEAT = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("proyecto_intento","toilet_seat"),EntityType.Builder.<ToiletSeatEntity>create(ToiletSeatEntity::new,SpawnGroup.MISC).dimensions(.1F,.1F).disableSaving().disableSummon().maxTrackingRange(4).build("proyecto_intento:toilet_seat"));
    public static final EntityType<PopoProjectileEntity> POPO_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("proyecto_intento", "popo_projectile"), EntityType.Builder.<PopoProjectileEntity>create(PopoProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build("proyecto_intento:popo_projectile"));
    public static final EntityType<DungBeetleEntity> DUNG_BEETLE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("proyecto_intento", "dung_beetle"), EntityType.Builder.create(DungBeetleEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.65F, 0.4F).maxTrackingRange(8).build("proyecto_intento:dung_beetle"));
    public static void initialize() {
        FabricDefaultAttributeRegistry.register(DUNG_BEETLE, DungBeetleEntity.createAttributes());
        SpawnRestriction.register(DUNG_BEETLE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, pos, random) -> {
                    var ground = world.getBlockState(pos.down());
                    boolean terrain = ground.isIn(net.minecraft.registry.tag.BlockTags.DIRT)
                            || ground.isIn(net.minecraft.registry.tag.BlockTags.SAND)
                            || ground.isOf(net.minecraft.block.Blocks.STONE) || ground.isOf(net.minecraft.block.Blocks.GRAVEL)
                            || ground.isOf(net.minecraft.block.Blocks.SNOW_BLOCK) || ground.isOf(net.minecraft.block.Blocks.MOSS_BLOCK)
                            || ground.isOf(net.minecraft.block.Blocks.TERRACOTTA);
                    return terrain && world.getBaseLightLevel(pos, 0) > 8 && world.isSkyVisible(pos)
                            && world.getFluidState(pos).isEmpty() && random.nextInt(4) == 0;
                });
        BiomeModifications.addSpawn(BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD)
                .or(BiomeSelectors.foundInOverworld()), SpawnGroup.CREATURE, DUNG_BEETLE, 2, 1, 2);
    }
    private ModEntities() { }
}
