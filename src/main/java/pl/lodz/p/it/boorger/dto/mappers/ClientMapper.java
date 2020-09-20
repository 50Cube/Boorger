package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.ClientDTO;
import pl.lodz.p.it.boorger.entities.Client;

public class ClientMapper {

    public static ClientDTO mapToDto(Client client) {
        return ClientDTO.builder()
                .login(client.getAccount().getLogin())
                .email(client.getAccount().getEmail())
                .firstname(client.getAccount().getFirstname())
                .lastname(client.getAccount().getLastname())
                .build();
    }
}
