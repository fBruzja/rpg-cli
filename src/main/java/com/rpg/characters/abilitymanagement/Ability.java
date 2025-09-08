package com.rpg.characters.abilitymanagement;

import com.rpg.game.outcome.AbilityOutcome;

public interface Ability {
    AbilityOutcome execute(AbilityContext ctx);
}

