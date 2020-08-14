package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
public class AccountDTO {

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String login;

    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String password;

    @NotNull
    private boolean active;

    @NotNull
    private boolean confirmed;

    @NotBlank
    @Size(min = 2, max = 2)
    private String language;

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String firstname;

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9`-]+")
    private String lastname;

    @Email
    @NotBlank
    private String email;

    @Builder.Default
    private Collection<String> accessLevels = new ArrayList<>();
    private AuthDataDTO authData;
}
