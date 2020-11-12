package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.RestaurantController;
import pl.lodz.p.it.boorger.dto.DishDTO;
import pl.lodz.p.it.boorger.dto.FreeTableDTO;
import pl.lodz.p.it.boorger.dto.RestaurantDTO;
import pl.lodz.p.it.boorger.dto.TableDTO;
import pl.lodz.p.it.boorger.dto.mappers.DishMapper;
import pl.lodz.p.it.boorger.dto.mappers.RestaurantMapper;
import pl.lodz.p.it.boorger.dto.mappers.TableMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.RestaurantService;
import pl.lodz.p.it.boorger.utils.DateFormatter;
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
    public List<RestaurantDTO> getRestaurants(@RequestHeader("lang") String language) throws AppBaseException {
        return restaurantService.getRestaurants().stream()
                .map(restaurant -> RestaurantMapper.mapToDto(restaurant, language))
                .collect(Collectors.toList());
    }

    @GetMapping("restaurants/{filter}")
    public List<RestaurantDTO> getFilteredRestaurants(@PathVariable String filter, @RequestHeader("lang") String language) throws AppBaseException {
        return restaurantService.getFilteredRestaurants(filter).stream()
                .map(restaurant -> RestaurantMapper.mapToDto(restaurant, language))
                .collect(Collectors.toList());
    }

    @PostMapping("/restaurant")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public ResponseEntity<?> addRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO, @RequestHeader("lang") String language) throws AppBaseException {
        restaurantService.addRestaurant(RestaurantMapper.mapFromDto(restaurantDTO));
        return ResponseEntity.ok(MessageProvider.getTranslatedText("restaurant.addnew", language));
    }

    @GetMapping("/restaurant/{name}")
    public RestaurantDTO getRestaurant(@PathVariable String name, @RequestHeader("lang") String language) throws AppBaseException {
        return RestaurantMapper.mapToDto(restaurantService.getRestaurantByName(name), language);
    }

    @PostMapping("/dish/{restaurantName}")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public void addDish(@PathVariable String restaurantName, @Valid @RequestBody DishDTO dishDTO, @RequestHeader("lang") String language) throws AppBaseException {
        restaurantService.addDish(restaurantName, DishMapper.mapFromDto(dishDTO));
    }

    @PutMapping("/restaurant/activity")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public void changeRestaurantActivity(@Valid @RequestBody RestaurantDTO restaurantDTO) throws AppBaseException {
        restaurantService.changeRestaurantActivity(RestaurantMapper.mapFromDto(restaurantDTO), restaurantDTO.getSignature());
    }

    @PutMapping("/restaurant/edit")
    @PreAuthorize("hasAuthority(@environment.getProperty('boorger.roleManager'))")
    public void editRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) throws AppBaseException {
        restaurantService.editRestaurant(RestaurantMapper.mapFromDto(restaurantDTO), restaurantDTO.getSignature());
    }

    @PostMapping("/tables")
    public List<TableDTO> getFreeTables(@Valid @RequestBody FreeTableDTO freeTableDTO, @RequestHeader("lang") String language) throws AppBaseException {
        return restaurantService.getFreeTables(freeTableDTO.getRestaurantName(), DateFormatter.stringToDate(freeTableDTO.getStartDate()),
                DateFormatter.stringToDate(freeTableDTO.getEndDate()))
                .stream().map(table -> TableMapper.mapToDto(table, language)).collect(Collectors.toList());
    }
}
