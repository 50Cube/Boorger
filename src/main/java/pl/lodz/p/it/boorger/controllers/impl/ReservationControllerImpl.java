package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.ReservationController;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.dto.mappers.ReservationMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.ReservationService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class ReservationControllerImpl implements ReservationController {

    private ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationDTO reservationDTO, @RequestHeader("lang") String language) throws AppBaseException {
        reservationService.addReservation(ReservationMapper.mapFromDto(reservationDTO), reservationDTO.getClientDTO().getLogin(),
                reservationDTO.getRestaurantName(), reservationDTO.getTableNumber(),
                reservationDTO.getDishDTOs().stream().map(DishDTO::getBusinessKey).collect(Collectors.toList()));
        return ResponseEntity.ok(MessageProvider.getTranslatedText("reservation.addnew", language));
    }

    @GetMapping("/reservations")
    public List<ReservationDTO> getReservations() throws AppBaseException {
        return reservationService.getReservations().stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/reservations/{login}")
    public List<ReservationDTO> getUserReservations(@PathVariable String login) throws AppBaseException {
        return reservationService.getUserReservations(login).stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }
}
