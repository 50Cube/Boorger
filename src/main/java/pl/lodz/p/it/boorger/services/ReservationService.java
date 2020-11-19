package pl.lodz.p.it.boorger.services;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.dto.payment.PaymentDTO;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.ClientRepository;
import pl.lodz.p.it.boorger.repositories.DishRepository;
import pl.lodz.p.it.boorger.repositories.ReservationRepository;
import pl.lodz.p.it.boorger.repositories.RestaurantRepository;
import pl.lodz.p.it.boorger.security.services.SignatureService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class ReservationService {

    public static final String SUCCESS_URL = "/paymentSucceeded";
    public static final String CANCEL_URL = "/paymentCancelled";
    public static final String APPROVAL_URL = "approval_url";
    public static final String TOKEN_PARAMETER = "token=";

    private ReservationRepository reservationRepository;
    private RestaurantRepository restaurantRepository;
    private ClientRepository clientRepository;
    private DishRepository dishRepository;
    private PaymentService paymentService;

    public String addReservation(@Valid Reservation reservation, String login, String restaurantName, int tableNumber,
                               Collection<String> menuKeys, PaymentDTO paymentDTO, String url, String path) throws AppBaseException {
        try {
            if(reservation.getStartDate().getHour() > reservation.getEndDate().getHour()
                || (reservation.getStartDate().getHour() == reservation.getEndDate().getHour() &&
                    reservation.getStartDate().getMinute() > reservation.getEndDate().getMinute()))
                reservation.setEndDate(reservation.getEndDate().plusDays(1));

            if (reservation.getStartDate().isBefore(LocalDateTime.now()) || reservation.getEndDate().isBefore(LocalDateTime.now()))
                throw new DateException("error.date.past");
            if (reservation.getStartDate().isAfter(reservation.getEndDate()))
                throw new DateException("error.date.wrong");

            Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                    .orElseThrow(RestaurantNotFoundException::new);

            if(checkIfRestaurantIsClosed(restaurant.getHours(), reservation.getStartDate(), reservation.getEndDate()))
                throw new DateException("error.reservation.closed");

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

            String paymentLink = "";
            String urlString = url.substring(0, url.length() - path.length());
            try {
                Payment payment = paymentService.createPayment(
                        paymentDTO.getPrice(),
                        paymentDTO.getCurrency(),
                        paymentDTO.getMethod(),
                        paymentDTO.getIntent(),
                        paymentDTO.getDescription(),
                        urlString + CANCEL_URL,
                        urlString + SUCCESS_URL);
                paymentLink = payment.getLinks().stream().filter(link -> link.getRel().equals(APPROVAL_URL)).findFirst().get().getHref();
            } catch (PayPalRESTException e) {

                throw new PaymentException();
            }
            String tokenArg;
            if(paymentLink.contains(TOKEN_PARAMETER))
                tokenArg =  paymentLink.substring(paymentLink.indexOf(TOKEN_PARAMETER) + TOKEN_PARAMETER.length());
            else throw new PaymentException();

            reservation.setPaymentToken(tokenArg);
            reservation.setStatus(Status.BOOKED);
            reservationRepository.saveAndFlush(reservation);
            return paymentLink;
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("reservation_dates_overlap"))
                throw new DateException("error.date.overlap");
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
        return null;
    }

    private boolean checkIfRestaurantIsClosed(Hours hours, LocalDateTime start, LocalDateTime end) {
        Pair<LocalDateTime, LocalDateTime> openingHours;

        switch(start.getDayOfWeek().getValue() + 1) {
            case Calendar.MONDAY:
                openingHours = calculateOpeningHours(hours.getMondayStart(), hours.getMondayEnd(), start.toLocalDate());
                break;
            case Calendar.TUESDAY:
                openingHours = calculateOpeningHours(hours.getTuesdayStart(), hours.getTuesdayEnd(), start.toLocalDate());
                break;
            case Calendar.WEDNESDAY:
                openingHours = calculateOpeningHours(hours.getWednesdayStart(), hours.getWednesdayEnd(), start.toLocalDate());
                break;
            case Calendar.THURSDAY:
                openingHours = calculateOpeningHours(hours.getThursdayStart(), hours.getThursdayEnd(), start.toLocalDate());
                break;
            case Calendar.FRIDAY:
                openingHours = calculateOpeningHours(hours.getFridayStart(), hours.getFridayEnd(), start.toLocalDate());
                break;
            case Calendar.SATURDAY:
                openingHours = calculateOpeningHours(hours.getSaturdayStart(), hours.getSaturdayEnd(), start.toLocalDate());
                break;
            case Calendar.SATURDAY + 1:
                openingHours = calculateOpeningHours(hours.getSundayStart(), hours.getSundayEnd(), start.toLocalDate());
                break;
            default:
                openingHours = null;
                break;
        }
        return start.isBefore(openingHours.getFirst()) || end.isAfter(openingHours.getSecond());
    }

    private Pair<LocalDateTime, LocalDateTime> calculateOpeningHours(LocalTime dayStart, LocalTime dayEnd, LocalDate start) {
        if(dayStart.isAfter(dayEnd))
            return Pair.of(LocalDateTime.of(start, dayStart), LocalDateTime.of(start.plusDays(1), dayEnd));
        else return Pair.of(LocalDateTime.of(start, dayStart), LocalDateTime.of(start, dayEnd));
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

    public void cancelUnpaidReservation(String paymentToken) throws AppBaseException {
        try {
            Reservation reservation = reservationRepository.findByPaymentToken(paymentToken)
                    .orElseThrow(AppBaseException::new);
            reservation.setStatus(Status.CANCELLED);
            reservationRepository.saveAndFlush(reservation);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
