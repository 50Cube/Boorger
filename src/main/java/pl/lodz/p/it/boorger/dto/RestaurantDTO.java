package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class RestaurantDTO {

    private long version;
    private String creationDate;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*,. -]+")
    private String name;

    @Size(min = 1, max = 255)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*,. -]+")
    private String description;

    @Digits(integer = 2, fraction = 0)
    private int installment;
    private boolean active;
    private byte[] photo;
    private AddressDTO addressDTO;
}
