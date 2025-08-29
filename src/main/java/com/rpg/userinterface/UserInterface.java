package com.rpg.userinterface;

import com.rpg.utils.GameLogger;

public class UserInterface {

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
        System.out.print("""
                
                Press 'w' to walk forward
                Press 'a' to move left
                Press 'd' to move right\
                
                Press 's' to move down
                Press 'i' to see your attributes
                Press 'v' to save your character
                Press'q' to exit\s
                
                """);
    }
}
