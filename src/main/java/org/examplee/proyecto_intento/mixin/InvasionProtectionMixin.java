package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.examplee.proyecto_intento.entity.InvasionSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class InvasionProtectionMixin {
    @Inject(method="damage",at=@At("HEAD"),cancellable=true)
    private void invasion$damage(DamageSource source,float amount,CallbackInfoReturnable<Boolean> ci) {
        Entity self=(Entity)(Object)this;
        Entity attacker = source.getAttacker();
        
        // Allows /kill and Magic damage
        if(source.isOf(net.minecraft.entity.damage.DamageTypes.OUT_OF_WORLD) || source.isOf(net.minecraft.entity.damage.DamageTypes.MAGIC)) return;

        if(attacker != null) {
            if(InvasionSystem.participant(self) && !InvasionSystem.enemies(self,attacker)) ci.setReturnValue(false);
            else if(InvasionSystem.participant(attacker) && !InvasionSystem.enemies(self,attacker)) ci.setReturnValue(false);
        }
    }
    @Inject(method="addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z",at=@At("HEAD"),cancellable=true)
    private void invasion$effect(StatusEffectInstance effect,Entity source,CallbackInfoReturnable<Boolean> ci) {
        if(InvasionSystem.participant((Entity)(Object)this) && effect.getEffectType() != net.minecraft.entity.effect.StatusEffects.GLOWING) {
            ci.setReturnValue(false);
        }
    }
}
