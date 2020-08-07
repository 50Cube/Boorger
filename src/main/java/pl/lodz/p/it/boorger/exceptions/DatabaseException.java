package pl.lodz.p.it.boorger.exceptions;

public class DatabaseException extends AppBaseException {
    static final public String KEY = "error.database";

    public DatabaseException() {
        super(KEY);
    }
}
