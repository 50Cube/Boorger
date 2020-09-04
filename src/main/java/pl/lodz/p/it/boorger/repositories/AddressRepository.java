package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Address;

import java.util.Optional;

@RepositoryTransaction
public interface AddressRepository extends JpaRepository<Address, Long> {

    @RepositoryReadOnlyTransaction
    Optional<Address> findByCityAndStreetAndStreetNo(String city, String street, int streetNo);
}
