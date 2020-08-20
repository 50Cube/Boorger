package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.AccountToken;

import java.util.Optional;

@RepositoryTransaction
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {

    @RepositoryReadOnlyTransaction
    Optional<AccountToken> findByBusinessKey(String token);
}
