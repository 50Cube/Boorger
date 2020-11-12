package pl.lodz.p.it.boorger.dto.mappers;

import org.springframework.util.Base64Utils;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.entities.Restaurant;
import pl.lodz.p.it.boorger.security.services.SignatureService;
import pl.lodz.p.it.boorger.utils.DateFormatter;

import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDTO mapToDto(Restaurant restaurant, String language) {
        return RestaurantDTO.builder()
                .signature(SignatureService.createSignature(restaurant.getSignatureString()))
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .active(restaurant.isActive())
                .installment(restaurant.getInstallment())
                .photo(restaurant.getPhoto() != null ? new String(Base64Utils.encode(restaurant.getPhoto())) : "")
                .creationDate(DateFormatter.dateToString(restaurant.getCreationDate()))
                .addressDTO(AddressMapper.mapToDto(restaurant.getAddress()))
                .hoursDTO(HoursMapper.mapToDto(restaurant.getHours()))
                .tableDTOs(restaurant.getTables().stream().map(table -> TableMapper.mapToDto(table, language)).collect(Collectors.toList()))
                .dishDTOs(restaurant.getDishes().stream().map(dish -> DishMapper.mapToDto(dish, language)).collect(Collectors.toList()))
                .build();
    }

    public static Restaurant mapFromDto(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .description(restaurantDTO.getDescription())
                .active(restaurantDTO.isActive())
                .installment(restaurantDTO.getInstallment())
                .photo(restaurantDTO.getPhoto() != null ? Base64Utils.decode(restaurantDTO.getPhoto().getBytes()) : null)
                .address(restaurantDTO.getAddressDTO() != null ? AddressMapper.mapFromDto(restaurantDTO.getAddressDTO()) : null)
                .hours(restaurantDTO.getHoursDTO() != null ? HoursMapper.mapFromDto(restaurantDTO.getHoursDTO()) : null)
                .tables(restaurantDTO.getTableDTOs() != null ? restaurantDTO.getTableDTOs().stream().map(TableMapper::mapFromDto).collect(Collectors.toList()) : null)
                .build();
    }
}
