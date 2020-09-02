package pl.lodz.p.it.boorger.controllers;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface RestaurantController {

    List<RestaurantDTO> getRestaurants() throws AppBaseException;
    ResponseEntity<?> addRestaurant(RestaurantDTO restaurantDTO, String language) throws AppBaseException;
}
