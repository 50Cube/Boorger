package pl.lodz.p.it.boorger.exceptions;

public class RestaurantAlreadyExistsException extends AppBaseException {
    static final public String KEY = "error.restaurant.exists";

    public RestaurantAlreadyExistsException() {
        super(KEY);
    }
}
