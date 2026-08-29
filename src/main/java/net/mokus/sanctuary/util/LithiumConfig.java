package net.mokus.sanctuary.util;

import net.fabricmc.loader.api.FabricLoader;
import net.mokus.sanctuary.Sanctuary;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class LithiumConfig {

    //You may ask Why? Cause Lithium just straight up replaces the collision getters which this needs.
    //Also i don't trust people to not mess around, or forget to include their config... so here it is.
    //It should probably check if Lithium is even on but... ehhh

    public static void iDontTrustAnyOfYouSoIAddedThisSoNoMatterWhatMyModShouldWork() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("lithium.properties");

        try {
            if (!Files.exists(path)) {
                Files.writeString(path, "mixin.entity.collisions=false" + System.lineSeparator());
                return;
            }

            List<String> lines = Files.readAllLines(path);
            boolean alreadyPresent = lines.stream().map(String::trim).anyMatch(line -> line.startsWith("mixin.entity.collisions" + "="));

            if (!alreadyPresent) {
                Files.writeString(path, "mixin.entity.collisions=false" + System.lineSeparator(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            Sanctuary.LOGGER.error("Failed to write {}", "lithium.properties", e);
        }
    }
}