package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class DishDTO {

    @Size(min = 1, max = 64)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@$^&*,. -]+")
    private String name;

    @Digits(integer = 5, fraction = 2)
    private double price;

    @Size(min = 1, max = 255)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*():,./\n -]+")
    private String description;
}
