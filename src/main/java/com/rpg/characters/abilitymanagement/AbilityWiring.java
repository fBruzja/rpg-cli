package com.rpg.characters.abilitymanagement;

import com.rpg.characters.abilitymanagement.abilities.BestDefenseAbility;
import com.rpg.characters.abilitymanagement.abilities.ChargeAbility;
import com.rpg.characters.abilitymanagement.abilities.CoupDeGraceAbility;
import com.rpg.characters.abilitymanagement.abilities.CriticalStrikeAbility;
import com.rpg.characters.abilitymanagement.abilities.DeathDomeAbility;
import com.rpg.characters.abilitymanagement.abilities.DegenAuraAbility;
import com.rpg.characters.abilitymanagement.abilities.ElectrocuteAbility;
import com.rpg.characters.abilitymanagement.abilities.FireballAbility;
import com.rpg.characters.abilitymanagement.abilities.HealingSalveAbility;
import com.rpg.characters.abilitymanagement.abilities.PoisonedDaggerAbility;
import com.rpg.characters.abilitymanagement.abilities.PowerAttackAbility;
import com.rpg.characters.abilitymanagement.abilities.WitherAbility;
import java.util.Map;

public final class AbilityWiring {
    private AbilityWiring() {}

    public static AbilityExecutor createExecutor() {
        Map<AbilityId, Ability> logic = Map.ofEntries(
                // SLOT_1 starters
                Map.entry(AbilityId.POWER_ATTACK, new PowerAttackAbility()),
                AbilityId.POISONED_DAGGER, new PoisonedDaggerAbility(),
                AbilityId.FIREBALL, new FireballAbility(),
                // SLOT_2 (level 3 unlocks)
                AbilityId.HEALING_SALVE, new HealingSalveAbility(),
                AbilityId.CRITICAL_STRIKE, new CriticalStrikeAbility(),
                AbilityId.ELECTROCUTE, new ElectrocuteAbility(),
                // SLOT_3
                AbilityId.CHARGE, new ChargeAbility(),
                AbilityId.WITHER, new WitherAbility(),
                AbilityId.DEGEN_AURA, new DegenAuraAbility(),
                // SLOT_4
                AbilityId.BEST_DEFENSE, new BestDefenseAbility(),
                AbilityId.COUP_DE_GRACE, new CoupDeGraceAbility(),
                AbilityId.DEATH_DOME, new DeathDomeAbility()
        );
        return new AbilityExecutor(logic);
    }
}