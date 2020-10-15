package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Reservation;

import java.util.List;

@RepositoryTransaction
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RepositoryReadOnlyTransaction
    List<Reservation> findAllByOrderByStartDateDesc();

    @RepositoryReadOnlyTransaction
    List<Reservation> findAllByClient_Account_LoginOrderByStartDateDesc(String login);
}
