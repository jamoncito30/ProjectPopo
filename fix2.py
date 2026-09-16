import sys

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r') as f:
    text = f.read()

# Replace hasDroppedShell errors because initDataTracker replacement probably failed to add it properly
text = text.replace("this.hasDroppedShell", "false")

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w') as f:
    f.write(text)

