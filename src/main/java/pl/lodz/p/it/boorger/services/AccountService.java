package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceReadOnlyTransaction;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.AccountRepository;
import pl.lodz.p.it.boorger.repositories.AccountTokenRepository;
import pl.lodz.p.it.boorger.repositories.AuthDataRepository;
import pl.lodz.p.it.boorger.security.services.SignatureService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class AccountService {

    private AccountRepository accountRepository;
    private AuthDataRepository authDataRepository;
    private AccountTokenRepository accountTokenRepository;
    private PasswordEncoder passwordEncoder;
    private Environment env;

    @ServiceReadOnlyTransaction
    public Account getAccount(String login) throws AppBaseException {
        try {
            return accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    @ServiceReadOnlyTransaction
    public Page<Account> getAccounts(int page) throws AppBaseException {
        try {
            return accountRepository.findAllByOrderByLogin(
                    PageRequest.of(page, Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.pageSize")))));
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    @ServiceReadOnlyTransaction
    public Page<Account> getFilteredAccounts(int page, String filter) throws AppBaseException {
        try {
            List<Account> list = accountRepository.findAll().stream().filter
                    (a -> a.getLogin().toLowerCase().contains(filter.toLowerCase()) || a.getEmail().toLowerCase().contains(filter.toLowerCase())
                    || a.getFirstname().toLowerCase().contains(filter.toLowerCase()) || a.getLastname().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            int size = Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.pageSize")));
            int start = (int) PageRequest.of(page, size).getOffset();
            int end = Math.min((start + PageRequest.of(page, size).getPageSize()), list.size());

            return new PageImpl<>(list.subList(start, end), PageRequest.of(page, size), list.size());
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    @ServiceReadOnlyTransaction
    public Account getAccountByLogin(String login) throws AppBaseException {
        try {
            return accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public String register(@Valid Account account) throws AppBaseException {
        try {
            account.setAccessLevels(generateAccessLevels(account));
            account.setConfirmed(false);
            account.setActive(true);
            account.getAuthData().setAccount(account);

            AccountConfirmToken token = generateConfirmToken(account);
            account.setAccountTokens(new ArrayList<>());
            account.getAccountTokens().add(token);

            PreviousPassword previousPassword = new PreviousPassword();
            previousPassword.setAccount(account);
            previousPassword.setPassword(account.getPassword());
            account.setPreviousPasswords(new ArrayList<>());
            account.getPreviousPasswords().add(previousPassword);

            accountRepository.saveAndFlush(account);
            accountTokenRepository.saveAndFlush(token);

            return token.getBusinessKey();
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("account_login_data_login_uindex"))
                throw new LoginAlreadyExistsException();
            if(Objects.requireNonNull(e.getMessage()).contains("account_personal_data_email_uindex"))
                throw new EmailAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
        return "";
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

    private AccountConfirmToken generateConfirmToken(Account account) {
        AccountConfirmToken token = new AccountConfirmToken();
        token.setAccount(account);
        token.setTokenType(env.getProperty("boorger.confirmToken"));
        token.setExpireDate(LocalDateTime.now()
                .plusMinutes(Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.confirmTokenExpirationTime")))));
        return token;
    }

    public void editAccount(@Valid Account account) throws AppBaseException {
        try {
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editLanguage(String login, String language) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(login)
                    .orElseThrow(AccountNotFoundException::new);
            account.setLanguage(language);
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editAuthData(@Valid AuthData authData) throws AppBaseException {
        try {
            authDataRepository.saveAndFlush(authData);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void confirmAccount(String token) throws AppBaseException {
        try {
            AccountConfirmToken accountConfirmToken = (AccountConfirmToken) accountTokenRepository.findByBusinessKey(token)
                    .orElseThrow(AppBaseException::new);
            if(accountConfirmToken.getExpireDate().isBefore(LocalDateTime.now()))
                throw new TokenExpiredException();
            Account account = accountConfirmToken.getAccount();
            if(account.isConfirmed())
                throw new AccountAlreadyConfirmedException();
            account.setConfirmed(true);
            accountRepository.saveAndFlush(account);
            accountTokenRepository.delete(accountConfirmToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public Account getAccountByEmail(String email) throws AppBaseException {
        try {
            return accountRepository.findByEmail(email)
                    .orElseThrow(AccountNotFoundException::new);
        } catch (AccountNotFoundException e) {
            log.warning("Password reset error: account with email " + email + " does not exists");
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void resetPassword(@Valid ForgotPasswordToken token, @Valid  ForgotPasswordToken newToken) throws AppBaseException {
        try {
            if(token != null) {
                accountTokenRepository.delete(token);
                accountTokenRepository.flush();
            }
            accountTokenRepository.saveAndFlush(newToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void changeResetPassword(String token, @Valid  Account editedAccount) throws AppBaseException {
        try {
            ForgotPasswordToken forgotPasswordToken = (ForgotPasswordToken) accountTokenRepository.findByBusinessKey(token)
                    .orElseThrow(AppBaseException::new);
            if(forgotPasswordToken.getExpireDate().isBefore(LocalDateTime.now()))
                throw new TokenExpiredException();
            Account account = forgotPasswordToken.getAccount();
            addPreviousPassword(account, editedAccount);
            accountRepository.saveAndFlush(account);
            accountTokenRepository.delete(forgotPasswordToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    private boolean checkIfPasswordWasUsed(@Valid Account account, String newPassword) {
        return account.getPreviousPasswords().stream().anyMatch(p -> passwordEncoder.matches(newPassword, p.getPassword()));
    }

    private void addPreviousPassword(@Valid Account account, @Valid Account editedAccount) throws AppBaseException {
        if(checkIfPasswordWasUsed(account, editedAccount.getPassword()))
            throw new PasswordAlreadyUsedException();
        editedAccount.setPassword(passwordEncoder.encode(editedAccount.getPassword()));

        PreviousPassword previousPassword = new PreviousPassword();
        previousPassword.setAccount(account);
        previousPassword.setPassword(editedAccount.getPassword());

        account.getPreviousPasswords().add(previousPassword);
        account.setPassword(editedAccount.getPassword());
    }

    public void changePassword(@Valid Account editedAccount, String previous) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(editedAccount.getLogin())
                    .orElseThrow(AccountNotFoundException::new);
            if(!passwordEncoder.matches(previous, account.getPassword()))
                throw new IncorrectCurrentPasswordException();
            addPreviousPassword(account, editedAccount);
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editPersonal(@Valid Account editedAccount, String signatureDTO) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(editedAccount.getLogin())
                    .orElseThrow(AccountNotFoundException::new);
            if(!SignatureService.verify(account.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            account.setFirstname(editedAccount.getFirstname());
            account.setLastname(editedAccount.getLastname());
            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public String resendConfirmationEmail(@Valid Account accountParam) throws AppBaseException {
        try {
            Account account = accountRepository.findByEmail(accountParam.getEmail())
                    .orElseThrow(AccountNotFoundException::new);

            if (account.getAccountTokens().stream().anyMatch(t -> t.getTokenType().equals(env.getProperty("boorger.confirmToken")))) {
                accountTokenRepository.deleteAll(account.getAccountTokens().stream().filter(t -> t.getTokenType().equals(env.getProperty("boorger.confirmToken"))).collect(Collectors.toList()));
            }

            AccountConfirmToken token = generateConfirmToken(account);
            account.getAccountTokens().add(token);
            accountRepository.saveAndFlush(account);
            accountTokenRepository.saveAndFlush(token);
            return token.getBusinessKey();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editOtherAccount(@Valid Account editedAccount, Collection<String> accessLevels, String signatureDTO) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(editedAccount.getLogin())
                    .orElseThrow(AccountNotFoundException::new);
            if(!SignatureService.verify(account.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            account.setFirstname(editedAccount.getFirstname());
            account.setLastname(editedAccount.getLastname());
            account.setActive(editedAccount.isActive());

            for(AccessLevel a : account.getAccessLevels())
                if(accessLevels.contains(a.getAccessLevel()))
                    a.setActive(true);
                else a.setActive(false);

            accountRepository.saveAndFlush(account);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public String addAccount(@Valid Account account, Collection<String> accessLevels) throws AppBaseException {
        try {
            account.setAccessLevels(generateAccessLevels(account));

            for (AccessLevel level : account.getAccessLevels()) {
                if (level instanceof Client)
                    level.setActive(accessLevels.contains(env.getProperty("boorger.roleClient")));
                if (level instanceof Manager)
                    level.setActive(accessLevels.contains(env.getProperty("boorger.roleManager")));
                if (level instanceof Admin)
                    level.setActive(accessLevels.contains(env.getProperty("boorger.roleAdmin")));
            }

            account.getAuthData().setAccount(account);
            AccountConfirmToken token = generateConfirmToken(account);
            account.setAccountTokens(new ArrayList<>());
            account.getAccountTokens().add(token);
            account.setPreviousPasswords(new ArrayList<>());

            accountRepository.saveAndFlush(account);
            accountTokenRepository.saveAndFlush(token);
            return token.getBusinessKey();
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("account_login_data_login_uindex"))
                throw new LoginAlreadyExistsException();
            if(Objects.requireNonNull(e.getMessage()).contains("account_personal_data_email_uindex"))
                throw new EmailAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
        return "";
    }

    public String getEmailFromLogin(String login) throws AppBaseException {
        try {
            Account account = accountRepository.findByLogin(login).orElseThrow(AccountNotFoundException::new);
            return account.getEmail();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
