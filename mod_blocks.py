import sys

with open('src/main/java/org/examplee/proyecto_intento/block/ModBlocks.java', 'r') as f:
    text = f.read()

old_register = '''    public static void initialize() {'''
new_register = '''    public static final net.minecraft.block.Block INODORO = registerBlock("inodoro", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.copy(net.minecraft.block.Blocks.QUARTZ_BLOCK).nonOpaque()), true);
    public static final net.minecraft.block.Block EXTRACTOR_ESTIERCOL = registerBlock("extractor_estiercol", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.create().strength(2.0f)), true);

    public static void initialize() {'''

if "INODORO" not in text:
    text = text.replace(old_register, new_register)
    with open('src/main/java/org/examplee/proyecto_intento/block/ModBlocks.java', 'w') as f:
        f.write(text)
    print("Registered new blocks")
