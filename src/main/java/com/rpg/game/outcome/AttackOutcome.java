package com.rpg.game.outcome;

import java.util.List;

public record AttackOutcome(int damageDealt, boolean poisonConsumed, boolean targetDefeated, List<String> messages) {

}
