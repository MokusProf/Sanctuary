package net.mokus.sanctuary.events;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.mokus.sanctuary.cardinalComponents.KillstreakComponent;
import net.mokus.sanctuary.cardinalComponents.SanctuaryCComponents;
import net.mokus.sanctuary.item.strange.StrangeData;
import net.mokus.sanctuary.item.strange.StrangeType;
import net.mokus.sanctuary.util.SanctuaryDataComponents;

public class StrangeEvents {
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public static void announceRankUp(ServerPlayer player, ItemStack stack, StrangeData old, StrangeData updated) {
        if (updated.rank() == old.rank()) return;

        Component itemName = stack.has(DataComponents.CUSTOM_NAME)
                ? stack.get(DataComponents.CUSTOM_NAME)
                : Component.literal(old.name());

        player.level().getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(player.getName().getString() + "'s ")
                        .append(itemName)
                        .append(Component.literal(" has reached a new rank: "))
                        .append(Component.literal(updated.rank().title)
                                .withStyle(style -> style.withColor(updated.rank().color))),
                true);
    }

    public static void onDamageAbsorbed(ServerPlayer player, float absorbed) {
        int worn = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (!player.getItemBySlot(slot).isEmpty()) worn++;
        }
        if (worn == 0) return;

        float share = absorbed / worn;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            StrangeData data = stack.get(SanctuaryDataComponents.STRANGE_DATA);
            if (data == null || !StrangeType.of(stack).tracksDamage()) continue;

            StrangeData updated = data.withDamageAbsorbed(share);
            stack.set(SanctuaryDataComponents.STRANGE_DATA, updated);
            announceRankUp(player, stack, data, updated);
        }
    }

    private static boolean isStreakMilestone(int kills) {
        if (kills < 5) return false;
        return kills <= 20 ? kills % 5 == 0 : kills % 10 == 0;
    }

    //A lot of events
    public static void serverEvents() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((level, killer, killed, source) -> {
            if (!(killed instanceof ServerPlayer victim)) return;
            if (!(killer instanceof ServerPlayer player)) return;
            if (killer == killed) return;

            ItemStack weapon = player.getItemInHand(InteractionHand.MAIN_HAND);
            StrangeData data = weapon.get(SanctuaryDataComponents.STRANGE_DATA);

            if (data != null && StrangeType.of(weapon).tracksKills()) {
                StrangeData updated = data.withKill();
                weapon.set(SanctuaryDataComponents.STRANGE_DATA, updated);
                announceRankUp(player, weapon, data, updated);
            }

            ItemStack victimItem = victim.getItemInHand(InteractionHand.MAIN_HAND);
            StrangeData victimData = victimItem.get(SanctuaryDataComponents.STRANGE_DATA);
            if (victimData != null && StrangeType.of(victimItem).tracksKills()) {
                victimItem.set(SanctuaryDataComponents.STRANGE_DATA, victimData.withDeath());
            }

            Integer itemStreak = weapon.get(SanctuaryDataComponents.KILLSTREAK);
            if (itemStreak != null) {
                weapon.set(SanctuaryDataComponents.KILLSTREAK, itemStreak + 1);

                KillstreakComponent streakComponent = SanctuaryCComponents.KILLSTREAK.get(player);
                int total = streakComponent.addKill();

                if (isStreakMilestone(total)) {
                    level.getServer().getPlayerList().broadcastSystemMessage(
                            Component.literal(player.getName().getString() + " is on a ")
                                    .append(Component.literal(total + " killstreak")
                                            .withStyle(style -> style.withColor(0xFF5555)))
                                    .append(Component.literal("!")), true);
                }
            }
        });

        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (!(player instanceof ServerPlayer serverPlayer)) return;

            ItemStack tool = serverPlayer.getMainHandItem();
            StrangeData data = tool.get(SanctuaryDataComponents.STRANGE_DATA);
            if (data == null || !StrangeType.of(tool).tracksBlocks()) return;

            StrangeData updated = data.withBlockMined();
            tool.set(SanctuaryDataComponents.STRANGE_DATA, updated);
            announceRankUp(serverPlayer, tool, data, updated);
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) {
                SanctuaryCComponents.SANCTUARY_PLAYER.get(player).setPendingRawDamage(amount);
            }
            return true;
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            if (blocked) return;
            if (!(entity instanceof ServerPlayer player)) return;

            float damage = baseDamageTaken;

            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                float armor = (float) player.getArmorValue();
                float toughness = (float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

                float f = 2.0f + toughness / 4.0f;
                float g = Mth.clamp(armor - damage / f, armor * 0.2f, 20.0f);
                damage = damage * (1.0f - g / 25.0f);
            }

            if (!source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
                float protection = EnchantmentHelper.getDamageProtection(player.level(), player, source);
                damage = damage * (1.0f - Mth.clamp(protection, 0.0f, 20.0f) / 25.0f);
            }

            float absorbed = baseDamageTaken - damage;

            if (absorbed <= 0) return;
            onDamageAbsorbed(player, absorbed);
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) {
                KillstreakComponent streak = SanctuaryCComponents.KILLSTREAK.get(player);
                if (streak.getKills() <= 0) return true;

                player.level().getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal(player.getName().getString() + " died on a ")
                                .append(Component.literal(streak.getKills() + " killstreak")
                                        .withStyle(style -> style.withColor(0xFF5555)))
                                .append(Component.literal("!")), true);
                streak.reset();

                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (stack.has(SanctuaryDataComponents.KILLSTREAK)) {
                        stack.set(SanctuaryDataComponents.KILLSTREAK, 0);
                    }
                }
            }
            return true;
        });
    }

    public static void clientEvents() {
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            StrangeData data = stack.get(SanctuaryDataComponents.STRANGE_DATA);
            if (data == null || lines.isEmpty()) return;
            StrangeType type = StrangeType.of(stack);

            int i = 1;
            if (type.tracksKills())
                lines.add(i++, Component.literal("Player Kills: " + data.killCount()).withStyle(ChatFormatting.GRAY));
            if (type.tracksBlocks())
                lines.add(i++, Component.literal("Blocks Mined: " + data.blocksMined()).withStyle(ChatFormatting.GRAY));
            if (type.tracksDamage())
                lines.add(i++, Component.literal("Damage Absorbed: " + (int) data.damageAbsorbed()).withStyle(ChatFormatting.GRAY));
        });
    }
}
