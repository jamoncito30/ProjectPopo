package org.examplee.proyecto_intento.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
    @Inject(method="canPlantOnTop",at=@At("HEAD"),cancellable=true)
    private void popocraft$fertilizedSoil(BlockState floor,BlockView world,BlockPos pos,CallbackInfoReturnable<Boolean> cir) {
        if(floor.isOf(ModBlocks.FERTILIZED_FARMLAND)) cir.setReturnValue(true);
    }
}
