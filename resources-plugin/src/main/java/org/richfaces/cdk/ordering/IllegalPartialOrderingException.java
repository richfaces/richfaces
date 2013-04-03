package org.richfaces.cdk.ordering;

public class IllegalPartialOrderingException extends IllegalStateException {

    private static final long serialVersionUID = 205183029719165393L;

    public IllegalPartialOrderingException() {
        super();
    }

    public IllegalPartialOrderingException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPartialOrderingException(String s) {
        super(s);
    }

    public IllegalPartialOrderingException(Throwable cause) {
        super(cause);
    }
}
