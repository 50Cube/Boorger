package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.dto.FreeTableDTO;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.dto.TableDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface RestaurantController {

    List<RestaurantDTO> getRestaurants(String language) throws AppBaseException;
    List<RestaurantDTO> getFilteredRestaurants(String filter, String language) throws AppBaseException;
    ResponseEntity<?> addRestaurant(RestaurantDTO restaurantDTO, String language) throws AppBaseException;
    RestaurantDTO getRestaurant(String name, String language) throws AppBaseException;
    void addDish(String restaurantName, DishDTO dishDTO) throws AppBaseException;
    void changeRestaurantActivity(RestaurantDTO restaurantDTO) throws AppBaseException;
    void editRestaurant(RestaurantDTO restaurantDTO) throws AppBaseException;
    List<TableDTO> getFreeTables(FreeTableDTO freeTableDTO, String language) throws AppBaseException;
}
