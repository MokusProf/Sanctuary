package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.mokus.sanctuary.block.SanctuaryBlocks;

import java.util.concurrent.CompletableFuture;

public class SanctuaryTagProvider extends FabricTagProvider.BlockTagProvider {
    public SanctuaryTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SanctuaryBlocks.GILDED_OBSIDIAN);
        valueLookupBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(SanctuaryBlocks.GILDED_OBSIDIAN);

    }
}
