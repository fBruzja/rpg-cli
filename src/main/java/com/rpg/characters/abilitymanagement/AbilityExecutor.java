package com.rpg.characters.abilitymanagement;

import com.rpg.game.outcome.AbilityOutcome;
import java.util.Map;

public class AbilityExecutor {

    //TODO: Inject or build this map elsewhere when you add concrete abilities (maybe in the registry?)
    private final Map<AbilityId, Ability> logicById;

    public AbilityExecutor(Map<AbilityId, Ability> logicById) {
        this.logicById = logicById;
    }

    public AbilityUseCheck canUse(AbilityId id, int availableMana, PlayerAbilities pa) {
        return pa.canUse(id, availableMana);
    }

    public AbilityOutcome execute(AbilityId id, AbilityContext ctx, PlayerAbilities pa) {
        var meta = AbilityRegistry.getAbilityMetadata(id);
        var check = pa.canUse(id, ctx.player().getPlayerStats().getManaPoints());
        if (!check.ok()) {
            return new AbilityOutcome(
                    meta.name(),
                    0, 0, 0,
                    false, false, false, false, 0,
                    ctx.player().getPlayerStats().getHealthPoints(),
                    ctx.player().getPlayerStats().getManaPoints(),
                    ctx.target() != null ? ctx.target().getHealthPoints() : 0,
                    check.messages()
            );
        }

        var outcome = logicById.get(id).execute(ctx);

        // Spend mana (domain), then apply cooldown/charges flags through PlayerAbilities
        ctx.player().getPlayerStats().setManaPoints(
                ctx.player().getPlayerStats().getManaPoints() - outcome.manaSpent()
        );
        pa.onUse(id, outcome.cooldownTurnsApplied());

        return outcome;
    }
}