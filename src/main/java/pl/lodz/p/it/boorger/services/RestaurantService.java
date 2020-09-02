package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.Restaurant;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.DatabaseException;
import pl.lodz.p.it.boorger.repositories.AddressRepository;
import pl.lodz.p.it.boorger.repositories.RestaurantRepository;

import javax.validation.Valid;
import java.util.List;

@Log
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

    public void addRestaurant(@Valid Restaurant restaurant) throws AppBaseException {
        try {
            addressRepository.saveAndFlush(restaurant.getAddress());
            restaurantRepository.saveAndFlush(restaurant);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
