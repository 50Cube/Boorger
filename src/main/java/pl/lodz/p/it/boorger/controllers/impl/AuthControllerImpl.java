package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AuthController;
import pl.lodz.p.it.boorger.entities.AccessLevel;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.AuthData;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.FailedAuthException;
import pl.lodz.p.it.boorger.security.auth.JWTResponse;
import pl.lodz.p.it.boorger.security.auth.LoginRequest;
import pl.lodz.p.it.boorger.security.jwt.JWTUtils;
import pl.lodz.p.it.boorger.security.services.UserDetailsImpl;
import pl.lodz.p.it.boorger.services.AccountService;
import pl.lodz.p.it.boorger.utils.DateFormatter;
import pl.lodz.p.it.boorger.utils.EmailService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AuthControllerImpl implements AuthController {

    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JWTUtils jwtUtils;
    private Environment env;
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws AppBaseException {
        Authentication authentication;
        Account account = new Account();

        try {
            account = accountService.getAccountByLogin(loginRequest.getLogin());
            account.getAuthData().setFailedAuthCounter(account.getAuthData().getFailedAuthCounter() + 1);
            accountService.editAuthData(account.getAuthData());

            if (account.getAuthData().getFailedAuthCounter() ==
                    Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.failedAuthCounter")))) {
                account.setActive(false);
                accountService.editAccount(account);
                emailService.sendAccountBlockedEmail(account.getEmail(), account.getLanguage());
                throw new FailedAuthException();
            }


            if(account.getAccessLevels().stream().map(AccessLevel::getAccessLevel).collect(Collectors.toList())
                    .contains(env.getProperty("boorger.roleAdmin"))) {
                emailService.sendAdminAuthEmail(account.getEmail(), account.getLanguage(), request.getRemoteAddr());
            }

        } catch (FailedAuthException e) {
            throw e;
        } catch (AppBaseException e) {
            log.warning("Login attempt error. Account with username: " + loginRequest.getLogin() + " does not exists");
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.badcredentials", loginRequest.getLanguage()));
        }

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            account.getAuthData().setLastFailedAuth(LocalDateTime.now());
            accountService.editAuthData(account.getAuthData());
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.badcredentials", loginRequest.getLanguage()));
        } catch (LockedException | DisabledException e) {
            account.getAuthData().setLastFailedAuth(LocalDateTime.now());
            accountService.editAuthData(account.getAuthData());
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.account.inactive", loginRequest.getLanguage()));
        } catch (AuthenticationException e) {
            account.getAuthData().setLastFailedAuth(LocalDateTime.now());
            accountService.editAuthData(account.getAuthData());
            return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.default", loginRequest.getLanguage()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JWTResponse(
                jwt,
                prepareAuthDetails(account, loginRequest.getLanguage(), request.getRemoteAddr()),
                userDetails.getLanguage()));
    }

    private List<String> prepareAuthDetails(Account account, String language, String ip) throws AppBaseException {
        AuthData authData = account.getAuthData();
        List<String> messages = new ArrayList<>();
        String success = "";
        if(authData.getLastSuccessfulAuth() != null)
            success = MessageProvider.getTranslatedText("login.last.successful", language) + " "
                    + DateFormatter.dateToString(authData.getLastSuccessfulAuth());
        messages.add(success);

        String fail = "";
        if(authData.getLastFailedAuth() != null)
            fail = MessageProvider.getTranslatedText("login.last.failed", language) + " "
                    + DateFormatter.dateToString(authData.getLastFailedAuth());
        messages.add(fail);

        authData.setAccount(account);
        authData.setFailedAuthCounter(0);
        authData.setLastSuccessfulAuth(LocalDateTime.now());
        authData.setLastAuthIp(ip);
        account.setAuthData(authData);
        accountService.editAuthData(authData);

        return messages;
    }
}
