package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class AuthDataDTO {

    @NotNull
    @Digits(integer = 1, fraction = 0)
    private int failedAuthCounter;
    private LocalDateTime lastSuccessfulAuth;
    private LocalDateTime lastFailedAuth;

    @Size(max = 64)
    @Pattern(regexp = "[0-9:.]+")
    private String lastAuthIp;
}
