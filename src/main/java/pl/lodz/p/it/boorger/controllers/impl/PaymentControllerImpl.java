package pl.lodz.p.it.boorger.controllers.impl;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.PaymentController;
import pl.lodz.p.it.boorger.dto.payment.PaymentArgs;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.PaymentException;
import pl.lodz.p.it.boorger.services.PaymentService;
import pl.lodz.p.it.boorger.services.ReservationService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

@Log
@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class PaymentControllerImpl implements PaymentController {

    public static final String APPROVAL = "approved";

    private PaymentService paymentService;
    private ReservationService reservationService;

    @PostMapping("/cancelPayment/{token}")
    public void cancelPay(@PathVariable String token) throws AppBaseException {
        reservationService.cancelUnpaidReservation(token);
    }

    @PostMapping("/finishPayment")
    public ResponseEntity<?> successPayment(@RequestBody PaymentArgs paymentArgs, @RequestHeader("lang") String language) throws AppBaseException {
        try {
            Payment payment = paymentService.executePayment(paymentArgs.getPaymentId(), paymentArgs.getPayerId());
            if(payment.getState().equals(APPROVAL))
                return ResponseEntity.ok(MessageProvider.getTranslatedText("reservation.addnew", language));
        } catch (PayPalRESTException e) {
            log.info("An error occurred during making payment with ID: " + paymentArgs.getPaymentId());
            try {
                reservationService.cancelUnpaidReservation(paymentArgs.getToken());
            } catch (AppBaseException ex) {
                throw new PaymentException();
            }
            throw new PaymentException();
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }
}
