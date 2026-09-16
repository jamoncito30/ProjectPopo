import json
from pathlib import Path
R=Path(__file__).resolve().parents[1]/'src/main/resources'
A=R/'assets/proyecto_intento'
def write(path,data):
    path.parent.mkdir(parents=True,exist_ok=True)
    path.write_text(json.dumps(data,indent=2,ensure_ascii=False)+'\n',encoding='utf-8')
def cube(a,b,tex):
    return {'from':a,'to':b,'faces':{f:{'texture':'#'+tex} for f in ['north','south','east','west','up','down']}}
textures={'ceramic':'proyecto_intento:block/toilet_ceramic','water':'minecraft:block/lapis_block','rim':'minecraft:block/quartz_block_top','clog':'proyecto_intento:block/wet_popo','particle':'proyecto_intento:block/toilet_ceramic'}
elements=[cube([4,0,4],[12,2,12],'ceramic'),cube([5,2,5],[11,6,11],'ceramic'),cube([3,5,3],[13,7,13],'ceramic'),cube([2,0,12],[14,15,16],'ceramic'),cube([1,15,11],[15,16,16],'rim'),cube([3,7,2],[5,10,12],'rim'),cube([11,7,2],[13,10,12],'rim'),cube([5,7,2],[11,10,4],'rim'),cube([5,7,10],[11,10,12],'rim'),cube([11,12,11.5],[13,13,12],'rim')]
for clogged in (False,True):
    name='inodoro_clogged' if clogged else 'inodoro'
    write(A/f'models/block/{name}.json',{'parent':'minecraft:block/block','textures':textures,'elements':elements+[cube([5,7,4],[11,7.25,10],'clog' if clogged else 'water')]})
write(A/'blockstates/inodoro.json',{'variants':{f'clogged={str(c).lower()},facing={f}':{'model':'proyecto_intento:block/'+('inodoro_clogged' if c else 'inodoro'),'y':y} for c in (False,True) for f,y in [('north',0),('east',90),('south',180),('west',270)]}})
write(A/'models/item/inodoro.json',{'parent':'proyecto_intento:block/inodoro'})
write(A/'models/block/extractor_estiercol.json',{'parent':'minecraft:block/cube','textures':{'particle':'proyecto_intento:block/extractor_estiercol','north':'proyecto_intento:block/extractor_estiercol','east':'proyecto_intento:block/extractor_estiercol','west':'proyecto_intento:block/extractor_estiercol','south':'proyecto_intento:block/extractor_estiercol','up':'proyecto_intento:block/dry_popo','down':'proyecto_intento:block/extractor_estiercol'}})
write(A/'blockstates/extractor_estiercol.json',{'variants':{'':{'model':'proyecto_intento:block/extractor_estiercol'}}})
write(A/'blockstates/fertilized_farmland.json',{'variants':{f'charges={c},moisture={m}':{'model':'minecraft:block/farmland_moist' if m else 'minecraft:block/farmland'} for c in range(1,7) for m in range(8)}})
for item,parent in [('desatascador','handheld'),('estiercol','generated')]:
    write(A/f'models/item/{item}.json',{'parent':f'minecraft:item/{parent}','textures':{'layer0':f'proyecto_intento:item/{item}'}})
write(A/'models/item/beetle_shell.json',{'parent':'minecraft:block/block','textures':{'shell':'proyecto_intento:entity/dung_beetle','particle':'proyecto_intento:entity/dung_beetle'},'elements':[cube([3,4,3],[13,7,13],'shell'),cube([4,7,4],[12,10,12],'shell'),cube([6,10,5],[10,11,11],'shell')]})
for language in ['es_es','es_cl','en_us']:
    p=A/f'lang/{language}.json'; d=json.loads(p.read_text(encoding='utf-8-sig'))
    es=language!='en_us'
    for key,s,e in [('block.proyecto_intento.inodoro','Inodoro','Toilet'),('item.proyecto_intento.desatascador','Desatascador','Plunger'),('item.proyecto_intento.estiercol','Estiércol','Fertilizer'),('block.proyecto_intento.extractor_estiercol','Extractor de estiércol','Fertilizer Extractor'),('block.proyecto_intento.fertilized_farmland','Tierra de cultivo fertilizada','Fertilized Farmland'),('entity.proyecto_intento.toilet_seat','Asiento de inodoro','Toilet Seat')]: d[key]=s if es else e
    write(p,d)
for block,item in [('inodoro','proyecto_intento:inodoro'),('fertilized_farmland','minecraft:dirt')]:
    write(R/f'data/proyecto_intento/loot_table/blocks/{block}.json',{'type':'minecraft:block','pools':[{'rolls':1,'entries':[{'type':'minecraft:item','name':item}],'conditions':[{'condition':'minecraft:survives_explosion'}]}]})
write(R/'data/proyecto_intento/recipe/inodoro.json',{'type':'minecraft:crafting_shaped','pattern':['Q Q','QBQ',' Q '],'key':{'Q':{'item':'minecraft:quartz'},'B':{'item':'minecraft:bucket'}},'result':{'id':'proyecto_intento:inodoro','count':1}})
write(R/'data/proyecto_intento/recipe/desatascador.json',{'type':'minecraft:crafting_shaped','pattern':[' S ',' S ',' P '],'key':{'S':{'item':'minecraft:stick'},'P':{'item':'minecraft:slime_ball'}},'result':{'id':'proyecto_intento:desatascador','count':1}})
print('Roadmap blockstates, models, translations, loot and recipes generated.')
