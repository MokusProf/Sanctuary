package net.mokus.sanctuary;

import net.fabricmc.api.ModInitializer;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sanctuary implements ModInitializer {
    public static final String MOD_ID = "sanctuary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        SanctuaryBlocks.init();
        LithiumConfig.iDontTrustAnyOfYouSoIAddedThisSoNoMatterWhatMyModShouldWork();
        SanctuaryDamageSources.init();
    }
}
