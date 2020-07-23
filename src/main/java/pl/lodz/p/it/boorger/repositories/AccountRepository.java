package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
