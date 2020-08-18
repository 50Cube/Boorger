package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.entities.AccessLevel;
import pl.lodz.p.it.boorger.entities.Account;
import pl.lodz.p.it.boorger.entities.AuthData;
import pl.lodz.p.it.boorger.utils.DateFormatter;

import java.util.stream.Collectors;

public class AccountMapper {
    public static AccountDTO mapToDto(Account account) {
        return AccountDTO.builder()
                .login(account.getLogin())
                .password(account.getPassword())
                .active(account.isActive())
                .confirmed(account.isConfirmed())
                .language(account.getLanguage())
                .firstname(account.getFirstname())
                .lastname(account.getLastname())
                .email(account.getEmail())
                .accessLevels(account.getAccessLevels().stream().filter(AccessLevel::isActive)
                        .map(AccessLevel::getAccessLevel).collect(Collectors.toList()))
                .lastSuccessfulAuth(DateFormatter.dateToString(account.getAuthData().getLastSuccessfulAuth()))
                .lastAuthIp(account.getAuthData().getLastAuthIp())
                .creationDate(DateFormatter.dateToString(account.getCreationDate()))
                .build();
    }

    public static Account mapFromDto(AccountDTO accountDTO) {
        return Account.builder()
                .login(accountDTO.getLogin())
                .password(accountDTO.getPassword())
                .active(accountDTO.isActive())
                .confirmed(accountDTO.isConfirmed())
                .language(accountDTO.getLanguage())
                .firstname(accountDTO.getFirstname())
                .lastname(accountDTO.getLastname())
                .email(accountDTO.getEmail())
                .authData(new AuthData())
                .build();
    }
}
