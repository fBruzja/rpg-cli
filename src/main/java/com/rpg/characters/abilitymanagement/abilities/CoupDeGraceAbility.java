package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class CoupDeGraceAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.COUP_DE_GRACE);

        // Bring enemy near death: reduce to 5 HP if above 5
        int before = e.getHealthPoints();
        int targetHp = Math.max(5, before);
        int damage = before - targetHp;
        if (damage > 0) {
            p.consumeManaAndRemoveEnemyHealth(e, damage, 0);
        }

        boolean dead = e.getHealthPoints() <= 0;

        return new AbilityOutcome(
                meta.name(),
                damage,
                0,
                meta.manaCost(),
                false,
                false,
                dead,
                false,
                1,
                p.getPlayerStats().getHealthPoints(),
                p.getPlayerStats().getManaPoints(),
                e.getHealthPoints(),
                List.of("You use " + meta.name() + ".", "The enemy is brought to the brink of death.")
        );
    }
}