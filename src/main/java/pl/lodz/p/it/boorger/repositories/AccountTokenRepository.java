package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.entities.AccountToken;

public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {
}
