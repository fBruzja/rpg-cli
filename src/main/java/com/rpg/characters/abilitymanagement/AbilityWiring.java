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
                Map.entry(AbilityId.POISONED_DAGGER, new PoisonedDaggerAbility()),
                Map.entry(AbilityId.FIREBALL, new FireballAbility()),
                // SLOT_2 (level 3 unlocks)
                Map.entry(AbilityId.HEALING_SALVE, new HealingSalveAbility()),
                Map.entry(AbilityId.CRITICAL_STRIKE, new CriticalStrikeAbility()),
                Map.entry(AbilityId.ELECTROCUTE, new ElectrocuteAbility()),
                // SLOT_3
                Map.entry(AbilityId.CHARGE, new ChargeAbility()),
                Map.entry(AbilityId.WITHER, new WitherAbility()),
                Map.entry(AbilityId.DEGEN_AURA, new DegenAuraAbility()),
                // SLOT_4
                Map.entry(AbilityId.BEST_DEFENSE, new BestDefenseAbility()),
                Map.entry(AbilityId.COUP_DE_GRACE, new CoupDeGraceAbility()),
                Map.entry(AbilityId.DEATH_DOME, new DeathDomeAbility())
        );
        return new AbilityExecutor(logic);
    }
}