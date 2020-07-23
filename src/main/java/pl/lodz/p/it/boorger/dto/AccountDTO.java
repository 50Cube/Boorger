package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
public class AccountDTO {

    @NotBlank private String login;
    @NotBlank private String password;
    @NotNull  private boolean active;
    @NotNull  private boolean confirmed;
    @NotBlank private String language;
    @NotBlank private String firstname;
    @NotBlank private String lastname;
    @NotBlank private String email;
    @NotNull  private Collection<String> accessLevels;
}
