package net.mokus.sanctuary.cardinalComponents;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.mokus.sanctuary.util.SanctuaryDamageSources;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;


public class SanctuaryPlayerComponent implements Component, ServerTickingComponent, AutoSyncedComponent {
    private final Player player;

    private boolean isBannedFromPiratePlace;

    public SanctuaryPlayerComponent(Player player) {
        this.player = player;
    }

    //TBH, this is a bad way to do it, but I don't know how to do it better. If you know how please educate my stupid ass.

    @Override
    public void serverTick() {
        PirateComponent pirateComponent = SanctuaryCComponents.PIRATE_COMPONENT.get(player.level());

        if (isBannedFromPiratePlace){
            if (pirateComponent.isEntityInRegion(player)){
                Level level = player.level();
                DamageSource damageSource = new DamageSource(
                        level.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(SanctuaryDamageSources.SANCTUARY_DAMAGE.identifier()).get()
                );

                player.hurtServer((ServerLevel) level,damageSource, 1.0f);
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING,10,0,true,true));
            }
        }
    }


    public void setBannedFromPiratePlace(boolean bannedFromPiratePlace){
        this.isBannedFromPiratePlace = bannedFromPiratePlace;
        SanctuaryCComponents.SANCTUARY_PLAYER.sync(this.player);
    }

    public boolean isBannedFromPiratePlace(){
        return this.isBannedFromPiratePlace;
    }

    @Override
    public void readData(ValueInput readView) {
        this.isBannedFromPiratePlace = readView.getBooleanOr("isBanned",false);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("isBanned",this.isBannedFromPiratePlace);
    }

}
