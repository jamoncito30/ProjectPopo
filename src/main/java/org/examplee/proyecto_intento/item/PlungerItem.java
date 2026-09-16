package org.examplee.proyecto_intento.item;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.*;

public final class PlungerItem extends SwordItem {
    public PlungerItem(Settings settings) { super(ToolMaterials.WOOD,settings); }
    @Override public boolean postHit(ItemStack stack,LivingEntity target,LivingEntity attacker) {
        if(!target.getWorld().isClient) {
            target.takeKnockback(.9,MathHelper.sin(attacker.getYaw()*MathHelper.RADIANS_PER_DEGREE),-MathHelper.cos(attacker.getYaw()*MathHelper.RADIANS_PER_DEGREE));
            target.velocityModified=true;
        }
        return super.postHit(stack,target,attacker);
    }
    public static boolean unclog(World world,BlockPos pos,PlayerEntity player,Hand hand) {
        var state=world.getBlockState(pos);
        if(!state.isOf(ModBlocks.INODORO) || !state.get(ToiletBlock.CLOGGED) || !player.canModifyBlocks()) return false;
        if(!world.isClient) {
            world.setBlockState(pos,state.with(ToiletBlock.CLOGGED,false),3);
            player.getStackInHand(hand).damage(1,player,hand==Hand.MAIN_HAND?EquipmentSlot.MAINHAND:EquipmentSlot.OFFHAND);
            world.playSound(null,pos,SoundEvents.BLOCK_SLIME_BLOCK_BREAK,SoundCategory.BLOCKS,.6F,.8F);
        }
        return true;
    }
    @Override public ActionResult useOnBlock(ItemUsageContext c) {
        return c.getPlayer()!=null && unclog(c.getWorld(),c.getBlockPos(),c.getPlayer(),c.getHand()) ? ActionResult.success(c.getWorld().isClient) : ActionResult.PASS;
    }
}
