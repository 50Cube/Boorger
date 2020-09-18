package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.ReservationDTO;
import pl.lodz.p.it.boorger.entities.Reservation;
import pl.lodz.p.it.boorger.utils.DateFormatter;

public class ReservationMapper {

    public static ReservationDTO mapToDto(Reservation reservation) {
        return ReservationDTO.builder()
                .creationDate(DateFormatter.dateToString(reservation.getCreationDate()))
                .signature(reservation.getSignatureString())
                .guestNumber(reservation.getGuestNumber())
                .startDate(DateFormatter.dateToString(reservation.getStartDate()))
                .endDate(DateFormatter.dateToString(reservation.getEndDate()))
                .totalPrice(reservation.getTotalPrice())
                .businessKey(reservation.getBusinessKey())
                .build();
    }

    public static Reservation mapFromDto(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .guestNumber(reservationDTO.getGuestNumber())
                .startDate(DateFormatter.stringToDate(reservationDTO.getStartDate()))
                .endDate(DateFormatter.stringToDate(reservationDTO.getEndDate()))
                .totalPrice(reservationDTO.getTotalPrice())
                .build();
    }
}
