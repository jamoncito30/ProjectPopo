package org.examplee.proyecto_intento.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.SimpleParticleType;
import org.examplee.proyecto_intento.entity.SmellSystem;

public final class FlyParticle extends SpriteBillboardParticle {
    private final double anchorX, anchorY, anchorZ;
    private final double phase, radius;
    private final int followedEntity;
    private FlyParticle(ClientWorld world, double x, double y, double z, int followedEntity, SpriteProvider sprites) {
        super(world, x, y, z);
        this.anchorX = x; this.anchorY = y; this.anchorZ = z;
        this.followedEntity = followedEntity;
        phase = random.nextDouble() * Math.PI * 2;
        radius = 0.25 + random.nextDouble() * 0.35;
        maxAge = 45 + random.nextInt(35);
        scale = 0.065F;
        collidesWithWorld = false;
        setSprite(sprites);
    }
    @Override public void tick() {
        prevPosX = x; prevPosY = y; prevPosZ = z;
        if (++age >= maxAge) { markDead(); return; }
        double cx = anchorX, cy = anchorY, cz = anchorZ;
        if (followedEntity >= 0) {
            Entity entity = world.getEntityById(followedEntity);
            if (!(entity instanceof LivingEntity living) || !living.isAlive() || !SmellSystem.isSmelly(living)) { markDead(); return; }
            cx = entity.getX(); cy = entity.getY() + entity.getHeight() * 0.65; cz = entity.getZ();
        }
        double angle = phase + age * 0.32;
        setPos(cx + Math.cos(angle) * radius, cy + Math.sin(age * 0.47 + phase) * 0.13, cz + Math.sin(angle) * radius);
        this.prevAngle = this.angle;
        this.angle = (float) Math.sin(age * 2.7) * 0.6F;
    }
    @Override public ParticleTextureSheet getType() { return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT; }
    public record Factory(SpriteProvider sprites) implements ParticleFactory<SimpleParticleType> {
        @Override public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z,
                                                 double vx, double vy, double vz) {
            // vy marks entity-following particles; block particles use zero velocities.
            return new FlyParticle(world, x, y, z, vy == 1 ? (int) vx : -1, sprites);
        }
    }
}
