package net.mokus.sanctuary.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public abstract class ScreenShakeMixin {

    @ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float sanctuary$shakeYaw(float yRot) {
        return yRot + shakeOffset();
    }

    @ModifyVariable(method = "setRotation", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float sanctuary$shakePitch(float xRot) {
        return xRot + shakeOffset();
    }

    @Unique
    private float shakeOffset() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return 0f;

        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        float magnitude = SanctuaryCComponents.SANCTUARY_PLAYER.get(player).getShakeMagnitude(partialTick);
        if (magnitude <= 0f) return 0f;

        return (RandomSource.create().nextFloat() * 2f - 1f) * magnitude;
    }
}
