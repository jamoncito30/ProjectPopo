"""Native modular models; no raster editing. Coordinates are sixteenths of a block."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / 'src/main/resources'
ASSETS = RES / 'assets/proyecto_intento'

def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + '\n', encoding='utf-8')

def design(stage):
    boxes = []
    def box(x,y,z,X,Y,Z,tex='wet'):
        boxes.append(([x,y,z], [X,Y,Z], tex))
    # Chamfered footprint and raised dry threshold. Central tile is future controller.
    box(8,0,0,72,4,80)
    box(0,0,8,8,4,72)
    box(72,0,8,80,4,72)
    box(28,4,0,52,6,12,'dry')
    # Octagonal earthen courses, hand-built staggered buttresses and open entrance.
    top = [12,32,56,68][stage-1]
    for y in range(4, top, 4):
        inset = 4 if y < 32 else 4 + ((y-28)//8)*4
        lo, hi = inset, 80-inset
        thickness = 8
        tex = 'dry' if y % 12 == 0 else 'wet'
        for cx in (lo+4, hi-12):
            for cz in (lo+4, hi-12):
                box(cx,y,cz,cx+8,y+4,cz+8,tex)
        # Rounded corners: front/back run narrower than the side walls.
        box(lo,y,lo+8,lo+thickness,y+4,hi-8,tex)
        box(hi-thickness,y,lo+8,hi,y+4,hi-8,tex)
        box(lo+8,y,hi-8,hi-8,y+4,hi,tex)
        if y < 28:
            gap = 12 if y < 20 else 8
            box(lo+8,y,lo,40-gap,y+4,lo+8,tex)
            box(40+gap,y,lo,hi-8,y+4,lo+8,tex)
        elif stage != 3 or y < 40:
            box(lo+8,y,lo,hi-8,y+4,lo+8,tex)
    if stage >= 2:
        # Projecting arch: beetle-sized entrance, visible from the northern face.
        box(24,6,0,28,24,12,'dry'); box(52,6,0,56,24,12,'dry')
        box(28,24,0,32,28,12,'dry'); box(48,24,0,52,28,12,'dry')
        box(32,28,0,48,32,12,'dry')
        for x,z in [(4,16),(68,16),(4,52),(68,52)]:
            box(x,4,z,x+8,20,z+12,'dry')
    if stage == 4:
        # Closed exterior: entry is a symbolic dark inset, not an open corridor.
        box(28,6,4,52,24,12,'entrance')
        box(32,24,4,48,28,12,'entrance')
        box(24,64,24,56,68,56,'dry')
        box(28,68,28,52,72,52)
        # Crown ventilation opening, deliberately hollow.
        box(32,72,32,36,78,48,'dry'); box(44,72,32,48,78,48,'dry')
        box(36,72,32,44,78,36,'dry'); box(36,72,44,44,78,48,'dry')
        box(30,78,30,50,80,50)
        box(36,72,36,44,78,44,'dry')
    return boxes

variants, geometry, stages = {}, [], []
for stage in range(1,5):
    tiles = [[] for _ in range(125)]
    for low, high, texture in design(stage):
        assert all(0 <= low[a] < high[a] <= 80 for a in range(3))
        for y in range(5):
            for z in range(5):
                for x in range(5):
                    origin = [x*16,y*16,z*16]
                    a = [max(low[i],origin[i])-origin[i] for i in range(3)]
                    b = [min(high[i],origin[i]+16)-origin[i] for i in range(3)]
                    if all(a[i] < b[i] for i in range(3)):
                        tiles[x+5*z+25*y].append((a,b,texture))
    entries = []
    for piece, boxes in enumerate(tiles):
        name = f'grand_nest/stage_{stage}/piece_{piece}'
        elements = []
        for a,b,texture in boxes:
            elements.append({'from':a,'to':b,'faces':{face:{'texture':'#'+texture} for face in ['north','south','east','west','up','down']}})
        write(ASSETS / f'models/block/{name}.json', {'ambientocclusion':True,'textures':{'wet':'proyecto_intento:block/wet_popo','dry':'proyecto_intento:block/dry_popo','entrance':'minecraft:block/black_concrete','particle':'proyecto_intento:block/dry_popo'},'elements':elements})
        variants[f'piece={piece},stage={stage}'] = {'model':'proyecto_intento:block/'+name}
        geometry.append([a+b for a,b,_ in boxes])
        if boxes:
            entries.append({'pos':[piece%5,piece//25,(piece//5)%5], 'piece':piece})
    stages.append({'stage':stage,'pieces':entries})
write(ASSETS / 'blockstates/grand_beetle_nest_piece.json', {'variants':variants})
write(ASSETS / 'blockstates/grand_beetle_nest_controller.json', {'variants':{
    f'stage={s}':{'model':f'proyecto_intento:block/grand_nest/stage_{s}/piece_12'} for s in range(1,5)}})
write(ASSETS / 'grand_nest_shapes.json', geometry)
write(RES / 'data/proyecto_intento/grand_nest/blueprint.json', {
    'format':1,'size':[5,5,5],'anchor':'north-west bottom corner','entrance':'north (-Z)',
    'controller':[2,0,2],'controller_block':'proyecto_intento:grand_beetle_nest_controller',
    'block':'proyecto_intento:grand_beetle_nest_piece',
    'piece_formula':'x + 5*z + 25*y','functional':False,'stages':stages})
print('Generated 500 bounded models, collision shapes and four stage blueprints.')
