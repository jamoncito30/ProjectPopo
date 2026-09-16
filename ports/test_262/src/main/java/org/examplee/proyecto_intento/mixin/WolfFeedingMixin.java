package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.examplee.proyecto_intento.entity.PopoDrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public abstract class WolfFeedingMixin {
    @Inject(method = "interactMob", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/WolfEntity;heal(F)V", shift = At.Shift.AFTER))
    private void popocraft$healingFood(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PopoDrops.onFed((WolfEntity) (Object) this);
    }
}
