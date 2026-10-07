package net.mokus.sanctuary.sanctuary.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.sanctuary.SanctuaryRegion;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SanctuaryRenderer {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "textures/border/border.png");
    private static final float M = 2.0f;
    private static final int GROW_DURATION_TICKS = 20;

    private static final Vector3f COLOR_1 = new Vector3f(0.39f,0.0f,1.0f);
    private static final Vector3f COLOR_2 = new Vector3f(1.0f,0.00f,0.55f);

    private static final double CYCLE_SPEED = 0.25;

    private static float smoothstep(float t) {
        return t * t * (3f - 2f * t);
    }

    public static void render(SanctuaryRegion region, PoseStack matrices, MultiBufferSource consumers, Long startTick, Long time) {
        AABB fullBox = region.toBox();

        long elapsed = time - startTick;
        float progress = smoothstep(Mth.clamp((float) elapsed / GROW_DURATION_TICKS, 0f, 1f));

        Vec3 center = fullBox.getCenter();
        double halfX = (fullBox.getXsize() / 2.0) * progress;
        double halfY = (fullBox.getYsize() / 2.0) * progress;
        double halfZ = (fullBox.getZsize() / 2.0) * progress;

        AABB box = new AABB(
                center.x - halfX, center.y - halfY, center.z - halfZ,
                center.x + halfX, center.y + halfY, center.z + halfZ
        );

        VertexConsumer buffer = consumers.getBuffer(RenderTypes.entityTranslucent(TEXTURE, true));
        Matrix4f mat = matrices.last().pose();

        float dx = (float) box.getXsize();
        float dy = (float) box.getYsize();
        float dz = (float) box.getZsize();

//        double time = System.currentTimeMillis() / 1000.0 * CYCLE_SPEED;

        quad(buffer, mat, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ, box.minX, box.minY, box.maxZ, dz, dy, M, time);
        quad(buffer, mat, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, box.maxX, box.minY, box.maxZ, dz, dy, M, time);
        quad(buffer, mat, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, box.minX, box.maxY, box.minZ, dz, dy, M, time);
        quad(buffer, mat, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, dz, dy, M, time);

        quad(buffer, mat, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, dx, dz, M, time);
        quad(buffer, mat, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ, dx, dz, M, time);
    }

    private static void quad(VertexConsumer b, Matrix4f m, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, float width, float height, float texSize, double time) {
        vertex(b, m, x1, y1, z1, 0, 0, time);
        vertex(b, m, x2, y2, z2, 0, height / texSize, time);
        vertex(b, m, x3, y3, z3, width / texSize, height / texSize, time);
        vertex(b, m, x4, y4, z4, width / texSize, 0, time);
    }

    private static void vertex(VertexConsumer b, Matrix4f m, double x, double y, double z, float u, float v, double time) {
        float t = (float)(0.5 + 0.5 * Math.sin(2 * Math.PI * (time / 20 * 0.25)));
        Vector3f color = new Vector3f(COLOR_1).lerp(COLOR_2, t);
        //Vector3f color = rainbowColor(time);
        b.addVertex(m, (float) x, (float) y, (float) z).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0, 0, 1).setColor(color.x, color.y, color.z, 1f);
    }

    private static Vector3f rainbowColor(double time) {
        float f = (float) (time / 20 * 0.25);

        float r = (float) (0.5 + 0.5 * Math.sin(2 * Math.PI * f));
        float g = (float) (0.5 + 0.5 * Math.sin(2 * Math.PI * f + 2));
        float b = (float) (0.5 + 0.5 * Math.sin(2 * Math.PI * f + 4));
        return new Vector3f(r, g, b);
    }
}