package com.rpg.characters.abilitymanagement;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbilityState implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int currentCooldown; // in turns
    private int maxCharges; // -1 if not using charges
    private int currentCharges; // -1 if not using charges
    private boolean oncePerBattleUsed;

    public static AbilityState fresh() {
        return new AbilityState(0, -1, -1, false);
    }
}
