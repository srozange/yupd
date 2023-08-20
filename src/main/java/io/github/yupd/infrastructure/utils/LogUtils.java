package io.github.yupd.infrastructure.utils;

import org.jboss.logging.Logger;
import org.jboss.logmanager.LogContext;

import java.util.logging.Level;

public class LogUtils {

    private static final String LOGGER_NAME = "yupd";

    LogUtils() {
        throw new IllegalStateException("utility class");
    }

    public static Logger getConsoleLogger() {
        return Logger.getLogger(LOGGER_NAME);
    }

    public static void setConsoleLoggerLevel(String levelValue) {
        setConsoleLoggerLevel(LOGGER_NAME, levelValue);
    }

    private static void setConsoleLoggerLevel(String loggerName, String levelValue) {
        LogContext logContext = LogContext.getLogContext();
        org.jboss.logmanager.Logger logger = logContext.getLogger(loggerName);
        if (logger != null) {
            Level level = org.jboss.logmanager.Level.parse(levelValue);
            logger.setLevel(level);
        }
    }
}
