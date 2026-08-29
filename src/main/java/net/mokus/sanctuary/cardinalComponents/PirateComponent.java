package net.mokus.sanctuary.cardinalComponents;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.mokus.sanctuary.sanctuary.SanctuaryRegion;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PirateComponent implements Component, AutoSyncedComponent {

    private final Level world;
    @Nullable
    private SanctuaryRegion region;

    public PirateComponent(Level world) {
        this.world = world;
    }

    public void setRegion(BlockPos center, float size) {
        this.region = new SanctuaryRegion(center, size);
        this.sync();
    }

    public void clearRegion() {
        this.region = null;
        this.sync();
    }

    public void removeRegion(BlockPos center) {
        if (region != null && region.center().equals(center)) {
            this.region = null;
            this.sync();
        }
    }

    public void setRegionSize(float size) {
        if (region != null) {
            this.region = new SanctuaryRegion(region.center(), size);
            this.sync();
        }
    }

    public void growRegion(float amount) {
        if (region != null) {
            setRegionSize(region.size() + amount);
        }
    }

    @Nullable
    public SanctuaryRegion getRegion() {
        return region;
    }

    public boolean hasRegion() {
        return region != null;
    }

    private void sync() {
        SanctuaryCComponents.PIRATE_COMPONENT.sync(world);
    }

    public boolean isEntityInRegion(Entity entity) {
        return region != null && region.contains(entity);
    }

    public boolean isPosInRegion(Vec3 pos) {
        return region != null && region.contains(pos);
    }


    @Override
    public void readData(ValueInput view) {
        this.region = null;
        view.child("Region").ifPresent(regionView -> {
            long pos = regionView.getLongOr("Pos", 0L);
            float size = regionView.getFloatOr("Size", 0);
            this.region = new SanctuaryRegion(BlockPos.of(pos), size);
        });
    }

    @Override
    public void writeData(ValueOutput view) {
        if (region != null) {
            ValueOutput regionView = view.child("Region");
            regionView.putLong("Pos", region.center().asLong());
            regionView.putFloat("Size", region.size());
        }
    }
}
