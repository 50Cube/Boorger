package pl.lodz.p.it.boorger.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends AccessLevel {

    public Client() {
        super();
    }

//    @OneToMany
//    private Collection<ClientRestaurants> clientRestaurantsById;
//
//    @OneToMany
//    private Collection<Opinion> opinionsById;
//
//    @OneToMany
//    private Collection<Reservation> reservationsById;
}
