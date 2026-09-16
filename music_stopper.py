import sys

with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'r') as f:
    text = f.read()

old_finish = '''        if (targetStage > currentStage) {
            world.setBlockState(pos, getCachedState().with(GrandBeetleNestPieceBlock.STAGE, targetStage), Block.NOTIFY_ALL);
            updateStructure(targetStage);
            if (targetStage == 4 && world instanceof ServerWorld sw) {
                org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby(sw, pos, "finish_grand_nest");
            }
        }'''

new_finish = '''        if (targetStage > currentStage) {
            world.setBlockState(pos, getCachedState().with(GrandBeetleNestPieceBlock.STAGE, targetStage), Block.NOTIFY_ALL);
            updateStructure(targetStage);
            if (targetStage == 4 && world instanceof ServerWorld sw) {
                org.examplee.proyecto_intento.entity.AdvancementHelper.grantNearby(sw, pos, "finish_grand_nest");
                for (net.minecraft.entity.player.PlayerEntity player : sw.getPlayers()) {
                    if (player.squaredDistanceTo(pos.toCenterPos()) < 4096) {
                        ((net.minecraft.server.network.ServerPlayerEntity) player).networkHandler
                            .sendPacket(new net.minecraft.network.packet.s2c.play.StopSoundS2CPacket(
                                net.minecraft.util.Identifier.of("proyecto_intento", "grand_nest_start"), 
                                net.minecraft.sound.SoundCategory.RECORDS));
                    }
                }
            }
        }'''

text = text.replace(old_finish, new_finish)
with open('src/main/java/org/examplee/proyecto_intento/block/GrandBeetleNestBlockEntity.java', 'w') as f:
    f.write(text)
