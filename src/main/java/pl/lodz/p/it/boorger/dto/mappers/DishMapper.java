package pl.lodz.p.it.boorger.dto.mappers;

import pl.lodz.p.it.boorger.cache.CacheService;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.entities.Dish;

public class DishMapper {

    public static DishDTO mapToDto(Dish dish, String language) {
        return DishDTO.builder()
                .name(dish.getName())
                .description(
                        dish.getDescriptionLanguage().equals(language) ?
                                dish.getDescription() : CacheService.getFromCache(dish, language).getTranslation())
                .price(dish.getPrice())
                .descriptionLanguage(dish.getDescriptionLanguage())
                .businessKey(dish.getBusinessKey())
                .build();
    }

    public static Dish mapFromDto(DishDTO dishDTO) {
        return Dish.builder()
                .name(dishDTO.getName())
                .description(dishDTO.getDescription())
                .price(dishDTO.getPrice())
                .descriptionLanguage(dishDTO.getDescriptionLanguage())
                .build();
    }
}
