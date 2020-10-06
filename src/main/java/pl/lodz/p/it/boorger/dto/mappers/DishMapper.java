package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.entities.Dish;

public class DishMapper {

    public static DishDTO mapToDto(Dish dish) {
        return DishDTO.builder()
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .businessKey(dish.getBusinessKey())
                .build();
    }

    public static Dish mapFromDto(DishDTO dishDTO) {
        return Dish.builder()
                .name(dishDTO.getName())
                .description(dishDTO.getDescription())
                .price(dishDTO.getPrice())
                .build();
    }
}
