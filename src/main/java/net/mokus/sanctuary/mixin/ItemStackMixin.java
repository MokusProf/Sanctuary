package net.mokus.sanctuary.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.mokus.sanctuary.item.strange.StrangeData;
import net.mokus.sanctuary.item.strange.StrangeRank;
import net.mokus.sanctuary.util.SanctuaryDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void sanctuary$addPrefixes(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.has(DataComponents.CUSTOM_NAME)) return;

        StrangeData data = self.get(SanctuaryDataComponents.STRANGE_DATA);
        Integer streak = self.get(SanctuaryDataComponents.KILLSTREAK);
        if (data == null && streak == null) return;

        String name = cir.getReturnValue().getString();
        MutableComponent result;

        if (data != null) {
            StrangeRank rank = data.rank();
            String text = rank.title + " " + (streak != null ? "Killstreak " : "") + name;
            result = rank.style(text);
        } else {
            result = Component.literal("Killstreak " + name)
                    .withStyle(s -> s.withItalic(false));
        }
        if (streak != null) {
            result.append(Component.literal(" <" + streak + ">")
                    .withStyle(s -> s.withColor(0xAAAAAA).withItalic(false)));
        }

        cir.setReturnValue(result);
    }
}
