package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Restaurant;

import java.util.List;
import java.util.Optional;

@RepositoryTransaction
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @RepositoryReadOnlyTransaction
    List<Restaurant> findAllByOrderByName();

    @RepositoryReadOnlyTransaction
    List<Restaurant> findAllByNameIgnoreCaseContaining(String filter);

    @RepositoryReadOnlyTransaction
    Optional<Restaurant> findByName(String name);
}
