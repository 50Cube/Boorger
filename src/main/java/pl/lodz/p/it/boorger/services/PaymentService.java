package pl.lodz.p.it.boorger.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Retryable(value = TransactionException.class)
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = AppBaseException.class)
public class PaymentService {

    private APIContext context;

    @Transactional(propagation = Propagation.MANDATORY)
    public Payment createPayment(double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(new Amount(
                currency, String.valueOf(new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue())));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment(intent, payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(context);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(context, paymentExecution);
    }
}
