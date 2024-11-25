package com.railways.railways.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger  implements Logger {
    private static final String LOG_FILE = "logs/application.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void log(String message, LogLevel level) {
        if (message == null || message.isBlank()) {
            message = "No message provided.";
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = String.format("%s [%s] %s", timestamp, level, message);

        writeToFile(logEntry);
    }

    private void writeToFile(String logEntry) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }
}
