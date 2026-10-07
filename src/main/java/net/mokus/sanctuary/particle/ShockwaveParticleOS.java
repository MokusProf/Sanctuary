package net.mokus.sanctuary.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ShockwaveParticleOS extends ShockwaveParticle {
    public ShockwaveParticleOS(ClientLevel clientLevel, double d, double e, double f, double g, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, spriteSet);
    }

    public static final FacingCameraMode X_LOCKED = (quaternionf, camera, f) -> {
        quaternionf.rotationX((float)Math.toRadians(90));
    };

    @Override
    public FacingCameraMode getFacingCameraMode() {
        return X_LOCKED;
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
            return new ShockwaveParticleOS(clientLevel, d, e, f, g, this.sprites);
        }
    }
}
