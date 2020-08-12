package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class AuthDataDTO {

    @NotNull
    private int failedAuthCounter;
    private LocalDateTime lastSuccessfulAuth;
    private LocalDateTime lastFailedAuth;
    private String lastAuthIp;
}
