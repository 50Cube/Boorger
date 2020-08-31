package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Account;

import java.util.Optional;

@RepositoryTransaction
public interface AccountRepository extends JpaRepository<Account, Long> {

    @RepositoryReadOnlyTransaction
    Optional<Account> findByLogin(String login);

    @RepositoryReadOnlyTransaction
    Optional<Account> findByEmail(String email);

    @RepositoryReadOnlyTransaction
    Page<Account> findAll(Pageable pageable);
}
