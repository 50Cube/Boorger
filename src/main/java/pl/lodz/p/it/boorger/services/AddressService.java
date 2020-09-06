package pl.lodz.p.it.boorger.services;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.Address;
import pl.lodz.p.it.boorger.exceptions.AddressAlreadyExistsException;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.DatabaseException;
import pl.lodz.p.it.boorger.repositories.AddressRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Log
@Service
@AllArgsConstructor
@ServiceTransaction
@Retryable(value = TransactionException.class)
public class AddressService {

    private AddressRepository addressRepository;

    public List<Address> getAddresses() throws AppBaseException {
        try {
            return addressRepository.findAll();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }

    public void addAddress(@Valid Address address) throws AppBaseException {
        try {
            addressRepository.saveAndFlush(address);
        } catch (DataIntegrityViolationException e) {
            if(Objects.requireNonNull(e.getMessage()).contains("address_street_street_no_city_uindex"))
                throw new AddressAlreadyExistsException();
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
