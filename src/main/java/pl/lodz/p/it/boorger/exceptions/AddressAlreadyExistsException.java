package pl.lodz.p.it.boorger.exceptions;

public class AddressAlreadyExistsException extends AppBaseException {
    static final public String KEY = "error.address.exists";

    public AddressAlreadyExistsException() {
        super(KEY);
    }
}
