import sys
with open('src/main/java/org/examplee/proyecto_intento/entity/ModSounds.java', 'r') as f:
    content = f.read()

content = content.replace('public static final SoundEvent BEETLE_ROLL = register(\"beetle_roll\");', 'public static final SoundEvent BEETLE_ROLL = register(\"beetle_roll\");\n    public static final SoundEvent GRAND_NEST_START = register(\"grand_nest_start\");')

with open('src/main/java/org/examplee/proyecto_intento/entity/ModSounds.java', 'w') as f:
    f.write(content)
