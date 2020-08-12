package pl.lodz.p.it.boorger.exceptions;

public class AccountAlreadyConfirmedException extends AppBaseException {
    static final public String KEY = "error.account.confirmed";

    public AccountAlreadyConfirmedException() {
        super(KEY);
    }
}
