package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.AuthDataDTO;
import pl.lodz.p.it.boorger.entities.AuthData;

public class AuthDataMapper {

    public static AuthDataDTO mapToDto(AuthData authData) {
        return AuthDataDTO.builder()
                .failedAuthCounter(authData.getFailedAuthCounter())
                .lastAuthIp(authData.getLastAuthIp())
                .lastFailedAuth(authData.getLastFailedAuth())
                .lastSuccessfulAuth(authData.getLastSuccessfulAuth())
                .build();
    }
}
