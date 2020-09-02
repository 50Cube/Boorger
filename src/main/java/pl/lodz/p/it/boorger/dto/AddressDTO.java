package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class AddressDTO {

    private String creationDate;

    @Size(min = 36, max = 36)
    private String businessKey;

    @Size(min = 1, max = 64)
    @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]+")
    private String city;

    @Size(min = 1, max = 64)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]+")
    private String street;

    @Digits(integer = 4, fraction = 0)
    private int streetNo;
}
