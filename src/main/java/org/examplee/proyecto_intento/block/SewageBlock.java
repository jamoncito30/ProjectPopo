package org.examplee.proyecto_intento.block;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.item.ModItems;

/** Contained viscous liquid: deliberately does not spread into adjacent blocks. */
public final class SewageBlock extends Block {
    public SewageBlock(Settings settings) { super(settings); }
    @Override protected net.minecraft.util.shape.VoxelShape getCollisionShape(BlockState state,net.minecraft.world.BlockView world,BlockPos pos,ShapeContext context) {
        if(context instanceof EntityShapeContext e && e.getEntity() instanceof org.examplee.proyecto_intento.entity.DungBeetleEntity)return net.minecraft.util.shape.VoxelShapes.fullCube();
        return super.getCollisionShape(state,world,pos,context);
    }
    @Override protected void onEntityCollision(BlockState state,World world,BlockPos pos,Entity entity) {
        if(entity instanceof org.examplee.proyecto_intento.entity.DungBeetleEntity)return;
        if (entity instanceof LivingEntity living && !(entity instanceof PlayerEntity player && (player.isCreative() || player.isSpectator()))) {
            entity.slowMovement(state,new Vec3d(.15,.5,.15));
            if(entity.getVelocity().y>0) entity.setVelocity(entity.getVelocity().multiply(1,0,1));
            if (!world.isClient) living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,60));
        }
    }
    @Override protected ItemActionResult onUseWithItem(ItemStack stack,BlockState state,World world,BlockPos pos,PlayerEntity player,Hand hand,BlockHitResult hit) {
        if (!stack.isOf(Items.BUCKET) || !player.canModifyBlocks() || !world.canPlayerModifyAt(player,pos)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!world.isClient) {
            world.removeBlock(pos,false);
            player.setStackInHand(hand,ItemUsage.exchangeStack(stack,player,new ItemStack(ModItems.SEWAGE_BUCKET)));
        }
        return ItemActionResult.SUCCESS;
    }
}
