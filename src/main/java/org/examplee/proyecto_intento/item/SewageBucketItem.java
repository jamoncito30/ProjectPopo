package org.examplee.proyecto_intento.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;

public final class SewageBucketItem extends BlockItem {
    public SewageBucketItem(Block block,Settings settings) { super(block,settings); }
    @Override public String getTranslationKey() { return "item.proyecto_intento.sewage_bucket"; }
    @Override public ActionResult place(ItemPlacementContext context) {
        var result=super.place(context);
        if(result.isAccepted() && !context.getWorld().isClient && context.getPlayer()!=null && !context.getPlayer().isCreative()) {
            context.getPlayer().setStackInHand(context.getHand(),new ItemStack(Items.BUCKET));
        }
        return result;
    }
}
