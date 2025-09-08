package com.rpg.characters.abilitymanagement;

import com.rpg.characters.data.Profession;

public final class StarterAbilities {
    private StarterAbilities() {}

    public static AbilityId starterFor(Profession profession) {
        return switch (profession) {
            case WARRIOR -> AbilityId.POWER_ATTACK;
            case THIEF -> AbilityId.POISONED_DAGGER;
            case MAGE -> AbilityId.FIREBALL;
        };
    }
}
