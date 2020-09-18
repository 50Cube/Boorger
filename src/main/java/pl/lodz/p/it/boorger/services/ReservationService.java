package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.Reservation;
import pl.lodz.p.it.boorger.repositories.ReservationRepository;

import javax.validation.Valid;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class ReservationService {

    private ReservationRepository reservationRepository;

    public void addReservation(@Valid Reservation reservation) {

    }
}
