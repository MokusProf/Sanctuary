package net.mokus.sanctuary.util;


import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class SimpleText {

    /**
     * @Author vred
     */

    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?s)%0x([A-Fa-f0-9]{6})(.*?)(?=%0x|$)");

    public SimpleText() {
    }

    public static Component colorString(int color, String text) {
        return Component.literal(text).withStyle(Style.EMPTY.withColor(color));
    }

    public static int getEffectColor(ItemStack stack) {
        Integer nameColor = extractColor(stack.getHoverName());
        if (nameColor != null) return nameColor;

        Integer rarityColor = stack.getRarity().color().getColor();
        return rarityColor != null ? rarityColor : 0xFFFFFF;
    }

    private static Integer extractColor(Component text) {
        TextColor color = text.getStyle().getColor();
        if (color != null) return color.getValue();
        for (Component sibling : text.getSiblings()) {
            Integer nested = extractColor(sibling);
            if (nested != null) return nested;
        }
        return null;
    }

    public static int getMovingColor(int... colors) {
        if (colors == null || colors.length == 0) return 0xFFFFFF;
        if (colors.length == 1) return colors[0];

        long time = System.currentTimeMillis();
        float phase = (time % 4000L) / 4000.0f;

        return getLoopingMultiColorValue(colors, phase);
    }

    public static MutableComponent parseTranslatableMovingGradient(String translationKey, int colorStart, int colorEnd, Object... args) {
        String translatedText = Component.translatable(translationKey, args).getString();
        translatedText = replacePlaceholders(translatedText, args);
        return createMovingGradient(translatedText, colorStart, colorEnd);
    }

    public static final int[] RAINBOW = {0xFF0000, 0xFF7F00, 0xFFFF00, 0x00FF00, 0x0000FF, 0x4B0082, 0x9400D3};

    public static MutableComponent createMovingGradient(String text, int... colors) {
        MutableComponent result = Component.empty();
        int length = text.length();
        if (length == 0) return result;

        if (colors == null || colors.length == 0) return Component.literal(text);
        if (colors.length == 1) return Component.literal(text).withStyle(s -> s.withColor(colors[0]).withItalic(false));

        long time = System.currentTimeMillis();
        float phase = (time % 2000L) / 2000.0f;

        for (int i = 0; i < length; ++i) {
            float charOffset = (float) i / (float) length;

            float progress = (float) (Math.sin((phase + charOffset) * Math.PI * 2) * 0.5 + 0.5);

            int currentColor = getMultiColorValue(colors, progress);

            MutableComponent part = Component.literal(String.valueOf(text.charAt(i)))
                    .withStyle(style -> style.withColor(currentColor).withItalic(false));
            result.append(part);
        }

        return result;
    }

    private static int getMultiColorValue(int[] colors, float progress) {
        progress = Math.clamp(progress, 0.0f, 1.0f);

        int segments = colors.length - 1;
        float scaledProgress = progress * segments;
        int index = (int) Math.floor(scaledProgress);

        if (index >= segments) {
            return colors[colors.length - 1];
        }

        float segmentProgress = scaledProgress - index;

        return interpolateColor(colors[index], colors[index + 1], segmentProgress);
    }

    public static MutableComponent parseTranslatableWithColors(String translationKey, Object... args) {
        String translatedText = Language.getInstance().getOrDefault(translationKey);

        translatedText = replacePlaceholders(translatedText, args);

        return parseAndApplyColors(translatedText);
    }

    public static String replacePlaceholders(String text, Object... args) {
        for (Object arg : args) {
            if (text.contains("%s")) {
                text = text.replaceFirst("%s", Matcher.quoteReplacement(arg.toString()));
            }
        }
        return text;
    }

    public static MutableComponent parseTranslatableWithColors(String translationKey) {
        return parseTranslatableWithColors(translationKey, TranslatableContents.NO_ARGS);
    }

    public static MutableComponent createUniformMovingGradient(String text, int... colors) {
        if (text == null || text.isEmpty()) return Component.empty();
        if (colors == null || colors.length == 0) return Component.literal(text);
        if (colors.length == 1) return Component.literal(text).withStyle(s -> s.withColor(colors[0]).withItalic(false));

        long time = System.currentTimeMillis();
        float phase = (time % 4000L) / 4000.0f;

        int currentColor = getLoopingMultiColorValue(colors, phase);

        return Component.literal(text).withStyle(style -> style.withColor(currentColor).withItalic(false));
    }

    private static int getLoopingMultiColorValue(int[] colors, float progress) {
        progress = Math.max(0.0f, Math.min(progress, 1.0f));

        int segments = colors.length;
        float scaledProgress = progress * segments;
        int index = (int) Math.floor(scaledProgress);

        if (index >= segments) {
            index = segments - 1;
        }

        int nextIndex = (index + 1) % colors.length;

        float segmentProgress = scaledProgress - index;

        return interpolateColor(colors[index], colors[nextIndex], segmentProgress);
    }

    public static MutableComponent parseAndApplyColors(String input) {
        Matcher matcher = COLOR_CODE_PATTERN.matcher(input);
        MutableComponent resultText = Component.literal("");

        int lastEnd;
        for (lastEnd = 0; matcher.find(); lastEnd = matcher.end()) {
            if (matcher.start() > lastEnd) {
                resultText.append(Component.literal(input.substring(lastEnd, matcher.start())));
            }

            String hexColor = matcher.group(1);
            String text = matcher.group(2);
            int color = Integer.parseInt(hexColor, 16);
            MutableComponent coloredText = Component.literal(text).withStyle(style -> style.withColor(color));
            resultText.append(coloredText);
        }

        if (lastEnd < input.length()) {
            resultText.append(Component.literal(input.substring(lastEnd)));
        }

        return resultText;
    }

    public static MutableComponent createGradientText(String text, int colorStart, int colorEnd) {
        MutableComponent result = Component.empty();
        int length = text.length();

        for (int i = 0; i < length; ++i) {
            float progress = (float) i / (float) (length - 1);
            int currentColor = interpolateColor(colorStart, colorEnd, progress);
            MutableComponent part = Component.literal(String.valueOf(text.charAt(i))).withStyle(style -> style.withColor(currentColor));
            result.append(part);
        }

        return result;
    }

    public static MutableComponent createGradientTranslatable(String translationKey, int colorStart, int colorEnd, Object... args) {
        MutableComponent result = Component.empty();
        String translatedText = Component.translatable(translationKey, args).getString();
        translatedText = replacePlaceholders(translatedText, args);
        int length = translatedText.length();

        for (int i = 0; i < length; ++i) {
            float progress = (float) i / (float) (length - 1);
            int currentColor = interpolateColor(colorStart, colorEnd, progress);
            MutableComponent part = Component.literal(String.valueOf(translatedText.charAt(i))).withStyle(style -> style.withColor(currentColor));
            result.append(part);
        }

        return result;
    }

    private static int interpolateColor(int colorStart, int colorEnd, float progress) {
        int r1 = colorStart >> 16 & 255;
        int g1 = colorStart >> 8 & 255;
        int b1 = colorStart & 255;

        int r2 = colorEnd >> 16 & 255;
        int g2 = colorEnd >> 8 & 255;
        int b2 = colorEnd & 255;

        int r = (int) ((float) r1 + (float) (r2 - r1) * progress);
        int g = (int) ((float) g1 + (float) (g2 - g1) * progress);
        int b = (int) ((float) b1 + (float) (b2 - b1) * progress);
        return (r << 16) + (g << 8) + b;
    }

    /**
     * Not made by vred code
     */
    public static MutableComponent createFireworkText(String text, int... colors) {
        if (text == null || text.isEmpty()) return Component.empty();
        if (colors == null || colors.length == 0) return Component.literal(text);

        final long cycleMs = 1600L;
        long time = System.currentTimeMillis();
        long cycle = time / cycleMs;
        float t = (time % cycleMs) / (float) cycleMs;

        int burstColor = colors[(int) (cycle % colors.length)];
        int dimColor = interpolateColor(burstColor, 0x000000, 0.8f);

        MutableComponent result = Component.empty();

        for (int i = 0; i < text.length(); i++) {
            int h = (i * 73856093) ^ ((int) cycle * 19349663);
            h ^= h >>> 13;
            h *= 0x5bd1e995;
            h ^= h >>> 15;
            float random = (h & 0xFFFF) / 65535f;

            float delay = random * 0.3f;
            int color;

            if (t < delay) {
                color = dimColor;
            } else {
                float p = (t - delay) / (1.0f - delay);
                if (p < 0.12f) {
                    color = interpolateColor(dimColor, 0xFFFFFF, p / 0.12f);
                } else if (p < 0.3f) {
                    color = interpolateColor(0xFFFFFF, burstColor, (p - 0.12f) / 0.18f);
                } else {
                    color = interpolateColor(burstColor, dimColor, (p - 0.3f) / 0.7f);
                }
            }

            final int c = color;
            result.append(Component.literal(String.valueOf(text.charAt(i)))
                    .withStyle(style -> style.withColor(c).withItalic(false)));
        }

        return result;
    }
}