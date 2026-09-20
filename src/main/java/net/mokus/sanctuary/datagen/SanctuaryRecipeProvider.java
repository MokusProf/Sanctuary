package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.mokus.sanctuary.block.SanctuaryBlocks;

import java.util.concurrent.CompletableFuture;

public class SanctuaryRecipeProvider extends FabricRecipeProvider {
    public SanctuaryRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.BUILDING_BLOCKS, SanctuaryBlocks.GILDED_GLASS)
                        .define('#', Items.GOLD_NUGGET)
                        .define('X', Items.GLASS)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern(" # ")
                        .unlockedBy("has_gold", this.has(Items.GOLD_NUGGET))
                        .save(this.output);
                shaped(RecipeCategory.BUILDING_BLOCKS, SanctuaryBlocks.GILDED_OBSIDIAN)
                        .define('#', Items.GOLD_NUGGET)
                        .define('X', Items.OBSIDIAN)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern(" # ")
                        .unlockedBy("has_gold", this.has(Items.GOLD_NUGGET))
                        .save(this.output);

                stainedGlassPaneFromStainedGlass(SanctuaryBlocks.GILDED_GLASS_PANE,SanctuaryBlocks.GILDED_GLASS);
            }
        };
    }

    @Override
    public String getName() {
        return "SanctuaryRecipeProvider";
    }
}
