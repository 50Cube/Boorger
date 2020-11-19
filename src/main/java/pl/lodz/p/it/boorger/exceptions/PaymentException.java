package pl.lodz.p.it.boorger.exceptions;

public class PaymentException extends AppBaseException {
    static final public String KEY = "error.payment";

    public PaymentException() {
        super(KEY);
    }
}
