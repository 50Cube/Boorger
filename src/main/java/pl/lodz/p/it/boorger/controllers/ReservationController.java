package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface ReservationController {

    ResponseEntity<?> addReservation(ReservationDTO reservationDTO, String language) throws AppBaseException;
    List<ReservationDTO> getReservations() throws AppBaseException;
    List<ReservationDTO> getFilteredReservation(String filter) throws AppBaseException;
    List<ReservationDTO> getUserReservations(String login) throws AppBaseException;
    List<ReservationDTO> getUserFilteredReservations(String login, String filter) throws AppBaseException;
    ReservationDTO getReservation(String businessKey) throws AppBaseException;
    void finishReservation(String businessKey) throws AppBaseException;
    void cancelReservation(String businessKey) throws AppBaseException;
}
