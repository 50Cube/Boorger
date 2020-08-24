package pl.lodz.p.it.boorger.exceptions;

public class IncorrectCurrentPasswordException extends AppBaseException {
    static final public String KEY = "error.incorrect.currentpassword";

    public IncorrectCurrentPasswordException() {
        super(KEY);
    }
}
