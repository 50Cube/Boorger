package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.Restaurant;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.DatabaseException;
import pl.lodz.p.it.boorger.exceptions.RestaurantAlreadyExistsException;
import pl.lodz.p.it.boorger.exceptions.RestaurantNotFoundException;
import pl.lodz.p.it.boorger.repositories.AddressRepository;
import pl.lodz.p.it.boorger.repositories.RestaurantRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private AddressRepository addressRepository;

    public List<Restaurant> getRestaurants() throws AppBaseException {
        try {
            return restaurantRepository.findAll();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Restaurant> getFilteredRestaurants(String filter) throws AppBaseException {
        try {
            return restaurantRepository.findAllByNameIgnoreCaseContaining(filter);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void addRestaurant(@Valid Restaurant restaurant) throws AppBaseException {
        try {
            restaurant.getHours().setRestaurant(restaurant);
            restaurant.getTables().forEach(table -> table.setRestaurant(restaurant));
            restaurant.setDishes(new ArrayList<>());
            if(addressRepository.findByCityAndStreetAndStreetNo(restaurant.getAddress().getCity(),
                    restaurant.getAddress().getStreet(), restaurant.getAddress().getStreetNo()).isPresent())
                restaurant.setAddress(addressRepository.findByCityAndStreetAndStreetNo(restaurant.getAddress().getCity(),
                        restaurant.getAddress().getStreet(), restaurant.getAddress().getStreetNo()).get());
            else addressRepository.saveAndFlush(restaurant.getAddress());
            restaurantRepository.saveAndFlush(restaurant);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("restaurant_name_uindex"))
                throw new RestaurantAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public Restaurant getRestaurantByName(String name) throws AppBaseException {
        try {
            return restaurantRepository.findByName(name)
                    .orElseThrow(RestaurantNotFoundException::new);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
