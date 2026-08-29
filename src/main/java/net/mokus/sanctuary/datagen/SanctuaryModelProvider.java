package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.resources.Identifier;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.block.SanctuaryBlocks;

import java.util.Optional;

public class SanctuaryModelProvider extends FabricModelProvider {
    public SanctuaryModelProvider(FabricDataOutput output) {
        super(output);
    }

    public static final ModelTemplate REFORGED_BEACON = block("reforged_beacon_model", TextureSlot.ALL);


    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(SanctuaryBlocks.GILDED_OBSIDIAN);
        blockStateModelGenerator.createGlassBlocks(SanctuaryBlocks.GILDED_GLASS,SanctuaryBlocks.GILDED_GLASS_PANE);
        blockStateModelGenerator.createTrivialBlock(SanctuaryBlocks.REUSED_BEACON, TexturedModel.createDefault(TextureMapping::cube,REFORGED_BEACON));
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
    }
}
