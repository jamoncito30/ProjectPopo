package org.examplee.proyecto_intento.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.entity.ModParticles;
import org.examplee.proyecto_intento.entity.ModSounds;
import org.examplee.proyecto_intento.entity.SmellSystem;

public final class SmellAmbience {
    private static int buzzCooldown = 200;
    private static int ticks;
    public static void initialize() { ClientTickEvents.END_CLIENT_TICK.register(SmellAmbience::tick); }
    private static void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) { ticks = 0; buzzCooldown = 200; return; }
        if (client.isPaused()) return;
        ticks++;
        if (ticks % 12 == 0) {
            int emitted = 0;
            for (LivingEntity entity : client.world.getEntitiesByClass(LivingEntity.class,
                    client.player.getBoundingBox().expand(16), SmellSystem::isSmelly)) {
                if (++emitted > 12) break;
                client.world.addParticle(ModParticles.FLY, entity.getX(), entity.getBodyY(0.65), entity.getZ(), entity.getId(), 1, 0);
            }
        }
        // One shared budget per listener: fields of wet blocks never stack dozens of buzzing sounds.
        if (--buzzCooldown > 0) return;
        Vec3d source = null;
        if (SmellSystem.isSmelly(client.player)) source = client.player.getPos();
        if (source == null) {
            for (LivingEntity entity : client.world.getEntitiesByClass(LivingEntity.class,
                    client.player.getBoundingBox().expand(5), SmellSystem::isSmelly)) { source = entity.getPos(); break; }
        }
        if (source == null) {
            BlockPos center = client.player.getBlockPos();
            for (BlockPos pos : BlockPos.iterate(center.add(-4, -2, -4), center.add(4, 2, 4))) {
                if (client.world.getBlockState(pos).isOf(ModBlocks.WET_POPO) && client.world.getBlockState(pos.up()).isAir()) {
                    source = pos.toCenterPos(); break;
                }
            }
        }
        if (source != null) {
            client.world.playSound(source.x, source.y, source.z, ModSounds.FLIES, SoundCategory.AMBIENT, 0.10F,
                    1.55F + client.world.random.nextFloat() * 0.2F, false);
            buzzCooldown = 240 + client.world.random.nextInt(201); // 12–22 s
        } else buzzCooldown = 60;
    }
    private SmellAmbience() { }
}
