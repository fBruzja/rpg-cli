package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class BestDefenseAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.BEST_DEFENSE);

        // Permanently increase player's defense by 5
        var stats = p.getPlayerStats();
        stats.setDefense(stats.getDefense() + 5);

        return new AbilityOutcome(
                meta.name(),
                0,
                0,
                meta.manaCost(),
                false,
                false,
                e != null && e.getHealthPoints() <= 0,
                true,
                0,
                stats.getHealthPoints(),
                stats.getManaPoints(),
                e != null ? e.getHealthPoints() : 0,
                List.of("You use " + meta.name() + "!", "Your defense is permanently increased by 5.")
        );
    }
}