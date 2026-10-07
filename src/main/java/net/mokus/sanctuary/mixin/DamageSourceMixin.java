package net.mokus.sanctuary.mixin;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.mokus.sanctuary.util.SanctuaryDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {

    @Redirect(
            method = "getLocalizedDeathMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"
            )
    )
    private boolean sanctuary$strangeCountsAsNamed(ItemStack stack, DataComponentType<?> type) {
        boolean original = stack.has(type);
        if (type == DataComponents.CUSTOM_NAME) {
            return original || stack.has(SanctuaryDataComponents.STRANGE_DATA);
        }
        return original;
    }
}
