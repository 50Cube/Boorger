package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.RepositoryTransaction;
import pl.lodz.p.it.boorger.entities.Reservation;

import java.util.List;
import java.util.Optional;

@RepositoryTransaction
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RepositoryReadOnlyTransaction
    List<Reservation> findAllByOrderByStartDateDesc();

    @RepositoryReadOnlyTransaction
    List<Reservation> findAllByClient_Account_LoginOrderByStartDateDesc(String login);

    @RepositoryReadOnlyTransaction
    Optional<Reservation> findByBusinessKey(String businessKey);

    @RepositoryReadOnlyTransaction
    Optional<Reservation> findByPaymentToken(String paymentToken);
}
