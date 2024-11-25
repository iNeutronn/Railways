package com.railways.railways.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class FileLogger implements Logger {

    private static final String LOG_FILE_PATH = "logs/application.log";

    // Конструктор для створення лог-файлу
    public FileLogger() {
        try {
            File logFile = new File(LOG_FILE_PATH);
            File logDirectory = logFile.getParentFile(); // Папка 'logs'

            // Перевіряємо, чи існує директорія, і створюємо її, якщо необхідно
            if (logDirectory != null && !logDirectory.exists()) {
                boolean created = logDirectory.mkdirs(); // Створює папки, якщо вони не існують
                if (!created) {
                    throw new IOException("Failed to create log directory: " + logDirectory.getAbsolutePath());
                }
            }

            // Перевіряємо, чи існує файл, і створюємо його
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile(); // Створює файл 'application.log'
                if (!created) {
                    throw new IOException("Failed to create log file: " + logFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для запису логів
    @Override
    public void log(String message, LogLevel level) {
        if (message == null || message.isBlank()) {
            return;
        }

        String logMessage = String.format("%s [%s]: %s",
                LocalDateTime.now(),
                level.name(),
                message);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.println(logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }
}
