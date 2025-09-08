package com.rpg.characters.abilitymanagement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbilityState {
    private int currentCooldown; // in turns
    private int maxCharges; // -1 if not using charges
    private int currentCharges; // -1 if not using charges
    private boolean oncePerBattleUsed;

    public static AbilityState fresh() {
        return new AbilityState(0, -1, -1, false);
    }
}
