package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Client;

import java.util.Optional;

@RepositoryTransaction
public interface ClientRepository extends JpaRepository<Client, Long> {

    @RepositoryReadOnlyTransaction
    Optional<Client> findByAccount_Login(String login);
}
