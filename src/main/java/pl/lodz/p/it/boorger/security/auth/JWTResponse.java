package pl.lodz.p.it.boorger.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JWTResponse {

    private String token;
    private String login;
    private List<String> accessLevels;
    private List<String> messages;
}
