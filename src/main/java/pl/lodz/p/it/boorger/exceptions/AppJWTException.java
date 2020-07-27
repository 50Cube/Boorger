package pl.lodz.p.it.boorger.exceptions;

public class AppJWTException extends AppBaseException {
    static final public String KEY = "error.jwt";

    public AppJWTException() {
        super(KEY);
    }
}
