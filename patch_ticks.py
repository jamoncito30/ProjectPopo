import sys

with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'r') as f:
    text = f.read()

old_tick = '''        if (!nest.isComplete()) return;
        ServerWorld server = (ServerWorld) world;
        
        for (NbtCompound occupant : nest.occupants) {
            occupant.putInt("TicksInside", Math.min(24000, occupant.getInt("TicksInside") + 1));
            NbtCompound data = occupant.getCompound("Entity");
            int age = data.getInt("Age");
            data.putInt("Age", age < 0 ? age + 1 : Math.max(0, age - 1));
        }

        if (world.getTime() % 20 == 0) {
            List<NbtCompound> parents = nest.occupants.stream().filter(n -> n.getInt("TicksInside") >= 200
                    && n.getCompound("Entity").getInt("Age") == 0
                    && !n.getCompound("Entity").getBoolean("NurseryResident")).limit(2).toList();
            
            if (nest.getFoodCount() > 0 && parents.size() == 2 && nest.occupants.size() < POPULATION_CAPACITY && nest.localPopulation() < 30) {
                // Consume 1 food from the store
                if (nest.consumeFood(1)) {
                    DungBeetleEntity baby = ModEntities.DUNG_BEETLE.create(server);
                    if (baby != null) {
                        baby.setBreedingAge(-24000);
                        baby.setNest(pos);
                        NbtCompound data = new NbtCompound();
                        baby.saveNbt(data);
                        NbtCompound resident = new NbtCompound();
                        resident.put("Entity", data);
                        nest.occupants.add(resident);
                        for (NbtCompound parent : parents) parent.getCompound("Entity").putInt("Age", 6000);
                        server.spawnParticles(ParticleTypes.HEART, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, 4, .2, .15, .2, 0);
                    }
                }
            }

            for (int i = nest.occupants.size() - 1; i >= 0; i--) {
                NbtCompound resident = nest.occupants.get(i);
                int age = resident.getCompound("Entity").getInt("Age");
                int stay = age < 0 ? 600 : age == 0 && nest.getFoodCount() > 0 ? 1200 : 400;
                if (resident.getInt("TicksInside") >= stay && nest.release(server, resident, false)) nest.occupants.remove(i);
            }
            
            // Re-founding small nests if full'''

new_tick = '''        ServerWorld server = (ServerWorld) world;
        boolean complete = nest.isComplete();
        
        for (NbtCompound occupant : nest.occupants) {
            occupant.putInt("TicksInside", Math.min(24000, occupant.getInt("TicksInside") + 1));
            NbtCompound data = occupant.getCompound("Entity");
            int age = data.getInt("Age");
            data.putInt("Age", age < 0 ? age + 1 : Math.max(0, age - 1));
        }

        if (world.getTime() % 20 == 0) {
            if (complete) {
                List<NbtCompound> parents = nest.occupants.stream().filter(n -> n.getInt("TicksInside") >= 200
                        && n.getCompound("Entity").getInt("Age") == 0
                        && !n.getCompound("Entity").getBoolean("NurseryResident")).limit(2).toList();
                
                if (nest.getFoodCount() > 0 && parents.size() == 2 && nest.occupants.size() < POPULATION_CAPACITY && nest.localPopulation() < 30) {
                    if (nest.consumeFood(1)) {
                        DungBeetleEntity baby = ModEntities.DUNG_BEETLE.create(server);
                        if (baby != null) {
                            baby.setBreedingAge(-24000);
                            baby.setNest(pos);
                            NbtCompound data = new NbtCompound();
                            baby.saveNbt(data);
                            NbtCompound resident = new NbtCompound();
                            resident.put("Entity", data);
                            nest.occupants.add(resident);
                            for (NbtCompound parent : parents) parent.getCompound("Entity").putInt("Age", 6000);
                            server.spawnParticles(ParticleTypes.HEART, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, 4, .2, .15, .2, 0);
                        }
                    }
                }
            }

            for (int i = nest.occupants.size() - 1; i >= 0; i--) {
                NbtCompound resident = nest.occupants.get(i);
                int age = resident.getCompound("Entity").getInt("Age");
                // If not complete, eject quickly after 100 ticks (5 seconds) to keep collecting!
                int stay = !complete ? 100 : (age < 0 ? 600 : age == 0 && nest.getFoodCount() > 0 ? 1200 : 400);
                if (resident.getInt("TicksInside") >= stay && nest.release(server, resident, false)) nest.occupants.remove(i);
            }
            
            // Re-founding small nests if full'''

text = text.replace(old_tick, new_tick)
with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'w') as f:
    f.write(text)

# Also add the check to Refounding small nests
old_refound = '''            if (nest.occupants.size() >= POPULATION_CAPACITY && nest.getFoodCount() >= 10 && world.getTime() % 100 == 0) {'''
new_refound = '''            if (complete && nest.occupants.size() >= POPULATION_CAPACITY && nest.getFoodCount() >= 10 && world.getTime() % 100 == 0) {'''
text = text.replace(old_refound, new_refound)

# Also add particles in deposit()
old_deposit = '''                amountInvested += amount;
                offered.decrement(amount);
                transferred += amount;
                playSoundOnce(world, pos);
                checkConstructionStage();
            }'''
new_deposit = '''                amountInvested += amount;
                offered.decrement(amount);
                transferred += amount;
                
                // SPpawn dirt particles simulating construction!
                if (world instanceof ServerWorld sw) {
                    sw.spawnParticles(new net.minecraft.particle.BlockStateParticleEffect(net.minecraft.particle.ParticleTypes.BLOCK, Blocks.GRAVEL.getDefaultState()), pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 30, 1.5, 1.0, 1.5, 0.15);
                    sw.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_GRAVEL_BREAK, net.minecraft.sound.SoundCategory.BLOCKS, 0.7f, 1.2f);
                }
                
                playSoundOnce(world, pos);
                checkConstructionStage();
            }'''
text = text.replace(old_deposit, new_deposit)

with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'w') as f:
    f.write(text)

print('Modifications applied')
