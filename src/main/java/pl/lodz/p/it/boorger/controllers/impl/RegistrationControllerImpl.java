package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.RegistrationController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.CaptchaException;
import pl.lodz.p.it.boorger.security.services.CaptchaValidator;
import pl.lodz.p.it.boorger.services.AccountService;
import pl.lodz.p.it.boorger.utils.EmailService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log
@CrossOrigin
@RestController
@AllArgsConstructor
public class RegistrationControllerImpl implements RegistrationController {

    private AccountService accountService;
    private BCryptPasswordEncoder passwordEncoder;
    private CaptchaValidator captchaValidator;
    private EmailService emailService;
    private Environment env;

    @PostMapping("/register/{captcha}")
    public ResponseEntity<?> register(@Valid @RequestBody AccountDTO accountDTO, @PathVariable String captcha, HttpServletRequest request) throws AppBaseException {
        if(!captchaValidator.validateCaptcha(captcha))
            throw new CaptchaException();

        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        Account account = AccountMapper.mapFromDto(accountDTO);
        account.setAccessLevels(generateAccessLevels(account));
        account.setConfirmed(false);
        account.setActive(true);
        account.getAuthData().setAccount(account);

        AccountConfirmToken token = generateConfirmToken(account);
        account.setAccountTokens(new ArrayList<>());
        account.getAccountTokens().add(token);

        PreviousPassword previousPassword = new PreviousPassword();
        previousPassword.setAccount(account);
        previousPassword.setPassword(account.getPassword());
        account.setPreviousPasswords(new ArrayList<>());
        account.getPreviousPasswords().add(previousPassword);

        accountService.register(account, token);
        try {
            emailService.sendConfirmationEmail(account.getEmail(), account.getLanguage(),
                    token.getBusinessKey(), request.getRequestURL().toString(), request.getServletPath());
        } catch (MessagingException e) {
            log.severe("An error occurred while sending email");
        }
        return ResponseEntity.ok(MessageProvider.getTranslatedText("register.success", accountDTO.getLanguage()));
    }

    private List<AccessLevel> generateAccessLevels(Account account) {
        List<AccessLevel> list = new ArrayList<>();

        Client client = new Client();
        client.setAccount(account);
        client.setActive(true);
        client.setAccessLevel(env.getProperty("boorger.roleClient"));
        list.add(client);

        Manager manager = new Manager();
        manager.setAccount(account);
        manager.setActive(false);
        manager.setAccessLevel(env.getProperty("boorger.roleManager"));
        list.add(manager);

        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setActive(false);
        admin.setAccessLevel(env.getProperty("boorger.roleAdmin"));
        list.add(admin);

        return list;
    }

    private AccountConfirmToken generateConfirmToken(Account account) {
        AccountConfirmToken token = new AccountConfirmToken();
        token.setAccount(account);
        token.setTokenType(env.getProperty("boorger.confirmToken"));
        token.setExpireDate(LocalDateTime.now()
                .plusMinutes(Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.confirmTokenExpirationTime")))));
        return token;
    }

    @PostMapping(value = "/confirm/{token}")
    public ResponseEntity<?> confirmAccount(@PathVariable String token, @RequestHeader("lang") String language) throws AppBaseException {
        accountService.confirmAccount(token);
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.confirmed", language));
    }
}