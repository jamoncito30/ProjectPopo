import os
import re

def rep(filename, old, new):
    if not os.path.exists(filename): return
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()
    content = content.replace(old, new)
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(content)

base = 'src/main/java/org/examplee/proyecto_intento/'

# BeetleNestBlock
bn_path = base + 'block/BeetleNestBlock.java'
rep(bn_path, 'import net.minecraft.loot.context.LootContextParameterSet;', '')
bn_old = '''    @Override protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return List.of(new ItemStack(ModItems.POPO, state.get(STAGE)));
    }'''
bn_new = '''    @Override public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock()) && !world.isClient) {
            net.minecraft.util.ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.POPO, state.get(STAGE)));
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }'''
rep(bn_path, bn_old, bn_new)

# PopoPileBlock
pp_path = base + 'block/PopoPileBlock.java'
rep(pp_path, 'import net.minecraft.loot.context.LootContextParameterSet;', '')
pp_old = '''    @Override protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return List.of(new ItemStack(ModItems.POPO, state.get(AMOUNT)));
    }'''
pp_new = '''    @Override public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock()) && !world.isClient) {
            net.minecraft.util.ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.POPO, state.get(AMOUNT)));
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }'''
rep(pp_path, pp_old, pp_new)

# ToiletBlock
tb_path = base + 'block/ToiletBlock.java'
rep(tb_path, 'import net.minecraft.state.property.DirectionProperty;', 'import net.minecraft.state.property.EnumProperty;\nimport net.minecraft.util.math.Direction;')
rep(tb_path, 'public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;', 'public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;')
rep(tb_path, 'import net.minecraft.util.ItemActionResult;', 'import net.minecraft.util.ActionResult;')
rep(tb_path, 'ItemActionResult onUseWithItem', 'ActionResult onUseWithItem')
rep(tb_path, 'ItemActionResult.SUCCESS', 'ActionResult.SUCCESS')
rep(tb_path, 'ItemActionResult.PASS', 'ActionResult.PASS')

# PopoItem
po_path = base + 'item/PopoItem.java'
rep(po_path, 'TypedActionResult<ItemStack>', 'ActionResult')
rep(po_path, 'return TypedActionResult.success(stack);', 'return ActionResult.SUCCESS;')
rep(po_path, 'return TypedActionResult.pass(stack);', 'return ActionResult.PASS;')
rep(po_path, 'public TypedActionResult<ItemStack> use', 'public ActionResult use')

# ModArmor
ma_path = base + 'item/ModArmor.java'
rep(ma_path, 'import net.minecraft.item.ArmorMaterial;', 'import net.minecraft.item.equipment.ArmorMaterial;')
rep(ma_path, 'ArmorItem.Type', 'net.minecraft.item.equipment.EquipmentType')

print("Applied 1.21.3 mapping fixes locally!")
