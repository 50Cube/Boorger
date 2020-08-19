package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountController {

    List<AccountDTO> getAccounts(int page) throws AppBaseException;
    int getAccountsPageAmount() throws AppBaseException;
    ResponseEntity<?> register(AccountDTO accountDTO, String captcha, HttpServletRequest request) throws AppBaseException;
    ResponseEntity<?> confirmAccount(String token, String language) throws AppBaseException;
    void changeLanguage(String login, String language) throws AppBaseException;
}
