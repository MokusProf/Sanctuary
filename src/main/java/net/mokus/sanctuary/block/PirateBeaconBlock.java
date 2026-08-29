package net.mokus.sanctuary.block;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import net.mokus.sanctuary.util.SanctuaryMarkCheck;

public class PirateBeaconBlock extends Block {
    public float SIZE = 19.25f;
    private static final float EXPAND_PER_ORE = 1f;

    public PirateBeaconBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide()) {
            SanctuaryCComponents.PIRATE_COMPONENT.get(level).setRegion(blockPos, SIZE);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
        SanctuaryCComponents.PIRATE_COMPONENT.get(world).removeRegion(pos);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!SanctuaryMarkCheck.isPirateUUID(player)){
            return InteractionResult.PASS;
        }

        if (itemStack.is(Items.PAPER) && itemStack.has(DataComponents.CUSTOM_NAME)) {
            if (handleBanPaper(level, itemStack.getHoverName().getString())) {
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        if (itemStack.getItem() instanceof BlockItem && itemStack.is(ConventionalItemTags.ORES)) {
            SanctuaryCComponents.PIRATE_COMPONENT.get(level).growRegion(EXPAND_PER_ORE);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            level.playSound(null, blockPos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1f, 1f);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }


    private boolean handleBanPaper(Level level, String name) {
        String trimmed = name.trim();
        boolean add;
        String username;

        if (trimmed.regionMatches(true, 0, "add ", 0, 4)) {
            add = true;
            username = trimmed.substring(4).trim();
        } else if (trimmed.regionMatches(true, 0, "remove ", 0, 7)) {
            add = false;
            username = trimmed.substring(7).trim();
        } else {
            return false;
        }

        if (username.isEmpty() || !(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        ServerPlayer target = serverLevel.getServer().getPlayerList().getPlayerByName(username);
        if (target == null) {
            return true;
        }

        SanctuaryCComponents.SANCTUARY_PLAYER.get(target).setBannedFromPiratePlace(add);
        return true;
    }
}
