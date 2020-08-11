package pl.lodz.p.it.boorger.exceptions;

public class FailedAuthException extends AppBaseException {
    static final public String KEY = "error.auth";

    public FailedAuthException() {
        super(KEY);
    }
}
