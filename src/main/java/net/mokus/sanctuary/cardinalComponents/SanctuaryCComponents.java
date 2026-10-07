package net.mokus.sanctuary.cardinalComponents;

import net.minecraft.resources.Identifier;
import net.mokus.sanctuary.Sanctuary;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class SanctuaryCComponents implements EntityComponentInitializer, WorldComponentInitializer {

    public static final ComponentKey<PrisonComponent> PRISON =
            ComponentRegistry.getOrCreate(
                    Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "prison"),
                    PrisonComponent.class
            );

    public static final ComponentKey<PirateComponent> PIRATE_COMPONENT =
            ComponentRegistry.getOrCreate(
                    Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "pirate_component"),
                    PirateComponent.class
            );

    public static final ComponentKey<SanctuaryPlayerComponent> SANCTUARY_PLAYER =
            ComponentRegistry.getOrCreate(
                    Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "sanctuary_player"),
                    SanctuaryPlayerComponent.class
            );

    public static final ComponentKey<KillstreakComponent> KILLSTREAK =
            ComponentRegistry.getOrCreate(
                    Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "killstreak"),
                    KillstreakComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SANCTUARY_PLAYER,SanctuaryPlayerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(KILLSTREAK,KillstreakComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(PIRATE_COMPONENT,PirateComponent::new);
        registry.register(PRISON,PrisonComponent::new);
    }
}
