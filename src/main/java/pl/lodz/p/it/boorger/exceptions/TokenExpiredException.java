package pl.lodz.p.it.boorger.exceptions;

public class TokenExpiredException extends AppBaseException {
    static final public String KEY = "error.token.expired";

    public TokenExpiredException() {
        super(KEY);
    }
}
