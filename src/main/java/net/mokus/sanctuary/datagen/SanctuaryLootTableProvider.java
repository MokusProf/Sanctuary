package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.mokus.sanctuary.block.SanctuaryBlocks;

import java.util.concurrent.CompletableFuture;

public class SanctuaryLootTableProvider extends FabricBlockLootTableProvider {
    protected SanctuaryLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropWhenSilkTouch(SanctuaryBlocks.GILDED_GLASS);
        dropWhenSilkTouch(SanctuaryBlocks.GILDED_GLASS_PANE);
        dropSelf(SanctuaryBlocks.GILDED_OBSIDIAN);
    }
}
