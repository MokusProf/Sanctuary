package net.mokus.sanctuary.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record OwnerData(UUID uuid, String name) {
    public static final Codec<OwnerData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(OwnerData::uuid),
            Codec.STRING.fieldOf("name").forGetter(OwnerData::name))
            .apply(inst, OwnerData::new));
}