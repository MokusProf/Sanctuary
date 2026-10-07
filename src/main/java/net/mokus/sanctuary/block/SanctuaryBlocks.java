package net.mokus.sanctuary.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.mokus.sanctuary.Sanctuary;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SanctuaryBlocks {


    public static final Block REUSED_BEACON = registerBlock("reused_beacon",
            PirateBeaconBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON).strength(-1.0F, 3600000.0F)
                    .lightLevel(blockStatex -> 0),true,null);

    public static final Block MODIFIED_BEACON = registerBlock("modified_beacon",
            ModifiedBeaconBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON).strength(-1.0F, 3600000.0F)
                    .lightLevel(blockStatex -> 0),true,null);

    public static final Block GILDED_OBSIDIAN = registerBlock("gilded_obsidian",
            Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN),true, CreativeModeTabs.BUILDING_BLOCKS);

    public static final Block GILDED_GLASS = registerBlock("gilded_glass",
            Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS),true, CreativeModeTabs.BUILDING_BLOCKS);

    public static final Block GILDED_GLASS_PANE = registerBlock("gilded_glass_pane",
            IronBarsBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE),true, CreativeModeTabs.BUILDING_BLOCKS);


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings,boolean hasBlockItem,  @Nullable ResourceKey<CreativeModeTab> group) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
        Block block = factory.apply(settings.setId(key));
        if (hasBlockItem){
            registerBlockItem(name, block);
        }
        Block registeredBlock = Registry.register(BuiltInRegistries.BLOCK, key, block);
        if (!(group == null)){
            ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.accept(registeredBlock));
        }
        return registeredBlock;
    }

    private static void registerBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
        Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block, new Item.Properties().setId(key)));
    }

    public static void init(){
    }
}