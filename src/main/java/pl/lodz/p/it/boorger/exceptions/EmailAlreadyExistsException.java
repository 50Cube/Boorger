package pl.lodz.p.it.boorger.exceptions;

public class EmailAlreadyExistsException extends AppBaseException {
    static final public String KEY = "error.email.exists";

    public EmailAlreadyExistsException() {
        super(KEY);
    }
}
