package pl.lodz.p.it.boorger.configuration;

import com.paypal.base.rest.APIContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfig {

    @Value("${boorger.paymentMode}")
    private String mode;

    @Value("${boorger.paymentClientId}")
    private String clientId;

    @Value("${boorger.paymentClientSecret}")
    private String clientSecret;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode, paypalSdkConfig());
    }
}
