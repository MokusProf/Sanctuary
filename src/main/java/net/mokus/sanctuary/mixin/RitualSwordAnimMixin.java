package net.mokus.sanctuary.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import net.mokus.sanctuary.item.RitualSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class RitualSwordAnimMixin<T extends HumanoidRenderState> {

    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("TAIL")
    )
    private void Sanctuary$overheadPose(T state, CallbackInfo ci) {
        if (!state.isUsingItem) return;

        ItemStack usedStack = state.getUseItemStackForArm(state.mainArm);

        if (!(usedStack.getItem() instanceof RitualSwordItem)) return;

        this.rightArm.xRot = -2.75f;
        this.rightArm.yRot = -0.15f;
        this.rightArm.zRot = 0.4f;

        this.leftArm.xRot = -2.75f;
        this.leftArm.yRot = 0.15f;
        this.leftArm.zRot = -0.4f;
    }
}
