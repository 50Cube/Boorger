package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.ClientRepository;
import pl.lodz.p.it.boorger.repositories.ReservationRepository;
import pl.lodz.p.it.boorger.repositories.RestaurantRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RestaurantRepository restaurantRepository;
    private ClientRepository clientRepository;

    public void addReservation(@Valid Reservation reservation, String login, String restaurantName, int tableNumber) throws AppBaseException {
        try {
            if(reservation.getStartDate().isBefore(LocalDateTime.now()) || reservation.getEndDate().isBefore(LocalDateTime.now()))
                throw new DateException("error.date.past");
            if(reservation.getStartDate().isAfter(reservation.getEndDate()))
                throw new DateException("error.date.wrong");

            Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                    .orElseThrow(RestaurantNotFoundException::new);

            Table table = restaurant.getTables().stream().filter(t -> t.getNumber() == tableNumber).findFirst()
                        .orElseThrow(AppBaseException::new);
            table.getReservations().add(reservation);
            reservation.setTable(table);

            if(!restaurant.isActive() || !table.isActive())
                throw new AppBaseException("error.reservation.inactive");

            Client client = clientRepository.findByAccount_Login(login)
                    .orElseThrow(AccountNotFoundException::new);
            client.getReservations().add(reservation);
            reservation.setClient(client);

            reservation.setStatus(Status.BOOKED);
            reservationRepository.saveAndFlush(reservation);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
