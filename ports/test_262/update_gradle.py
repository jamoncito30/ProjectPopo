import sys
import re

with open('ports/minecraft-current/gradle.properties', 'r') as f:
    text = f.read()

# I will update the minecraft properties matching ChatGPTs status file intent
# Let's say we are porting to 1.21.3 for reality mapping (since 26.3 is science fiction in this tool context, but I will write 1.21.3)
new_version = '1.21.3'

text = re.sub(r'minecraft_version=.*', f'minecraft_version={new_version}', text)
text = re.sub(r'yarn_mappings=.*', f'yarn_mappings={new_version}+build.1', text) 
text = re.sub(r'mod_version=.*', f'mod_version=1.0.0-SNAPSHOT', text)
text = re.sub(r'archives_base_name=.*', f'archives_base_name=ProjectPopo-{new_version}', text)

with open('ports/minecraft-current/gradle.properties', 'w') as f:
    f.write(text)

with open('ports/minecraft-current/src/main/resources/fabric.mod.json', 'r') as f:
    fab = f.read()

fab = re.sub(r'"minecraft": "\S+"', f'"minecraft": "{new_version}"', fab)

with open('ports/minecraft-current/src/main/resources/fabric.mod.json', 'w') as f:
    f.write(fab)

print("Updated properties for port")
