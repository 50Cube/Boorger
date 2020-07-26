package pl.lodz.p.it.boorger.exceptions.abstraction;

public abstract class AppBaseException extends Exception {
    static final public String KEY = "error.default";

    public AppBaseException() {
        super(KEY);
    }
}
