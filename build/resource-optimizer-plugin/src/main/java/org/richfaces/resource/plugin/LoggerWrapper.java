package org.richfaces.resource.plugin;

import java.util.Locale;

import org.apache.maven.plugin.logging.Log;
import org.richfaces.l10n.BundleLoader;
import org.richfaces.l10n.InterpolationException;
import org.richfaces.l10n.MessageInterpolator;
import org.richfaces.log.Logger;

public class LoggerWrapper implements Logger {

    private MessageInterpolator messageInterpolator;

    private Log log;

    public LoggerWrapper(Log log) {
        this.log = log;
        messageInterpolator = new MessageInterpolator(new BundleLoader());
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public void debug(CharSequence content) {
        log(Level.DEBUG, content);
    }

    public void debug(Enum<?> messageKey, Object... args) {
        log(Level.DEBUG, messageKey, args);
    }

    public void debug(CharSequence content, Throwable thrown) {
        log(Level.DEBUG, content, thrown);
    }

    public void debug(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.DEBUG, error, messageKey, args);
    }

    public void debug(Throwable thrown) {
        log(Level.DEBUG, thrown);
    }

    public boolean isInfoEnabled() {
        return isLogEnabled(Level.INFO);
    }

    public void info(CharSequence content) {
        log(Level.INFO, content);
    }

    public void info(Enum<?> messageKey, Object... args) {
        log(Level.INFO, messageKey, args);
    }

    public void info(CharSequence content, Throwable thrown) {
        log(Level.INFO, content, thrown);
    }

    public void info(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.INFO, error, messageKey, args);
    }

    public void info(Throwable thrown) {
        log(Level.INFO, thrown);
    }

    public boolean isWarnEnabled() {
        return isLogEnabled(Level.WARNING);
    }

    public void warn(CharSequence content) {
        log(Level.WARNING, content);
    }

    public void warn(Enum<?> messageKey, Object... args) {
        log(Level.WARNING, messageKey, args);
    }

    public void warn(CharSequence content, Throwable thrown) {
        log(Level.WARNING, content, thrown);
    }

    public void warn(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.WARNING, error, messageKey, args);
    }

    public void warn(Throwable thrown) {
        log(Level.WARNING, thrown);
    }

    public boolean isErrorEnabled() {
        return isLogEnabled(Level.ERROR);
    }

    public void error(CharSequence content) {
        log(Level.ERROR, content);
    }

    public void error(Enum<?> messageKey, Object... args) {
        log(Level.ERROR, messageKey, args);
    }

    public void error(CharSequence content, Throwable thrown) {
        log(Level.ERROR, content, thrown);
    }

    public void error(Throwable error, Enum<?> messageKey, Object... args) {
        log(Level.ERROR, error, messageKey, args);
    }

    public void error(Throwable thrown) {
        log(Level.ERROR, thrown);
    }

    public boolean isLogEnabled(Level level) {
        switch (level) {
            case DEBUG:
                return log.isDebugEnabled();
            case INFO:
                return log.isInfoEnabled();
            case WARNING:
                return log.isWarnEnabled();
            case ERROR:
                return log.isErrorEnabled();
        }
        throw new IllegalStateException("Unknown Logger Level");
    }

    public void log(Level level, CharSequence content) {
        if (isLogEnabled(level)) {
            switch (level) {
                case DEBUG:
                    log.debug(content);
                    break;
                case INFO:
                    log.info(content);
                    break;
                case WARNING:
                    log.warn(content);
                    break;
                case ERROR:
                    log.error(content);
                    break;
            }
        }
    }

    public void log(Level level, Enum<?> messageKey, Object... args) {
        log(level, interpolate(messageKey, args));
    }

    public void log(Level level, CharSequence content, Throwable thrown) {
        if (isLogEnabled(level)) {
            switch (level) {
                case DEBUG:
                    log.debug(content, thrown);
                    break;
                case INFO:
                    log.info(content, thrown);
                    break;
                case WARNING:
                    log.warn(content, thrown);
                    break;
                case ERROR:
                    log.error(content, thrown);
                    break;
            }
        }
    }

    public void log(Level level, Throwable thrown, Enum<?> messageKey, Object... args) {
        log(level, interpolate(messageKey, args), thrown);
    }

    public void log(Level level, Throwable thrown) {
        if (isLogEnabled(level)) {
            switch (level) {
                case DEBUG:
                    log.debug(thrown);
                    break;
                case INFO:
                    log.info(thrown);
                    break;
                case WARNING:
                    log.warn(thrown);
                    break;
                case ERROR:
                    log.error(thrown);
                    break;
            }
        }
    }

    private String interpolate(Enum<?> messageKey, Object... args) {
        try {
            return messageInterpolator.interpolate(Locale.getDefault(), messageKey, args);
        } catch (InterpolationException e) {
            return "???" + e.getMessageKey() + "???";
        }
    }
}
