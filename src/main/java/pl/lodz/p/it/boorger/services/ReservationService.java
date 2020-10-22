package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.ClientRepository;
import pl.lodz.p.it.boorger.repositories.DishRepository;
import pl.lodz.p.it.boorger.repositories.ReservationRepository;
import pl.lodz.p.it.boorger.repositories.RestaurantRepository;
import pl.lodz.p.it.boorger.security.services.SignatureService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RestaurantRepository restaurantRepository;
    private ClientRepository clientRepository;
    private DishRepository dishRepository;

    public void addReservation(@Valid Reservation reservation, String login, String restaurantName, int tableNumber, Collection<String> menuKeys) throws AppBaseException {
        try {
            if (reservation.getStartDate().isBefore(LocalDateTime.now()) || reservation.getEndDate().isBefore(LocalDateTime.now()))
                throw new DateException("error.date.past");
            if (reservation.getStartDate().isAfter(reservation.getEndDate()))
                throw new DateException("error.date.wrong");

            Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                    .orElseThrow(RestaurantNotFoundException::new);

            Table table = restaurant.getTables().stream().filter(t -> t.getNumber() == tableNumber).findFirst()
                    .orElseThrow(AppBaseException::new);
            table.getReservations().add(reservation);
            reservation.setTable(table);

            if (!restaurant.isActive() || !table.isActive())
                throw new AppBaseException("error.reservation.inactive");

            Client client = clientRepository.findByAccount_Login(login)
                    .orElseThrow(AccountNotFoundException::new);
            client.getReservations().add(reservation);
            reservation.setClient(client);

            Collection<Dish> menu = new ArrayList<>();
            for (String key : menuKeys)
                menu.add(dishRepository.findByBusinessKey(key).orElseThrow(AppBaseException::new));
            reservation.setMenu(menu);

            reservation.setStatus(Status.BOOKED);
            reservationRepository.saveAndFlush(reservation);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("reservation_dates_overlap"))
                throw new DateException("error.date.overlap");
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Reservation> getReservations() throws AppBaseException {
        try {
            return reservationRepository.findAllByOrderByStartDateDesc();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Reservation> getFilteredReservations(String filter) throws AppBaseException {
        try {
            return reservationRepository.findAllByOrderByStartDateDesc().stream()
                    .filter(r -> r.getBusinessKey().contains(filter)
                            || r.getClient().getAccount().getLogin().toLowerCase().contains(filter.toLowerCase())
                            || r.getTable().getRestaurant().getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Reservation> getUserReservations(String login) throws AppBaseException {
        try {
            return reservationRepository.findAllByClient_Account_LoginOrderByStartDateDesc(login);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Reservation> getUserFilteredReservation(String login, String filter) throws AppBaseException {
        try {
            return reservationRepository.findAllByClient_Account_LoginOrderByStartDateDesc(login).stream()
                    .filter(r -> r.getBusinessKey().contains(filter)
                    || r.getTable().getRestaurant().getName().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public Reservation getReservation(String businessKey) throws AppBaseException {
        try {
            return reservationRepository.findByBusinessKey(businessKey)
                    .orElseThrow(AppBaseException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void finishReservation(String businessKey, String signatureDTO) throws AppBaseException {
        try {
            Reservation reservation = reservationRepository.findByBusinessKey(businessKey)
                    .orElseThrow(AppBaseException::new);
            if (!SignatureService.verify(reservation.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            reservation.setStatus(Status.FINISHED);
            reservationRepository.saveAndFlush(reservation);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void cancelReservation(String businessKey, String signatureDTO) throws AppBaseException {
        try {
            Reservation reservation = reservationRepository.findByBusinessKey(businessKey)
                    .orElseThrow(AppBaseException::new);
            if (!SignatureService.verify(reservation.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            reservation.setStatus(Status.CANCELLED);
            reservationRepository.saveAndFlush(reservation);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
