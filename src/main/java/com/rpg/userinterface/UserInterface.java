package com.rpg.userinterface;

import com.rpg.characters.Enemy;
import com.rpg.characters.Player;
import com.rpg.characters.abilitymanagement.AbilityRegistry;
import com.rpg.characters.abilitymanagement.AbilitySlot;
import com.rpg.characters.data.Stats;
import com.rpg.game.PlayerFightDecision;
import com.rpg.map.Map;
import com.rpg.utils.GameLogger;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    public static Scanner userInput = new Scanner(System.in);

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\u001B[2J\u001B[H");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 30; i++) {
                System.out.println();
            }
            System.out.flush();
        }
    }

    public static void showIntro() {
        renderMessage("""
                
                Welcome traveler to this mysterious trial you will be facing!
                This is the magical land of Marghor, ruled by the tyrannical sorcerer Zoram.
                We bid you welcome!
                
                Since you accept the challenge to defeat Zoram.
                You must choose...
                
                """);
    }

    public static void showIntroMenu() {
        renderMessage("""
                    1) New Game
                    2) Continue
                    3) Exit
                
                """);
    }

    public static void showMenu() {
        renderMessage("""
                
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

        renderMessage("------------GENERAL INFORMATION----------");
        renderMessage("\tName: " + playerInformation.name());
        renderMessage("\n\tLevel: " + p.getLevel());
        renderMessage("\n\tProfession: " + playerInformation.profession().getDisplayName());
        renderMessage("\n--------------BASIC ATTRIBUTES----------");
        renderMessage("\tStrength: " + playerStats.getStrength());
        renderMessage("\n\tAgility: " + playerStats.getAgility());
        renderMessage("\n\tIntelligence: " + playerStats.getIntelligence());
        renderMessage("\n-------------------STATS----------------");
        renderMessage("\tHP: " + playerStats.getHealthPoints());
        renderMessage("\n\tMP: " + playerStats.getManaPoints());
        renderMessage("\n\tAttack: " + playerStats.getAttackPoints());
        renderMessage("\n\tDefense: " + playerStats.getDefense());
        renderMessage("\n\tEXP: " + playerStats.getExp());
    }

    public static void printPlayerHUD(Player p, Enemy e) {
        Stats stats = p.getPlayerStats();
        renderMessage(p.getPlayerInformation().name() + " HP: " + stats.getHealthPoints() + " | MP: " + stats.getManaPoints());
        renderMessage(e.getName() + " HP: " + e.getHealthPoints());
        renderMessage("It is your turn");
        renderMessage("What will your move be?");
        renderMessage("'a' for attack\n'b' for abilities\n");
    }

    public static PlayerChoiceCommand readMainMenuChoice() {
        while (true) {
            String in = userInput.next();
            if (in == null || in.isEmpty()) {
                renderMessages(List.of("Please enter a command."));
                continue;
            }
            char c = in.charAt(0);
            var cmd = PlayerChoiceCommand.fromChar(c);
            if (cmd.isPresent()) {
                return cmd.get();
            }
            renderMessages(List.of("Please enter one of: 1/2/3"));
        }
    }

    public static String readPlayerName() {
        renderMessages(java.util.List.of("Tell us about yourself...", "What is your name, traveler?"));
        String name = userInput.next();
        if (name.isBlank() || name.length() > 30) {
            renderMessages(List.of("Please enter a valid name. It must be between 1 and 30 characters long."));
            return readPlayerName();
        }
        userInput.reset();
        return name;
    }

    public static char readPlayerProfession() {
        renderMessages(java.util.List.of("What do you consider yourself?", "'w' for warrior, 't' for a thief, 'm' for mage"));

        char profession = ' ';

        String in = UserInterface.userInput.next();
        if (in != null && !in.isEmpty()) {
            profession = in.charAt(0);
        }
        if (profession != 'w' && profession != 'W' && profession != 't' && profession != 'T' && profession != 'm' && profession != 'M') {
            renderMessages(List.of("Please enter 'w', 't' or 'm'."));
            return readPlayerProfession();
        }
        return profession;
    }

    public static PlayerFightDecision askPlayerForActionInTurn() {
        char choice = ' ';

        while (choice != 'a' && choice != 'b') {
            choice = userInput.next().charAt(0);
            if (choice != 'a' && choice != 'b') {
                renderMessage("Please choose the appropriate choice.\n");
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
            renderMessage(msg);
        }
    }

    public static void renderMessage(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        GameLogger.print(message);
    }

    public static void renderChar(Character character) {
        if (character == null) {
            return;
        }
        GameLogger.print(character);
    }

    // TODO: have a method to handle showing errors

    public static void showEquippedAbilities(Player p) {
        var pa = p.abilitiesFacade();
        renderMessage("-------- Abilities --------");
        for (AbilitySlot slot : AbilitySlot.values()) {
            var equipped = pa.getEquipped(slot);
            String name = equipped.map(id -> AbilityRegistry.getAbilityMetadata(id).name()).orElse("--- Empty ---");
            renderMessage(slotLabel(slot) + ": " + name);
        }
    }

    private static String slotLabel(AbilitySlot slot) {
        return switch (slot) {
            case SLOT_1 -> "0) SLOT_1";
            case SLOT_2 -> "1) SLOT_2";
            case SLOT_3 -> "2) SLOT_3";
            case SLOT_4 -> "3) SLOT_4";
        };
    }

    public static void printPreBattleIntro(String enemyName) {
        renderMessage("----- BATTLE -----");
        renderMessage("You have stumbled upon a " + enemyName + ", prepare yourself!");
    }

    public static MainCommand readMainLoopCommand() {
        while (true) {
            String in = userInput.next();
            if (in == null || in.isEmpty()) {
                renderMessages(List.of("Please enter a command."));
                continue;
            }
            char c = in.charAt(0);
            var cmd = MainCommand.fromChar(c);
            if (cmd.isPresent()) {
                return cmd.get();
            }
            renderMessages(List.of("Please enter one of: w/a/s/d/i/v/q"));
        }
    }

    public static void printMap(Map map) {
        var gameMap = map.getGameMap();

        for (int i = 0; i < gameMap[0].length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                renderChar(gameMap[i][j]);
            }
            renderMessage("\n");
        }
    }

}
