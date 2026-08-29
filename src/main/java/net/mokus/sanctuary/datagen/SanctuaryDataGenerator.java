package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SanctuaryDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(SanctuaryLangProvider::new);
        pack.addProvider(SanctuaryModelProvider::new);
        pack.addProvider(SanctuaryDamageTagProvider::new);
        pack.addProvider(SanctuaryRecipeProvider::new);
        pack.addProvider(SanctuaryLootTableProvider::new);
        pack.addProvider(SanctuaryTagProvider::new);
    }
}
