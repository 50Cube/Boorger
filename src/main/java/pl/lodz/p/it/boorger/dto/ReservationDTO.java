package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

@Data
@Builder
public class ReservationDTO {

    private String signature;
    private String creationDate;

    @Size(min = 36, max = 36)
    private String businessKey;

    @Digits(integer = 2, fraction = 0)
    private int guestNumber;
    private String startDate;
    private String endDate;

    @Digits(integer = 5, fraction = 2)
    private double totalPrice;
}
