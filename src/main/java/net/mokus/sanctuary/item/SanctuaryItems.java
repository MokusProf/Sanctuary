package net.mokus.sanctuary.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.mokus.sanctuary.Sanctuary;
import net.mokus.sanctuary.item.strange.Strangifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SanctuaryItems {

//    public static final ResourceKey<CreativeModeTab> ATE_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, "ate_item_group"));
//    public static final CreativeModeTab ATE_ITEM = FabricItemGroup.builder()
//            .icon(() -> new ItemStack(SanctuaryItems.EMBLEM))
//            .title(Component.translatable("buildGroup.sanctuary_items"))
//            .build();

    public static final Item RITUAL_SWORD = registerItem("ritual_sword",
            settings -> new RitualSwordItem(settings.fireResistant().sword(ToolMaterial.DIAMOND, 4, -2.7F)), null);

    public static final Item EMBLEM = registerItem("emblem",
            settings -> new EmblemItem(settings.fireResistant().rarity(Rarity.EPIC).stacksTo(1)), null);

    public static final Item STRANGIFIER = registerItem("strangifier",
            settings -> new Strangifier(settings.fireResistant().rarity(Rarity.EPIC).stacksTo(1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)), CreativeModeTabs.COMBAT);

    public static final Item KILLSTREAK_KIT = registerItem("killstreak_kit",
            settings -> new KillstreakKit(settings.fireResistant().rarity(Rarity.EPIC).stacksTo(1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)), CreativeModeTabs.COMBAT);


    private static Item registerItem(String name, Function<Item.Properties, Item> factory, @Nullable ResourceKey<CreativeModeTab> group) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
        Item item = factory.apply(new Item.Properties().setId(key));
        Item registeredItem = Registry.register(BuiltInRegistries.ITEM, key, item);
        if (!(group == null)){
            ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.accept(registeredItem));
        }
        return registeredItem;
    }

    public static void init(){
//        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ATE_KEY, ATE_ITEM);
    }
}
