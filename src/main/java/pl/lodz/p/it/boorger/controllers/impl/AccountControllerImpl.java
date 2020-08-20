package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AccountController;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.dto.mappers.AccountMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.AccountService;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@AllArgsConstructor
public class AccountControllerImpl implements AccountController {

    private AccountService accountService;

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
}
