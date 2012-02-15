package org.richfaces.application.push;

public class PushContextInitializationException extends RuntimeException {

    private static final long serialVersionUID = 4770267004332438459L;

    public PushContextInitializationException() {
        super();
    }

    public PushContextInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushContextInitializationException(String message) {
        super(message);
    }

    public PushContextInitializationException(Throwable cause) {
        super(cause);
    }
}
