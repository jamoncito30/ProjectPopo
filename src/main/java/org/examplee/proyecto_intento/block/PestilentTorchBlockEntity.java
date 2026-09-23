package org.examplee.proyecto_intento.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.TorchRepulsion;

public final class PestilentTorchBlockEntity extends BlockEntity {
    public PestilentTorchBlockEntity(BlockPos p,BlockState s) { super(ModBlocks.PESTILENT_TORCH_ENTITY,p,s); }
    @Override public void markRemoved() { if(world!=null) TorchRepulsion.remove(world,pos); super.markRemoved(); }
    public static void tick(World world,BlockPos pos,BlockState state,PestilentTorchBlockEntity torch) {
        TorchRepulsion.add(world,pos);
        if(world.getTime()%10!=0) return;
        for(var mob:world.getEntitiesByClass(MobEntity.class,new Box(pos).expand(10),e->e instanceof Monster && e.isAlive() && e.squaredDistanceTo(pos.toCenterPos())<100)) {
            mob.setTarget(null);
            var away=mob.getPos().subtract(pos.toCenterPos());
            if(away.horizontalLengthSquared()<.01) away=new Vec3d(1,0,0);
            mob.addVelocity(away.x/Math.max(1,away.horizontalLength())*.25,0,away.z/Math.max(1,away.horizontalLength())*.25);
            mob.velocityModified=true;
            if(mob instanceof PathAwareEntity pathAware) {
                var destination=net.minecraft.entity.ai.FuzzyTargeting.findFrom(pathAware,16,7,pos.toCenterPos());
                if(destination!=null) mob.getNavigation().startMovingTo(destination.x,destination.y,destination.z,1.4);
            }
        }
    }
}
