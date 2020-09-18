package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.ReservationController;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.dto.mappers.ReservationMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.ReservationService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.validation.Valid;

@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class ReservationControllerImpl implements ReservationController {

    private ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationDTO reservationDTO, @RequestHeader("lang") String language) throws AppBaseException {
        reservationService.addReservation(ReservationMapper.mapFromDto(reservationDTO));
        return ResponseEntity.ok(MessageProvider.getTranslatedText("reservation.addnew", language));
    }
}
