package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.mokus.sanctuary.block.SanctuaryBlocks;

import java.util.concurrent.CompletableFuture;

public class SanctuaryLangProvider extends FabricLanguageProvider {
    protected SanctuaryLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {

        translationBuilder.add(SanctuaryBlocks.REUSED_BEACON.asItem(),"Runic Beacon");
        translationBuilder.add(SanctuaryBlocks.GILDED_GLASS.asItem(),"Gilded Glass");
        translationBuilder.add(SanctuaryBlocks.GILDED_GLASS_PANE.asItem(),"Gilded Glass Pane");
        translationBuilder.add(SanctuaryBlocks.GILDED_OBSIDIAN.asItem(),"Gilded Obsidian");

    }
}
