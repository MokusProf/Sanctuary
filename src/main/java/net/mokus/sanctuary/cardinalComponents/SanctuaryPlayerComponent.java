package net.mokus.sanctuary.cardinalComponents;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.util.SanctuaryDamageSources;
import net.mokus.sanctuary.util.SanctuaryMarkCheck;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;


public class SanctuaryPlayerComponent implements Component, ServerTickingComponent, ClientTickingComponent, AutoSyncedComponent {
    private final Player player;

    private boolean isBannedFromPiratePlace;
    private float shakeIntensity = 0f;
    private int shakeTicksRemaining = 0;
    private int shakeTotalTicks = 1;
    private float pendingRawDamage = -1f;

    public SanctuaryPlayerComponent(Player player) {
        this.player = player;
    }

    //TBH, this is a bad way to do it, but I don't know how to do it better. If you know how please educate my stupid ass.

    @Override
    public void serverTick() {
        PirateComponent pirateComponent = SanctuaryCComponents.PIRATE_COMPONENT.get(player.level());
        PrisonComponent component = SanctuaryCComponents.PRISON.get(player.level());

        if (isBannedFromPiratePlace){
            if (pirateComponent.isEntityInRegion(player)){
                Level level = player.level();
                if (level.getGameTime() % 24 == 0){
                    DamageSource damageSource = new DamageSource(
                            level.registryAccess()
                                    .lookupOrThrow(Registries.DAMAGE_TYPE)
                                    .get(SanctuaryDamageSources.SANCTUARY_DAMAGE.identifier()).get()
                    );

                    player.hurtServer((ServerLevel) level,damageSource, 1.0f);
                }
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING,10,0,true,true));
            }
        }

        if (!component.isEntityInRegion(player)) return;

        if (!SanctuaryMarkCheck.hasMark(player)){
            Level level = player.level();
            long time = level.getGameTime();

            if (time % 5 == 0) {
                DamageSource damageSource = new DamageSource(
                        level.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(SanctuaryDamageSources.SANCTUARY_DAMAGE.identifier()).get()
                );

                player.hurtServer((ServerLevel) level,damageSource, 2.0f);
                spawnParticleLine((ServerLevel) level, component.getRegion().center().getCenter(), new Vec3(player.getX(),player.getY()+1,player.getZ()));
            }
        }
    }



    @Override
    public void clientTick() {
        if (shakeTicksRemaining > 0) {
            shakeTicksRemaining--;
        }
    }

    public void triggerShake(float intensity, int durationTicks) {
        this.shakeIntensity = intensity;
        this.shakeTicksRemaining = durationTicks;
        this.shakeTotalTicks = Math.max(durationTicks, 1);
        SanctuaryCComponents.SANCTUARY_PLAYER.sync(this.player);
    }

    public float getShakeMagnitude(float partialTick) {
        if (shakeTicksRemaining <= 0) return 0f;
        float progress = Mth.clamp((shakeTicksRemaining - partialTick) / shakeTotalTicks, 0f, 1f);
        return shakeIntensity * progress;
    }

    public void setBannedFromPiratePlace(boolean bannedFromPiratePlace){
        this.isBannedFromPiratePlace = bannedFromPiratePlace;
        SanctuaryCComponents.SANCTUARY_PLAYER.sync(this.player);
    }

    public boolean isBannedFromPiratePlace(){
        return this.isBannedFromPiratePlace;
    }



    public void setPendingRawDamage(float amount) {
        this.pendingRawDamage = amount;
        SanctuaryCComponents.SANCTUARY_PLAYER.sync(this.player);
    }

    public float consumePendingRawDamage() {
        float value = this.pendingRawDamage;
        this.pendingRawDamage = -1f;
        SanctuaryCComponents.SANCTUARY_PLAYER.sync(this.player);
        return value;
    }

    @Override
    public void readData(ValueInput readView) {
        this.shakeIntensity = readView.getFloatOr("shakeIntensity",0f);
        this.shakeTicksRemaining = readView.getIntOr("shakeTicks",0);
        this.shakeTotalTicks = readView.getIntOr("shakeTotal",0);
        this.isBannedFromPiratePlace = readView.getBooleanOr("isBanned",false);
        this.pendingRawDamage = readView.getFloatOr("pendingRawDamage", -1f);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putFloat("shakeIntensity",this.shakeIntensity);
        writeView.putInt("shakeTicks",this.shakeTicksRemaining);
        writeView.putInt("shakeTotal",this.shakeTotalTicks);
        writeView.putBoolean("isBanned",this.isBannedFromPiratePlace);
        writeView.putFloat("pendingRawDamage", this.pendingRawDamage);
    }

    private void spawnParticleLine(ServerLevel level, Vec3 from, Vec3 to) {
        double distance = from.distanceTo(to);
        int points = (int) Math.ceil(distance * 2);
        if (points < 1) return;

        DustParticleOptions purpleDust = new DustParticleOptions(14353404, 1.0f);

        for (int i = 0; i <= points; i++) {
            double t = (double) i / points;
            double x = Mth.lerp(t, from.x, to.x);
            double y = Mth.lerp(t, from.y, to.y) + 0.1;
            double z = Mth.lerp(t, from.z, to.z);
            level.sendParticles(purpleDust, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

}
