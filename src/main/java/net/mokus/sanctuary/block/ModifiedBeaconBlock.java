package net.mokus.sanctuary.block;


import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import org.jspecify.annotations.Nullable;

public class ModifiedBeaconBlock extends BaseEntityBlock {
    public float SIZE = 55f;

    public ModifiedBeaconBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ModifiedBeaconBlock::new);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide()) {
            SanctuaryCComponents.PRISON.get(level).setStartTick(level.getGameTime());
            SanctuaryCComponents.PRISON.get(level).setRegion(blockPos, SIZE);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
        SanctuaryCComponents.PRISON.get(world).removeRegion(pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ModifiedBeaconBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
