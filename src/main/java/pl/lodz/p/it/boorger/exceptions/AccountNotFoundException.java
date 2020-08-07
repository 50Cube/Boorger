package pl.lodz.p.it.boorger.exceptions;

public class AccountNotFoundException extends AppBaseException {
    static final public String KEY = "error.account.notfound";

    public AccountNotFoundException() {
        super(KEY);
    }
}
