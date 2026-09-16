import os, re
f = 'src/main/java/org/examplee/proyecto_intento/item/PlungerItem.java'
with open(f, 'r', encoding='utf-8') as file: c = file.read()
c = re.sub(r'return true.*?PASS;', 'return ActionResult.SUCCESS;', c)
with open(f, 'w', encoding='utf-8') as file: file.write(c)

print("Fixed syntax")
