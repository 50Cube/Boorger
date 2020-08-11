package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface AccountController {

    List<AccountDTO> getAccounts() throws AppBaseException;
    ResponseEntity<?> register(AccountDTO accountDTO) throws AppBaseException;
    void changeLanguage(String login, String language) throws AppBaseException;
}
