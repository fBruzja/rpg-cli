package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class PoisonedDaggerAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.POISONED_DAGGER);

        return new AbilityOutcome(
                meta.name(),
                0,
                0,
                meta.manaCost(),
                true,
                false,
                false,
                false,
                1,
                p.getPlayerStats().getHealthPoints(),
                p.getPlayerStats().getManaPoints(),
                e != null ? e.getHealthPoints() : 0,
                List.of("You use " + meta.name() + ".", "Your next attack will deal extra damage.")
        );
    }
}

