package org.examplee.proyecto_intento.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.entity.mob.SlimeEntity$SlimeMoveControl")
public interface SlimeMoveControlAccessor {
    @Invoker("look") void popocraft$look(float yaw, boolean jumpOften);
    @Invoker("move") void popocraft$move(double speed);
}
