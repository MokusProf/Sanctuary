package net.mokus.sanctuary.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ModifiedBeaconBlockEntity extends BlockEntity {
    public ModifiedBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SanctuaryBlockEntities.MODIFIED_BEACON_ENTITY, blockPos, blockState);
    }

    private float plantYaw = 35f;
    private long placedAtTick = -1;

    public void onPlaced(float lookYaw) {
        this.plantYaw = lookYaw;
        this.placedAtTick = this.level != null ? this.level.getGameTime() : -1;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public float getPlantYaw() {
        return this.plantYaw;
    }

    public float getElapsedTicksSincePlaced(float partialTick) {
        if (this.placedAtTick < 0 || this.level == null) {
            return -1;
        }
        return ((this.level.getGameTime() - this.placedAtTick) + partialTick);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putFloat("PlantYaw",this.plantYaw);
        valueOutput.putLong("PlaceAtTick",this.placedAtTick);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.plantYaw = valueInput.getFloatOr("PlantYaw",0f);
        this.placedAtTick = valueInput.contains("PlacedAtTick") ? valueInput.getLongOr("PlacedAtTick",0) : -1;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putFloat("PlantYaw", this.plantYaw);
        tag.putLong("PlacedAtTick", this.placedAtTick);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
