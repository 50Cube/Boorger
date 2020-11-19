package pl.lodz.p.it.boorger.dto.payment;

import lombok.Data;

@Data
public class PaymentDTO {

    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
