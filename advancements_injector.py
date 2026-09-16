import sys

# 1. DungBeetleEntity.java mods
with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r') as f:
    beetle = f.read()

beetle = beetle.replace('tryInitiateGrandNest();', 'org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), getBlockPos(), "find_beetle"); tryInitiateGrandNest();')

old_init = '''        if (builtNests >= 5) {
            // Find 5x5 flat spot'''
new_init = '''        if (builtNests >= 4) org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), getBlockPos(), "small_town");
        if (builtNests >= 5) {
            // Find 5x5 flat spot'''
beetle = beetle.replace(old_init, new_init)

old_start = '''                    if (getWorld().getBlockEntity(controllerPos) instanceof GrandBeetleNestBlockEntity grandNest) {
                        grandNest.setOrigin(p);
                        grandNest.updateStructure(1);
                        playSound(ModSounds.BEETLE_CHIRP, 1.0F, 1.0F); // some signal
                    }'''
new_start = '''                    if (getWorld().getBlockEntity(controllerPos) instanceof GrandBeetleNestBlockEntity grandNest) {
                        grandNest.setOrigin(p);
                        grandNest.updateStructure(1);
                        playSound(ModSounds.BEETLE_CHIRP, 1.0F, 1.0F); // some signal
                        org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), controllerPos, "start_grand_nest");
                    }'''
beetle = beetle.replace(old_start, new_start)

old_place = '''                        placed = getWorld().setBlockState(deposit, ModBlocks.BEETLE_NEST.getDefaultState());
                    }
                        
                    if (placed) {'''
new_place = '''                        placed = getWorld().setBlockState(deposit, ModBlocks.BEETLE_NEST.getDefaultState());
                        if (placed) org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby((ServerWorld)getWorld(), deposit, "first_small_nest");
                    }
                        
                    if (placed) {'''
beetle = beetle.replace(old_place, new_place)

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w') as f:
    f.write(beetle)


# 2. BeetleNestBlockEntity.java -> breed_beetle
with open('src/main/java/org/examplee/proyecto_intento/block/BeetleNestBlockEntity.java', 'r') as f:
    nest = f.read()

old_breed = '''                    if (baby != null) {
                        baby.setBreedingAge(-24000);'''
new_breed = '''                    if (baby != null) {
                        org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby(server, pos, "breed_beetle");
                        baby.setBreedingAge(-24000);'''
nest = nest.replace(old_breed, new_breed)

with open('src/main/java/org/examplee/proyecto_intento/block/BeetleNestBlockEntity.java', 'w') as f:
    f.write(nest)


# 3. GrandBeetleNestBlockEntity.java -> finish_grand_nest
with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'r') as f:
    grand_nest = f.read()

old_finish = '''        if (targetStage > currentStage) {
            world.setBlockState(pos, getCachedState().with(GrandBeetleNestPieceBlock.STAGE, targetStage), Block.NOTIFY_ALL);
            updateStructure(targetStage);
        }'''
new_finish = '''        if (targetStage > currentStage) {
            world.setBlockState(pos, getCachedState().with(GrandBeetleNestPieceBlock.STAGE, targetStage), Block.NOTIFY_ALL);
            updateStructure(targetStage);
            if (targetStage == 4 && world instanceof ServerWorld sw) {
                org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby(sw, pos, "finish_grand_nest");
            }
        }'''
grand_nest = grand_nest.replace(old_finish, new_finish)

with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'w') as f:
    f.write(grand_nest)

print("Modifications applied.")