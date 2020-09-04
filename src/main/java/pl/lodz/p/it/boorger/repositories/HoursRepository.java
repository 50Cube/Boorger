package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Hours;

@RepositoryTransaction
public interface HoursRepository extends JpaRepository<Hours, Long> {
}
