package pl.lodz.p.it.boorger.exceptions;

public class OptimisticLockException extends AppBaseException {
    static final public String KEY = "error.optimistic.lock";

    public OptimisticLockException() {
        super(KEY);
    }
}
