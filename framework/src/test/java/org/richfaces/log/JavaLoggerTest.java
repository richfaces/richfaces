/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.log.Logger.Level;

/**
 * @author Nick Belaevski
 *
 */
public class JavaLoggerTest {
    private static final String ANOTHER_DUMMY_MESSAGE = "another message";
    private static final String DUMMY_MESSAGE = "message";
    private static final String TEST_MESSAGE_PATTERN = "RF-000000 Test message with arguments: {0} and {1}";
    private static final String CHAR_SEQUENCE_THROWABLE_PATTERN = "(CharSequence, Throwable)({0})";
    private static final String CHAR_SEQUENCE_PATTERN = "(CharSequence)({0})";

    /**
     * @author Nick Belaevski
     *
     */
    private final class TrackingHandler extends Handler {
        @Override
        public void publish(LogRecord record) {
            publishedRecords.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    private java.util.logging.Logger wrappedLogger;
    private JavaLogger logger;
    private List<LogRecord> publishedRecords;

    @Before
    public void setUp() throws Exception {
        LogManager.getLogManager().reset();
        publishedRecords = new ArrayList<LogRecord>();

        wrappedLogger = java.util.logging.Logger.getLogger("org.richfaces.JavaLoggerTest");
        wrappedLogger.addHandler(new TrackingHandler());

        logger = new JavaLogger(wrappedLogger.getName());
    }

    @After
    public void tearDown() throws Exception {
        publishedRecords = null;

        wrappedLogger = null;
        logger = null;
    }

    private void verifyLogRecord(LogRecord logRecord, Level level, String message, Class<? extends Throwable> thrownClass,
        String thrownMessage) {
        assertEquals(JavaLogger.LEVELS_MAP.get(level), logRecord.getLevel());
        assertEquals(message, logRecord.getMessage());

        if (thrownClass != null && thrownMessage != null) {
            assertTrue(thrownClass.isInstance(logRecord.getThrown()));
            assertEquals(thrownMessage, logRecord.getThrown().getMessage());
        } else {
            assertNull(logRecord.getThrown());
        }
    }

    private void verifyEnabledMethods(Level loggerLevel) throws Exception {
        String[] levels = { "Debug", "Info", "Warn", "Error" };

        for (Level level : Level.values()) {
            boolean enabledValue = (Boolean) Logger.class.getMethod(
                MessageFormat.format("is{0}Enabled", levels[level.ordinal()])).invoke(logger);

            if (level.compareTo(loggerLevel) < 0) {
                assertFalse(loggerLevel.toString(), enabledValue);
            } else {
                assertTrue(loggerLevel.toString(), enabledValue);
            }
        }
    }

    private void verifyLoggingLevels(Level loggerLevel) {
        for (Level messageLevel : Level.values()) {
            logger.log(messageLevel, "test");

            if (messageLevel.compareTo(loggerLevel) < 0) {
                assertTrue(publishedRecords.isEmpty());
            } else {
                assertTrue(publishedRecords.size() == 1);
                publishedRecords.clear();
            }
        }
    }

    @Test
    public void testLogging() throws Exception {
        wrappedLogger.setLevel(java.util.logging.Level.ALL);

        String[] levels = { "debug", "info", "warn", "error" };

        for (String levelName : levels) {
            Logger.class.getMethod(levelName, CharSequence.class).invoke(logger,
                MessageFormat.format(CHAR_SEQUENCE_PATTERN, levelName));
            Logger.class.getMethod(levelName, CharSequence.class, Throwable.class).invoke(logger,
                MessageFormat.format(CHAR_SEQUENCE_THROWABLE_PATTERN, levelName), new NullPointerException(levelName));

            Logger.class.getMethod(levelName, Enum.class, Object[].class).invoke(logger, LoggerTestMessages.TEST_MESSAGE,
                new Object[] { levelName, DUMMY_MESSAGE });

            Logger.class.getMethod(levelName, Throwable.class).invoke(logger, new IllegalArgumentException(levelName));

            Logger.class.getMethod(levelName, Throwable.class, Enum.class, Object[].class).invoke(logger,
                new UnsupportedOperationException(levelName), LoggerTestMessages.TEST_MESSAGE,
                new Object[] { levelName, ANOTHER_DUMMY_MESSAGE });
        }

        Iterator<LogRecord> iterator = publishedRecords.iterator();

        for (Level level : Level.values()) {
            String levelName = levels[level.ordinal()];

            verifyLogRecord(iterator.next(), level, MessageFormat.format(CHAR_SEQUENCE_PATTERN, levelName), null, null);
            verifyLogRecord(iterator.next(), level, MessageFormat.format(CHAR_SEQUENCE_THROWABLE_PATTERN, levelName),
                NullPointerException.class, levelName);

            verifyLogRecord(iterator.next(), level, MessageFormat.format(TEST_MESSAGE_PATTERN, levelName, DUMMY_MESSAGE), null,
                null);

            verifyLogRecord(iterator.next(), level, null, IllegalArgumentException.class, levelName);

            verifyLogRecord(iterator.next(), level,
                MessageFormat.format(TEST_MESSAGE_PATTERN, levelName, ANOTHER_DUMMY_MESSAGE),
                UnsupportedOperationException.class, levelName);
        }
    }

    @Test
    public void testLevels() throws Exception {
        for (Level loggerLevel : Level.values()) {
            wrappedLogger.setLevel(JavaLogger.LEVELS_MAP.get(loggerLevel));

            verifyEnabledMethods(loggerLevel);
            verifyLoggingLevels(loggerLevel);
        }
    }
}
