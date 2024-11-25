package com.railways.railways.logging;

public class ConsoleLogger implements Logger {
    @Override
    public void log(String message, LogLevel level) {
        System.out.println(level + ": " + message);
    }
}
