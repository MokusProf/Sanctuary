package net.mokus.sanctuary.item.strange;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mokus.sanctuary.util.SanctuaryDataComponents;

public class Strangifier extends Item {
    public Strangifier(Properties properties) {
        super(properties);
    }


    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) return false;

        ItemStack target = slot.getItem();
        if (target.isEmpty()
                || target.getCount() != 1
                || target.getItem() instanceof Strangifier
                || target.has(SanctuaryDataComponents.STRANGE_DATA)
                || !slot.allowModification(player)) {
            return false;
        }

        target.set(SanctuaryDataComponents.STRANGE_DATA,
                new StrangeData(target.getHoverName().getString(), 0));
        itemStack.shrink(1);

        player.playSound(SoundEvents.ANVIL_USE, 0.6f, 1.4f);
        return true;
    }
}
