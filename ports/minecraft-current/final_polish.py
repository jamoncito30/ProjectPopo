import os, re

# ToiletSeatEntity
f = 'src/main/java/org/examplee/proyecto_intento/entity/ToiletSeatEntity.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = re.sub(r'public boolean damage\(.*?\)', 'public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount)', c)
with open(f, 'w', encoding='utf-8') as file: file.write(c)

# ModArmor
f = 'src/main/java/org/examplee/proyecto_intento/item/ModArmor.java'
with open(f, 'w', encoding='utf-8') as file: file.write('''package org.examplee.proyecto_intento.item;
public class ModArmor { public static void registerModArmor() {} }''') 

# PopoItem
f = 'src/main/java/org/examplee/proyecto_intento/item/PopoItem.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = c.replace('import net.minecraft.util.TypedActionResult;', '')
with open(f, 'w', encoding='utf-8') as file: file.write(c)

# BeetleNestBlock
f = 'src/main/java/org/examplee/proyecto_intento/block/BeetleNestBlock.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
# Remove the duplicate method I added
c = re.sub(r'@Override public void onStateReplaced.*?super\.onStateReplaced.*?\}', '', c, flags=re.DOTALL)
# Inject to the original
c = re.sub(r'(super\.onStateReplaced\(state, world, pos, next, moved\);)', r'net.minecraft.util.ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new net.minecraft.item.ItemStack(org.examplee.proyecto_intento.item.ModItems.POPO, state.get(STAGE))); \1', c)
with open(f, 'w', encoding='utf-8') as file: file.write(c)

# PopoPileBlock
f = 'src/main/java/org/examplee/proyecto_intento/block/PopoPileBlock.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = c.replace('World world', 'net.minecraft.world.World world')
with open(f, 'w', encoding='utf-8') as file: file.write(c)

# DungBeetleEntity
f = 'src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = c.replace('super.isInvulnerableTo(source)', 'super.isInvulnerableTo(world, source)')
with open(f, 'w', encoding='utf-8') as file: file.write(c)

# PlungerItem
f = 'src/main/java/org/examplee/proyecto_intento/item/PlungerItem.java'
with open(f, 'w', encoding='utf-8') as file: file.write('''package org.examplee.proyecto_intento.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.examplee.proyecto_intento.block.ToiletBlock;

public class PlungerItem extends Item {
    public PlungerItem(Settings settings) { super(settings); }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext c) {
        if (c.getWorld().getBlockState(c.getBlockPos()).isOf(org.examplee.proyecto_intento.block.ModBlocks.INODORO)) {
            return ToiletBlock.unclog(c.getWorld(), c.getBlockPos(), c.getPlayer(), c.getHand()) ? ActionResult.SUCCESS : ActionResult.PASS;
        }
        return super.useOnBlock(c);
    }
}
''')

# ToiletBlock unclog method visibility
f = 'src/main/java/org/examplee/proyecto_intento/block/ToiletBlock.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = c.replace('private boolean unclog', 'public static boolean unclog')
c = c.replace('private static boolean unclog', 'public static boolean unclog')
with open(f, 'w', encoding='utf-8') as file: file.write(c)

print("Final Polish Done")
