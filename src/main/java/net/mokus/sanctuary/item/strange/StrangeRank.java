package net.mokus.sanctuary.item.strange;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.mokus.sanctuary.util.SimpleText;

public enum StrangeRank {
    PVP_MASTER("PvP Master", Integer.MIN_VALUE, ChatFormatting.RED),
    KINDFUL("Kindful", -10, ChatFormatting.GREEN),
    HARMLESS("Harmless", -1, ChatFormatting.AQUA),
    STRANGE("Strange", 0, ChatFormatting.GOLD),
    IRRATIONAL("Irrational", 3, ChatFormatting.GRAY),
    BROKE("Broke Mf", 4, ChatFormatting.DARK_GRAY),
    GRANDDAD("Grand dad", 7, ChatFormatting.YELLOW),
    LORE_RELEVANT("Lore Relevant", 8, ChatFormatting.GREEN),
    UwU("UwU", 35, ChatFormatting.AQUA),
    SIXSEVEN("Grow Up", 67, ChatFormatting.DARK_GREEN),
    BLAHAJ("Blåhaj", 68, new int[]{0x5BCEFA,0xF5A9B8, 0xFFFFFF}),
    SERVER_CLEARING("Server-Clearing", 100, ChatFormatting.RED),
    APOSTATE("Apostate", 150, ChatFormatting.DARK_RED),
    PIRATES_OWN("Pirate's Treasure", 200, ChatFormatting.LIGHT_PURPLE),
    FORGOTTEN("Forgone", 250, new int[]{0xD60270,0x9B4F96, 0x0038A8}),
    ABYSS("Abyss", 300, ChatFormatting.BLUE),
    APOCALYPTIC("Apocalyptic", 500, ChatFormatting.DARK_BLUE),
    MILLENNIUM("Millennium", 1000, new int[]{0xFF3030, 0xFFD700, 0x30FF60, 0x30B0FF}, true),
    LITERALLY("Literally", 1984, new int[]{0x880808, 0x000000}),
    AURA_FARMER("Aura Farmer", 1985, new int[]{0x000000, 0x555555}),
    MURDER_HOBO("Murder Hobo", 2500, new int[]{0x880808, 0xDC143C});


    public final String title;
    public final int killCount;
    public final int color;
    public final int[] gradient;
    public final boolean firework;

    StrangeRank(String title, int killCount, ChatFormatting formatting) {
        this(title, killCount, formatting.getColor());
    }

    StrangeRank(String title, int killCount, int color) {
        this.title = title;
        this.killCount = killCount;
        this.color = color;
        this.gradient = null;
        this.firework = false;
    }

    StrangeRank(String title, int killCount, int[] gradient) {
        this.title = title;
        this.killCount = killCount;
        this.color = gradient[0];
        this.gradient = gradient;
        this.firework = false;
    }

    StrangeRank(String title, int killCount, int[] colors, boolean firework) {
        this.title = title;
        this.killCount = killCount;
        this.color = colors[0];
        this.gradient = colors;
        this.firework = firework;
    }

    public MutableComponent style(String text) {
        if (gradient != null && firework) {
            return SimpleText.createFireworkText(text, gradient);
        }
        if (gradient != null) {
            return SimpleText.createMovingGradient(text, gradient);
        }
        return Component.literal(text).withStyle(s -> s.withColor(color).withItalic(false));
    }

    public static StrangeRank forKills(int kills) {
        StrangeRank result = STRANGE;
        for (StrangeRank r : values()) {
            if (kills >= r.killCount) result = r;
        }
        return result;
    }
}
