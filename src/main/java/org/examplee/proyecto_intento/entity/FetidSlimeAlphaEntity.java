package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;

public class FetidSlimeAlphaEntity extends FetidSlimeEntity {
    public FetidSlimeAlphaEntity(EntityType<? extends FetidSlimeAlphaEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public static DefaultAttributeContainer.Builder createFetidSlimeAlphaAttributes() {
        return FetidSlimeEntity.createAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.getWorld().isClient() && this.getWorld() instanceof ServerWorld serverWorld) {
            Box box = new Box(this.getBlockPos()).expand(30.0);
            for (ServerPlayerEntity player : serverWorld.getNonSpectatingEntities(ServerPlayerEntity.class, box)) {
                // Apply Fetid Omen for 60 minutes (72000 ticks)
                player.addStatusEffect(new StatusEffectInstance(ModEffects.FETID_OMEN, 72000, 0, false, true, true));
            }
        }
        super.onDeath(damageSource);
    }

    @Override
    public void setSize(int size, boolean heal) {
        super.setSize(4, false);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(100.0);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0);
        if (heal) setHealth(getMaxHealth());
    }
}

