package net.mokus.sanctuary.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.item.SanctuaryItems;
import net.mokus.sanctuary.util.SanctuaryDataComponents;

import java.util.Optional;

public class SanctuaryModelProvider extends FabricModelProvider {
    public SanctuaryModelProvider(FabricDataOutput output) {
        super(output);
    }

    public static final ModelTemplate REFORGED_BEACON = block("reforged_beacon_model", TextureSlot.ALL);
    public static final ModelTemplate LONG_SWORD_MODEL = createItem("long_sword", TextureSlot.LAYER0);
    public static final ModelTemplate LONG_SWORD_USE_MODEL = createItem("long_sword_use", TextureSlot.LAYER0);


    private static ModelTemplate createItem(final String id, final TextureSlot... slots) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID,"item/" + id)), Optional.empty(), slots);
    }

    public static Identifier registerSwordModel(Item item, ItemModelGenerators generator, ModelTemplate model, String name, String texture) {
        Identifier modelId = ModelLocationUtils.getModelLocation(item, name);
        return model.create(modelId, TextureMapping.singleSlot(TextureSlot.LAYER0, new Material(TextureAtlas.LOCATION_ITEMS,ModelLocationUtils.getModelLocation(item, texture)).texture()), generator.modelOutput);
    }

    public final void generateSwordItem(Item punch, ItemModelGenerators itemModelGenerators) {
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(registerSwordModel(punch, itemModelGenerators, LONG_SWORD_MODEL, "_in_hand","_in_hand"));
        ItemModel.Unbaked inUseModel = ItemModelUtils.plainModel(registerSwordModel(punch, itemModelGenerators, LONG_SWORD_USE_MODEL,"_in_use","_in_hand"));
        ItemModel.Unbaked inGuiModel = ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(punch, ModelTemplates.FLAT_ITEM));
        itemModelGenerators.itemModelOutput.accept(
                punch,
                ItemModelUtils.select(
                        new DisplayContext(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                inUseModel,
                                inHandModel
                        ),
                        ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel)
                )
        );
    };


    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(SanctuaryBlocks.GILDED_OBSIDIAN);
        blockStateModelGenerator.createGlassBlocks(SanctuaryBlocks.GILDED_GLASS,SanctuaryBlocks.GILDED_GLASS_PANE);
        blockStateModelGenerator.createTrivialBlock(SanctuaryBlocks.REUSED_BEACON, TexturedModel.createDefault(TextureMapping::cube,REFORGED_BEACON));
        blockStateModelGenerator.createTrivialBlock(SanctuaryBlocks.MODIFIED_BEACON, TexturedModel.createDefault(TextureMapping::cube,REFORGED_BEACON));
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateBooleanDispatch(
                SanctuaryItems.EMBLEM,
                ItemModelUtils.hasComponent(SanctuaryDataComponents.EMBLEM_OWNER),
                ItemModelUtils.plainModel(itemModelGenerator.createFlatItemModel(SanctuaryItems.EMBLEM, "_signed", ModelTemplates.FLAT_ITEM)),
                ItemModelUtils.plainModel(itemModelGenerator.createFlatItemModel(SanctuaryItems.EMBLEM, "_not_signed", ModelTemplates.FLAT_ITEM))
        );

        this.generateSwordItem(SanctuaryItems.RITUAL_SWORD,itemModelGenerator);
        itemModelGenerator.generateFlatItem(SanctuaryItems.KILLSTREAK_KIT,ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(SanctuaryItems.STRANGIFIER,ModelTemplates.FLAT_ITEM);
    }
}
