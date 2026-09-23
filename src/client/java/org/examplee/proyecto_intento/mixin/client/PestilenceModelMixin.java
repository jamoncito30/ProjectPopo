package org.examplee.proyecto_intento.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.item.ModPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class PestilenceModelMixin {
    @Inject(method="getModel",at=@At("RETURN"),cancellable=true)
    private void popocraft$pestilenceModel(ItemStack stack,World world,LivingEntity entity,int seed,CallbackInfoReturnable<BakedModel> cir) {
        if(stack.isOf(Items.POTION) && stack.getOrDefault(DataComponentTypes.POTION_CONTENTS,PotionContentsComponent.DEFAULT).matches(ModPotions.PESTILENCE)) {
            var manager=MinecraftClient.getInstance().getBakedModelManager();
            var model=manager.getModel(Identifier.of("proyecto_intento","item/pestilence"));
            if(model!=manager.getMissingModel())cir.setReturnValue(model);
        }
    }
}
