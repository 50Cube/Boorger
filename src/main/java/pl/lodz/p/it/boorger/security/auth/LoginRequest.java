package pl.lodz.p.it.boorger.security.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String login;

    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String password;

    @NotBlank
    @Size(min = 2, max = 2)
    private String language;
}
