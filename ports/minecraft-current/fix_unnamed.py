import os

f = 'src/main/java/org/examplee/proyecto_intento/entity/ToiletSeatEntity.java'
with open(f, 'w', encoding='utf-8') as file: file.write('''package org.examplee.proyecto_intento.entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
public final class ToiletSeatEntity extends Entity {
    public ToiletSeatEntity(EntityType<?> t, World w) { super(t,w); }
    @Override protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder b) {}
    @Override protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override protected void writeCustomDataToNbt(NbtCompound nbt) {}
    @Override public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount) { return false; }
    
    public static boolean sit(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.player.PlayerEntity player) { 
        if (world.isClient) return true;
        ToiletSeatEntity seat = ModEntities.TOILET_SEAT.create((net.minecraft.server.world.ServerWorld)world, net.minecraft.entity.SpawnReason.NATURAL);
        if (seat != null) {
            seat.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            world.spawnEntity(seat);
            player.startRiding(seat);
            return true;
        }
        return false;
    }
}''')
print("Fixed unnamed class issue")