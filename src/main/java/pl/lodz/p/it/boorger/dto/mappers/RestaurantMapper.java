package pl.lodz.p.it.boorger.dto.mappers;

import org.springframework.util.Base64Utils;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.entities.Restaurant;
import pl.lodz.p.it.boorger.utils.DateFormatter;

import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDTO mapToDto(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .version(restaurant.getVersion())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .active(restaurant.isActive())
                .installment(restaurant.getInstallment())
                .photo(restaurant.getPhoto() != null ? new String(Base64Utils.encode(restaurant.getPhoto())) : "")
                .creationDate(DateFormatter.dateToString(restaurant.getCreationDate()))
                .addressDTO(AddressMapper.mapToDto(restaurant.getAddress()))
                .hoursDTO(HoursMapper.mapToDto(restaurant.getHours()))
                .tableDTOs(restaurant.getTables().stream().map(TableMapper::mapToDto).collect(Collectors.toList()))
                .dishDTOs(restaurant.getDishes().stream().map(DishMapper::mapToDto).collect(Collectors.toList()))
                .build();
    }

    public static Restaurant mapFromDto(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .description(restaurantDTO.getDescription())
                .active(restaurantDTO.isActive())
                .installment(restaurantDTO.getInstallment())
                .photo(Base64Utils.decode(restaurantDTO.getPhoto().getBytes()))
                .address(AddressMapper.mapFromDto(restaurantDTO.getAddressDTO()))
                .hours(HoursMapper.mapFromDto(restaurantDTO.getHoursDTO()))
                .tables(restaurantDTO.getTableDTOs().stream().map(TableMapper::mapFromDto).collect(Collectors.toList()))
                .build();
    }
}
