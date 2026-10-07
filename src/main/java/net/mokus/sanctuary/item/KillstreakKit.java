package net.mokus.sanctuary.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mokus.sanctuary.util.SanctuaryDataComponents;

public class KillstreakKit extends Item {
    public KillstreakKit(Properties properties) {
        super(properties);
    }


    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) return false;

        ItemStack target = slot.getItem();
        if (target.isEmpty()
                || target.getCount() != 1
                || target.getItem() instanceof KillstreakKit
                || target.has(SanctuaryDataComponents.KILLSTREAK)
                || !slot.allowModification(player)) {
            return false;
        }

        target.set(SanctuaryDataComponents.KILLSTREAK, 0);
        itemStack.shrink(1);

        player.playSound(SoundEvents.ANVIL_USE, 0.6f, 1.4f);
        return true;
    }
}
