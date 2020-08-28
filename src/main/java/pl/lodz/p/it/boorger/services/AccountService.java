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
    public Account getAccount(String login) throws AppBaseException {
        try {
            return accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

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
            accountRepository.saveAndFlush(account);
            accountTokenRepository.saveAndFlush(token);
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
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editLanguage(String login, String language) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
            account.setLanguage(language);
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editAuthData(@Valid AuthData authData) throws AppBaseException {
        try {
            authDataRepository.saveAndFlush(authData);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void confirmAccount(String token) throws AppBaseException {
        try {
            AccountConfirmToken accountConfirmToken = (AccountConfirmToken) accountTokenRepository.findByBusinessKey(token)
                    .orElseThrow(AppBaseException::new);
            if(accountConfirmToken.getExpireDate().isBefore(LocalDateTime.now()))
                throw new TokenExpiredException();
            Account account = accountConfirmToken.getAccount();
            if(account.isConfirmed())
                throw new AccountAlreadyConfirmedException();
            account.setConfirmed(true);
            accountRepository.saveAndFlush(account);
            accountTokenRepository.delete(accountConfirmToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
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
            accountTokenRepository.saveAndFlush(newToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void changeResetPassword(String token, Account editedAccount) throws AppBaseException {
        try {
            ForgotPasswordToken forgotPasswordToken = (ForgotPasswordToken) accountTokenRepository.findByBusinessKey(token)
                    .orElseThrow(AppBaseException::new);
            if(forgotPasswordToken.getExpireDate().isBefore(LocalDateTime.now()))
                throw new TokenExpiredException();
            Account account = forgotPasswordToken.getAccount();
            addPreviousPassword(account, editedAccount);
            accountRepository.saveAndFlush(account);
            accountTokenRepository.delete(forgotPasswordToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    private boolean checkIfPasswordWasUsed(Account account, String newPassword) {
        return account.getPreviousPasswords().stream().anyMatch(p -> passwordEncoder.matches(newPassword, p.getPassword()));
    }

    private void addPreviousPassword(Account account, Account editedAccount) throws AppBaseException {
        if(checkIfPasswordWasUsed(account, editedAccount.getPassword()))
            throw new PasswordAlreadyUsedException();
        editedAccount.setPassword(passwordEncoder.encode(editedAccount.getPassword()));

        PreviousPassword previousPassword = new PreviousPassword();
        previousPassword.setAccount(account);
        previousPassword.setPassword(editedAccount.getPassword());

        account.getPreviousPasswords().add(previousPassword);
        account.setPassword(editedAccount.getPassword());
    }

    public void changePassword(Account editedAccount, String previous) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(editedAccount.getLogin())
                    .orElseThrow(AccountNotFoundException::new);
            if(!passwordEncoder.matches(previous, account.getPassword()))
                throw new IncorrectCurrentPasswordException();
            addPreviousPassword(account, editedAccount);
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editPersonal(Account editedAccount) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(editedAccount.getLogin())
                    .orElseThrow(AccountNotFoundException::new);
            account.setFirstname(editedAccount.getFirstname());
            account.setLastname(editedAccount.getLastname());
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public String resendConfirmationEmail(Account accountParam) throws AppBaseException {
        Account account = accountRepository.findByEmail(accountParam.getEmail())
                .orElseThrow(AccountNotFoundException::new);

        if(account.getAccountTokens().stream().anyMatch(t -> t.getTokenType().equals(env.getProperty("boorger.confirmToken")))) {
            accountTokenRepository.deleteAll(account.getAccountTokens().stream().filter(t -> t.getTokenType().equals(env.getProperty("boorger.confirmToken"))).collect(Collectors.toList()));
        }
        AccountConfirmToken token = new AccountConfirmToken();
        token.setAccount(account);
        token.setTokenType(env.getProperty("boorger.confirmToken"));
        token.setExpireDate(LocalDateTime.now()
                .plusMinutes(Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.confirmTokenExpirationTime")))));
        account.getAccountTokens().add(token);
        accountRepository.saveAndFlush(account);
        accountTokenRepository.saveAndFlush(token);
        return token.getBusinessKey();
    }
}
