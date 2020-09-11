package pl.lodz.p.it.boorger.exceptions;

public class DishAlreadyExistsException extends AppBaseException {
    static final public String KEY = "error.dish.exists";

    public DishAlreadyExistsException() {
        super(KEY);
    }
}
