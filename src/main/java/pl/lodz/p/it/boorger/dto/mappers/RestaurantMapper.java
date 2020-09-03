package pl.lodz.p.it.boorger.dto.mappers;

import org.springframework.util.Base64Utils;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.entities.Restaurant;
import pl.lodz.p.it.boorger.utils.DateFormatter;

public class RestaurantMapper {

    public static RestaurantDTO mapToDto(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .version(restaurant.getVersion())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .active(restaurant.isActive())
                .installment(restaurant.getInstallment())
                .photo(new String(Base64Utils.encode(restaurant.getPhoto())))
                .creationDate(DateFormatter.dateToString(restaurant.getCreationDate()))
                .addressDTO(AddressMapper.mapToDto(restaurant.getAddress()))
                .build();
    }

    public static Restaurant mapFromDto(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .description(restaurantDTO.getDescription())
                .active(restaurantDTO.isActive())
                .installment(restaurantDTO.getInstallment())
//                .photo(restaurantDTO.getPhoto())
                .address(AddressMapper.mapFromDto(restaurantDTO.getAddressDTO()))
                .build();
    }
}
