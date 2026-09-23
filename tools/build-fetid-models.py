"""Native geometry and resource models; does not generate or edit raster artwork."""
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
A = ROOT / 'src/main/resources/assets/proyecto_intento'

def write(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False)+'\n', encoding='utf-8')

def cube(a, b, tex):
    return {'from': a, 'to': b, 'faces': {f: {'texture': '#'+tex} for f in ('north','south','east','west','up','down')}}

for name in ('viscous_biomass', 'pestilence', 'toxic_plunger'):
    write(A/f'models/item/{name}.json', {'parent': 'minecraft:item/'+('handheld' if name == 'toxic_plunger' else 'generated'), 'textures': {'layer0': f'proyecto_intento:item/{name}'}})
write(A/'models/item/fetid_slime_spawn_egg.json', {'parent': 'minecraft:item/template_spawn_egg'})
write(A/'models/block/sewage.json', {'parent':'minecraft:block/cube_all','textures':{'all':'proyecto_intento:block/sewage'}})
write(A/'blockstates/sewage.json', {'variants':{'':{'model':'proyecto_intento:block/sewage'}}})
# Three-dimensional bucket, with visible sludge inside its metal rim.
write(A/'models/item/sewage_bucket.json', {
    'parent':'minecraft:block/block',
    'textures':{'metal':'minecraft:block/iron_block','sludge':'proyecto_intento:block/sewage','particle':'proyecto_intento:block/sewage'},
    'elements':[cube([4,2,4],[12,3,12],'metal'),cube([3,3,3],[5,11,13],'metal'),cube([11,3,3],[13,11,13],'metal'),cube([5,3,3],[11,11,5],'metal'),cube([5,3,11],[11,11,13],'metal'),cube([5,8,5],[11,9,11],'sludge'),cube([3,11,7],[4,15,9],'metal'),cube([12,11,7],[13,15,9],'metal'),cube([4,14,7],[12,15,9],'metal')],
    'display':{'gui':{'rotation':[25,35,0],'translation':[0,0,0],'scale':[0.9,0.9,0.9]},'thirdperson_righthand':{'rotation':[0,0,0],'translation':[0,1,0],'scale':[0.5,0.5,0.5]},'firstperson_righthand':{'rotation':[0,-35,0],'translation':[0,1,0],'scale':[0.65,0.65,0.65]}}
})
write(A/'models/block/pestilent_torch.json', {'parent':'minecraft:block/cross','textures':{'cross':'proyecto_intento:block/pestilent_torch'}})
write(A/'blockstates/pestilent_torch.json', {'variants':{'':{'model':'proyecto_intento:block/pestilent_torch'}}})
write(A/'models/item/pestilent_torch.json', {'parent':'minecraft:item/generated','textures':{'layer0':'proyecto_intento:block/pestilent_torch'}})
print('Fetid slime expansion native models generated.')
# Explicit untinted faces preserve the generated glass and liquid colors.
write(A/'models/item/pestilence.json', {
    'parent':'minecraft:block/block','gui_light':'front',
    'textures':{'bottle':'proyecto_intento:item/pestilence','particle':'proyecto_intento:item/pestilence'},
    'elements':[{'from':[0,0,7.5],'to':[16,16,8.5],'faces':{f:{'texture':'#bottle','uv':[0,0,16,16]} for f in ('north','south')}}],
    'display':{'gui':{'rotation':[0,0,0],'translation':[0,0,0],'scale':[1,1,1]},'ground':{'rotation':[0,0,0],'translation':[0,2,0],'scale':[.5,.5,.5]},'firstperson_righthand':{'rotation':[0,-90,25],'translation':[1.13,3.2,1.13],'scale':[.68,.68,.68]},'thirdperson_righthand':{'rotation':[0,0,0],'translation':[0,3,1],'scale':[.55,.55,.55]}}
})
R=ROOT/'src/main/resources/data/proyecto_intento'
write(R/'loot_table/entities/fetid_slime.json',{'type':'minecraft:entity','pools':[{'rolls':1,'entries':[{'type':'minecraft:item','name':'proyecto_intento:viscous_biomass','functions':[{'function':'minecraft:set_count','count':{'type':'minecraft:uniform','min':1,'max':3}}]}]}]})
write(R/'loot_table/blocks/pestilent_torch.json',{'type':'minecraft:block','pools':[{'rolls':1,'entries':[{'type':'minecraft:item','name':'proyecto_intento:pestilent_torch'}],'conditions':[{'condition':'minecraft:survives_explosion'}]}]})
for name, ingredients, count in [
    ('toxic_plunger',['proyecto_intento:desatascador','proyecto_intento:viscous_biomass'],1),
    ('sewage_bucket',['minecraft:bucket','proyecto_intento:viscous_biomass','proyecto_intento:popo'],1),
    ('pestilent_torch',['minecraft:torch','proyecto_intento:viscous_biomass'],1)]:
    write(R/f'recipe/{name}.json',{'type':'minecraft:crafting_shapeless','ingredients':[{'item':i} for i in ingredients],'result':{'id':f'proyecto_intento:{name}','count':count}})
translations={
    'entity.proyecto_intento.fetid_slime':('Slime fétido','Fetid Slime'),
    'item.proyecto_intento.fetid_slime_spawn_egg':('Huevo de slime fétido','Fetid Slime Spawn Egg'),
    'item.proyecto_intento.viscous_biomass':('Biomasa viscosa','Viscous Biomass'),
    'item.proyecto_intento.toxic_plunger':('Desatascador tóxico','Toxic Plunger'),
    'item.proyecto_intento.sewage_bucket':('Cubo de aguas residuales','Sewage Bucket'),
    'block.proyecto_intento.sewage':('Aguas residuales','Sewage'),
    'block.proyecto_intento.pestilent_torch':('Antorcha pestilente','Pestilent Torch'),
    'item.minecraft.potion.effect.pestilence':('Poción de pestilencia','Potion of Pestilence'),
    'item.minecraft.splash_potion.effect.pestilence':('Poción arrojadiza de pestilencia','Splash Potion of Pestilence'),
    'item.minecraft.lingering_potion.effect.pestilence':('Poción persistente de pestilencia','Lingering Potion of Pestilence'),
    'item.minecraft.tipped_arrow.effect.pestilence':('Flecha de pestilencia','Arrow of Pestilence')}
for language in ('es_es','es_cl','en_us'):
    p=A/f'lang/{language}.json'
    data=json.loads(p.read_text(encoding='utf-8-sig'))
    data.update({key:pair[language=='en_us'] for key,pair in translations.items()})
    write(p,data)
