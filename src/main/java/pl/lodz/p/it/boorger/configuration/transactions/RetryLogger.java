package pl.lodz.p.it.boorger.configuration.transactions;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.listener.RetryListenerSupport;

import java.util.Collections;
import java.util.List;

@Log
@Configuration
public class RetryLogger {

    @Bean
    public List<RetryListener> retryListeners() {
        return Collections.singletonList(new RetryListenerSupport() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.info("Transaction is being repeated for " + context.getRetryCount() + " time");
            }
        });
    }
}
