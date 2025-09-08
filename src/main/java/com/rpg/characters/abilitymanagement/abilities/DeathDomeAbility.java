package com.rpg.characters.abilitymanagement.abilities;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.Ability;
import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.game.AttackBonusType;
import com.rpg.game.outcome.AbilityOutcome;
import java.util.List;

public class DeathDomeAbility implements Ability {
    @Override
    public AbilityOutcome execute(AbilityContext ctx) {
        Player p = ctx.player();
        Enemy e = ctx.target();

        var meta = AbilityRegistry.getAbilityMetadata(AbilityId.DEATH_DOME);
        int damage = p.calculateDamageDoneByPlayer(e.getDefense(), 25, AttackBonusType.ADDITIVE);
        p.consumeManaAndRemoveEnemyHealth(e, damage, 0);

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
                List.of("You cast " + meta.name() + "!", "You dealt " + damage + " damage.")
        );
    }
}
