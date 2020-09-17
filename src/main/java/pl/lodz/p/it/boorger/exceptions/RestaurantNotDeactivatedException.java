package pl.lodz.p.it.boorger.exceptions;

public class RestaurantNotDeactivatedException extends AppBaseException {
    static final public String KEY = "error.restaurant.notdeactivated";

    public RestaurantNotDeactivatedException() {
        super(KEY);
    }
}
