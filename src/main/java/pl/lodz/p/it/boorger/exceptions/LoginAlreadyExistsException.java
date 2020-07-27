package pl.lodz.p.it.boorger.exceptions;

public class LoginAlreadyExistsException extends AppBaseException {
    static final public String KEY = "error.login.exists";

    public LoginAlreadyExistsException() {
        super(KEY);
    }
}
