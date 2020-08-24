package pl.lodz.p.it.boorger.exceptions;

public class PasswordAlreadyUsedException extends AppBaseException {
    static final public String KEY = "error.password.used";

    public PasswordAlreadyUsedException() {
        super(KEY);
    }
}
