package com.rpg.characters.abilitymanagement;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;

public record AbilityContext(
        Player player,
        Enemy target
) {}

