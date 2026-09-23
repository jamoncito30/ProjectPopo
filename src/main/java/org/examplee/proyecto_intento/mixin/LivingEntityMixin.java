package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.examplee.proyecto_intento.block.WetPopoBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @org.spongepowered.asm.mixin.injection.Inject(method="jump",at=@At("HEAD"),cancellable=true)
    private void popocraft$sewagePreventsJump(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        var entity=(LivingEntity)(Object)this;
        if(entity instanceof org.examplee.proyecto_intento.entity.DungBeetleEntity)return;
        if(entity instanceof PlayerEntity player && (player.isCreative() || player.isSpectator())) return;
        if(entity.getWorld().getBlockState(entity.getBlockPos()).isOf(org.examplee.proyecto_intento.block.ModBlocks.SEWAGE)) ci.cancel();
    }
    // Reuses vanilla air depletion, Respiration, water breathing, recovery and drowning damage.
    @Redirect(method = "baseTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean popocraft$canBreathe(LivingEntity entity, TagKey<Fluid> fluid) {
        return entity.isSubmergedIn(fluid) || (fluid == FluidTags.WATER
                && entity instanceof PlayerEntity && WetPopoBlock.coversEyes(entity));
    }
}
