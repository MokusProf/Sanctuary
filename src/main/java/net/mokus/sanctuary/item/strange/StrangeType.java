package net.mokus.sanctuary.item.strange;


import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

public enum StrangeType {
    KILLS(true, false, false),
    BLOCKS(false, true, false),
    KILLS_AND_BLOCKS(true, true, false),
    DAMAGE(false, false, true);

    private final boolean kills, blocks, damage;

    StrangeType(boolean kills, boolean blocks, boolean damage) {
        this.kills = kills;
        this.blocks = blocks;
        this.damage = damage;
    }

    public boolean tracksKills()  { return kills; }
    public boolean tracksBlocks() { return blocks; }
    public boolean tracksDamage() { return damage; }

    public static StrangeType of(ItemStack stack) {
        if (stack.is(ItemTags.AXES)) return KILLS_AND_BLOCKS;
        if (stack.is(ItemTags.PICKAXES) || stack.is(ItemTags.SHOVELS) || stack.is(ItemTags.HOES)) return BLOCKS;
        if (stack.is(ItemTags.HEAD_ARMOR) || stack.is(ItemTags.CHEST_ARMOR) || stack.is(ItemTags.LEG_ARMOR) || stack.is(ItemTags.FOOT_ARMOR)) return DAMAGE;
        return KILLS;
    }
}