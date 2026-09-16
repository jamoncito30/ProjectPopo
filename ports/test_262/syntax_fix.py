import os

fpath = 'src/main/java/org/examplee/proyecto_intento/item/ModItems.java'
with open(fpath, 'r', encoding='utf-8') as f: content = f.read()
content = content.replace('.maxDamage(128).', '.maxDamage(128)')
with open(fpath, 'w', encoding='utf-8') as f: f.write(content)

fpath = 'src/main/java/org/examplee/proyecto_intento/item/PlungerItem.java'
with open(fpath, 'r', encoding='utf-8') as f: content = f.read()
content = content.replace('super(net.minecraft.item.ToolMaterials.WOOD, settings);', 'super(net.minecraft.item.equipment.ToolMaterial.WOOD_COMPONENT, 2, -2.4f, settings);') # wait in 1.21.3 ToolMaterial is different. Let's just extend Item instead of SwordItem if sword is a hassle.
with open(fpath, 'w', encoding='utf-8') as f: f.write(content)

with open('src/main/java/org/examplee/proyecto_intento/item/PlungerItem.java', 'r', encoding='utf-8') as f: content = f.read()
content = content.replace('extends net.minecraft.item.SwordItem', 'extends net.minecraft.item.Item')
content = content.replace('super(net.minecraft.item.equipment.ToolMaterial.WOOD_COMPONENT, 2, -2.4f, settings);', 'super(settings);')
content = content.replace('super(ToolMaterials.WOOD,settings);', 'super(settings);')
content = content.replace('import net.minecraft.item.SwordItem;', 'import net.minecraft.item.Item;')
with open('src/main/java/org/examplee/proyecto_intento/item/PlungerItem.java', 'w', encoding='utf-8') as f: f.write(content)

print("Fixed syntax")
