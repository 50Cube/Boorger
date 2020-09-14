package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface RestaurantController {

    List<RestaurantDTO> getRestaurants() throws AppBaseException;
    List<RestaurantDTO> getFilteredRestaurants(String filter) throws AppBaseException;
    ResponseEntity<?> addRestaurant(RestaurantDTO restaurantDTO, String language) throws AppBaseException;
    RestaurantDTO getRestaurant(String name) throws AppBaseException;
    void addDish(String restaurantName, DishDTO dishDTO, String language) throws AppBaseException;
    void changeRestaurantActivity(RestaurantDTO restaurantDTO) throws AppBaseException;
}
