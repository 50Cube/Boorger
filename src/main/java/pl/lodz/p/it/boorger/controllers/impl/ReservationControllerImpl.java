package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public List<ReservationDTO> getReservations() throws AppBaseException {
        return reservationService.getReservations().stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/reservations/filtered/{filter}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public List<ReservationDTO> getFilteredReservation(@PathVariable String filter) throws AppBaseException {
        return reservationService.getFilteredReservations(filter).stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/reservations/{login}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager')) or #login == authentication.principal.username")
    public List<ReservationDTO> getUserReservations(@PathVariable String login) throws AppBaseException {
        return reservationService.getUserReservations(login).stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/reservations/filtered/{login}/{filter}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager')) or #login == authentication.principal.username")
    public List<ReservationDTO> getUserFilteredReservations(@PathVariable String login, @PathVariable String filter) throws AppBaseException {
        return reservationService.getUserFilteredReservation(login, filter).stream().map(ReservationMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/reservation/{login}/{businessKey}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager')) or #login == authentication.principal.username")
    public ReservationDTO getReservation(@PathVariable String businessKey) throws AppBaseException {
        return ReservationMapper.mapToDto(reservationService.getReservation(businessKey));
    }

    @PutMapping("/reservation/finish/{businessKey}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public void finishReservation(@PathVariable String businessKey) throws AppBaseException {
        reservationService.finishReservation(businessKey);
    }

    @PutMapping("/reservation/cancel/{businessKey}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public void cancelReservation(@PathVariable String businessKey) throws AppBaseException {
        reservationService.cancelReservation(businessKey);
    }
}
