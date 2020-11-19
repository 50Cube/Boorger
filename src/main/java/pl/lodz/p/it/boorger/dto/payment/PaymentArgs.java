package pl.lodz.p.it.boorger.dto.payment;

import lombok.Data;

@Data
public class PaymentArgs {

    private String paymentId;
    private String payerId;
    private String token;
}
