package com.railways.railways.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Implementation of the Logger interface for writing log messages to a file.
 * This class ensures that the log file and its parent directories exist, and it
 * appends log entries to the file with a timestamp and log level.
 */
public class FileLogger implements Logger {

    private static final String LOG_FILE_PATH = "logs/application.log";

    /**
     * Constructor for the FileLogger.
     *
     * Ensures that the log file and its parent directories exist. If they do not exist,
     * this constructor creates them.
     */
    public FileLogger() {
        try {
            File logFile = new File(LOG_FILE_PATH);
            File logDirectory = logFile.getParentFile();

            if (logDirectory != null && !logDirectory.exists()) {
                boolean created = logDirectory.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create log directory: " + logDirectory.getAbsolutePath());
                }
            }

            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (!created) {
                    throw new IOException("Failed to create log file: " + logFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to the log file.
     *
     * Each log entry includes a timestamp, log level, and the provided message. Log entries
     * are appended to the file specified by {@link #LOG_FILE_PATH}.
     *
     * @param message The message to log. If null or blank, the message is ignored.
     * @param level   The severity level of the log message.
     */
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
