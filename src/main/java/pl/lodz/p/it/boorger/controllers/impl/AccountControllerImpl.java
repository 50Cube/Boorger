package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AccountController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
public class AccountControllerImpl implements AccountController {

    private AccountService accountService;
    private BCryptPasswordEncoder passwordEncoder;
    private Environment env;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts().stream().map(AccountMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/register")
    public void register(@RequestBody AccountDTO accountDTO) {
        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        Account account = AccountMapper.mapFromDto(accountDTO);
        account.setAccessLevels(generateAccessLevels(account));
        account.setConfirmed(false);
        accountService.register(account);
    }

    private List<AccessLevel> generateAccessLevels(Account account) {
        List<AccessLevel> list = new ArrayList<>();

        Client client = new Client();
        client.setAccount(account);
        client.setActive(true);
        client.setAccessLevel(env.getProperty("boorger.roleClient"));
        list.add(client);

        Manager manager = new Manager();
        manager.setAccount(account);
        manager.setActive(false);
        manager.setAccessLevel(env.getProperty("boorger.roleManager"));
        list.add(manager);

        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setActive(false);
        admin.setAccessLevel(env.getProperty("boorger.roleAdmin"));
        list.add(admin);

        return list;
    }
}
