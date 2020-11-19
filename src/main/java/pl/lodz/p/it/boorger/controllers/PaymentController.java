package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.payment.PaymentArgs;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

public interface PaymentController {
    void cancelPay(String token) throws AppBaseException;
    ResponseEntity<?> successPayment(PaymentArgs paymentArgs, String language) throws AppBaseException;
}
