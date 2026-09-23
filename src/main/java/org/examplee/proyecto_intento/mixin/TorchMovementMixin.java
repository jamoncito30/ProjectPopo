package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.examplee.proyecto_intento.entity.TorchRepulsion;

@Mixin(Entity.class)
public abstract class TorchMovementMixin {
    @ModifyVariable(method="move",at=@At("HEAD"),argsOnly=true)
    private Vec3d popocraft$torchBarrier(Vec3d movement) { return TorchRepulsion.restrict((Entity)(Object)this,movement); }
}
