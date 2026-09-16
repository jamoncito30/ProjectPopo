package org.examplee.proyecto_intento.item;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.*;

public final class FertilizerItem extends Item {
    public FertilizerItem(Settings settings) { super(settings); }
    public static int apply(World world,BlockPos clicked,boolean simulate) {
        BlockPos center=world.getBlockState(clicked).getBlock() instanceof CropBlock?clicked.down():clicked;
        int count=0;
        for(BlockPos pos:BlockPos.iterate(center.add(-1,0,-1),center.add(1,0,1))) {
            if(!world.isChunkLoaded(pos)) continue;
            var soil=world.getBlockState(pos); var crop=world.getBlockState(pos.up());
            if(soil.isOf(Blocks.FARMLAND) && crop.getBlock() instanceof CropBlock plant && !plant.isMature(crop)) {
                count++;
                if(!simulate && !world.isClient) {
                    world.setBlockState(pos,ModBlocks.FERTILIZED_FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE,soil.get(FarmlandBlock.MOISTURE)),3);
                    ((ServerWorld)world).spawnParticles(ParticleTypes.HAPPY_VILLAGER,pos.getX()+.5,pos.getY()+1.2,pos.getZ()+.5,2,.3,.1,.3,0);
                }
            }
        }
        return count;
    }
    @Override public ActionResult useOnBlock(ItemUsageContext c) {
        var player=c.getPlayer();
        if(player==null || !player.canModifyBlocks()) return ActionResult.PASS;
        BlockPos center=c.getWorld().getBlockState(c.getBlockPos()).getBlock() instanceof CropBlock?c.getBlockPos().down():c.getBlockPos();
        for(BlockPos p:BlockPos.iterate(center.add(-1,0,-1),center.add(1,0,1))) if(!c.getWorld().canPlayerModifyAt(player,p)) return ActionResult.FAIL;
        if(apply(c.getWorld(),c.getBlockPos(),c.getWorld().isClient)==0) return ActionResult.PASS;
        if(!c.getWorld().isClient && !player.isCreative()) c.getStack().decrement(1);
        return ActionResult.SUCCESS;
    }
}
