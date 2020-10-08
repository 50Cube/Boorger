package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface ReservationController {

    ResponseEntity<?> addReservation(ReservationDTO reservationDTO, String language) throws AppBaseException;
    List<ReservationDTO> getReservations() throws AppBaseException;
    List<ReservationDTO> getUserReservations(String login) throws AppBaseException;
}
