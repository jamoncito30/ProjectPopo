package org.examplee.proyecto_intento.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.PopoDrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    @Unique private int popocraft$dropTicks = -1;

    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> type, World world) { super(type, world); }

    @Unique
    private int popocraft$nextInterval() { return 6000 + random.nextInt(6001); }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void popocraft$tick(CallbackInfo ci) {
        if (getWorld().isClient || !isAlive() || isBaby() || isAiDisabled()) return;
        if (popocraft$dropTicks < 0) popocraft$dropTicks = popocraft$nextInterval();
        if (--popocraft$dropTicks <= 0) {
            PopoDrops.drop((AnimalEntity) (Object) this);
            popocraft$dropTicks = popocraft$nextInterval();
        }
    }

    // Runs only when vanilla accepts food, including creative feeding, never merely on right click.
    @Inject(method = "eat", at = @At("TAIL"))
    private void popocraft$fed(PlayerEntity player, Hand hand, ItemStack stack, CallbackInfo ci) {
        PopoDrops.onFed((AnimalEntity) (Object) this);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void popocraft$save(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("PopoCraftDropTicks", popocraft$dropTicks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void popocraft$load(NbtCompound nbt, CallbackInfo ci) {
        popocraft$dropTicks = nbt.contains("PopoCraftDropTicks")
                ? Math.clamp(nbt.getInt("PopoCraftDropTicks"), -1, 12000) : -1;
    }
}
