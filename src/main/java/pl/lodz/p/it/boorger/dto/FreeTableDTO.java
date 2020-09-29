package pl.lodz.p.it.boorger.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class FreeTableDTO {

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@*,. -]+")
    private String restaurantName;
    private String startDate;
    private String endDate;
}
