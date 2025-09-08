package com.rpg.characters.abilitymanagement;

import com.rpg.characters.data.Profession;

public record AbilityMetadata(
        String name,
        String description,
        int unlockLevel,
        Profession profession,
        int manaCost,
        CooldownPolicy cooldownPolicy
) {

}
