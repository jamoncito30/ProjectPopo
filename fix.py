import sys

with open('src/main/java/org/examplee/proyecto_intento/block/ModBlocks.java', 'r') as f:
    text = f.read()

old_bad = '''    public static final net.minecraft.block.Block INODORO = registerBlock("inodoro", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.copy(net.minecraft.block.Blocks.QUARTZ_BLOCK).nonOpaque()), true);
    public static final net.minecraft.block.Block EXTRACTOR_ESTIERCOL = registerBlock("extractor_estiercol", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.create().strength(2.0f)), true);
'''
new_good = '''    public static final net.minecraft.block.Block INODORO = register("inodoro", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.copy(net.minecraft.block.Blocks.QUARTZ_BLOCK).nonOpaque()));
    public static final net.minecraft.block.Block EXTRACTOR_ESTIERCOL = register("extractor_estiercol", new net.minecraft.block.Block(net.minecraft.block.AbstractBlock.Settings.create().strength(2.0f)));
'''
text = text.replace(old_bad, new_good)

with open('src/main/java/org/examplee/proyecto_intento/block/ModBlocks.java', 'w') as f:
    f.write(text)

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r') as f:
    text = f.read()

text = text.replace("nbt.putBoolean(\"IsTrader\", this.dataTracker.get(IS_TRADER));", "nbt.putBoolean(\"IsTrader\", false);")
text = text.replace("this.dataTracker.set(IS_TRADER, nbt.getBoolean(\"IsTrader\"));", "")

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w') as f:
    f.write(text)
    
print("Fixed errors!")

