package org.examplee.proyecto_intento.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/** Local scheduled ticks persist with chunks; no global crop scans or offline growth. */
public final class FertilizedFarmlandBlock extends FarmlandBlock {
    public static final IntProperty CHARGES=IntProperty.of("charges",1,6);
    public static final int INTERVAL=200;
    public static final MapCodec<FarmlandBlock> CODEC=createCodec(FertilizedFarmlandBlock::new);
    public FertilizedFarmlandBlock(Settings settings) { super(settings); setDefaultState(getStateManager().getDefaultState().with(MOISTURE,0).with(CHARGES,6)); }
    @Override public MapCodec<FarmlandBlock> getCodec() { return CODEC; }
    @Override protected void appendProperties(StateManager.Builder<Block,BlockState> b) { super.appendProperties(b); b.add(CHARGES); }
    @Override protected void onBlockAdded(BlockState s,net.minecraft.world.World w,BlockPos p,BlockState old,boolean notify) {
        super.onBlockAdded(s,w,p,old,notify);
        if(!w.isClient && !old.isOf(this)) w.scheduleBlockTick(p,this,INTERVAL);
    }
    @Override protected void scheduledTick(BlockState s,ServerWorld w,BlockPos p,Random r) {
        super.scheduledTick(s,w,p,r);
        if(!w.getBlockState(p).isOf(this)) return;
        var crop=w.getBlockState(p.up());
        if(crop.getBlock() instanceof CropBlock plant && !plant.isMature(crop) && w.getBaseLightLevel(p.up(),0)>=9) {
            w.setBlockState(p.up(),plant.withAge(plant.getAge(crop)+1),Block.NOTIFY_LISTENERS);
        }
        int charges=s.get(CHARGES);
        w.setBlockState(p,charges==1?Blocks.FARMLAND.getDefaultState().with(MOISTURE,w.getBlockState(p).get(MOISTURE)):w.getBlockState(p).with(CHARGES,charges-1),Block.NOTIFY_LISTENERS);
        if(charges>1) w.scheduleBlockTick(p,this,INTERVAL);
    }
}
