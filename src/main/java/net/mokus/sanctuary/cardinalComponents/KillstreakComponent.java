package net.mokus.sanctuary.cardinalComponents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;


public class KillstreakComponent implements Component, AutoSyncedComponent {
    private final Player player;

    private int kills = 0;

    public KillstreakComponent(Player player) {
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public int addKill() {
        kills++;
        SanctuaryCComponents.KILLSTREAK.sync(this.player);
        return kills;
    }

    public void reset() {
        kills = 0;
        SanctuaryCComponents.KILLSTREAK.sync(this.player);
    }

    @Override
    public void readData(ValueInput readView) {
        this.kills = readView.getIntOr("kills", 0);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("kills", this.kills);
    }
}
