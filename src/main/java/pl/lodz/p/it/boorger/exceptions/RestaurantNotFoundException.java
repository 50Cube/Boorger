package pl.lodz.p.it.boorger.exceptions;

public class RestaurantNotFoundException extends AppBaseException {
    static final public String KEY = "error.restaurant.notfound";

    public RestaurantNotFoundException() {
        super(KEY);
    }
}
