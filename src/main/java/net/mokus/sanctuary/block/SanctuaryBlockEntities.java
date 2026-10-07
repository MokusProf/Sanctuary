package net.mokus.sanctuary.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mokus.sanctuary.Sanctuary;

public class SanctuaryBlockEntities {

    public static final BlockEntityType<ModifiedBeaconBlockEntity> MODIFIED_BEACON_ENTITY =
            register("modified_beacon_entity", ModifiedBeaconBlockEntity::new, SanctuaryBlocks.MODIFIED_BEACON);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory, Block... blocks) {
        Identifier id = Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init(){}
}
