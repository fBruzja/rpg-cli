package com.rpg.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger {

    private GameLogger() {}

    private static final Logger logger = Logger.getLogger(GameLogger.class.getName());

    static {
        logger.setUseParentHandlers(false); // Disable default logging handlers
        ConsoleHandler handler = new ConsoleHandler();

        // Custom Formatter that prints only the message
        handler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n"; // Only the message
            }
        });

        logger.addHandler(handler);
    }

    public static void print(String message) {
        logger.info(message);
    }
}