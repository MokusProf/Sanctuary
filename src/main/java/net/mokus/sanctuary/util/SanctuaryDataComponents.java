package net.mokus.sanctuary.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.item.data.OwnerData;
import net.mokus.sanctuary.item.data.StrangeData;


public class SanctuaryDataComponents {

    public static final DataComponentType<OwnerData> EMBLEM_OWNER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "emblem_owner"),
            DataComponentType.<OwnerData>builder().persistent(OwnerData.CODEC).build()
    );

    public static final DataComponentType<StrangeData> STRANGE_DATA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "strange_data"),
            DataComponentType.<StrangeData>builder().persistent(StrangeData.CODEC)
                    .networkSynchronized(StrangeData.STREAM_CODEC).build()
    );

    public static final DataComponentType<Integer> KILLSTREAK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "killstreak"),
            DataComponentType.<Integer>builder().persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT).build());

    public static void init() {
    }
}
