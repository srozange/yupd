package io.github.yupd.infrastructure.utils;

import org.jboss.logmanager.LogContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.mockito.Mockito.*;

class LogUtilsTest {

    private static LogContext currentLogContext;

    @BeforeAll
    static void tearUp() {
        currentLogContext = LogUtils.getLogContext();
    }

    @AfterAll
    static void tearDown() {
         LogUtils.setLogContext(currentLogContext);
    }

    @Test
    void setConsoleLoggerLevel() {
        // Setup
        LogContext mockLogContext = mock(LogContext.class);
        LogUtils.setLogContext(mockLogContext);
        org.jboss.logmanager.Logger mockLogger = mock(org.jboss.logmanager.Logger.class);
        when(mockLogContext.getLogger("yupd")).thenReturn(mockLogger);

        // Test
        LogUtils.setConsoleLoggerLevel("TRACE");
        
        // Assert
        verify(mockLogger).setLevel(Level.parse("TRACE"));
    }

}