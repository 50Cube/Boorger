package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AccountController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.ForgotPasswordToken;
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
    private CaptchaValidator captchaValidator;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/account/{login}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin')) or #login == authentication.principal.username")
    public AccountDTO getAccount(@PathVariable String login) throws AppBaseException {
        return AccountMapper.mapToDto(accountService.getAccount(login));
    }

    @GetMapping("/accounts/{page}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public List<AccountDTO> getAccounts(@PathVariable int page) throws AppBaseException {
        return accountService.getAccounts(page).stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/accounts/pageAmount")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public int getAccountsPageAmount() throws AppBaseException {
        return accountService.getAccounts(0).getTotalPages();
    }

    @GetMapping("/accounts/{page}/{filter}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public List<AccountDTO> getFilteredAccounts(@PathVariable int page, @PathVariable String filter) throws AppBaseException {
        return accountService.getFilteredAccounts(page, filter).stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/accounts/pageAmount/{filter}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public int getFilteredAccountsPageAmount(@PathVariable String filter) throws AppBaseException {
        return accountService.getFilteredAccounts(0, filter).getTotalPages();
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
            ForgotPasswordToken oldToken = null;
            if(account.getAccountTokens().stream().anyMatch(t -> t.getTokenType().equals(env.getProperty("boorger.resetToken")))) {
                oldToken = (ForgotPasswordToken) account.getAccountTokens().stream()
                        .filter(t -> t.getTokenType().equals(env.getProperty("boorger.resetToken"))).findFirst().get();
                account.getAccountTokens().remove(oldToken);
            }
            token = generateForgotPasswordToken(account);
            account.getAccountTokens().add(token);
            accountService.resetPassword(oldToken, token);
            accountService.editAccount(account);
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

    @PostMapping("/changeResetPassword/{token}/{captcha}")
    public ResponseEntity<?> changeResetPassword(@PathVariable String token, @Valid @RequestBody AccountDTO accountDTO,
                                            @PathVariable String captcha, HttpServletRequest request) throws AppBaseException {
        if(!captchaValidator.validateCaptcha(captcha))
            throw new CaptchaException();
        accountService.changeResetPassword(token, AccountMapper.mapFromDto(accountDTO), request.getRemoteUser());
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.password.changed", accountDTO.getLanguage()));
    }

    @PostMapping("/changePassword/{captcha}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDTO accountDTO, @PathVariable String captcha, HttpServletRequest request) throws AppBaseException {
        if(!captchaValidator.validateCaptcha(captcha))
            throw new CaptchaException();
        accountService.changePassword(AccountMapper.mapFromDto(accountDTO), accountDTO.getPreviousPassword(), request.getRemoteUser());
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.password.changed", accountDTO.getLanguage()));
    }

    @PutMapping("/editPersonal")
    public void editAccount(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException {
        accountService.editPersonal(AccountMapper.mapFromDto(accountDTO), accountDTO.getSignature(), request.getRemoteUser());
    }

    @PostMapping("/resendEmail")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public void resendConfirmationEmail(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException {
        String token = accountService.resendConfirmationEmail(AccountMapper.mapFromDto(accountDTO));
        try {
            emailService.sendConfirmationEmail(accountDTO.getEmail(), accountDTO.getLanguage(), token,
                    request.getRequestURL().toString(), request.getServletPath());
        } catch (MessagingException e) {
            log.severe("An error occurred while sending email");
        }
    }

    @PutMapping("/editOtherAccount")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public void editOtherAccount(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException {
        accountService.editOtherAccount(AccountMapper.mapFromDto(accountDTO),
                accountDTO.getAccessLevels(), accountDTO.getSignature(), request.getRemoteUser());
    }

    @PostMapping("/addAccount")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleAdmin'))")
    public ResponseEntity<?> addAccount(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException {
        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        String token = accountService.addAccount(AccountMapper.mapFromDto(accountDTO), accountDTO.getAccessLevels(), request.getRemoteUser());
        try {
            emailService.sendConfirmationEmail(accountDTO.getEmail(), accountDTO.getLanguage(), token,
                    request.getRequestURL().toString(), request.getServletPath());
        } catch (MessagingException e) {
            log.severe("An error occurred while sending email");
        }
        return ResponseEntity.ok(MessageProvider.getTranslatedText("account.addnew", accountDTO.getLanguage()));
    }
}
