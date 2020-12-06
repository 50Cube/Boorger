package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReservationController {

    ResponseEntity<?> addReservation(ReservationDTO ReservationDTO, String language, HttpServletRequest request) throws AppBaseException;
    List<ReservationDTO> getReservations(String language) throws AppBaseException;
    List<ReservationDTO> getFilteredReservation(String filter, String language) throws AppBaseException;
    List<ReservationDTO> getUserReservations(String login, String language) throws AppBaseException;
    List<ReservationDTO> getUserFilteredReservations(String login, String filter, String language) throws AppBaseException;
    ReservationDTO getReservation(String businessKey, String language) throws AppBaseException;
    void finishReservation(ReservationDTO reservationDTO, String language, HttpServletRequest request) throws AppBaseException;
    void cancelReservation(ReservationDTO reservationDTO, String language, HttpServletRequest request) throws AppBaseException;
}
