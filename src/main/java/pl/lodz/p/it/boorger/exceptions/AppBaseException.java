package pl.lodz.p.it.boorger.exceptions;

public class AppBaseException extends Exception {
    static final public String KEY = "error.default";

    public AppBaseException() {
        super(KEY);
    }

    public AppBaseException(String message) {
        super(message);
    }
}
