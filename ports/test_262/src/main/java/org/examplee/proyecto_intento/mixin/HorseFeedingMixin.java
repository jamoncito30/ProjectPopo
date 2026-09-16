package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.examplee.proyecto_intento.entity.PopoDrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseFeedingMixin {
    @Inject(method = "interactHorse", at = @At("RETURN"))
    private void popocraft$fed(PlayerEntity player, ItemStack food, CallbackInfoReturnable<ActionResult> cir) {
        // SUCCESS is returned server-side only when receiveFood actually accepts the item.
        // Includes virtual receiveFood implementations in llamas and camels.
        if (cir.getReturnValue() == ActionResult.SUCCESS) PopoDrops.onFed((AbstractHorseEntity) (Object) this);
    }
}
