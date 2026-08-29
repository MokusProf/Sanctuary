package net.mokus.sanctuary.sanctuary.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import net.mokus.sanctuary.sanctuary.SanctuaryRegion;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PirateSanctuaryRenderer {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "textures/border/pirate_border.png");
    private static final Vector3f COLOR1 = new Vector3f(0.96f,0.8f,0.15f);
    private static final Vector3f COLOR2 = new Vector3f(0.45f,0f,0f);

    public static void render(SanctuaryRegion region, PoseStack matrices, MultiBufferSource consumers, LocalPlayer player) {
        AABB box = region.toBox();
        VertexConsumer buffer = consumers.getBuffer(RenderTypes.entityTranslucent(TEXTURE, true));
        Matrix4f mat = matrices.last().pose();

        float dx = (float) box.getXsize();
        float dy = (float) box.getYsize();
        float dz = (float) box.getZsize();
        Vector3f color;

        boolean banned = SanctuaryCComponents.SANCTUARY_PLAYER.get(player).isBannedFromPiratePlace();

        float alpha;
        if (banned) {
            color = COLOR2;
            alpha = 1f;
        } else {
            color = COLOR1;
            double wallDist = distanceToWalls(box, player.position());
            float d = 5f;
            alpha = Mth.clamp(1f - (float) ((wallDist - d) / d), 0f, 1f);
        }

        if (alpha <= 0f) return;

        float m = 2.0f;
        quad(buffer, mat, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ, dz, dy, m, color, alpha);
        quad(buffer, mat, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ, dz, dy, m, color, alpha);
        quad(buffer, mat, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.minX, box.maxY, box.minZ, dz, dy, m, color, alpha);
        quad(buffer, mat, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, dz, dy, m, color, alpha);

        quad(buffer, mat, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, dx, dz, m, color, alpha);
        quad(buffer, mat, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ, dx, dz, m, color, alpha);
    }

    private static double distanceToWalls(AABB box, Vec3 pos) {
        double outsideX = Math.max(0, Math.max(box.minX - pos.x, pos.x - box.maxX));
        double outsideY = Math.max(0, Math.max(box.minY - pos.y, pos.y - box.maxY));
        double outsideZ = Math.max(0, Math.max(box.minZ - pos.z, pos.z - box.maxZ));

        if (outsideX > 0 || outsideY > 0 || outsideZ > 0) {
            return Math.sqrt(outsideX * outsideX + outsideY * outsideY + outsideZ * outsideZ);
        }

        double distToMinX = pos.x - box.minX;
        double distToMaxX = box.maxX - pos.x;
        double distToMinY = pos.y - box.minY;
        double distToMaxY = box.maxY - pos.y;
        double distToMinZ = pos.z - box.minZ;
        double distToMaxZ = box.maxZ - pos.z;

        return Math.min(distToMinX, Math.min(distToMaxX, Math.min(distToMinY, Math.min(distToMaxY, Math.min(distToMinZ, distToMaxZ)))));
    }

    private static void quad(VertexConsumer b, Matrix4f m, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, float width, float height, float texSize, Vector3f color, float alpha) {
        vertex(b, m, x1, y1, z1, 0, 0, color, alpha);
        vertex(b, m, x2, y2, z2, 0, height / texSize, color, alpha);
        vertex(b, m, x3, y3, z3, width / texSize, height / texSize, color, alpha);
        vertex(b, m, x4, y4, z4, width / texSize, 0, color, alpha);
    }

    private static void vertex(VertexConsumer b, Matrix4f m, double x, double y, double z, float u, float v, Vector3f color, float alpha) {
        b.addVertex(m, (float) x, (float) y, (float) z).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setColor(color.x, color.y, color.z, alpha).setNormal(0, 0, 1);
    }
}