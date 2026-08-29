package net.mokus.sanctuary.sanctuary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record SanctuaryRegion(BlockPos center, float size) {
    public AABB toBox() {
        float r = size / 2;
        return new AABB(
                center.getX() - r, center.getY() - r, center.getZ() - r,
                center.getX() + r + 1, center.getY() + r + 1, center.getZ() + r + 1
        );
    }

    public boolean contains(Entity entity) {
        return toBox().contains(entity.position());
    }

    public boolean contains(Vec3 pos) {
        return toBox().contains(pos);
    }
}

