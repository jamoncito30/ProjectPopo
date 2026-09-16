import sys

# GrandBeetleNestBlockEntity.java
with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'r') as f:
    content = f.read()

content = content.replace('nbt.putInt(\"AmountInvested\", amountInvested);', 'nbt.putInt(\"AmountInvested\", amountInvested);\n        nbt.putBoolean(\"SoundPlayed\", soundPlayed);')
content = content.replace('amountInvested = nbt.getInt(\"AmountInvested\");', 'amountInvested = nbt.getInt(\"AmountInvested\");\n        soundPlayed = nbt.getBoolean(\"SoundPlayed\");')
content = content.replace('public int getAmountInvested() { return amountInvested; }', 'public int getAmountInvested() { return amountInvested; }\n    public void playSoundOnce(World world, BlockPos pos) {\n        if (!soundPlayed && world != null && !world.isClient) {\n            world.playSound(null, pos, org.examplee.proyecto_intento.entity.ModSounds.GRAND_NEST_START, net.minecraft.sound.SoundCategory.BLOCKS, 1.0F, 1.0F);\n            soundPlayed = true;\n            markDirty();\n        }\n    }')
content = content.replace('checkConstructionStage();', 'playSoundOnce(world, pos);\n                checkConstructionStage();')

with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'w') as f:
    f.write(content)

# sounds.json
import json
with open('src/main/resources/assets/proyecto_intento/sounds.json', 'r') as f:
    sounds = json.load(f)

sounds['grand_nest_start'] = {
    'sounds': [
        {
            'name': 'proyecto_intento:grand_nest_start',
            'volume': 1.0,
            'type': 'event'
        }
    ],
    'subtitle': 'subtitles.proyecto_intento.grand_nest_start'
}

with open('src/main/resources/assets/proyecto_intento/sounds.json', 'w') as f:
    json.dump(sounds, f, indent=4)
