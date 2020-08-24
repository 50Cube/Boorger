package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.AccountRepository;
import pl.lodz.p.it.boorger.repositories.AccountTokenRepository;
import pl.lodz.p.it.boorger.repositories.AuthDataRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class AccountService {

    private AccountRepository accountRepository;
    private AuthDataRepository authDataRepository;
    private AccountTokenRepository accountTokenRepository;
    private PasswordEncoder passwordEncoder;
    private Environment env;

    @ServiceReadOnlyTransaction
    public Page<Account> getAccounts(int page) throws AppBaseException {
        try {
            return accountRepository.findAll(
                    PageRequest.of(page, Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.pageSize")))));
        } catch (DataAccessException e) {
                throw new DatabaseException();
            }
    }

    @ServiceReadOnlyTransaction
    public Account getAccountByLogin(String login) throws AppBaseException {
        try {
            return accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void register(@Valid Account account, @Valid AccountConfirmToken token) throws AppBaseException {
        try {
            accountRepository.save(account);
            accountTokenRepository.save(token);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("account_login_data_login_uindex"))
                throw new LoginAlreadyExistsException();
            if(Objects.requireNonNull(e.getMessage()).contains("account_personal_data_email_uindex"))
                throw new EmailAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editAccount(@Valid Account account) throws AppBaseException {
        try {
            accountRepository.save(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editLanguage(String login, String language) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
            account.setLanguage(language);
            accountRepository.save(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editAuthData(@Valid AuthData authData) throws AppBaseException {
        try {
            authDataRepository.save(authData);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void confirmAccount(String token) throws AppBaseException {
        AccountConfirmToken accountConfirmToken = (AccountConfirmToken) accountTokenRepository.findByBusinessKey(token)
                .orElseThrow(AppBaseException::new);
        if(accountConfirmToken.getExpireDate().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException();
        Account account = accountConfirmToken.getAccount();
        if(account.isConfirmed())
            throw new AccountAlreadyConfirmedException();
        account.setConfirmed(true);
        accountRepository.save(account);
        accountTokenRepository.delete(accountConfirmToken);
    }

    public Account getAccountByEmail(String email) throws AppBaseException {
        try {
            return accountRepository.findByEmail(email)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (AccountNotFoundException e) {
            log.warning("Password reset error: account with email " + email + " does not exists");
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void resetPassword(ForgotPasswordToken token, ForgotPasswordToken newToken) throws AppBaseException {
        try {
            if(token != null) {
                accountTokenRepository.delete(token);
                accountTokenRepository.flush();
            }
            accountTokenRepository.save(newToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void changeResetPassword(String token, AccountDTO accountDTO) throws AppBaseException {
        ForgotPasswordToken forgotPasswordToken = (ForgotPasswordToken) accountTokenRepository.findByBusinessKey(token)
                .orElseThrow(AppBaseException::new);

        if(forgotPasswordToken.getExpireDate().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException();

        Account account = forgotPasswordToken.getAccount();
        if(checkIfPasswordWasUsed(account, accountDTO.getPassword()))
            throw new PasswordAlreadyUsedException();

        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        PreviousPassword previousPassword = new PreviousPassword();
        previousPassword.setAccount(account);
        previousPassword.setPassword(accountDTO.getPassword());

        account.getPreviousPasswords().add(previousPassword);
        account.setPassword(accountDTO.getPassword());
        accountRepository.save(account);
        accountTokenRepository.delete(forgotPasswordToken);
    }

    private boolean checkIfPasswordWasUsed(Account account, String newPassword) {
        return account.getPreviousPasswords().stream().anyMatch(p -> passwordEncoder.matches(newPassword, p.getPassword()));
    }
}
