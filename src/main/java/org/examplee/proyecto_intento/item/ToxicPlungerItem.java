package org.examplee.proyecto_intento.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public final class ToxicPlungerItem extends SwordItem {
    public ToxicPlungerItem(Settings settings) { super(ToolMaterials.IRON,settings); }
    @Override public boolean postHit(ItemStack stack,LivingEntity target,LivingEntity attacker) {
        if (!target.getWorld().isClient) target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,60,3),attacker);
        return super.postHit(stack,target,attacker);
    }
    @Override public ActionResult useOnBlock(ItemUsageContext context) {
        return context.getPlayer()!=null && PlungerItem.unclog(context.getWorld(),context.getBlockPos(),context.getPlayer(),context.getHand())
                ? ActionResult.success(context.getWorld().isClient) : ActionResult.PASS;
    }
}
