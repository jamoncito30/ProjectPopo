package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class FetidSlimeEntity extends SlimeEntity implements InvasionParticipant {
    private final CombatState combatState = new CombatState();
    private static final net.minecraft.entity.data.TrackedData<Integer> COMBAT_ROLE = net.minecraft.entity.data.DataTracker.registerData(FetidSlimeEntity.class,net.minecraft.entity.data.TrackedDataHandlerRegistry.INTEGER);
    public CombatState combat(){return combatState;}
    public int combatRole(){return dataTracker.get(COMBAT_ROLE);}
    public void combatRole(int role){dataTracker.set(COMBAT_ROLE,role);}
    @Override protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder b){super.initDataTracker(b);b.add(COMBAT_ROLE,0);}
    @Override public void writeCustomDataToNbt(net.minecraft.nbt.NbtCompound n){super.writeCustomDataToNbt(n);combatState.write(n,combatRole());}
    @Override public void readCustomDataFromNbt(net.minecraft.nbt.NbtCompound n){super.readCustomDataFromNbt(n);combatRole(combatState.read(n));}
    @Override public void checkDespawn(){if(combatState.event==null)super.checkDespawn();}

    public FetidSlimeEntity(EntityType<? extends SlimeEntity> type, World world) {
        super(type, world);
        setSize(2, true);
    }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return SlimeEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 16)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .3).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3);
    }
    @Override public void setSize(int size, boolean heal) {
        super.setSize(2, false);
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(16);
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(3);
        if (heal) setHealth(getMaxHealth());
    }
    @Override public void remove(RemovalReason reason) {
        // Vanilla splits only dead slimes larger than one. This mutation is local to removal.
        if (reason == RemovalReason.KILLED) super.setSize(1, false);
        super.remove(reason);
    }
    @Override public void onDeath(DamageSource source) {
        InvasionSystem.died(this);
        if (!dead && combatState.event == null && getWorld() instanceof ServerWorld world) {
            var cloud = new AreaEffectCloudEntity(world, getX(), getY()+.05, getZ());
            cloud.setRadius(2.5F);
            cloud.setDuration(200);
            cloud.setWaitTime(0);
            cloud.setRadiusOnUse(0);
            cloud.setRadiusGrowth(-2.0F/200);
            cloud.addEffect(new StatusEffectInstance(StatusEffects.POISON, 80));
            cloud.addEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 120));
            world.spawnEntity(cloud);
            world.spawnParticles(ParticleTypes.SNEEZE, getX(),getY()+.4,getZ(),25,.6,.3,.6,.04);
            playSound(SoundEvents.ENTITY_SLIME_DEATH,1,.6F);
        }
        super.onDeath(source);
    }
}

