package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.validation.constraints.*;
import java.util.Collection;

@Data
@Builder
public class AccountDTO {

    private String signature;
    private String creationDate;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@$^&*]+")
    private String login;

    @Size(min = 8, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String password;

    @Size(min = 8, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*]+")
    private String previousPassword;
    private boolean active;
    private boolean confirmed;

    @Size(min = 2, max = 2)
    private String language;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+")
    private String firstname;

    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ`-]+")
    private String lastname;

    @Email
    private String email;

    @Singular
    private Collection<String> accessLevels;
    private String lastSuccessfulAuth;
    private String lastFailedAuth;

    @Size(max = 64)
    @Pattern(regexp = "[0-9:.]+")
    private String lastAuthIp;
}
