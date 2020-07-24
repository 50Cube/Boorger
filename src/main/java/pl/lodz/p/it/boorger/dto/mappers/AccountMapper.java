package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.AccountDTO;
import pl.lodz.p.it.boorger.entities.AccessLevel;
import pl.lodz.p.it.boorger.entities.Account;

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
                .accessLevels(account.getAccessLevels().stream().map(AccessLevel::getAccessLevel).collect(Collectors.toList()))
                .build();
    }
}