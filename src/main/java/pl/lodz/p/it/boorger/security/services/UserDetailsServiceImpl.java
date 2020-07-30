package pl.lodz.p.it.boorger.security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.repositories.AccountRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("user not found")); //TODO i18n error message
        return UserDetailsImpl.build(account);
    }
}
