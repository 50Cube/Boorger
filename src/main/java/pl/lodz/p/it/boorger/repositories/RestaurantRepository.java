package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Restaurant;

import java.util.List;

@RepositoryTransaction
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @RepositoryReadOnlyTransaction
    List<Restaurant> findAll();
}
