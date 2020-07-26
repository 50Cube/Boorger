package pl.lodz.p.it.boorger.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends AccessLevel {

    public Manager() {
        super();
    }
}
