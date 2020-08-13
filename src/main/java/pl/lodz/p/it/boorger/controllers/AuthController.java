package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.security.auth.LoginRequest;

import javax.servlet.http.HttpServletRequest;

public interface AuthController {

    ResponseEntity<?> login(LoginRequest loginRequest, HttpServletRequest request) throws AppBaseException;
}
