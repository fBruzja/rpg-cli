package com.rpg.game.outcome;

import java.util.List;

public record BattleResult(boolean playerDied, boolean enemyDied, int expGained, List<String> messages) {

}
