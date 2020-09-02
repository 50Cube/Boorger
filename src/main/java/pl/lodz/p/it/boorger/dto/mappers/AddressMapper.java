package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.AddressDTO;
import pl.lodz.p.it.boorger.entities.Address;
import pl.lodz.p.it.boorger.utils.DateFormatter;

public class AddressMapper {

    public static AddressDTO mapToDto(Address address) {
        return AddressDTO.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .streetNo(address.getStreetNo())
                .businessKey(address.getBusinessKey())
                .creationDate(DateFormatter.dateToString(address.getCreationDate()))
                .build();
    }

    public static Address mapFromDto(AddressDTO addressDTO) {
        return Address.builder()
                .city(addressDTO.getCity())
                .street(addressDTO.getStreet())
                .streetNo(addressDTO.getStreetNo())
                .build();
    }
}
