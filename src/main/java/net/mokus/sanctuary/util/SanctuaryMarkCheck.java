package net.mokus.sanctuary.util;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SanctuaryMarkCheck {
    private static final UUID MOKUS = UUID.fromString("c1115be4-d7e8-4979-b1bd-1a6820c736da");
    private static final UUID ASHER = UUID.fromString("298f9f6a-f27a-449e-b8c2-9f0443e956cd");
    private static final UUID PICKLE = UUID.fromString("83811c25-f1f3-4c0c-87dd-1733b501e880");
    private static final UUID DEMENTIA = UUID.fromString("be7d5d9e-eee1-4602-9702-aa15504859ff");

    public static boolean isPirateUUID(Player player){
        return player.getUUID().equals(MOKUS) || player.getUUID().equals(ASHER) || player.getUUID().equals(PICKLE) || player.getUUID().equals(DEMENTIA);
    }
}
