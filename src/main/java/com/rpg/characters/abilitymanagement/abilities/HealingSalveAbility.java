package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class HealingSalveAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.HEALING_SALVE);
        int heal = 50;

        var stats = p.getPlayerStats();
        stats.setHealthPoints(stats.getHealthPoints() + heal);

        return new AbilityOutcome(
                meta.name(),
                0,
                heal,
                meta.manaCost(),
                false,
                false,
                e != null && e.getHealthPoints() <= 0,
                false,
                1,
                stats.getHealthPoints(),
                stats.getManaPoints(),
                e != null ? e.getHealthPoints() : 0,
                List.of("You use " + meta.name() + ".", "You are healed for " + heal + " HP.")
        );
    }
}