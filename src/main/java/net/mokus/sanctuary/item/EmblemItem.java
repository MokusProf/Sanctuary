package net.mokus.sanctuary.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.mokus.sanctuary.item.data.OwnerData;
import net.mokus.sanctuary.util.SanctuaryDataComponents;

import java.util.function.Consumer;

public class EmblemItem extends Item {
    public EmblemItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!level.isClientSide()) {
            if (!stack.has(SanctuaryDataComponents.EMBLEM_OWNER)){
                stack.set(SanctuaryDataComponents.EMBLEM_OWNER, new OwnerData(player.getUUID(),player.getName().getString()));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;

    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        OwnerData data = itemStack.get(SanctuaryDataComponents.EMBLEM_OWNER);
        if (data != null) {
            consumer.accept(Component.literal(data.name()).withStyle(ChatFormatting.WHITE));
        }
    }
}
