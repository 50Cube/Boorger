package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.AccountConfirmToken;
import pl.lodz.p.it.boorger.entities.AuthData;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.AccountRepository;
import pl.lodz.p.it.boorger.repositories.AccountTokenRepository;
import pl.lodz.p.it.boorger.repositories.AuthDataRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class AccountService {

    private AccountRepository accountRepository;
    private AuthDataRepository authDataRepository;
    private AccountTokenRepository accountTokenRepository;
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
    public Account getAccount(String login) throws AppBaseException {
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
}
