package pl.lodz.p.it.boorger.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("forgot_password_token")
public class ForgotPasswordToken extends AccountToken {

    public ForgotPasswordToken() {
        super();
    }
}
