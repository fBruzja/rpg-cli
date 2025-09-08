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
import java.util.List;
import java.util.Scanner;
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
    public static final int STARTING_X_COORDINATE = 38;
    public static final int STARTING_Y_COORDINATE = 19;

    /* All stats are based upon the basic attributes */

    // Player information
    int level = 1;
    Position playerPosition;
    Stats playerStats;
    PersonalPlayerInformation playerInformation;

    // New facade (in parallel)
    private final PlayerAbilities playerAbilities = new PlayerAbilities();

    String[] abilities = new String[4];
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

        //TODO: Profession selection needs a revamp as well!!!
        Profession selectedProfession = Profession.WARRIOR;

        if(newProfession == 'w' || newProfession == 'W') {
            abilities[0] = "Power Attack";
        } else if(newProfession == 't' || newProfession == 'T') {
            selectedProfession = Profession.THIEF;
            abilities[0] = "Poisoned Dagger";
        } else if(newProfession == 'm' || newProfession == 'M') {
            selectedProfession = Profession.MAGE;
            abilities[0] = "Fireball";
        }

        AbilityId starter = StarterAbilities.starterFor(selectedProfession);
        playerAbilities.learn(starter);
        playerAbilities.equip(AbilitySlot.SLOT_1, starter);

        this.playerInformation = new PersonalPlayerInformation(newName, selectedProfession);

        for (int i = 1; i < 4; i++) {
            abilities[i] = "---Empty---";
        }
    }

    public void move(char movement) {
        switch (movement) {
            case 'w':
                playerPosition.setX(playerPosition.getX() - 1);
                break;
            case 'a':
                playerPosition.setY(playerPosition.getY() - 1);
                break;
            case 'd':
                playerPosition.setY(playerPosition.getY() + 1);
                break;
            case 's':
                playerPosition.setX(playerPosition.getX() + 1);
                break;
            default:
                System.out.println("Please enter one of the directions!");
        }
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

    public char showAndSelectAbilityDuringBattle(Player p, Scanner userInput) {
        boolean emptySpellChoice = false;
        char choice = ' ';

        System.out.println("Your abilities are: ");
        String[] abilities = p.getAbilities();

        for (int i = 0; i < abilities.length; i++) {
            System.out.println(i + ") " + abilities[i]);
        }

        System.out.println("\nChoose according to number");
        userInput.reset();

        while (!emptySpellChoice) {
            choice = userInput.next()
                    .charAt(0);
            if (choice != '0' && choice != '1' && choice != '2' && choice != '3') {
                System.out.println("Please choose the appropriate number.");
            } else {
                if (abilities[Character.getNumericValue(choice)].equals("---Empty---")) {
                    System.out.println("Please choose a spell slot that it is not empty.");
                } else {
                    emptySpellChoice = true;
                }
            }
        }

        return choice;
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

        playerStats.setStrength(playerStats.getStrength() + ((playerProfession == Profession.WARRIOR) ? 3 : 1));
        playerStats.setAgility(playerStats.getAgility() + ((playerProfession == Profession.THIEF) ? 3 : 1));
        playerStats.setIntelligence(playerStats.getIntelligence() + ((playerProfession == Profession.MAGE) ? 3 : 1));

        refreshSkills();
        addNewAbilities(playerProfession, level);
    }

    public void addNewAbilities(Profession profession, int level) {
        switch (profession) {
            case Profession.WARRIOR: {
                switch (level) {
                    case 3: {
                        System.out.println("You have learned a new ability: Healing Salve. \n It heals 50 HP");
                        modifyAbility(1, "Healing Salve");
                    }
                    break;
                    case 6: {
                        System.out.println(
                                "You have learned a new ability: Charge\nOpponent gets disabled for the next turn!");
                        modifyAbility(2, "Charge");
                    }
                    break;
                    case 10: {
                        System.out.println(
                                "You have learned a new ability: Best Defense\nYour defense permanently increases by 5");
                        modifyAbility(3, "Best Defense");
                    }
                    break;
                }
            }
            break;
            case Profession.THIEF: {
                switch (level) {
                    case 3: {
                        System.out.println(
                                "You have learned a new ability: Critical Strike. \n Your next attack does triple damage");
                        modifyAbility(1, "Critical Strike");
                    }
                    break;
                    case 6: {
                        System.out.println("You have learned a new ability: Wither\nOpponent's defense is ignored!");
                        modifyAbility(2, "Wither");
                    }
                    break;
                    case 10: {
                        System.out.println(
                                "You have learned a new ability: Coup De Grace\nYour attack brings your enemy near death!");
                        modifyAbility(3, "Coup De Grace");
                    }
                    break;
                }
            }
            break;
            case Profession.MAGE: {
                switch (level) {
                    case 3: {
                        System.out.println("You have learned a new ability: Electrocute. \n Deals 10+ damage!");
                        modifyAbility(1, "Electrocute");
                    }
                    break;
                    case 6: {
                        System.out.println(
                                "You have learned a new ability: Degen Aura\nEnemy is disabled the next turn!");
                        modifyAbility(2, "Degen Aura");
                    }
                    break;
                    case 10: {
                        System.out.println("You have learned a new ability: Death Dome\nDeals 25+ damage");
                        modifyAbility(3, "Best Defense");
                    }
                    break;
                }
            }
            break;
        }
    }

    public void getBattleRewards(int exp) {
        playerStats.setExp(playerStats.getExp() + exp);
        if (playerStats.getExp() >= 50) {
            if (level == Player.MAX_LEVEL) {
                System.out.println("You have achieved the maximum level of your abilities!");
            } else {
                System.out.println("You have leveled up! Your basic attributes have grown stronger!");
                levelUp();
            }
        }
    }

    public void modifyAbility(int index, String ability) {
        abilities[index] = ability;
    }

    public PlayerAbilities abilitiesFacade() {
        return playerAbilities;
    }

}