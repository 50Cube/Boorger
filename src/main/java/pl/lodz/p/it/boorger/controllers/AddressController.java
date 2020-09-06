package pl.lodz.p.it.boorger.controllers;

import pl.lodz.p.it.boorger.dto.AddressDTO;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.util.List;

public interface AddressController {

    List<AddressDTO> getAddresses() throws AppBaseException;
    void addAddress(AddressDTO addressDTO) throws AppBaseException;
}
