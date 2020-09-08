package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

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

    @Digits(integer = 3, fraction = 0)
    private int installment;
    private boolean active;
    private String photo;
    private AddressDTO addressDTO;
    private HoursDTO hoursDTO;
    private Collection<TableDTO> tableDTOs;
}
