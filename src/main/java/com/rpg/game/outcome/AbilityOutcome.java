package com.rpg.game.outcome;

import java.util.List;

public record AbilityOutcome(
        String abilityName,

        int damageDealt,
        int healingDone,
        int manaSpent,

        boolean preparedPoisonForNextAttack,
        boolean targetDisabled,
        boolean targetDefeated,
        boolean oncePerBattleConsumed,
        int cooldownTurnsApplied,

        int playerHpAfter,
        int playerMpAfter,
        int targetHpAfter,

        // Effects applied (buffs/debuffs) with magnitude and duration (maybe not needed at all)
//        List<StatusEffectInstance> effectsOnTarget,
//        List<StatusEffectInstance> effectsOnPlayer,

        List<String> messages
) {}
