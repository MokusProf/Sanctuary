package net.mokus.sanctuary;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.mokus.sanctuary.block.SanctuaryBlockEntities;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.events.StrangeEvents;
import net.mokus.sanctuary.item.SanctuaryItems;
import net.mokus.sanctuary.networking.ScreenShakePacket;
import net.mokus.sanctuary.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sanctuary implements ModInitializer {
    public static final SimpleParticleType SHOCKWAVE = FabricParticleTypes.simple();
    public static final SimpleParticleType SHOCKWAVEOS = FabricParticleTypes.simple();
    //Could someone tell me WHY THE FUCK THIS REG DIDN'T WORK PROPERLY BUT IT WORKS FINE HERE? WHYYYYY

    public static final String MOD_ID = "sanctuary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        SanctuaryItems.init();

        SanctuaryBlocks.init();
        SanctuaryBlockEntities.init();

        SanctuarySounds.init();

        LithiumConfig.iDontTrustAnyOfYouSoIAddedThisSoNoMatterWhatMyModShouldWork();

        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "shockwave"), SHOCKWAVE);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "shockwaveos"), SHOCKWAVEOS);

        ScreenShakePacket.registerPayloadType();

        SanctuaryDamageSources.init();
        SanctuaryDataComponents.init();

        StrangeEvents.serverEvents();
        SanctuaryLootTables.init();
    }
}
