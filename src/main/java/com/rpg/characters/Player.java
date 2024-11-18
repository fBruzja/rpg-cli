package com.rpg.characters;

import com.rpg.characters.data.PersonalPlayerInformation;
import com.rpg.characters.data.Position;
import com.rpg.characters.data.Profession;
import com.rpg.characters.data.Stats;
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

        Profession selectedProfession = Profession.WARRIOR;
        switch (newProfession) {
            case 'w': {
                abilities[0] = "Power Attack";
            }
            break;
            case 't': {
                selectedProfession = Profession.THIEF;
                abilities[0] = "Poisoned Dagger";
            }
            break;
            case 'm': {
                selectedProfession = Profession.MAGE;
                abilities[0] = "Fireball";
            }
            break;
            // TODO: add default "forsaken" class
        }
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

    public boolean physicalAtatck(Player p, Enemy e, boolean poison) {
        if (poison) {
            System.out.println("You attack with your poisoned blade! You made: "
                    + p.calculateDamageDoneByPlayer(e.getDefense(), 1, true)
                    + " damage!");
            p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 1, true), 0);
            poison = false;
        } else {
            System.out.println("You attack! You made: "
                    + p.calculateDamageDoneByPlayer(e.getDefense(), 0, true)
                    + " damage!");
            p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 0, true), 0);
        }
        return poison;
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

    public boolean firstAbilityUsage(Player p, Enemy e) {
        boolean poison = false;
        var playerProfession = p.getPlayerInformation()
                .profession();
        var manaPoints = playerStats.getManaPoints();

        if (playerProfession == Profession.WARRIOR) {
            if (manaPoints >= 20) {
                System.out.println("You use "
                        + abilities[0]
                        + "! You made: "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), 2, false)
                        + " damage!");
                p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 2, false), 20);
            } else {
                System.out.println("You do not have enough mana. You need 20 for this skill.");
            }
        } else if (playerProfession == Profession.THIEF) {
            if (manaPoints >= 15) {
                System.out.println("You use " + abilities[0] + "! Your next attack will do more damage.");
                poison = true;
                playerStats.setManaPoints(playerStats.getManaPoints() - 15);
            } else {
                System.out.println("You do not have enough mana. You need 15 for this skill.");
            }
        } else if (playerProfession == Profession.MAGE) {
            if (manaPoints >= 15) {
                System.out.println("You use! "
                        + abilities[0]
                        + " You made "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), 3, true)
                        + " damage.");
                p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 3, true), 15);
            } else {
                System.out.println("You do not have enough mana. You need 15 for this skill.");
            }
        }
        return poison;
    }

    public void secondAbilityUsage(Player p, Enemy e) {
        var manaPoints = playerStats.getManaPoints();
        var playerProfession = p.getPlayerInformation()
                .profession();
        if (playerProfession == Profession.WARRIOR) {
            if (manaPoints >= 40) {
                System.out.println("You use " + abilities[1] + ". Your health is permanently regenerated +50");
                // TODO: METHODS FOR INCREASE/DECREASE
                playerStats.setHealthPoints(playerStats.getHealthPoints() + 50);
            } else {
                System.out.println("You do not have enough mana.");
            }
        } else if (playerProfession == Profession.THIEF) {
            if (manaPoints >= 50) {
                System.out.println("You use "
                        + abilities[1]
                        + "! You made "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), 3, false)
                        + " damage");
                p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 3, false), 50);
            } else {
                System.out.println("You do not have enough mana. You need 50 for this skill");
            }
        } else if (playerProfession == Profession.MAGE) {
            if (manaPoints >= 20) {
                System.out.println("You use "
                        + abilities[1]
                        + "! You made "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), 10, true)
                        + " damage");
                p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 10, true), 20);
            } else {
                System.out.println("You do not have enough mana. You need 20 for this skill.");
            }
        }
    }

    public boolean thirdAbilityUsage(Player p, Enemy e) {
        boolean disable = false;
        var playerProfession = p.getPlayerInformation()
                .profession();
        var manaPoints = playerStats.getManaPoints();
        if (playerProfession == Profession.WARRIOR) {
            System.out.println("You use " + abilities[2] + "! Your opponent is disabled for the next turn");
            disable = true;
        } else if (playerProfession == Profession.THIEF) {
            if (manaPoints >= 30) {
                System.out.println("You use "
                        + abilities[2]
                        + " to ignore the opponent's defense! \nYou made "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), (1 + e.getDefense() / 2), true)
                        // TODO: make calculations more readable
                        + " damage");
                p.consumeManaAndRemoveEnemyHealth(e,
                        p.calculateDamageDoneByPlayer(e.getDefense(), (1 + e.getDefense() / 2), true),
                        30
                );
            } else {
                System.out.println("You do not have enough mana. You need 30 mana for this skill.");
            }
        } else if (playerProfession == Profession.MAGE) {
            if (manaPoints >= 35) {
                System.out.println("You use " + abilities[2] + "! Opponent is disabled for the next turn!");
                disable = true;
            } else {
                System.out.println("You do not have enough mana. You need 35 for this skill.");
            }
        }

        return disable;
    }

    public void fourthAbilityUsage(Player p, Enemy e) {
        var playerProfession = p.getPlayerInformation()
                .profession();
        var manaPoints = playerStats.getManaPoints();

        if (playerProfession == Profession.WARRIOR) {
            if (p.isWarriorUsedLastSkill()) {
                System.out.println("You used " + abilities[3] + "! Your defense is increased by 5 permanently.");
                p.setWarriorUsedLastSkill(false);
            } else {
                System.out.println("That skill can be used only once.");
            }
        } else if (playerProfession == Profession.THIEF) {
            if (manaPoints >= 60) {
                System.out.println("You used " + abilities[3] + ". Opponent is in near death.");
                p.consumeManaAndRemoveEnemyHealth(e, e.getHealthPoints() - 5, 60);
            } else {
                System.out.println("You do not have enough mana. You need 60 mana for this skill.");
            }
        } else if (playerProfession == Profession.MAGE) {
            if (manaPoints >= 30) {
                System.out.println("You use "
                        + abilities[3]
                        + "! You made "
                        + p.calculateDamageDoneByPlayer(e.getDefense(), 25, true)
                        + " damage.");
                p.consumeManaAndRemoveEnemyHealth(e, p.calculateDamageDoneByPlayer(e.getDefense(), 25, true), 30);
            } else {
                System.out.println("You do not have enough mana. You need 30 for this skill.");
            }
        }
    }

    public int calculateDamageDoneByPlayer(int enemyDefense, int bonusFactor, boolean bonusType) {
        int adjustedAttack = bonusType
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

}