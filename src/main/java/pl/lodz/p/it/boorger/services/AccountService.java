package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.repositories.AccountRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public void register(Account account) {
        accountRepository.save(account);
    }
}
