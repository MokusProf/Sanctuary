package net.mokus.sanctuary;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import net.mokus.sanctuary.sanctuary.SanctuaryRegion;
import net.mokus.sanctuary.sanctuary.render.PirateSanctuaryRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SanctuaryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_TRANSLUCENT.register(context -> {
            PoseStack matrices = context.matrices();
            Camera camera = context.gameRenderer().getMainCamera();
            Level level = context.gameRenderer().getMinecraft().level;
            LocalPlayer player = context.gameRenderer().getMinecraft().player;

            int renderDistanceChunks = Minecraft.getInstance().options.renderDistance().get();
            double maxDistance = renderDistanceChunks * 16.0;
            double maxDistanceSq = maxDistance * maxDistance;

            Vec3 camPos = camera.position();

            SanctuaryRegion region = SanctuaryCComponents.PIRATE_COMPONENT.get(level).getRegion();
            if (region == null) return;

            AABB box = region.toBox();

            double dx = Math.max(0, Math.max(box.minX - camPos.x, camPos.x - box.maxX));
            double dy = Math.max(0, Math.max(box.minY - camPos.y, camPos.y - box.maxY));
            double dz = Math.max(0, Math.max(box.minZ - camPos.z, camPos.z - box.maxZ));
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq > maxDistanceSq) return;

            matrices.pushPose();
            matrices.translate(-camPos.x, -camPos.y, -camPos.z);
            PirateSanctuaryRenderer.render(region, matrices, context.consumers(), player);
            matrices.popPose();
        });

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
                SanctuaryBlocks.REUSED_BEACON,
                SanctuaryBlocks.GILDED_GLASS,
                SanctuaryBlocks.GILDED_GLASS_PANE);
    }
}
