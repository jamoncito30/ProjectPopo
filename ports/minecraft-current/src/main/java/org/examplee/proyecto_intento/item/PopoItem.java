package org.examplee.proyecto_intento.item;
import net.minecraft.util.ActionResult;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;

import net.minecraft.world.World;
import org.examplee.proyecto_intento.entity.PopoProjectileEntity;

public final class PopoItem extends Item {
    public PopoItem(Settings settings) { super(settings); }
    @Override public ActionResult use(World world, PlayerEntity player, Hand hand) { ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            PopoProjectileEntity projectile = new PopoProjectileEntity(world, player);
            projectile.setItem(stack.copyWithCount(1));
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0, 1.3F, 1);
            world.spawnEntity(projectile);
        }
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.PLAYERS, 0.5F, 0.8F);
        player.getItemCooldownManager().set(new ItemStack(this), 12);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        stack.decrementUnlessCreative(1, player);
        return ActionResult.SUCCESS;
    }
}
