package com.rpg.characters;

import com.rpg.characters.abilitymanagement.AbilityContext;
import com.rpg.characters.abilitymanagement.AbilityExecutor;
import com.rpg.characters.abilitymanagement.AbilityId;
import com.rpg.characters.abilitymanagement.AbilitySlot;
import com.rpg.characters.abilitymanagement.AbilityWiring;
import com.rpg.characters.abilitymanagement.PlayerAbilities;
import com.rpg.characters.abilitymanagement.StarterAbilities;
import com.rpg.characters.data.PersonalPlayerInformation;
import com.rpg.characters.data.Position;
import com.rpg.characters.data.Profession;
import com.rpg.characters.data.Stats;
import com.rpg.game.AttackBonusType;
import com.rpg.game.outcome.AbilityOutcome;
import com.rpg.game.outcome.AttackOutcome;
import com.rpg.map.Coordinates;
import com.rpg.userinterface.UserInterface;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    // TODO: move them somewhere else later
    public static final int STARTING_MAIN_ATTRIBUTE_VALUE = 10;
    public static final int STAT_VALUE_MULTIPLIER_VALUE = 5;
    public static final int STAT_VALUE_DIVIDER_VALUE = 2;
    public static final int MAX_LEVEL = 10;
    public static final int STARTING_X_COORDINATE = 28;
    public static final int STARTING_Y_COORDINATE = 15;

    /* All stats are based upon the basic attributes */

    // Player information
    int level = 1;
    Position playerPosition;
    Stats playerStats;
    PersonalPlayerInformation playerInformation;

    public static char PLAYER_SYMBOL = '@';

    // Abilities facade
    private final PlayerAbilities playerAbilities = new PlayerAbilities();

    boolean warriorUsedLastSkill = false;

    public Player(String newName, char newProfession) {
        this.playerStats = Stats.builder()
                .strength(STARTING_MAIN_ATTRIBUTE_VALUE)
                .agility(STARTING_MAIN_ATTRIBUTE_VALUE)
                .intelligence(STARTING_MAIN_ATTRIBUTE_VALUE)
                .healthPoints(STARTING_MAIN_ATTRIBUTE_VALUE * STAT_VALUE_MULTIPLIER_VALUE)
                .manaPoints(STARTING_MAIN_ATTRIBUTE_VALUE * STAT_VALUE_MULTIPLIER_VALUE)
                .attackPoints(STARTING_MAIN_ATTRIBUTE_VALUE / STAT_VALUE_DIVIDER_VALUE)
                .defense(STARTING_MAIN_ATTRIBUTE_VALUE / STAT_VALUE_DIVIDER_VALUE)
                .exp(0)
                .build();
        this.playerPosition = new Position(STARTING_X_COORDINATE, STARTING_Y_COORDINATE);

        //TODO: Profession selection needs a revamp as well! (maybe?)
        Profession selectedProfession = Profession.WARRIOR;

        if (newProfession == 't' || newProfession == 'T') {
            selectedProfession = Profession.THIEF;
        } else if (newProfession == 'm' || newProfession == 'M') {
            selectedProfession = Profession.MAGE;
        }

        AbilityId starter = StarterAbilities.starterFor(selectedProfession);
        playerAbilities.learn(starter);
        playerAbilities.equip(AbilitySlot.SLOT_1, starter);

        this.playerInformation = new PersonalPlayerInformation(newName, selectedProfession);
    }

    public void move(Coordinates target) {
        playerPosition.setX(target.x());
        playerPosition.setY(target.y());
    }

    public AttackOutcome physicalAttack(Enemy defender, boolean poison) {
        int bonusFactor = poison ? 1 : 0;
        int damageDealt = calculateDamageDoneByPlayer(defender.getDefense(), bonusFactor, AttackBonusType.ADDITIVE);
        consumeManaAndRemoveEnemyHealth(defender, damageDealt, 0);

        String attackMessage = "You attack" + (poison ? " with your poisoned blade!" : "!\n");
        String outcomeMessage = "You made: " + damageDealt + " damage!\n";

        boolean defenderIsDead = defender.getHealthPoints() <= 0;

        return new AttackOutcome(damageDealt, poison, defenderIsDead, List.of(attackMessage, outcomeMessage));
    }

    public AbilityOutcome useFirstSlotAbility(Enemy e) {
        return useAbilityInSlot(AbilitySlot.SLOT_1, e);
    }

    public AbilityOutcome useSecondSlotAbility(Enemy e) {
        return useAbilityInSlot(AbilitySlot.SLOT_2, e);
    }

    public AbilityOutcome useThirdSlotAbility(Enemy e) {
        return useAbilityInSlot(AbilitySlot.SLOT_3, e);
    }

    public AbilityOutcome useFourthSlotAbility(Enemy e) {
        return useAbilityInSlot(AbilitySlot.SLOT_4, e);
    }

    private AbilityId defaultAbilityFor(AbilitySlot slot) {
        return switch (playerInformation.profession()) {
            case WARRIOR -> switch (slot) {
                case SLOT_1 -> StarterAbilities.starterFor(playerInformation.profession());
                case SLOT_2 -> AbilityId.HEALING_SALVE;
                case SLOT_3 -> AbilityId.CHARGE;
                case SLOT_4 -> AbilityId.BEST_DEFENSE;
            };
            case THIEF -> switch (slot) {
                case SLOT_1 -> StarterAbilities.starterFor(playerInformation.profession());
                case SLOT_2 -> AbilityId.CRITICAL_STRIKE;
                case SLOT_3 -> AbilityId.WITHER;
                case SLOT_4 -> AbilityId.COUP_DE_GRACE;
            };
            case MAGE -> switch (slot) {
                case SLOT_1 -> StarterAbilities.starterFor(playerInformation.profession());
                case SLOT_2 -> AbilityId.ELECTROCUTE;
                case SLOT_3 -> AbilityId.DEGEN_AURA;
                case SLOT_4 -> AbilityId.DEATH_DOME;
            };
        };
    }

    private AbilityOutcome useAbilityInSlot(AbilitySlot slot, Enemy target) {
        var equipped = playerAbilities.getEquipped(slot);
        AbilityId toUse = equipped.orElseGet(() -> defaultAbilityFor(slot));
        AbilityExecutor executor = AbilityWiring.createExecutor();

        var ctx = new AbilityContext(this, target);
        return executor.execute(toUse, ctx, playerAbilities);
    }


    public int calculateDamageDoneByPlayer(int enemyDefense, int bonusFactor, AttackBonusType bonusType) {
        if(bonusFactor == 0 || bonusType == AttackBonusType.NOT_APPLICABLE ) {
            return Math.max(0, playerStats.getAttackPoints());
        }

        int adjustedAttack = bonusType == AttackBonusType.ADDITIVE
                ? playerStats.getAttackPoints() + bonusFactor
                : playerStats.getAttackPoints() * bonusFactor;

        int damage = adjustedAttack - (enemyDefense / 2);
        return Math.max(0, damage);
    }

    public void consumeManaAndRemoveEnemyHealth(Enemy e, int damage, int manaToRemove) {
        e.reduceHealth(damage);
        playerStats.setManaPoints(playerStats.getManaPoints() - manaToRemove);
    }

    public void refreshSkills() {
        playerStats.setAttackPoints(playerStats.getStrength() / 2);
        playerStats.setManaPoints(playerStats.getIntelligence() * 5);
        playerStats.setHealthPoints(playerStats.getStrength() * 5);
        playerStats.setDefense(playerStats.getAgility() / 2);
    }

    public void levelUp() {
        setLevel(getLevel() + 1);
        playerStats.setExp(0);

        var playerProfession = playerInformation.profession();

        playerStats.setStrength(playerStats.getStrength() + playerProfession.getStrengthBonus());
        playerStats.setAgility(playerStats.getAgility() + playerProfession.getAgilityBonus());
        playerStats.setIntelligence(playerStats.getIntelligence() + playerProfession.getIntelligenceBonus());

        refreshSkills();

        var learnMessages = learnAndAutoEquipNewAbilities(playerProfession, level);
        UserInterface.renderMessages(learnMessages);
    }

    private List<String> learnAndAutoEquipNewAbilities(Profession profession, int level) {
        List<String> messages = new ArrayList<>();
        List<AbilityId> unlocks = unlocksFor(profession, level);

        if (unlocks.isEmpty()) {
            return messages;
        }

        for (AbilityId id : unlocks) {
            // Learn
            if (!playerAbilities.isLearned(id)) {
                playerAbilities.learn(id);
                messages.add("You have learned a new ability: " + id.name().replace('_', ' '));
            }

            // Auto-equip into first empty slot (SLOT_2..SLOT_4; SLOT_1 is starter)
            AbilitySlot free = firstEmptySlot();
            if (free != null) {
                try {
                    playerAbilities.equip(free, id);
                    messages.add("Auto-equipped " + id.name().replace('_', ' ') + " to " + free.name());
                } catch (IllegalStateException ignored) {
                    // Already equipped or not learned; skip quietly
                }
            }
        }
        return messages;
    }

    private AbilitySlot firstEmptySlot() {
        for (AbilitySlot s : new AbilitySlot[]{AbilitySlot.SLOT_2, AbilitySlot.SLOT_3, AbilitySlot.SLOT_4}) {
            if (playerAbilities.getEquipped(s).isEmpty()) {
                return s;
            }
        }
        return null;
    }

    // Map profession+level to ability unlocks (IDs)
    private List<AbilityId> unlocksFor(Profession profession, int level) {
        switch (profession) {
            case WARRIOR -> {
                return switch (level) {
                    case 3 -> List.of(AbilityId.HEALING_SALVE);
                    case 6 -> List.of(AbilityId.CHARGE);
                    case 10 -> List.of(AbilityId.BEST_DEFENSE);
                    default -> List.of();
                };
            }
            case THIEF -> {
                return switch (level) {
                    case 3 -> List.of(AbilityId.CRITICAL_STRIKE);
                    case 6 -> List.of(AbilityId.WITHER);
                    case 10 -> List.of(AbilityId.COUP_DE_GRACE);
                    default -> List.of();
                };
            }
            case MAGE -> {
                return switch (level) {
                    case 3 -> List.of(AbilityId.ELECTROCUTE);
                    case 6 -> List.of(AbilityId.DEGEN_AURA);
                    case 10 -> List.of(AbilityId.DEATH_DOME);
                    default -> List.of();
                };
            }
            default -> {
                return List.of();
            }
        }
    }

    public void getBattleRewards(int exp) {
        playerStats.setExp(playerStats.getExp() + exp);
        if (playerStats.getExp() >= 50) {
            if (level == Player.MAX_LEVEL) {
                UserInterface.renderMessage("You have achieved the maximum level of your abilities!");
            } else {
                UserInterface.renderMessage("You have leveled up! Your basic attributes have grown stronger!");
                levelUp();
            }
        }
    }

    public PlayerAbilities abilitiesFacade() {
        return playerAbilities;
    }

    public static Player createNewPlayer() {
        String name = UserInterface.readPlayerName();
        char profession = UserInterface.readPlayerProfession();

        return new Player(name, profession);
    }

    public void setPlayerAbilities(PlayerAbilities abilities) {
        // Learn all learned abilities
        for (var abilityId : abilities.learned()) {
            this.playerAbilities.learn(abilityId);
        }

        // Re-equip all equipped abilities
        for (var entry : abilities.equippedView().entrySet()) {
            this.playerAbilities.equip(entry.getKey(), entry.getValue());
        }

        // Copy ability states (cooldowns, charges, etc.)
        for (var abilityId : abilities.learned()) {
            var savedState = abilities.stateOf(abilityId);
            var currentState = this.playerAbilities.stateOf(abilityId);
            currentState.setCurrentCooldown(savedState.getCurrentCooldown());
            currentState.setCurrentCharges(savedState.getCurrentCharges());
            currentState.setOncePerBattleUsed(savedState.isOncePerBattleUsed());
        }
    }

}