package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.boorger.dto.payment.PaymentDTO;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

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
    private String status;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@*,. -]+")
    private String restaurantName;

    @Digits(integer = 2, fraction = 0)
    private int tableNumber;
    private ClientDTO clientDTO;
    private Collection<DishDTO> dishDTOs;
    private PaymentDTO paymentDTO;
}
