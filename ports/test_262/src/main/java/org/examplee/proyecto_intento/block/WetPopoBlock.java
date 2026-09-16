package org.examplee.proyecto_intento.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random;
import org.examplee.proyecto_intento.entity.ModParticles;

public final class WetPopoBlock extends Block {
    public static final MapCodec<WetPopoBlock> CODEC = createCodec(WetPopoBlock::new);

    public WetPopoBlock(Settings settings) { super(settings); }

    @Override
    public MapCodec<WetPopoBlock> getCodec() { return CODEC; }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof PlayerEntity player) || player.isSpectator() || player.getAbilities().flying) {
            return;
        }
        // Applied on both sides for smooth prediction. Gravity produces gradual sinking.
        player.slowMovement(state, new Vec3d(0.35, 0.08, 0.35));
        player.fallDistance = 0;
        if (!world.isClient && !player.isCreative()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 40, 1, false, false, true));
        }
    }

    public static boolean coversEyes(Entity entity) {
        return entity.getWorld().getBlockState(BlockPos.ofFloored(
                entity.getX(), entity.getEyeY(), entity.getZ())).isOf(ModBlocks.WET_POPO);
    }

    @Override public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.up()).isAir() && random.nextInt(5) == 0) {
            world.addParticle(ModParticles.FLY, pos.getX() + 0.5, pos.getY() + 1.1 + random.nextDouble() * 0.45,
                    pos.getZ() + 0.5, 0, 0, 0);
        }
    }
}
