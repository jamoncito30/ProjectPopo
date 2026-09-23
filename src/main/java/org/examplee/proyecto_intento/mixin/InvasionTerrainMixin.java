package org.examplee.proyecto_intento.mixin;

import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.examplee.proyecto_intento.entity.InvasionSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Freeze terrain inside the active arena, including fluids, fire and explosions. */
@Mixin(World.class)
public abstract class InvasionTerrainMixin {
    @Inject(method="setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",at=@At("HEAD"),cancellable=true)
    private void invasion$terrain(BlockPos pos,BlockState state,int flags,int depth,CallbackInfoReturnable<Boolean> ci) {
        if(InvasionSystem.protectedAt((World)(Object)this,pos))ci.setReturnValue(false);
    }
}
