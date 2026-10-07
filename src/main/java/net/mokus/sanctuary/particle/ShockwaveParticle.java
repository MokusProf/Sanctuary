package net.mokus.sanctuary.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ShockwaveParticle extends SonicBoomParticle {
    public ShockwaveParticle(ClientLevel clientLevel, double d, double e, double f, double g, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, spriteSet);
        this.lifetime = 64;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.alpha = 0.8F;
    }

    public static final FacingCameraMode X_LOCKED = (quaternionf, camera, f) -> {
        quaternionf.rotationX((float)Math.toRadians(-90));
    };

    @Override
    public FacingCameraMode getFacingCameraMode() {
        return X_LOCKED;
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize++;
        this.quadSize++;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(
                SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource
        ) {
            return new ShockwaveParticle(clientLevel, d, e, f, g, this.sprites);
        }
    }
}
