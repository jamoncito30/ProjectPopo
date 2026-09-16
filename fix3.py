import sys

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r') as f:
    text = f.read()

# Fix the dummy false replacement syntax error
text = text.replace('false = nbt.getBoolean("HasDroppedShell");', '')

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w') as f:
    f.write(text)

