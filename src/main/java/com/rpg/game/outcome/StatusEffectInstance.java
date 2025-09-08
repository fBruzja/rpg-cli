// Java
package com.rpg.game.outcome;

public record StatusEffectInstance(
        StatusEffectType type,
        int magnitude,       // e.g., +10 defense, 25 shield HP, 3 extra damage per tick
        int durationTurns    // remaining turns; 0 means instant/consumed this turn
) {}