package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Table;

import java.util.List;

@RepositoryTransaction
public interface TableRepository extends JpaRepository<Table, Long> {

    @RepositoryReadOnlyTransaction
    List<Table> findAll();

    @RepositoryReadOnlyTransaction
    List<Table> findAllByRestaurantName(String restaurantName);
}
