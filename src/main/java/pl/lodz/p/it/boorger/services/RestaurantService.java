package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.*;
import pl.lodz.p.it.boorger.exceptions.*;
import pl.lodz.p.it.boorger.repositories.*;
import pl.lodz.p.it.boorger.security.services.SignatureService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private AddressRepository addressRepository;
    private DishRepository dishRepository;
    private HoursRepository hoursRepository;
    private TableRepository tableRepository;

    public List<Restaurant> getRestaurants() throws AppBaseException {
        try {
            return restaurantRepository.findAllByOrderByName();
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

    public void addDish(String restaurantName, @Valid Dish dish) throws AppBaseException {
        try {
            Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                    .orElseThrow(RestaurantNotFoundException::new);
            dish.setRestaurant(restaurant);
            restaurant.getDishes().add(dish);
            dishRepository.saveAndFlush(dish);
            restaurantRepository.saveAndFlush(restaurant);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("dish_name_restaurant_id_uindex"))
                throw new DishAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void changeRestaurantActivity(@Valid Restaurant editedRestaurant, String signatureDTO) throws AppBaseException {
        try {
            Restaurant restaurant = restaurantRepository.findByName(editedRestaurant.getName())
                    .orElseThrow(RestaurantNotFoundException::new);
            if(!SignatureService.verify(restaurant.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            restaurant.setActive(!restaurant.isActive());
            restaurantRepository.saveAndFlush(restaurant);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void editRestaurant(@Valid Restaurant editedRestaurant, String signatureDTO) throws AppBaseException {
        try {
            Restaurant restaurant = restaurantRepository.findByName(editedRestaurant.getName())
                    .orElseThrow(RestaurantNotFoundException::new);
            if(restaurant.isActive())
                throw new RestaurantNotDeactivatedException();
            if (!SignatureService.verify(restaurant.getSignatureString(), signatureDTO))
                throw new OptimisticLockException();
            restaurant.setDescription(editedRestaurant.getDescription());
            restaurant.setInstallment(editedRestaurant.getInstallment());
            hoursRepository.delete(restaurant.getHours());
            editedRestaurant.getHours().setRestaurant(restaurant);
            restaurant.setHours(editedRestaurant.getHours());
            for(Table table : restaurant.getTables())
                for(Table table2 : editedRestaurant.getTables())
                    if(table.getNumber() == table2.getNumber())
                        table.setActive(table2.isActive());
            restaurantRepository.saveAndFlush(restaurant);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public List<Table> getFreeTables(String restaurantName, LocalDateTime start, LocalDateTime end) throws AppBaseException {
        List<Table> returnList = new ArrayList<>();
        List<Table> tmpList = tableRepository.findAllByRestaurantName(restaurantName).stream().filter(Table::isActive).collect(Collectors.toList());

        for(Table table : tmpList) {
            if(checkDatesOverlap(table, start, end))
                returnList.add(table);
        }
        return returnList;
    }

    private boolean checkDatesOverlap(Table table, LocalDateTime start, LocalDateTime end) {
        boolean tmp = true;
        for(Reservation reservation : table.getReservations()) {
            if(start.isBefore(reservation.getEndDate()) && end.isAfter(reservation.getStartDate()) && reservation.getStatus().equals(Status.BOOKED))
                tmp = false;
        }
        return tmp;
    }
}
