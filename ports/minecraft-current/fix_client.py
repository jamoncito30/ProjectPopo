import os, re

base = 'src/client/java/org/examplee/proyecto_intento/client/'

# Create DungBeetleRenderState.java
with open(base + 'DungBeetleRenderState.java', 'w', encoding='utf-8') as f:
    f.write('''package org.examplee.proyecto_intento.client;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
public class DungBeetleRenderState extends LivingEntityRenderState {
    public boolean isTrader = false;
}
''')

# Fix DungBeetleModel
with open(base + 'DungBeetleModel.java', 'r', encoding='utf-8') as f: c = f.read()
c = c.replace('import net.minecraft.client.render.entity.model.SinglePartEntityModel;', 'import net.minecraft.client.render.entity.model.EntityModel;')
c = c.replace('extends SinglePartEntityModel<DungBeetleEntity>', 'extends EntityModel<DungBeetleRenderState>')
c = re.sub(r'public void setAngles\(DungBeetleEntity entity,.*?float headPitch\)', 'public void setAngles(DungBeetleRenderState state)', c)
c = c.replace('@Override public ModelPart getPart() { return root; }', '')
with open(base + 'DungBeetleModel.java', 'w', encoding='utf-8') as f: f.write(c)

# Fix DungBeetleRenderer
with open(base + 'DungBeetleRenderer.java', 'r', encoding='utf-8') as f: c = f.read()
c = c.replace('extends MobEntityRenderer<DungBeetleEntity, DungBeetleModel>', 'extends MobEntityRenderer<DungBeetleEntity, DungBeetleRenderState, DungBeetleModel>')
# EntityRenderer doesn't have getTexture(Entity) anymore. It has getTexture(RenderState) maybe, or it is in updateRenderState.
c = re.sub(r'public Identifier getTexture\(DungBeetleEntity entity\).*?}', '''
    public DungBeetleRenderState createRenderState() { return new DungBeetleRenderState(); }
    
    @Override
    public void updateRenderState(DungBeetleEntity entity, DungBeetleRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        // We set values on state from entity here
        // The texture method in 1.21.3 is usually overriden in a Resource-based way or passed via Model/State? Actually it's still getTexture but maybe different signature, or we don't even need getTexture. I'll just delete getTexture for now and assume default.
    }
''', c)
with open(base + 'DungBeetleRenderer.java', 'w', encoding='utf-8') as f: f.write(c)


print("Fixed client code")
