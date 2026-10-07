package net.mokus.sanctuary.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;

public class ScreenShakePacket {

    public static void registerPayloadType() {
        PayloadTypeRegistry.playS2C().register(ScreenShakePayload.TYPE, ScreenShakePayload.CODEC);
    }

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenShakePayload.TYPE, (payload, context) ->
                context.client().execute(() -> SanctuaryCComponents.SANCTUARY_PLAYER.get(context.player()).triggerShake(payload.intensity(), payload.durationTicks())));
    }

    public static void send(ServerLevel level, Vec3 center, double radius, float intensity, int durationTicks) {
        ScreenShakePayload payload = new ScreenShakePayload(intensity, durationTicks);
        double radiusSq = radius * radius;

        for (ServerPlayer player : level.players()) {
            if (player.position().distanceToSqr(center) <= radiusSq) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }
}
