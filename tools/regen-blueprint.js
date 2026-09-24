const fs = require('fs');

function design(stage) {
    let boxes = [];
    function box(x,y,z,X,Y,Z,tex='wet') {
        boxes.push([[x,y,z], [X,Y,Z], tex]);
    }
    box(8,0,0,72,4,80);
    box(0,0,8,8,4,72);
    box(72,0,8,80,4,72);
    box(28,4,0,52,6,12,'dry');
    let tops = [12,32,56,68];
    let top = tops[stage-1];
    
    for (let y = 4; y < top; y += 4) {
        let inset = y < 32 ? 4 : 4 + Math.floor((y-28)/8)*4;
        let lo = inset, hi = 80 - inset;
        let thickness = 8;
        let tex = y % 12 === 0 ? 'dry' : 'wet';
        
        for (let cx of [lo+4, hi-12]) {
            for (let cz of [lo+4, hi-12]) {
                box(cx,y,cz,cx+8,y+4,cz+8,tex);
            }
        }
        
        box(lo,y,lo+8,lo+thickness,y+4,hi-8,tex);
        box(hi-thickness,y,lo+8,hi,y+4,hi-8,tex);
        box(lo+8,y,hi-8,hi-8,y+4,hi,tex);
        
        if (y < 28) {
            let gap = y < 20 ? 12 : 8;
            if (stage < 4) {
                box(lo+8,y,lo,40-gap,y+4,lo+8,tex);
                box(40+gap,y,lo,hi-8,y+4,lo+8,tex);
            } else {
                box(lo+8,y,lo,hi-8,y+4,lo+8,tex);
            }
        } else if (stage !== 3 || y < 40) {
            box(lo+8,y,lo,hi-8,y+4,lo+8,tex);
        }
    }
    
    if (stage >= 2) {
        box(24,6,0,28,24,12,'dry');
        box(52,6,0,56,24,12,'dry');
        
        if (stage < 4) {
            box(28,24,0,32,28,12,'dry');
            box(48,24,0,52,28,12,'dry');
            box(32,28,0,48,32,12,'dry');
        } else {
            box(28,24,0,52,28,12,'dry');
        }
        
        for (let pair of [[4,16],[68,16],[4,52],[68,52]]) {
            box(pair[0],4,pair[1],pair[0]+8,20,pair[1]+12,'dry');
        }
    }
    
    if (stage === 4) {
        box(16,4,12,64,32,68,'wet');
        box(20,32,16,60,48,64,'dry');
        box(24,48,20,56,60,60,'wet');
        
        box(28,6,4,52,24,12,'entrance');
        box(24,64,24,56,68,56,'dry');
        box(28,68,28,52,72,52,'wet'); // note: implicitly wet in original if 6th param missing, default was wet
        
        box(32,72,32,36,78,48,'dry'); 
        box(44,72,32,48,78,48,'dry');
        box(36,72,32,44,78,36,'dry'); 
        box(36,72,44,44,78,48,'dry');
        box(30,78,30,50,80,50,'wet');
        box(36,72,36,44,78,44,'dry');
    }
    
    return boxes;
}

let stages = [];
for (let stage = 1; stage <= 4; stage++) {
    let tiles = Array.from({length: 125}, () => []);
    let boxes = design(stage);
    
    for (let box of boxes) {
        let low = box[0], high = box[1], texture = box[2];
        for (let y = 0; y < 5; y++) {
            for (let z = 0; z < 5; z++) {
                for (let x = 0; x < 5; x++) {
                    let origin = [x*16, y*16, z*16];
                    let a = [Math.max(low[0], origin[0]) - origin[0], Math.max(low[1], origin[1]) - origin[1], Math.max(low[2], origin[2]) - origin[2]];
                    let b = [Math.min(high[0], origin[0]+16) - origin[0], Math.min(high[1], origin[1]+16) - origin[1], Math.min(high[2], origin[2]+16) - origin[2]];
                    
                    if (a[0] < b[0] && a[1] < b[1] && a[2] < b[2]) {
                        tiles[x + 5*z + 25*y].push([a, b, texture]);
                    }
                }
            }
        }
    }
    
    let entries = [];
    for (let piece = 0; piece < tiles.length; piece++) {
        if (tiles[piece].length > 0) {
            entries.push({'pos': [piece%5, Math.floor(piece/25), Math.floor(piece/5)%5], 'piece': piece});
        }
    }
    stages.push({'stage': stage, 'pieces': entries});
}

let out = {
    'format': 1, 'size': [5,5,5], 'anchor': 'north-west bottom corner', 'entrance': 'north (-Z)',
    'controller': [2,0,2], 'controller_block': 'proyecto_intento:grand_beetle_nest_controller',
    'block': 'proyecto_intento:grand_beetle_nest_piece',
    'piece_formula': 'x + 5*z + 25*y', 'functional': false, 'stages': stages
};

fs.writeFileSync('src/main/resources/data/proyecto_intento/grand_nest/blueprint.json', JSON.stringify(out, null, 2));
console.log('Blueprint regenerated.');