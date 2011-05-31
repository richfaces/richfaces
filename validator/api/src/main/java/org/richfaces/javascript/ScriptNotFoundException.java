package org.richfaces.javascript;

public class ScriptNotFoundException extends Exception {
    public ScriptNotFoundException() {
        super();
    }

    public ScriptNotFoundException(String message) {
        super(message);
    }

    public ScriptNotFoundException(Throwable cause) {
        super(cause);
    }

    public ScriptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
