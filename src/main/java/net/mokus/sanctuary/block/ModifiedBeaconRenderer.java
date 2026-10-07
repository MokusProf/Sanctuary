package net.mokus.sanctuary.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.item.SanctuaryItems;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class ModifiedBeaconRenderer implements BlockEntityRenderer<ModifiedBeaconBlockEntity,ModifiedBeaconBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;
    private static final Vector3f start = new Vector3f(0.8f, 1.3f, 0.5f);
    private static final Vector3f end = new Vector3f(0.5f, 0.7f, 0.5f);
    private static final int animTicks = 20;

    public ModifiedBeaconRenderer(BlockEntityRendererProvider.Context context){
        this.itemModelResolver = context.itemModelResolver();
    }

    private static float easeInStartsSlowEndsFast(float t) {
        return t * t * t;
    }

    private static Vector3f cords(float elapsedTicks, float plantYaw) {
        float progress = elapsedTicks < 0 ? 1f : easeInStartsSlowEndsFast(Mth.clamp(elapsedTicks / animTicks, 0f, 1f));

        Vector3f pullBack = new Vector3f(start).sub(end);
        pullBack.rotateY((float) Math.toRadians(plantYaw));

        return new Vector3f(end).add(pullBack.mul(1f - progress));
    }

    @Override
    public ModifiedBeaconBlockEntityRenderState createRenderState() {
        return new ModifiedBeaconBlockEntityRenderState();
    }

    @Override
    public void submit(ModifiedBeaconBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        poseStack.translate(state.plunge.x(), state.plunge.y(), state.plunge.z());

        poseStack.mulPose(Axis.YP.rotationDegrees(state.plantYaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(20f));
//        poseStack.mulPose(Axis.XP.rotationDegrees(-6f));
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));

        poseStack.scale(1.4f, 1.4f, 1.4f);

        state.swordRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }

    @Override
    public void extractRenderState(ModifiedBeaconBlockEntity blockEntity, ModifiedBeaconBlockEntityRenderState state, float tickProgress, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, vec3, crumblingOverlay);

        state.plantYaw = blockEntity.getPlantYaw();
        state.plunge = cords(blockEntity.getElapsedTicksSincePlaced(tickProgress), state.plantYaw);

        ItemStack sword = SanctuaryItems.RITUAL_SWORD.asItem().getDefaultInstance();
        this.itemModelResolver.updateForTopItem(
                state.swordRenderState,
                sword,
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                (int) blockEntity.getBlockPos().asLong()
        );
    }
}
