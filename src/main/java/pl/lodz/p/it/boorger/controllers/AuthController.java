package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.security.auth.LoginRequest;

public interface AuthController {

    ResponseEntity<?> login(LoginRequest loginRequest);
}
