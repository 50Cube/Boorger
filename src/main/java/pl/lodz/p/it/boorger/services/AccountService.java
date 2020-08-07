package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.AccountRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public void register(Account account) throws AppBaseException {
        try {
            accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("account_login_data_login_uindex"))
                throw new LoginAlreadyExistsException();
            if(Objects.requireNonNull(e.getMessage()).contains("account_personal_data_email_uindex"))
                throw new EmailAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editLanguage(String login, String language) throws AccountNotFoundException {
        if(accountRepository.findByLogin(login).isEmpty())
            throw new AccountNotFoundException();
        Account account = accountRepository.findByLogin(login).get();
        account.setLanguage(language);
        accountRepository.save(account);
    }
}
