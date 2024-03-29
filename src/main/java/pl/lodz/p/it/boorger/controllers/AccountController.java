package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountController {

    AccountDTO getAccount(String login) throws AppBaseException;
    List<AccountDTO> getAccounts(int page) throws AppBaseException;
    int getAccountsPageAmount() throws AppBaseException;
    List<AccountDTO> getFilteredAccounts(int page, String filter) throws AppBaseException;
    int getFilteredAccountsPageAmount(String filter) throws AppBaseException;
    void changeLanguage(String login, String language) throws AppBaseException;
    ResponseEntity<?> resetPassword(String email, String language, HttpServletRequest request) throws AppBaseException;
    ResponseEntity<?> changeResetPassword(String token, AccountDTO accountDTO, String captcha, HttpServletRequest request) throws AppBaseException;
    ResponseEntity<?> changePassword(AccountDTO accountDTO, String captcha, HttpServletRequest request) throws AppBaseException;
    void editAccount(AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException;
    void resendConfirmationEmail(AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException;
    void editOtherAccount(AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException;
    ResponseEntity<?> addAccount(AccountDTO accountDTO, HttpServletRequest request) throws AppBaseException;
}
