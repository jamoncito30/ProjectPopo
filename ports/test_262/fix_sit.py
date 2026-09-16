import os

f = 'src/main/java/org/examplee/proyecto_intento/entity/ToiletSeatEntity.java'
with open(f, 'a', encoding='utf-8') as file: file.write('''
    public static boolean sit(World world, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.player.PlayerEntity player) { 
        if (world.isClient) return true;
        ToiletSeatEntity seat = ModEntities.TOILET_SEAT.create((net.minecraft.server.world.ServerWorld)world, net.minecraft.entity.SpawnReason.NATURAL);
        if (seat != null) {
            seat.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            world.spawnEntity(seat);
            player.startRiding(seat);
            return true;
        }
        return false;
    }
''')

print("Added sit method back")