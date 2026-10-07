package net.mokus.sanctuary.item.strange;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StrangeData(String name, int killCount, int blocksMined, float damageAbsorbed) {
    public static final Codec<StrangeData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                    Codec.STRING.fieldOf("name").forGetter(StrangeData::name),
                    Codec.INT.fieldOf("killCount").forGetter(StrangeData::killCount),
                    Codec.INT.optionalFieldOf("blocksMined", 0).forGetter(StrangeData::blocksMined),
                    Codec.FLOAT.optionalFieldOf("damageAbsorbed", 0f).forGetter(StrangeData::damageAbsorbed))
            .apply(inst, StrangeData::new));

    public static final StreamCodec<ByteBuf, StrangeData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, StrangeData::name,
            ByteBufCodecs.VAR_INT, StrangeData::killCount,
            ByteBufCodecs.VAR_INT, StrangeData::blocksMined,
            ByteBufCodecs.FLOAT, StrangeData::damageAbsorbed,
            StrangeData::new);

    public StrangeData(String name, int killCount) {
        this(name, killCount, 0, 0f);
    }

    public StrangeData withKill()  { return new StrangeData(name, killCount + 1, blocksMined, damageAbsorbed); }
    public StrangeData withDeath() { return new StrangeData(name, killCount - 1, blocksMined, damageAbsorbed); }
    public StrangeData withBlockMined() { return new StrangeData(name, killCount, blocksMined + 1, damageAbsorbed); }
    public StrangeData withDamageAbsorbed(float amount) { return new StrangeData(name, killCount, blocksMined, damageAbsorbed + amount); }

    public int score() {
        return killCount + blocksMined + (int) damageAbsorbed;
    }

    public StrangeRank rank() {
        return StrangeRank.forKills(score());
    }
}
