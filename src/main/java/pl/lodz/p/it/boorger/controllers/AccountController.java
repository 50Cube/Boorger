package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountController {

    List<AccountDTO> getAccounts(int page) throws AppBaseException;
    int getAccountsPageAmount() throws AppBaseException;
    void changeLanguage(String login, String language) throws AppBaseException;
    ResponseEntity<?> resetPassword(String email, String language, HttpServletRequest request) throws AppBaseException;
    ResponseEntity<?> changePassword(String login, String token, Object object, String captcha) throws AppBaseException;
}
