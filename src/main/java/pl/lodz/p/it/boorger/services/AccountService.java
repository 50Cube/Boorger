package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.AccountConfirmToken;
import pl.lodz.p.it.boorger.entities.AuthData;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.AccountRepository;
import pl.lodz.p.it.boorger.repositories.AccountTokenRepository;
import pl.lodz.p.it.boorger.repositories.AuthDataRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private AuthDataRepository authDataRepository;
    private AccountTokenRepository accountTokenRepository;

    public List<Account> getAccounts() throws AppBaseException {
        try {
            return accountRepository.findAll();
        } catch (DataAccessException e) {
                throw new DatabaseException();
            }
    }

    public Account getAccount(String login) throws AppBaseException {
        try {
            return accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void register(Account account, AccountConfirmToken token) throws AppBaseException {
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

    public void editAccount(Account account) throws AppBaseException {
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

    public void editAuthData(AuthData authData) throws AppBaseException {
        try {
            authDataRepository.save(authData);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
