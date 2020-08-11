package pl.lodz.p.it.boorger.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("account_confirm_token")
public class AccountConfirmToken extends AccountToken {

    public AccountConfirmToken() {
        super();
    }
}
