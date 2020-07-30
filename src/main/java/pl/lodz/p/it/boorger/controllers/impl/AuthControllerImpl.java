package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AuthController;
import pl.lodz.p.it.boorger.security.auth.JWTResponse;
import pl.lodz.p.it.boorger.security.auth.LoginRequest;
import pl.lodz.p.it.boorger.security.jwt.JWTUtils;
import pl.lodz.p.it.boorger.security.services.UserDetailsImpl;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
public class AuthControllerImpl implements AuthController {

    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.badcredentials", loginRequest.getLanguage()));
        } catch (LockedException | DisabledException e) {
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.account.inactive", loginRequest.getLanguage()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.default", loginRequest.getLanguage()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        List<String> messages = new ArrayList<>();
        messages.add("Last suc ");
        messages.add("Last failed ");

        return ResponseEntity.ok(new JWTResponse(
                jwt,
                userDetails.getUsername(),
                roles,
                messages));
    }
}
