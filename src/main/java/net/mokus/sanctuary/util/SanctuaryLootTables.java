package net.mokus.sanctuary.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.mokus.sanctuary.item.SanctuaryItems;

public class SanctuaryLootTables {
    private static final Identifier TRIAL_REWARD =
            Identifier.withDefaultNamespace("chests/trial_chambers/reward");
    private static final Identifier TRIAL_OMINOUS =
            Identifier.withDefaultNamespace("chests/trial_chambers/reward_ominous");
    private static final Identifier TRIAL_UNIQUE =
            Identifier.withDefaultNamespace("chests/trial_chambers/reward_unique");

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            if (!key.identifier().equals(TRIAL_REWARD)) return;

            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(SanctuaryItems.STRANGIFIER)));
            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(SanctuaryItems.KILLSTREAK_KIT)));
        });
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            if (!key.identifier().equals(TRIAL_OMINOUS)) return;

            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.20f))
                    .add(LootItem.lootTableItem(SanctuaryItems.STRANGIFIER)));
            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.15f))
                    .add(LootItem.lootTableItem(SanctuaryItems.KILLSTREAK_KIT)));
        });
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;
            if (!key.identifier().equals(TRIAL_UNIQUE)) return;

            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.10f))
                    .add(LootItem.lootTableItem(SanctuaryItems.STRANGIFIER)));
            tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.10f))
                    .add(LootItem.lootTableItem(SanctuaryItems.KILLSTREAK_KIT)));
        });
    }
}
