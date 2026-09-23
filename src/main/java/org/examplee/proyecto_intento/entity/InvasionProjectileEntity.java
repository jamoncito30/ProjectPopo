package org.examplee.proyecto_intento.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.*;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.item.ModItems;

public final class InvasionProjectileEntity extends ThrownItemEntity {
    public InvasionProjectileEntity(EntityType<? extends InvasionProjectileEntity> type,World world){super(type,world);}
    @Override protected Item getDefaultItem(){return ModItems.INVASION_DUNG_SHOT;}
    public static void fire(MobEntity owner,LivingEntity target) {
        var shot=new InvasionProjectileEntity(ModEntities.INVASION_PROJECTILE,owner.getWorld());shot.setOwner(owner);
        shot.setItem(new ItemStack(owner instanceof DungBeetleEntity?ModItems.INVASION_DUNG_SHOT:ModItems.INVASION_ACID_SPIT));
        shot.setPosition(owner.getX(),owner.getEyeY(),owner.getZ());
        var d=target.getPos().add(0,target.getHeight()*.5,0).subtract(shot.getPos());
        shot.setVelocity(d.x,d.y+d.horizontalLength()*.08,d.z,.85F,1);owner.getWorld().spawnEntity(shot);
    }
    @Override protected boolean canHit(Entity e){return super.canHit(e) && getOwner()!=null && InvasionSystem.enemies(getOwner(),e);}
    @Override protected void onEntityHit(EntityHitResult hit) {
        if(!getWorld().isClient && getOwner() instanceof LivingEntity owner && InvasionSystem.enemies(owner,hit.getEntity())) {
            var e=hit.getEntity();if(e.damage(getDamageSources().thrown(this,owner),owner instanceof DungBeetleEntity?5:2) && e instanceof LivingEntity living)
                living.takeKnockback(.35,owner.getX()-e.getX(),owner.getZ()-e.getZ());
        }
    }
    @Override protected void onCollision(HitResult hit){super.onCollision(hit);if(getWorld() instanceof ServerWorld w){w.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM,getStack()),getX(),getY(),getZ(),6,.1,.1,.1,.03);discard();}}
    @Override public void tick(){super.tick();if(!getWorld().isClient && (age>100 || getOwner()==null || !InvasionSystem.participant(getOwner())))discard();}
}
