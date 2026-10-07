package net.mokus.sanctuary.networking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.mokus.sanctuary.Sanctuary;

public record ScreenShakePayload(float intensity, int durationTicks) implements CustomPacketPayload {

    public static final Type<ScreenShakePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "shake_packet_id"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenShakePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ScreenShakePayload::intensity,
            ByteBufCodecs.VAR_INT, ScreenShakePayload::durationTicks,
            ScreenShakePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

