package net.mokus.sanctuary.mixin;


import com.google.common.collect.Iterables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import net.mokus.sanctuary.sanctuary.SanctuaryRegion;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = CollisionGetter.class)
public interface SanctuaryCollisionMixin {

    @Inject(method = "getBlockCollisions", at = @At("RETURN"), cancellable = true)
    private void sanctuary$prisonCollision(@Nullable Entity entity, AABB box, CallbackInfoReturnable<Iterable<VoxelShape>> cir){
        if (entity == null || !(this instanceof Level level)) return;
        if (!(entity instanceof Player player)) return;

        List<VoxelShape> customShapes = new ArrayList<>();

        boolean bannedFromPiratePlace = SanctuaryCComponents.SANCTUARY_PLAYER.get(player).isBannedFromPiratePlace();

        if (bannedFromPiratePlace) {
            SanctuaryRegion pirateRegion = SanctuaryCComponents.PIRATE_COMPONENT.get(level).getRegion();
            VoxelShape pirateShape = sanctuary$RegionShape(pirateRegion, player, box);
            if (pirateShape != null) {
                customShapes.add(pirateShape);
            }
        }

        if (!customShapes.isEmpty()) {
            Iterable<VoxelShape> original = cir.getReturnValue();
            cir.setReturnValue(Iterables.concat(original, customShapes));
        }
    }

    @Unique
    private static VoxelShape sanctuary$RegionShape(@Nullable SanctuaryRegion region, Entity entity, AABB box) {
        if (region == null) return null;

        AABB regionBox = region.toBox();
        boolean entityInside = regionBox.contains(entity.position());

        if (!entityInside) {
            if (regionBox.intersects(box)) {
                return Shapes.create(regionBox);
            }
            return null;
        }

        VoxelShape queryShape = Shapes.create(box);
        VoxelShape regionShape = Shapes.create(regionBox);
        VoxelShape outsideWithinQuery = Shapes.joinUnoptimized(queryShape, regionShape, BooleanOp.ONLY_FIRST);

        return outsideWithinQuery.isEmpty() ? null : outsideWithinQuery;
    }
}
