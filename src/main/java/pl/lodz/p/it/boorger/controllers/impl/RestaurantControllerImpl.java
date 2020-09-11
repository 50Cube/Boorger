package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.RestaurantController;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.dto.mappers.DishMapper;
import pl.lodz.p.it.boorger.dto.mappers.RestaurantMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.RestaurantService;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class RestaurantControllerImpl implements RestaurantController {

    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<RestaurantDTO> getRestaurants() throws AppBaseException {
        return restaurantService.getRestaurants().stream().map(RestaurantMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("restaurants/{filter}")
    public List<RestaurantDTO> getFilteredRestaurants(@PathVariable String filter) throws AppBaseException {
        return restaurantService.getFilteredRestaurants(filter).stream().map(RestaurantMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/restaurant")
    public ResponseEntity<?> addRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO, @RequestHeader("lang") String language) throws AppBaseException {
        restaurantService.addRestaurant(RestaurantMapper.mapFromDto(restaurantDTO));
        return ResponseEntity.ok(MessageProvider.getTranslatedText("restaurant.addnew", language));
    }

    @GetMapping("/restaurant/{name}")
    public RestaurantDTO getRestaurant(@PathVariable String name) throws AppBaseException {
        return RestaurantMapper.mapToDto(restaurantService.getRestaurantByName(name));
    }

    @PostMapping("/dish/{restaurantName}")
    public void addDish(@PathVariable String restaurantName, @Valid @RequestBody DishDTO dishDTO, @RequestHeader("lang") String language) throws AppBaseException {
        restaurantService.addDish(restaurantName, DishMapper.mapFromDto(dishDTO));
    }
}
