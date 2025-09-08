package com.rpg.characters.abilitymanagement;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PlayerAbilities {

    private final Set<AbilityId> learned = EnumSet.noneOf(AbilityId.class);
    private final Map<AbilitySlot, AbilityId> equipped = new EnumMap<>(AbilitySlot.class);
    private final Map<AbilityId, AbilityState> state = new EnumMap<>(AbilityId.class);

    public void learn(AbilityId id) {
        if (learned.add(id)) {
            state.putIfAbsent(id, AbilityState.fresh());
        }
    }

    public boolean isLearned(AbilityId id) {
        return learned.contains(id);
    }

    public Set<AbilityId> learned() {
        return Set.copyOf(learned);
    }

    public Optional<AbilityId> getEquipped(AbilitySlot slot) {
        return Optional.ofNullable(equipped.get(slot));
    }

    public Map<AbilitySlot, AbilityId> equippedView() {
        return Map.copyOf(equipped);
    }

    public void equip(AbilitySlot slot, AbilityId id) {
        if (!isLearned(id)) {
            throw new IllegalStateException("Ability not learned: " + id);
        }
        equipped.put(slot, id);
    }

    public void unequip(AbilitySlot slot) {
        equipped.remove(slot);
    }

    public AbilityState stateOf(AbilityId id) {
        return state.computeIfAbsent(id, k -> AbilityState.fresh());
    }

    public AbilityUseCheck canUse(AbilityId id, int availableMana) {
        if (!isLearned(id)) {
            return AbilityUseCheck.fail(AbilityUseStatus.NOT_LEARNED, "Ability not learned.");
        }
        if (!equipped.containsValue(id)) {
            return AbilityUseCheck.fail(AbilityUseStatus.NOT_EQUIPPED, "Ability not equipped.");
        }
        var meta = AbilityRegistry.getAbilityMetadata(id);
        var st = stateOf(id);

        switch (meta.cooldownPolicy()) {
            case TURNS -> {
                if (st.getCurrentCooldown() > 0) {
                    return AbilityUseCheck.fail(AbilityUseStatus.ON_COOLDOWN, "Ability is on cooldown.");
                }
            }
            case CHARGES -> {
                if (st.getCurrentCharges() == 0) {
                    return AbilityUseCheck.fail(AbilityUseStatus.NO_CHARGES, "No charges left.");
                }
            }
            case ONCE_PER_BATTLE -> {
                if (st.isOncePerBattleUsed()) {
                    return AbilityUseCheck.fail(AbilityUseStatus.ONCE_PER_BATTLE_ALREADY_USED, "Can be used only once per battle.");
                }
            }
        }

        if (availableMana < meta.manaCost()) {
            return AbilityUseCheck.fail(AbilityUseStatus.NOT_ENOUGH_MANA, "Not enough mana.");
        }

        return AbilityUseCheck.abilityUseCheck();
    }

    public void onUse(AbilityId id, int appliedCooldownIfAny) {
        var meta = AbilityRegistry.getAbilityMetadata(id);
        var abilityState = stateOf(id);

        switch (meta.cooldownPolicy()) {
            case TURNS -> abilityState.setCurrentCooldown(Math.max(abilityState.getCurrentCooldown(), appliedCooldownIfAny));
            case CHARGES -> {
                if (abilityState.getCurrentCharges() > 0) {
                    abilityState.setCurrentCharges(abilityState.getCurrentCharges() - 1);
                }
            }
            case ONCE_PER_BATTLE -> abilityState.setOncePerBattleUsed(true);
        }
    }

    public void tickTurnEnd() {
        state.values().forEach(s -> {
            if (s.getCurrentCooldown() > 0) {
                s.setCurrentCooldown(s.getCurrentCooldown() - 1);
            }
        });
    }

}