package com.rpg.characters.abilitymanagement;

import java.util.List;

public record AbilityUseCheck(AbilityUseStatus status, List<String> messages) {
    public boolean ok() {
        return status == AbilityUseStatus.OK;
    }

    public static AbilityUseCheck abilityUseCheck() {
        return new AbilityUseCheck(AbilityUseStatus.OK, List.of());
    }

    public static AbilityUseCheck fail(AbilityUseStatus status, String msg) {
        return new AbilityUseCheck(status, List.of(msg));
    }
}