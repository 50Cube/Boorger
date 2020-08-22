package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AccountController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.ForgotPasswordToken;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.AccountService;
import pl.lodz.p.it.boorger.utils.EmailService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AccountControllerImpl implements AccountController {

    private AccountService accountService;
    private EmailService emailService;
    private Environment env;

    @GetMapping("/accounts/{page}")
    public List<AccountDTO> getAccounts(@PathVariable int page) throws AppBaseException {
        return accountService.getAccounts(page).stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/accounts/pageAmount")
    public int getAccountsPageAmount() throws AppBaseException {
        return accountService.getAccounts(0).getTotalPages();
    }

    @PutMapping("language/{login}")
    public void changeLanguage(@PathVariable String login, @RequestHeader("lang") String language) throws AppBaseException {
        accountService.editLanguage(login, language);
    }

    @PostMapping("reset/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @RequestHeader("lang") String language, HttpServletRequest request) throws AppBaseException {
        Account account = accountService.getAccountByEmail(email);
        if(account != null) {
            ForgotPasswordToken token;
            if(account.getAccountTokens().stream().noneMatch(t -> t.getTokenType().equals(env.getProperty("boorger.resetToken")))) {
                token = generateForgotPasswordToken(account);
            } else {
                token = (ForgotPasswordToken) account.getAccountTokens().stream()
                        .filter(t -> t.getTokenType().equals(env.getProperty("boorger.resetToken"))).findFirst().get();
                token.setExpireDate(LocalDateTime.now()
                        .plusMinutes(Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.resetTokenExpirationTime")))));
            }
            account.getAccountTokens().add(token);
            accountService.editAccount(account);
            accountService.editForgotPasswordToken(token);
            try {
                emailService.sendPasswordResetEmail(email, language, token.getBusinessKey(),
                        request.getRequestURL().toString(), request.getServletPath());
            } catch (MessagingException e) {
                log.severe("An error occurred while sending email");
            }
        }
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.reset", language));
    }

    private ForgotPasswordToken generateForgotPasswordToken(Account account) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setAccount(account);
        token.setTokenType(env.getProperty("boorger.resetToken"));
        token.setExpireDate(LocalDateTime.now()
                .plusMinutes(Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.resetTokenExpirationTime")))));
        return token;
    }

    @PostMapping("/changePassword/{login}/{token}")
    public ResponseEntity<?> changePassword(@PathVariable String login, @PathVariable String token, @RequestBody Object object, @PathVariable String captcha) throws AppBaseException {
        log.info("login = " + login);
        log.info("token = " + token);
        log.info("obj = " + object);
        return null;
    }
}
