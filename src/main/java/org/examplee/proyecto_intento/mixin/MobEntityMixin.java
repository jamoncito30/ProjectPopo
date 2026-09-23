package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.SmellSystem;
import org.examplee.proyecto_intento.entity.ModEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow @Final protected GoalSelector goalSelector;
    @Shadow @Final protected GoalSelector targetSelector;
    @Shadow protected abstract SoundEvent getAmbientSound();
    @Unique private LivingEntity popocraft$smellSource;
    @Unique private Vec3d popocraft$panicCenter;
    @Unique private int popocraft$panicTicks;
    @Unique private int popocraft$searchTicks;
    @Unique private int popocraft$pathTicks;
    @Unique private int popocraft$soundTicks;
    @Unique private boolean popocraft$fleeing;

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world) { super(type, world); }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    private void popocraft$fleeSmell(CallbackInfo ci) {
        MobEntity mob = (MobEntity) (Object) this;
        if(org.examplee.proyecto_intento.entity.InvasionSystem.tickFighter(mob)) {
            goalSelector.getGoals().stream().filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::stop);
            targetSelector.getGoals().stream().filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::stop);
            ci.cancel();return;
        }
        // Bosses have encounter-specific movement. Preserve those fights.
        if (mob instanceof EnderDragonEntity || mob instanceof WitherEntity || mob.isAiDisabled()) return;
        if (--popocraft$searchTicks <= 0) {
            popocraft$searchTicks = 10;
            popocraft$smellSource = getWorld().getEntitiesByClass(LivingEntity.class, getBoundingBox().expand(12),
                    entity -> entity != this && entity.isAlive() && !entity.isSpectator()
                            && squaredDistanceTo(entity) <= 144 && SmellSystem.isSmelly(entity))
                    .stream().min(java.util.Comparator.comparingDouble(this::squaredDistanceTo)).orElse(null);
        }
        LivingEntity source = popocraft$smellSource;
        boolean panic = hasStatusEffect(ModEffects.STINKY);
        if (panic && popocraft$panicCenter == null) { popocraft$panicCenter = getPos(); popocraft$panicTicks = 0; }
        if (!panic) popocraft$panicCenter = null;
        boolean affected = panic || (source != null && source.isAlive() && !source.isSpectator()
                && source.getWorld() == getWorld() && SmellSystem.isSmelly(source) && squaredDistanceTo(source) <= 144);
        if (!affected) {
            if (popocraft$fleeing) {
                mob.getNavigation().stop();
                popocraft$fleeing = false;
                popocraft$pathTicks = 0;
            }
            return;
        }
        if (!popocraft$fleeing) {
            goalSelector.getGoals().stream().filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::stop);
            targetSelector.getGoals().stream().filter(PrioritizedGoal::isRunning).forEach(PrioritizedGoal::stop);
            popocraft$fleeing = true;
        }
        mob.setTarget(null);
        if (panic) popocraft$panicTicks++;
        // Temporarily replaces both goal AI and Brain AI (villagers/piglins included).
        // Physics, health, status effects and animal production still tick normally.
        if (--popocraft$pathTicks <= 0) {
            popocraft$pathTicks = panic ? 6 : 10;
            double angle = popocraft$panicTicks * 0.10 + getId();
            Vec3d away = panic ? popocraft$panicCenter.add(Math.cos(angle) * 2.5, 0, Math.sin(angle) * 2.5)
                    : mob instanceof PathAwareEntity pathAware ? FuzzyTargeting.findFrom(pathAware, 16, 7, source.getPos())
                    : getPos().add(getPos().subtract(source.getPos()).normalize().multiply(12));
            if (away != null && (panic || away.squaredDistanceTo(source.getPos()) > squaredDistanceTo(source))) {
                mob.getNavigation().startMovingTo(away.x, away.y, away.z, 1.65);
                if (!(mob instanceof PathAwareEntity)) {
                    mob.getMoveControl().moveTo(away.x, away.y, away.z, 1.65);
                }
            }
        }
        if (--popocraft$soundTicks <= 0) {
            SoundEvent sound = getAmbientSound();
            playSound(sound == null ? SoundEvents.ENTITY_GENERIC_HURT : sound, 0.65F, 1.5F + random.nextFloat() * 0.35F);
            popocraft$soundTicks = 12 + random.nextInt(13);
        }
        despawnCounter++;
        mob.getNavigation().tick();
        if (mob.getMoveControl() instanceof SlimeMoveControlAccessor slimeControl) {
            Vec3d away = panic ? new Vec3d(Math.cos(popocraft$panicTicks * 0.1), 0, Math.sin(popocraft$panicTicks * 0.1))
                    : getPos().subtract(source.getPos());
            slimeControl.popocraft$look((float) (Math.toDegrees(Math.atan2(away.z, away.x)) - 90), true);
            slimeControl.popocraft$move(1.65);
        }
        if (mob.isTouchingWater() || mob.isInLava()) mob.getJumpControl().setActive();
        mob.getMoveControl().tick();
        mob.getLookControl().tick();
        mob.getJumpControl().tick();
        ci.cancel();
    }
}
