package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.item.SanctuaryItems;

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

        translationBuilder.add(SanctuaryItems.EMBLEM,"Emblem");
        translationBuilder.add(SanctuaryItems.RITUAl_SWORD,"Ritual Sword");
        translationBuilder.add(SanctuaryBlocks.MODIFIED_BEACON.asItem(),"Modified Beacon");

        translationBuilder.add(SanctuaryItems.STRANGIFIER,"Strangifier");
        translationBuilder.add(SanctuaryItems.KILLSTREAK_KIT,"Killstreak Kit");


        translationBuilder.add("death.attack.sanctuary_damage","%s was absorbed by the Sanctuary");

    }
}
