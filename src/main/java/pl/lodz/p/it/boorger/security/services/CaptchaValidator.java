package pl.lodz.p.it.boorger.security.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CaptchaValidator {

    private Environment env;

    public boolean validateCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", env.getProperty("boorger.recaptchaSecret"));
        requestMap.add("response", captchaResponse);
        CaptchaResponse response = restTemplate.postForObject(
                Objects.requireNonNull(env.getProperty("boorger.googleCaptchaApi")), requestMap, CaptchaResponse.class);
        return Boolean.TRUE.equals(response.getSuccess());
    }

    @Data
    private static class CaptchaResponse {
        private Boolean success;
        private LocalDateTime timestamp;
        private String hostname;
        @JsonProperty("error-codes")
        private List<String> errorCodes;
    }
}
