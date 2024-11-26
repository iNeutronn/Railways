package com.railways.railways.logging;

/**
 * Interface for logging messages with different log levels.
 *
 * Implementing classes should provide specific logic for logging messages,
 * such as writing them to a file, database, or remote server.
 */
public interface Logger {
    /**
     * Logs a message with a specified log level.
     *
     * The message should be logged with the appropriate severity or priority
     * indicated by the {@code level}.
     *
     * @param message The message to log. This should not be null or blank.
     * @param level   The severity level of the log message (e.g., INFO, ERROR).
     */
    void log(String message, LogLevel level) ;
}
