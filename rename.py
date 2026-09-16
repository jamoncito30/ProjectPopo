import sys

with open('gradle.properties', 'r') as f:
    text = f.read()

text = text.replace('archives_base_name=proyecto_intento', 'archives_base_name=ProjectPopo')
text = text.replace('mod_version=1.0-SNAPSHOT', 'mod_version=1.0.0')

with open('gradle.properties', 'w') as f:
    f.write(text)

with open('src/main/resources/fabric.mod.json', 'r') as f:
    fab = f.read()

fab = fab.replace('"name": "proyecto_intento"', '"name": "ProjectPopo"')
fab = fab.replace('"description": ""', '"description": "A unique ecosystem mod featuring Dung Beetles that build massive colonial nests out of poop."')

with open('src/main/resources/fabric.mod.json', 'w') as f:
    f.write(fab)

print("Updated project names successfully")
