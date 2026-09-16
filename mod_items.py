import sys

with open('src/main/java/org/examplee/proyecto_intento/item/ModItems.java', 'r') as f:
    text = f.read()

old_register = '''    public static void registerModItems() {'''
new_register = '''    public static final net.minecraft.item.Item BEETLE_SHELL = registerItem("beetle_shell", new net.minecraft.item.Item(new net.minecraft.item.Item.Settings()));
    public static final net.minecraft.item.Item ESTIERCOL = registerItem("estiercol", new net.minecraft.item.Item(new net.minecraft.item.Item.Settings()));
    public static final net.minecraft.item.Item DESATASCADOR = registerItem("desatascador", new net.minecraft.item.SwordItem(net.minecraft.item.ToolMaterials.WOOD, new net.minecraft.item.Item.Settings().attributeModifiers(net.minecraft.item.SwordItem.createAttributeModifiers(net.minecraft.item.ToolMaterials.WOOD, 2, -2.4f))));

    public static void registerModItems() {'''

if "BEETLE_SHELL" not in text:
    text = text.replace(old_register, new_register)
    with open('src/main/java/org/examplee/proyecto_intento/item/ModItems.java', 'w') as f:
        f.write(text)
    print("Registered new items")
