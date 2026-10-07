package net.mokus.sanctuary.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.block.ModifiedBeaconBlockEntity;
import net.mokus.sanctuary.block.SanctuaryBlocks;
import net.mokus.sanctuary.networking.ScreenShakePacket;
import net.mokus.sanctuary.util.SanctuarySounds;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

public class RitualSwordItem extends Item {
    public RitualSwordItem(Properties properties) {
        super(properties);
    }
    private static final UUID MOKUS = UUID.fromString("c1115be4-d7e8-4979-b1bd-1a6820c736da");

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (player.getUUID().equals(MOKUS)){
            player.startUsingItem(interactionHand);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            BlockHitResult hit = this.getBlock(level, livingEntity, 6.0);

            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hit.getBlockPos();
                BlockState state = level.getBlockState(pos);
                if (state.is(Blocks.BEACON)) {
                    serverLevel.setBlockAndUpdate(pos, SanctuaryBlocks.MODIFIED_BEACON.defaultBlockState());
                    if (serverLevel.getBlockEntity(pos) instanceof ModifiedBeaconBlockEntity modifiedBeacon) {
                        modifiedBeacon.onPlaced(90 -livingEntity.getYRot());
                    }
                    serverLevel.sendParticles(Sanctuary.SHOCKWAVE, pos.getX(),pos.getY() + 0.5f,pos.getZ(),1,0,0,0,0);
                    serverLevel.sendParticles(Sanctuary.SHOCKWAVEOS, pos.getX(),pos.getY() + 0.5f,pos.getZ(),1,0,0,0,0);
                    serverLevel.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 0.2f, 1.0f);
                    serverLevel.playSound(null, pos, SanctuarySounds.SANCTUARY, SoundSource.PLAYERS, 32.0f, 1.0f);
                    fuckoff(level, (Player) livingEntity);
                    ScreenShakePacket.send(serverLevel,livingEntity.getEyePosition(),256,2,60);
                    return true;
                }
            }
        }
        return super.releaseUsing(itemStack, level, livingEntity, i);
    }

    private BlockHitResult getBlock(Level level, LivingEntity entity, double reach) {
        Vec3 eyePos = entity.getEyePosition(1.0F);
        Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);

        ClipContext clipContext = new ClipContext(eyePos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);

        return level.clip(clipContext);
    }

    private void fuckoff(Level level, Player player){
        double radius = 60.0;

        Vec3 origin = player.getEyePosition();
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(origin.add(-radius, -radius, -radius), origin.add(radius, radius, radius)),
                LivingEntity::isAlive
        );
        for (LivingEntity target : targets) {
            double distance = origin.distanceTo(target.getEyePosition());
            if (distance <= radius) {
                Vec3 knockbackDir = target.getEyePosition().subtract(origin).normalize();
                Vec3 knockback = knockbackDir.multiply(10,15,10);
                target.addDeltaMovement(knockback);
            }
        }
    }
}
