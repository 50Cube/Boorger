package pl.lodz.p.it.boorger.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "boorger")
public class CustomProperties {

    private String clientRole;
    private String managerRole;
    private String adminRole;

    private int jwtExpirationMs;
    private String jwtHeaderString;
    private String jwtTokenPrefix;
    private String jwtSecret;

    private int failedAuthCounter;

    private String confirmToken;
    private String resetToken;
    private int confirmTokenExpirationTime;
    private int resetTokenExpirationTime;

    private String googleCaptchaApi;
    private String recaptchaSecret;
}
