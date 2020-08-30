package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.RegistrationController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.CaptchaException;
import pl.lodz.p.it.boorger.security.services.CaptchaValidator;
import pl.lodz.p.it.boorger.services.AccountService;
import pl.lodz.p.it.boorger.utils.EmailService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Log
@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class RegistrationControllerImpl implements RegistrationController {

    private AccountService accountService;
    private BCryptPasswordEncoder passwordEncoder;
    private CaptchaValidator captchaValidator;
    private EmailService emailService;

    @PostMapping("/register/{captcha}")
    public ResponseEntity<?> register(@Valid @RequestBody AccountDTO accountDTO, @PathVariable String captcha, HttpServletRequest request) throws AppBaseException {
        if(!captchaValidator.validateCaptcha(captcha))
            throw new CaptchaException();

        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        String token = accountService.register(AccountMapper.mapFromDto(accountDTO));
        try {
            emailService.sendConfirmationEmail(accountDTO.getEmail(), accountDTO.getLanguage(),
                    token, request.getRequestURL().toString(), request.getServletPath());
        } catch (MessagingException e) {
            log.severe("An error occurred while sending email");
        }
        return ResponseEntity.ok(MessageProvider.getTranslatedText("register.success", accountDTO.getLanguage()));
    }

    @PostMapping(value = "/confirm/{token}")
    public ResponseEntity<?> confirmAccount(@PathVariable String token, @RequestHeader("lang") String language) throws AppBaseException {
        accountService.confirmAccount(token);
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.confirmed", language));
    }
}
