package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

public interface ReservationController {

    ResponseEntity<?> addReservation(ReservationDTO reservationDTO, String language) throws AppBaseException;
}
