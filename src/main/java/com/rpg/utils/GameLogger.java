package com.rpg.utils;

public class GameLogger {

    private GameLogger() {}

    public static void print(String message) {
        System.out.print(message);
    }

    public static void print(Character charMessage) {
        System.out.print(charMessage);
    }
}