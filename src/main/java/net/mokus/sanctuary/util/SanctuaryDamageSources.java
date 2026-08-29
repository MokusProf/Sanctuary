package net.mokus.sanctuary.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.mokus.sanctuary.Sanctuary;

public class SanctuaryDamageSources {

    public static final ResourceKey<DamageType> SANCTUARY_DAMAGE = registerDamage("sanctuary_damage");

    public static ResourceKey<DamageType> registerDamage(String name){
        return  ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Sanctuary.MOD_ID, name));
    }

    public static void init(){}
}
