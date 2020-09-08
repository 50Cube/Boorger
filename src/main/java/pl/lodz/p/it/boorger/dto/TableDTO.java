package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;

@Data
@Builder
public class TableDTO {

    @Digits(integer = 2, fraction = 0)
    private int number;

    @Digits(integer = 1, fraction = 0)
    private int capacity;
    private boolean active;
}
