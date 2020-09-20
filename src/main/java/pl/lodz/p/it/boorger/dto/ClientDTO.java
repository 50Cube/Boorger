package pl.lodz.p.it.boorger.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO {

    private String login;
    private String email;
    private String firstname;
    private String lastname;
}
