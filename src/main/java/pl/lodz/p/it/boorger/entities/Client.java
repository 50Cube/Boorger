package pl.lodz.p.it.boorger.entities;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@DiscriminatorValue("CLIENT")
public class Client extends AccessLevel {

    public Client() {
        super();
    }

//    @OneToMany
//    private Collection<ClientRestaurants> clientRestaurants;
//
//    @OneToMany
//    private Collection<Opinion> opinions;

    @OneToMany(mappedBy = "client")
    private Collection<Reservation> reservations = new ArrayList<>();

}
