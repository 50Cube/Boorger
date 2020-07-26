package pl.lodz.p.it.boorger.controllers;

import pl.lodz.p.it.boorger.dto.AccountDTO;

import java.util.List;

public interface AccountController {

    List<AccountDTO> getAccounts();
    void register(AccountDTO accountDTO);
}
