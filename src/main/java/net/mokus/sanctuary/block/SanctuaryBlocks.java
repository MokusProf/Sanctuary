package net.mokus.sanctuary.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.mokus.sanctuary.Sanctuary;

import java.util.function.Function;

public class SanctuaryBlocks {


    public static final Block REUSED_BEACON = registerBlock("reused_beacon",
            PirateBeaconBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON).strength(-1.0F, 3600000.0F)
                    .lightLevel(blockStatex -> 0));


    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
        Block block = factory.apply(settings.setId(key));
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static void registerBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
        Registry.register(BuiltInRegistries.ITEM, key, new BlockItem(block, new Item.Properties().setId(key)));
    }

    public static void init(){
    }
}