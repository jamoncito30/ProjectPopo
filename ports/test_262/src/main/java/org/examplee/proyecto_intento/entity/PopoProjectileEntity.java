package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import org.examplee.proyecto_intento.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.item.ModItems;

public final class PopoProjectileEntity extends ThrownItemEntity {
    public PopoProjectileEntity(EntityType<? extends PopoProjectileEntity> type, World world) { super(type, world); }
    public PopoProjectileEntity(World world, LivingEntity owner) { super(ModEntities.POPO_PROJECTILE, owner, world, new ItemStack(ModItems.POPO)); }
    @Override protected Item getDefaultItem() { return ModItems.POPO; }

    @Override protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (!getWorld().isClient && result.getEntity() instanceof MobEntity mob) {
            mob.addStatusEffect(new StatusEffectInstance(ModEffects.STINKY, 100, 0, false, true, true), getOwner());
        }
    }

    @Override protected void onCollision(HitResult result) {
        super.onCollision(result);
        if (getWorld() instanceof ServerWorld world) {
            world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, getStack()),
                    getX(), getY(), getZ(), 12, 0.15, 0.15, 0.15, 0.08);
            playSound(SoundEvents.BLOCK_MUD_BREAK, 0.7F, 1.1F);
            discard();
        }
    }

    @Override public void tick() {
        super.tick();
        if (!getWorld().isClient && age > 200) discard();
    }
}
