package com.rpg.characters.abilitymanagement;

import com.rpg.characters.data.Profession;
import java.util.Map;

public class AbilityRegistry {

    private static final Map<AbilityId, AbilityMetadata> abilities = Map.ofEntries(
            Map.entry(AbilityId.POWER_ATTACK, new AbilityMetadata(
                    "Power Attack",
                    "A smashing attack",
                    0,
                    Profession.WARRIOR,
                    10,
                    CooldownPolicy.TURNS)),
            Map.entry(AbilityId.FIREBALL, new AbilityMetadata(
                    "Fireball",
                    "A ball of fire to hurl at your enemies",
                    0,
                    Profession.MAGE,
                    15,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.POISONED_DAGGER, new AbilityMetadata(
                    "Poisoned Dagger",
                    "Coats your dagger with poison",
                    0,
                    Profession.THIEF,
                    10,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.HEALING_SALVE, new AbilityMetadata(
                    "Healing Salve",
                    "Heals you for 50 HP.",
                    3,
                    Profession.WARRIOR,
                    15,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.CHARGE, new AbilityMetadata(
                    "Charge",
                    "Opponent gets disabled for the next turn.",
                    6,
                    Profession.WARRIOR,
                    20,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.BEST_DEFENSE, new AbilityMetadata(
                    "Best Defense",
                    "Your defense permanently increases by 5.",
                    10,
                    Profession.WARRIOR,
                    30,
                    CooldownPolicy.ONCE_PER_BATTLE
            )),
            Map.entry(AbilityId.CRITICAL_STRIKE, new AbilityMetadata(
                    "Critical Strike",
                    "Your next attack does triple damage.",
                    3,
                    Profession.THIEF,
                    30,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.WITHER, new AbilityMetadata(
                    "Wither",
                    "Opponent's defense is ignored.",
                    6,
                    Profession.THIEF,
                    30,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.COUP_DE_GRACE, new AbilityMetadata(
                    "Coup De Grace",
                    "Your attack brings your enemy near death.",
                    10,
                    Profession.THIEF,
                    40,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.ELECTROCUTE, new AbilityMetadata(
                    "Electrocute",
                    "Deals 10+ electric damage.",
                    3,
                    Profession.MAGE,
                    20,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.DEGEN_AURA, new AbilityMetadata(
                    "Degen Aura",
                    "Lowers enemy's defense by 3.",
                    6,
                    Profession.MAGE,
                    30,
                    CooldownPolicy.TURNS
            )),
            Map.entry(AbilityId.DEATH_DOME, new AbilityMetadata(
                    "Death Dome",
                    "Drains 25+ health as damage.",
                    10,
                    Profession.MAGE,
                    35,
                    CooldownPolicy.TURNS
            ))
            );

    public static AbilityMetadata getAbilityMetadata(AbilityId abilityId) {
        return abilities.get(abilityId);
    }
}
