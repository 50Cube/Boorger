package pl.lodz.p.it.boorger.exceptions;

public class CaptchaException extends AppBaseException {
    static final public String KEY = "error.captcha";

    public CaptchaException() {
        super(KEY);
    }
}
