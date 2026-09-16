package org.examplee.proyecto_intento.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.examplee.proyecto_intento.block.ToiletBlock;

public class PlungerItem extends Item {
    public PlungerItem(Settings settings) { super(settings); }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext c) {
        if (c.getWorld().getBlockState(c.getBlockPos()).isOf(org.examplee.proyecto_intento.block.ModBlocks.INODORO)) {
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(c);
    }
}
