package com.rpg.userinterface;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.data.Stats;
import com.rpg.game.PlayerFightDecision;
import com.rpg.utils.GameLogger;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    public static Scanner userInput = new Scanner(System.in);

    public static void showIntro() {
        GameLogger.print("""
                
                Welcome traveler to this mysterious trial you will be facing!
                This is the magical land of Marghor, ruled by the tyrannical sorcerer Zoram.
                We bid you welcome!
                
                Since you accept the challenge to defeat Zoram (which main character doesn't?)
                You must choose...
                    1) New Game
                    2) Continue
                    3) Exit
                """);
    }

    public static void showMenu() {
        GameLogger.print("""
                
                Press 'w' to walk forward
                Press 'a' to move left
                Press 'd' to move right\
                
                Press 's' to move down
                Press 'i' to see your attributes
                Press 'v' to save your character
                Press'q' to exit\s
                
                """);
    }

    public static void showStats(Player p) {
        var playerStats = p.getPlayerStats();
        var playerInformation = p.getPlayerInformation();

        GameLogger.print("------------GENERAL INFORMATION----------");
        GameLogger.print("\tName: " + playerInformation.name());
        GameLogger.print("\n\tLevel: " + p.getLevel());
        GameLogger.print("\n\tProfession: " + playerInformation.profession().getDisplayName());
        GameLogger.print("\n--------------BASIC ATTRIBUTES----------");
        GameLogger.print("\tStrength: " + playerStats.getStrength());
        GameLogger.print("\n\tAgility: " + playerStats.getAgility());
        GameLogger.print("\n\tIntelligence: " + playerStats.getIntelligence());
        GameLogger.print("\n-------------------STATS----------------");
        GameLogger.print("\tHP: " + playerStats.getHealthPoints());
        GameLogger.print("\n\tMP: " + playerStats.getManaPoints());
        GameLogger.print("\n\tAttack: " + playerStats.getAttackPoints());
        GameLogger.print("\n\tDefense: " + playerStats.getDefense());
        GameLogger.print("\n\tEXP: " + playerStats.getExp());
    }

    public static void printPlayerHUD(Player p, Enemy e) {
        Stats stats = p.getPlayerStats();
        GameLogger.print(p.getPlayerInformation().name() + " HP: " + stats.getHealthPoints() + " | MP: " + stats.getManaPoints());
        GameLogger.print(e.getName() + " HP: " + e.getHealthPoints());
        GameLogger.print("It is your turn");
        GameLogger.print("What will your move be?");
        GameLogger.print("'a' for attack\n'b' for abilities\n");
    }

    public static PlayerFightDecision askPlayerForActionInTurn() {
        char choice = ' ';

        while (choice != 'a' && choice != 'b') {
            choice = userInput.next().charAt(0);
            if (choice != 'a' && choice != 'b') {
                GameLogger.print("Please choose the appropriate choice.\n");
            }
        }

        if (choice == 'a') {
            return PlayerFightDecision.ATTACK;
        } else if (choice == 'b') {
            return PlayerFightDecision.ABILITIES;
        } else {
            return null;
        }
    }

    public static void renderMessages(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        for (String msg : messages) {
            GameLogger.print(msg);
        }
    }
}
