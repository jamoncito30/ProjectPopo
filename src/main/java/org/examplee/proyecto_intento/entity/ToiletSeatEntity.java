package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.block.ToiletBlock;

/** Unsaved, invisible mount: vanilla passengers synchronize the sitting pose. */
public final class ToiletSeatEntity extends Entity {
    public ToiletSeatEntity(EntityType<?> type,World world) { super(type,world); noClip=true; setNoGravity(true); setInvisible(true); }
    public static boolean sit(World world,BlockPos pos,PlayerEntity player) {
        if(world.isClient || player.hasVehicle() || !player.isAlive() || !world.getBlockState(pos).isOf(ModBlocks.INODORO)
                || player.squaredDistanceTo(pos.toCenterPos())>16 || !world.getBlockState(pos.up()).isAir() || !world.getBlockState(pos.up(2)).isAir()) return false;
        for(var seat:world.getEntitiesByClass(ToiletSeatEntity.class,new Box(pos),e->e.getBlockPos().equals(pos))) {
            if(seat.hasPassengers()) return false;
            seat.discard();
        }
        var seat=ModEntities.TOILET_SEAT.create(world);
        if(seat==null) return false;
        seat.setPosition(pos.getX()+.5,pos.getY()+.625,pos.getZ()+.5);
        seat.setYaw(world.getBlockState(pos).get(ToiletBlock.FACING).asRotation());
        if(!world.spawnEntity(seat)) return false;
        if(!player.startRiding(seat,true)) { seat.discard(); return false; }
        return true;
    }
    @Override public void tick() {
        super.tick();
        if(!getWorld().isClient && (!getWorld().getBlockState(getBlockPos()).isOf(ModBlocks.INODORO)
                || !hasPassengers() || getFirstPassenger().isRemoved() || !getFirstPassenger().isAlive())) { removeAllPassengers(); discard(); }
    }
    @Override protected Vec3d getPassengerAttachmentPos(Entity passenger,EntityDimensions dimensions,float scale) { return Vec3d.ZERO; }
    @Override protected boolean canAddPassenger(Entity passenger) { return !hasPassengers() && passenger instanceof PlayerEntity; }
    @Override public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        for(Direction d:Direction.Type.HORIZONTAL) {
            Vec3d out=getBlockPos().offset(d).toBottomCenterPos();
            if(getWorld().isSpaceEmpty(passenger,passenger.getDimensions(passenger.getPose()).getBoxAt(out))) return out;
        }
        return getBlockPos().up().toBottomCenterPos();
    }
    @Override protected void initDataTracker(DataTracker.Builder b) { }
    @Override protected void readCustomDataFromNbt(NbtCompound nbt) { }
    @Override protected void writeCustomDataToNbt(NbtCompound nbt) { }
}
