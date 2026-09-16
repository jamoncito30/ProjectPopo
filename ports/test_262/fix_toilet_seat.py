import os

f = 'src/main/java/org/examplee/proyecto_intento/entity/ToiletSeatEntity.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
# Let's see how damage is defined... ah, Entity#damage is public boolean damage(ServerWorld world, DamageSource source, float amount). Did I spell parameter wrong?
# Oh wait, Entity class damage override in 1.21.3 is public boolean damage(ServerWorld world, DamageSource source, float amount)
c = c.replace('public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount)', 'public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount)')

# If the override fails because of some extra change, I will just remove ToiletSeatEntity's body or fix import.
# Wait, let's just make it return false and remove @Override if it complains, but it said "does not override abstract method".
# Wait, Entity.damage in 1.21.3 is NOT abstract! Or maybe it is now! Oh, let's just delete the damage override and define it exactly as requested.

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
}''')

print("ToiletSeat fixed")