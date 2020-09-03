package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Dish;

import java.util.List;

@RepositoryTransaction
public interface DishRepository extends JpaRepository<Dish, Long> {

    @RepositoryReadOnlyTransaction
    List<Dish> findAll();
}
