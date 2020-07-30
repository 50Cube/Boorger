package pl.lodz.p.it.boorger.security.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
    private String language;
}
