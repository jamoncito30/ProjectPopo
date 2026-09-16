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
    // Reuses vanilla air depletion, Respiration, water breathing, recovery and drowning damage.
    @Redirect(method = "baseTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean popocraft$canBreathe(LivingEntity entity, TagKey<Fluid> fluid) {
        return entity.isSubmergedIn(fluid) || (fluid == FluidTags.WATER
                && entity instanceof PlayerEntity && WetPopoBlock.coversEyes(entity));
    }
}
