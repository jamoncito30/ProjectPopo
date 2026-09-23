package org.examplee.proyecto_intento.entity;

import java.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.ModBlocks;

/** Loaded torches indexed by chunk. Weak world keys do not retain closed worlds. */
public final class TorchRepulsion {
    private static final Map<World,Map<Long,Set<BlockPos>>> TORCHES=new WeakHashMap<>();
    public static void add(World world,BlockPos pos) {
        if(!world.isClient) TORCHES.computeIfAbsent(world,w->new HashMap<>()).computeIfAbsent(ChunkPos.toLong(pos),k->new HashSet<>()).add(pos.toImmutable());
    }
    public static void remove(World world,BlockPos pos) {
        var chunks=TORCHES.get(world); if(chunks==null)return;
        var entries=chunks.get(ChunkPos.toLong(pos));
        if(entries!=null) { entries.remove(pos);if(entries.isEmpty())chunks.remove(ChunkPos.toLong(pos)); }
        if(chunks.isEmpty()) TORCHES.remove(world);
    }
    public static Vec3d restrict(Entity entity,Vec3d movement) {
        if(entity.getWorld().isClient || !(entity instanceof Monster) || movement.lengthSquared()==0) return movement;
        var chunks=TORCHES.get(entity.getWorld()); if(chunks==null)return movement;
        var start=entity.getPos(); var end=start.add(movement);
        int minX=MathHelper.floor((Math.min(start.x,end.x)-10)/16), maxX=MathHelper.floor((Math.max(start.x,end.x)+10)/16);
        int minZ=MathHelper.floor((Math.min(start.z,end.z)-10)/16), maxZ=MathHelper.floor((Math.max(start.z,end.z)+10)/16);
        // Very large displacements use the sparse index rather than enumerating empty chunks.
        Collection<Set<BlockPos>> candidates;
        if((long)(maxX-minX+1)*(maxZ-minZ+1)>64) candidates=chunks.values();
        else {
            candidates=new ArrayList<>();
            for(int x=minX;x<=maxX;x++)for(int z=minZ;z<=maxZ;z++) { var set=chunks.get(ChunkPos.toLong(x,z));if(set!=null)candidates.add(set); }
        }
        for(var set:candidates)for(var pos:set) {
            if(!entity.getWorld().isChunkLoaded(pos) || !entity.getWorld().getBlockState(pos).isOf(ModBlocks.PESTILENT_TORCH))continue;
            var center=pos.toCenterPos();
            double before=start.squaredDistanceTo(center), after=end.squaredDistanceTo(center);
            if(before<100) { if(after>=before) continue; }
            else {
                double fraction=Math.clamp(center.subtract(start).dotProduct(movement)/movement.lengthSquared(),0,1);
                if(start.add(movement.multiply(fraction)).squaredDistanceTo(center)>=100)continue;
            }
            // Preserve gravity when the horizontal component is what enters the protected sphere.
            Vec3d vertical=new Vec3d(0,movement.y,0);
            return start.add(vertical).squaredDistanceTo(center)>=Math.min(before,100)?vertical:Vec3d.ZERO;
        }
        return movement;
    }
    private TorchRepulsion() { }
}
