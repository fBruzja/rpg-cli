package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class DegenAuraAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.DEGEN_AURA);

        // Disable target for next turn; no direct damage (consistent with description)
        boolean targetDefeated = e != null && e.getHealthPoints() <= 0;

        return new AbilityOutcome(
                meta.name(),
                0,
                0,
                meta.manaCost(),
                false,
                true,              // targetDisabled
                targetDefeated,
                false,
                1,
                p.getPlayerStats().getHealthPoints(),
                p.getPlayerStats().getManaPoints(),
                e != null ? e.getHealthPoints() : 0,
                List.of("You cast " + meta.name() + "!", "The enemy is disabled next turn.")
        );
    }
}
