package pl.lodz.p.it.boorger.controllers;

import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface AccountController {

    List<AccountDTO> getAccounts(int page) throws AppBaseException;
    int getAccountsPageAmount() throws AppBaseException;
    void changeLanguage(String login, String language) throws AppBaseException;
}
